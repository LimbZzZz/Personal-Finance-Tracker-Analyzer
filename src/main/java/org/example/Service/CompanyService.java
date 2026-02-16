package org.example.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.CustomException.CompanyNotFoundException;
import org.example.DTO.Response.CompanyDtoResponse;
import org.example.Entity.Company;
import org.example.Entity.Transaction;
import org.example.Repository.CompanyRepository;
import org.example.Service.AOP.LogExecutionTime;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompanyService{
    private final CompanyRepository companyRepository;

    @LogExecutionTime
    public List<CompanyDtoResponse> findAllCompanies(){
        List<Company> companies = companyRepository.findAll();
        return companies.stream()
                .map(this::mapToDtoCompany)
                .toList();
    }

    @LogExecutionTime
    public CompanyDtoResponse findByCompanyId(Long id){
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new CompanyNotFoundException(id));
        return mapToDtoCompany(company);
    }
    private CompanyDtoResponse mapToDtoCompany(Company company){
        CompanyDtoResponse dtoResponse = CompanyDtoResponse.builder()
                .name(company.getName())
                .transactionMap(
                        company.getTransactions().stream()
                                .collect(Collectors.toMap(
                                        Transaction::getId,
                                        Transaction::getDate,
                                        (existing, replacement) -> existing,
                                        LinkedHashMap::new
                                ))
                )
                .build();
        return dtoResponse;
    }

}
