package org.example.Controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.DTO.Response.CompanyDtoResponse;
import org.example.Service.CompanyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/company")
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyService companyService;

    @GetMapping("/show")
    public ResponseEntity<List<CompanyDtoResponse>> showAllCompanies(){
        log.info("Запрос на получение списка компаний получен");
        return ResponseEntity.ok(companyService.findAllCompanies());
    }
}
