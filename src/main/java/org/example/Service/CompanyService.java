package org.example.Service;

import lombok.extern.slf4j.Slf4j;
import org.example.Entity.Company;
import org.example.Repository.CompanyRepository;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CompanyService extends BaseService<Company, Long>{
    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        super(companyRepository);
        this.companyRepository = companyRepository;
    }

}
