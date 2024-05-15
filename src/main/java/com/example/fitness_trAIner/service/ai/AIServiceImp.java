package com.example.fitness_trAIner.service.ai;

import com.example.fitness_trAIner.common.exception.exceptions.AIException;
import com.example.fitness_trAIner.common.exception.exceptions.EmptyDirectoryException;
import com.example.fitness_trAIner.common.exception.exceptions.FileStoreException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Service
@Slf4j
public class AIServiceImp implements AIService{

    @Value("${posepath}")
    private String posePath;

    @Override
    public String pythonProcess(String data) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("python", "C:/ai/test.py", data);
        Process process  = processBuilder.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder result = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }
        System.out.println(result);

        try {
            int exitCode = process.waitFor();
            System.out.println("Python script exit code: " + exitCode);
            if (exitCode != 0) {
                throw new AIException("파이썬 파일 실행 결과 오류");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return result.toString();

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
    public List<String> filesNameView(String parentPath) {
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

        ZipOutputStream zipOutputStream = new ZipOutputStream(baos);
        addFilesToZip(zipOutputStream, directory, directory.getName());
        zipOutputStream.close();
    }

    private void addFilesToZip(ZipOutputStream zipOutputStream, File directory, String parentDir) throws IOException {
        File[] files = directory.listFiles();
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
    }

    private void addFileToZip(ZipOutputStream zipOutputStream, File file, String parentDir) throws IOException {
        byte[] buffer = new byte[4096];

        FileInputStream fileInputStream = new FileInputStream(file);
        String entryPath = parentDir + "/" + file.getName(); // 디렉터리 구조 유지를 위해 전체 경로 사용
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
            throw new FileStoreException("삭제할 파일이 존재하지 않습니다.");
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

}
