package com.example.fitness_trAIner.service.workout;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class WorkOutServiceImp implements WorkOutService {
    private String uploadDir = "./src/video";

    @Override
    public void fileUpload(MultipartFile file) {
        String randomName = UUID.randomUUID().toString();
        Path copyOfLocation = Paths.get(uploadDir + File.separator + StringUtils.cleanPath(randomName));


        System.out.println("fileUpload 들어왔음");
        try{
            System.out.println("file경로 " + copyOfLocation.toString());
            Files.copy(file.getInputStream(), copyOfLocation, StandardCopyOption.REPLACE_EXISTING);
        }catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not store file : " + file.getOriginalFilename());
        }

    }
}
