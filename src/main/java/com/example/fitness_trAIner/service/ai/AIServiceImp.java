package com.example.fitness_trAIner.service.ai;

import com.example.fitness_trAIner.common.exception.exceptions.AIException;

import com.example.fitness_trAIner.service.ai.dto.response.AIServiceResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.fitness_trAIner.common.exception.exceptions.EmptyDirectoryException;
import com.example.fitness_trAIner.common.exception.exceptions.FileStoreException;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.multipart.MultipartFile;
import org.zeroturnaround.exec.ProcessExecutor;
import org.zeroturnaround.exec.ProcessResult;
import org.zeroturnaround.exec.stream.LogOutputStream;
import com.google.gson.Gson;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Service
@Slf4j
public class AIServiceImp implements AIService{

    @Value("${posepath}")
    private String posePath;
    @Value("${exerciseAiPath}")
    private String exerciseAiPath;

    private final SimpMessagingTemplate messagingTemplate;

    @Value("${ai.workout}")
    private String workoutPath;
  
  
    @Override
    public AIServiceResponse pythonProcess(String data) throws IOException {
        String replaceData = data.replace("\"", "\\\"");



        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("python", workoutPath, replaceData);
        Process process  = processBuilder.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder result = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }

        try {
            int exitCode = process.waitFor();
            System.out.println("Python script exit code: " + exitCode);
            if (exitCode != 0) {
                throw new AIException("파이썬 파일 실행 결과 오류");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        String jsonResult = result.toString().replace("'", "\"");

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonResult);

        int perfect = jsonNode.get("perfect").asInt();
        int good = jsonNode.get("good").asInt();
        int bad = jsonNode.get("bad").asInt();

        List<String> feedbackList = new ArrayList<>();
        JsonNode feedbackNode = jsonNode.get("feedback");
        if (feedbackNode != null && feedbackNode.isArray()) {
            for (JsonNode node : feedbackNode) {
                feedbackList.add(node.asText());
            }
        }

        return AIServiceResponse.builder()
                .perfect(perfect)
                .good(good)
                .bad(bad)
                .feedback(feedbackList)
                .build();

    }

    @Override
    public String uploadFiles(List<MultipartFile> files, String parentPath, String uploadPath) throws IOException {
        File uploadDir = new File(posePath + File.separator + parentPath + File.separator + uploadPath);

        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        try {
            for (MultipartFile file : files) {
                File uploadedFile = new File(uploadDir, file.getOriginalFilename());
                file.transferTo(uploadedFile);
            }

            return "파일 저장 성공";
        } catch (IOException e) {
            e.printStackTrace();
            throw new FileStoreException("파일 저장 실패: " + e.getMessage());
        }
    }

    @Override
    public List<String> getFilesName(String parentPath) {
        File directory = new File(posePath + File.separator + parentPath);
        System.out.println(directory.getAbsolutePath());
        if (!directory.exists()) {
            throw new EmptyDirectoryException("디렉토리가 존재하지 않습니다.");
        }

        File[] files = directory.listFiles();
        if (files == null || files.length == 0) {
            throw new EmptyDirectoryException("디렉터리에 파일이 없습니다.");
        }

        return Arrays.stream(files)
                .map(File::getName)
                .collect(Collectors.toList());
    }

    @Override
    public void filesView(String parentPath, String filePath, ByteArrayOutputStream baos) throws IOException {
        File directory = new File(posePath + File.separator + parentPath + File.separator + filePath);
        if (!directory.exists()) {
            throw new FileStoreException("디렉토리가 존재하지 않습니다.");
        }
        System.out.println("filesView: " + directory.getName());

        ZipOutputStream zipOutputStream = new ZipOutputStream(baos);
        addFilesToZip(zipOutputStream, directory, directory.getName());
        zipOutputStream.close();
    }

    private void addFilesToZip(ZipOutputStream zipOutputStream, File directory, String parentDir) throws IOException {
        if(directory.isDirectory()) {
            File[] files = directory.listFiles();
            System.out.println("files: " + Arrays.toString(files));
            if (files == null || files.length == 0) {
                return;
            }

            for (File file : files) {
                if (file.isDirectory()) {
                    addFilesToZip(zipOutputStream, file, parentDir + "/" + file.getName());
                } else {
                    addFileToZip(zipOutputStream, file, parentDir);
                }
            }
        } else {
            addFileToZip(zipOutputStream, directory, directory.getParent());
        }
    }

    private void addFileToZip(ZipOutputStream zipOutputStream, File file, String parentDir) throws IOException {
        byte[] buffer = new byte[4096];

        FileInputStream fileInputStream = new FileInputStream(file);
        String entryPath = parentDir + File.separator + file.getName(); // 디렉터리 구조 유지를 위해 전체 경로 사용
        ZipEntry zipEntry = new ZipEntry(entryPath);
        zipOutputStream.putNextEntry(zipEntry);

        int bytesRead;
        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
            zipOutputStream.write(buffer, 0, bytesRead);
        }

        fileInputStream.close();
        zipOutputStream.closeEntry();
    }

    @Override
    public String deleteFiles(String parentPath, String deletePath) throws IOException {
        File deleteDir = new File(posePath + File.separator + parentPath + File.separator + deletePath);

        if (!deleteDir.exists()) {
            File newDeleteDir;
            if (deletePath.contains("&")) {
                System.out.println("deletePath: " + deletePath);
                String[]finalPath = deletePath.split("&");
                System.out.println("finalPath: " + finalPath[0] + ", " + Arrays.toString(Arrays.copyOfRange(finalPath, 1, finalPath.length)));
                newDeleteDir = new File(exerciseAiPath + File.separator + parentPath + File.separator + finalPath[0] + File.separator + Arrays.toString(Arrays.copyOfRange(finalPath, 1, finalPath.length)));
            } else {
                newDeleteDir = new File(exerciseAiPath + File.separator + parentPath + File.separator + deletePath);
            }
            System.out.println("newDeleteDir: " + newDeleteDir.getAbsolutePath());
            if (!newDeleteDir.exists()) {
                throw new FileStoreException("삭제할 파일이 존재하지 않습니다.");
            } else {
                deleteDir = newDeleteDir;
            }
        }

        deleteDirectoryRecursively(deleteDir);

        return "파일 삭제 성공";
    }

    private void deleteDirectoryRecursively(File dir) throws IOException {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectoryRecursively(file);
                }
                if (!file.delete()) {
                    throw new FileStoreException("파일 삭제 실패: " + file.getName());
                }
            }
        }
        if (!dir.delete()) {
            throw new FileStoreException("디렉토리 삭제 실패: " + dir.getName());
        }
    }

    @Override
//    public void startTraining(String pythonFilePath, String exerciseName, String params) throws Exception {
    public void startTraining(String pythonFilePath, String exerciseName) throws Exception {
        System.out.println("pythonFilePath: " + pythonFilePath);
//        System.out.println("params: " + params);
        System.out.println("exerciseName: " + exerciseName);
//        // params에 있는 파일명에 exerciseAiPath를 추가
//        String[] words = params.split(" ");
//        for (int i = 0; i < words.length; i++) {
//            if (words[i].contains(".json")) {
//                words[i] = exerciseAiPath + "/" + exerciseName + "/" + words[i];
//                break;
//            }
//        }
//        params = String.join(" ", words);

//        ProcessResult exitCode = new ProcessExecutor().command("python", pythonFilePath, params)
        ProcessResult exitCode = new ProcessExecutor().command("python", pythonFilePath)
                .redirectOutput(new LogOutputStream() {
                    @Override
                    protected void processLine(String line) {
                        messagingTemplate.convertAndSend("/topic/progress", line);
                    }
                })
                .execute();

        if (exitCode.getExitValue() == 0) {
            messagingTemplate.convertAndSend("/topic/progress", "END");
        } else {
            messagingTemplate.convertAndSend("/topic/progress", "ERROR");
        }
    }

    @Override
    public List<String> getModelList() {
        File directory = new File(exerciseAiPath);
        if (!directory.exists()) {
            throw new EmptyDirectoryException("디렉토리가 존재하지 않습니다.");
        }

        File[] files = directory.listFiles(File::isDirectory);
        if (files == null || files.length == 0) {
            throw new EmptyDirectoryException("디렉터리에 하위 디렉터리가 없습니다.");
        }

        return Arrays.stream(files)
                .map(File::getName)
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, List<String>> getModelInfo(String exerciseName) {
        File directory = new File(exerciseAiPath+ File.separator + exerciseName);
//        System.out.println("file: " + directory.getAbsolutePath());
        if (!directory.exists() || !directory.isDirectory()) { // 디렉토리가 존재 확인 및 디렉토리인지 확인
            throw new EmptyDirectoryException("디렉토리가 존재하지 않습니다.");
        }

        // 파일 이름 조회
//        File[] files = directory.listFiles();
        List<File> files = new ArrayList<>();
        List<File> directories = new ArrayList<>();
        for (File file : directory.listFiles()) {
            if (file.isFile()) {
                files.add(file);
            } else {
                directories.add(file);
            }
        }

        if (files.isEmpty()) {
            throw new EmptyDirectoryException("파일이 없습니다.");
        }

//        System.out.println("files: " + files);
        Map<String, List<String>> result = new HashMap<>();
        for (File file : directories) {
            if (file.getName().equals("Inuse")) {
                if (file.listFiles().length == 0) {
                    continue;
                }
                String parent = file.getAbsolutePath();
                File getInuse = file.listFiles()[0];

                String key = getInuse.getName();
                List<String> values = Stream.concat(
                        Stream.of("File: " + getInuse.getName()),
                        loadModelParams(parent, getInuse.getName()).entrySet().stream()
                                .map(entry -> entry.getKey() + ": " + entry.getValue())
                ).collect(Collectors.toList());

                // "Inuse: true"를 리스트에 추가
                values.add("Inuse: true");

                result.put(key, values);
            }
        }

        for (File file : files) {
            String fileName = file.getName();

            List<String> modelParams = Stream.concat(
                    Stream.of("File: " + fileName),
                    loadModelParams(directory.getAbsolutePath(), fileName).entrySet().stream()
                            .map(entry -> entry.getKey() + ": " + entry.getValue())
            ).collect(Collectors.toList());
            modelParams.add("Inuse: false");
            result.put(fileName, modelParams);
        }
        return result;
    }

    public Map<String, Object> loadModelParams(String parentPath ,String fileName) {
        try (Reader reader = new FileReader(parentPath + File.separator + fileName)) {
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);

            Map<String, Object> modelParams = new HashMap<>();
            for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                String key = entry.getKey();
                JsonElement value = entry.getValue();
                if (value.isJsonPrimitive()) {
                    JsonPrimitive primitive = value.getAsJsonPrimitive();
                    if (primitive.isNumber()) {
                        modelParams.put(key, primitive.getAsDouble());
                    } else if (primitive.isBoolean()) {
                        modelParams.put(key, primitive.getAsBoolean());
                    } else {
                        modelParams.put(key, primitive.getAsString());
                    }
                } else if (value.isJsonArray()) {
                    // JSON 배열 처리 로직 추가

                } else if (value.isJsonObject()) {
                    // JSON 객체 처리 로직 추가

                }
            }

            return modelParams;
        } catch (IOException e) {
            // 예외 처리
            throw new EmptyDirectoryException("파일 읽기 실패: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getModelDetail(String exerciseName, String modelVersion) {
        File directory = new File(exerciseAiPath + File.separator + exerciseName);
        System.out.println("file: " + directory.getAbsolutePath());
        if (!directory.exists()) {
            throw new EmptyDirectoryException("디렉토리가 존재하지 않습니다.");
        }

        File[] files = directory.listFiles();
        if (files == null || files.length == 0) {
            throw new EmptyDirectoryException("디렉터리에 파일이 없습니다.");
        }

        List<File> fileList = new ArrayList<>();
        List<File> directories = new ArrayList<>();
        for (File file : directory.listFiles()) {
            if (file.isFile()) {
                fileList.add(file);
            } else {
                directories.add(file);
            }
        }
        System.out.println("files: " + fileList);
        System.out.println("directories: " + directories);

        Map<String, Object> result = new HashMap<>();
        result.put("modelVersion", modelVersion);

        // directory의 파일 중 modelVersion으로 시작하는 파일 찾기
        Pattern versionPattern = Pattern.compile("ver-?(\\d+(?:\\.\\d+)*)"); // 버전 번호를 추출하는 정규식 패턴

        File modelFile = null;
        long createdTime = 0;

        for (File file : files) {
            String fileName = file.getName();
            Matcher matcher = versionPattern.matcher(fileName);

            if (matcher.find()) {
                String version = matcher.group(1);
                System.out.println("version: " + version);
                if (version.equals(modelVersion)) {
                    modelFile = file;
                    createdTime = file.lastModified();
                    break;
                }
            } else {
                if (file.isDirectory()) {
                    for (File subFile : file.listFiles()) {
                        String subFileName = subFile.getName();
                        Matcher subMatcher = versionPattern.matcher(subFileName);

                        if (subMatcher.find()) {
                            String version = subMatcher.group(1);
                            System.out.println("version: " + version);
                            if (version.equals(modelVersion)) {
                                modelFile = subFile;
                                createdTime = subFile.lastModified();
                                break;
                            }
                        }
                    }
                }

            }
        }
        System.out.println("modelFile: " + modelFile);

        if (modelFile == null) {
            throw new EmptyDirectoryException("모델 버전 파일을 찾을 수 없습니다.");
        }

        String parentPath = modelFile.getParentFile().getName();

        if (parentPath.equals("Inuse")) {
            result.put("Inuse", true);
        } else {
            result.put("Inuse", false);
        }
        System.out.println("parent: " + parentPath);

        result.put("modelFile", modelFile.getName());
        result.put("modelParams", loadModelParams(modelFile.getParentFile().getAbsolutePath(), modelFile.getName()));
        result.put("createdTime", createdTime);
        return result;
    }

    @Override
    public String applyModel(String exerciseName, String modelVersion, String params) {
        String[] words = params.split(" ");

        // 모델을 Inuse 디렉터리로 이동
        File directory = new File(exerciseAiPath + File.separator + exerciseName);
        if (!directory.exists()) {
            throw new EmptyDirectoryException("디렉토리가 존재하지 않습니다.");
        }

        File[] files = directory.listFiles();
        if (files == null || files.length == 0) {
            throw new EmptyDirectoryException("디렉터리에 파일이 없습니다.");
        }

        File inuseDir = new File(directory, "Inuse");
        if (!inuseDir.exists()) {
            inuseDir.mkdirs();
        }

        File modelFile = null;
        for (File file : files) {
            String[] version = file.getName().split("-");
            System.out.println("version: " + Arrays.toString(version));
            String versionStr = "";
            if (version.length > 1) {
                if (version[1].contains("_")) {
                    versionStr = version[1].split("_")[0];
                    System.out.println("versionStr: " + versionStr);
                    if (versionStr.equals(modelVersion)) {
                        modelFile = file;
                        break;
                    }
                } else {
                    versionStr = version[1];
                    if (versionStr.equals(modelVersion)) {
                        modelFile = file;
                        break;
                    }
                }
            }
            System.out.println("version: " + versionStr);
            if (version.equals(modelVersion)) {
                modelFile = file;
                break;
            }
        }

        System.out.println("modelFile: " + modelFile.getAbsolutePath());

        if (modelFile == null) {
            throw new EmptyDirectoryException("모델 버전 파일을 찾을 수 없습니다.");
        }

        File inuseFile = new File(inuseDir, modelFile.getName());
//        try {
//            Files.copy(modelFile.toPath(), inuseFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
//        } catch (IOException e) {
//            throw new RuntimeException("모델 파일 복사 실패: " + e.getMessage());
//        }
        System.out.println("inuseFile: " + inuseFile.getAbsolutePath());
        if (!modelFile.renameTo(inuseFile)) {
            throw new FileStoreException("모델 파일 이동 실패");
        }

        // Inuse 디렉터리에 있는 모델 파일의 Inuse 값을 true로 변경
        String fileContent = null;
        try (FileReader reader = new FileReader(inuseFile)) {
            fileContent = new String(Files.readAllBytes(inuseFile.toPath()));
        } catch (IOException e) {
            throw new FileStoreException("모델 파일 읽기 실패: " + e.getMessage());
        }

        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(fileContent, JsonObject.class);
        jsonObject.addProperty("Inuse", true);

        try (FileWriter writer = new FileWriter(inuseFile)) {
            writer.write(gson.toJson(jsonObject));
        } catch (IOException e) {
            throw new FileStoreException("모델 파일 수정 실패: " + e.getMessage());
        }

        return "모델 적용 성공";
    }

    @Override
    public String deleteModel(String exerciseName, String modelVersion) {
        File directory = new File(exerciseAiPath + File.separator + exerciseName);
        if (!directory.exists()) {
            throw new EmptyDirectoryException("디렉토리가 존재하지 않습니다.");
        }

        File[] files = directory.listFiles();
        if (files == null || files.length == 0) {
            throw new EmptyDirectoryException("디렉터리에 파일이 없습니다.");
        }

        File modelFile = null;
        for (File file : files) {
            if (file.getName().equals(modelVersion)) {
                modelFile = file;
                break;
            }
        }

        if (modelFile == null) {
            throw new EmptyDirectoryException("모델 버전 파일을 찾을 수 없습니다.");
        }

        if (!modelFile.delete()) {
            throw new FileStoreException("모델 파일 삭제 실패");
        }

        return "모델 삭제 성공";
    }
}
