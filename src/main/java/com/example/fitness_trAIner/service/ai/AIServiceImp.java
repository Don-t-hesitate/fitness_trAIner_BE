package com.example.fitness_trAIner.service.ai;

import com.example.fitness_trAIner.common.exception.exceptions.AIException;
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
    public String uploadFiles(List<MultipartFile> files, String uploadPath) throws IOException {
        File uploadDir = new File(posePath + File.separator + uploadPath);

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
    public void filesView(String exerciseType, ByteArrayOutputStream baos) throws IOException {
        File directory = new File(posePath + File.separator + exerciseType);
        if (!directory.exists()) {
            throw new FileStoreException("디렉토리가 존재하지 않습니다.");
        }

        ZipOutputStream zipOutputStream = new ZipOutputStream(baos);
        addFilesToZip(zipOutputStream, directory);
        zipOutputStream.close();
    }

    private void addFilesToZip(ZipOutputStream zipOutputStream, File directory) throws IOException {
        File[] files = directory.listFiles();
        if (files == null || files.length == 0) {
            return;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                addFilesToZip(zipOutputStream, file);
            } else {
                addFileToZip(zipOutputStream, file, file.getName());
            }
        }
    }

    private void addFileToZip(ZipOutputStream zipOutputStream, File file, String entryName) throws IOException {
        byte[] buffer = new byte[4096];

        FileInputStream fileInputStream = new FileInputStream(file);
        ZipEntry zipEntry = new ZipEntry(entryName);
        zipOutputStream.putNextEntry(zipEntry);

        int bytesRead;
        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
            zipOutputStream.write(buffer, 0, bytesRead);
        }

        fileInputStream.close();
        zipOutputStream.closeEntry();
    }

}
