package org.example.Service;

import lombok.RequiredArgsConstructor;
import org.example.Entity.Company;
import org.example.Repository.CompanyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;

    public void createCompany(Company company){
        companyRepository.save(getCompanyOrThrowException(company.getId()));
    }

    public Company getCompanyById(Long id){
        return getCompanyOrThrowException(id);
    }

    public List<Company> getAllCompanies(){
        return companyRepository.findAll();
    }

    public void dropCompany(Long id){
        Company company = getCompanyOrThrowException(id);
        companyRepository.delete(company);
    }
    private Company getCompanyOrThrowException(Long id){
        return companyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Такая компания уже существует"));
    }
}
