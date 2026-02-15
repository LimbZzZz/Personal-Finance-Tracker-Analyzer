package org.example.Service;

import lombok.extern.slf4j.Slf4j;
import org.example.DTO.Response.CompanyDtoResponse;
import org.example.Entity.Company;
import org.example.Entity.Transaction;
import org.example.Repository.CompanyRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CompanyService extends BaseService<Company, Long>{
    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        super(companyRepository);
        this.companyRepository = companyRepository;
    }

    public List<CompanyDtoResponse> findAllCompanies(){
        List<Company> companies = companyRepository.findAll();
        return companies.stream()
                .map(this::mapToDtoCompany)
                .toList();
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
