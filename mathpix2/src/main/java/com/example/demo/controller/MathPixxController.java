package com.example.demo.controller;

import com.example.demo.repository.OptionsDataRepository;
import com.example.demo.service.CsvExport;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/mathpix2")
@CrossOrigin("*")
public class MathPixxController {



    @Autowired
    OptionsDataRepository optionsDataRepository;

    @Autowired
    private CsvExport csvExport;





    @GetMapping("/export")
    public void exportQuestionsToCsv(@RequestParam(defaultValue = "0") int offset, HttpServletResponse response) throws IOException {
        csvExport.writeCsvToResponse(response, offset);
    }
    
    @PostMapping(value = "/upload", consumes = "multipart/form-data", produces = "text/csv")
    public void processCsvFile(@RequestParam("file") MultipartFile file, HttpServletResponse response) throws IOException {
        if (file.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "File is empty");
            return;
        }

        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=questionInfo.csv");

        csvExport.processCsvFile(file, response);
    }


}