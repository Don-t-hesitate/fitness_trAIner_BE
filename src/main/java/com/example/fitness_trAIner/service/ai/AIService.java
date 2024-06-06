package com.example.fitness_trAIner.service.ai;



import com.example.fitness_trAIner.service.ai.dto.response.AIServiceResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface AIService {

    public AIServiceResponse pythonProcess(String data) throws IOException;
    public String uploadFiles(List<MultipartFile> files, String parentPath, String uploadPath) throws IOException;
    public List<String> getFilesName(String parentPath);
    public void filesView(String parentPath, String filePath, ByteArrayOutputStream baos) throws IOException;
    public String deleteFiles(String parentPath, String deletePath) throws IOException;
    public void startTraining(String pythonFilePath, String exerciseName, String params) throws Exception;
//    public void startTraining(String pythonFilePath, String exerciseName) throws Exception;
    public List<String> getModelList();
    public Map<String, List<String>> getModelInfo(String exerciseName);
    public Map<String, Object> getModelDetail(String exerciseName, String modelVersion);
    // ai 모델 적용
    public String applyModel(String exerciseName, String modelVersion, String params) throws Exception;
    public String deleteModel(String exerciseName, String modelVersion) ;

    ResponseEntity<byte[]> downloadModel(String exerciseName, String exerciseVersion, ByteArrayOutputStream baos);
}
