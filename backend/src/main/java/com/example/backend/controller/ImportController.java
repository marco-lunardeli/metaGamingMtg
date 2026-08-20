package com.example.backend.controller;

import com.example.backend.service.CsvImportService;
import com.example.backend.service.CsvImportService.ImportReport;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/import")
public class ImportController {

    private final CsvImportService csvImportService;

    public ImportController(CsvImportService csvImportService) {
        this.csvImportService = csvImportService;
    }

    @PostMapping("/games")
    public ResponseEntity<?> importGames(@RequestParam("file") MultipartFile file) {
        try {
            ImportReport report = csvImportService.importCsv(file);
            return ResponseEntity.ok(report);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
