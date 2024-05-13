package com.example.fitness_trAIner.service.ai;

import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public interface AIService {
    public String pythonProcess(String data) throws IOException;
    public String uploadFiles(List<MultipartFile> files, String parentPath, String uploadPath) throws IOException;
    public List<String> filesNameView(String parentPath);
    public void filesView(String parentPath, String filePath, ByteArrayOutputStream baos) throws IOException;
    public String deleteFiles(String parentPath, String deletePath) throws IOException;
    public void startTraining(String pythonFilePath, String params) throws Exception;

}
