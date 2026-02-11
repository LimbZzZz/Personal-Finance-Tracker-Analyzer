package org.example.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.Entity.Account;
import org.example.Repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    public Account createAccount(Account account){
        if(accountRepository.existsByCardNumber(account.getCardNumber())){
            throw new IllegalArgumentException("Такая карта уже существует");
        }
        return accountRepository.save(account);
    }

    public Double getCurrentBalanceFromOnceCard(Long id){
        return getAccountOrThrowException(id).getTotalAccount();
    }

    public void subtractFromAccount(Long id, Double subtract){
        if(subtract <= 0){
            throw new IllegalArgumentException("Сумма должна быть положительной");
        }
        Account account = getAccountOrThrowException(id);
        if(account.getTotalAccount() < subtract){
            throw new IllegalArgumentException("Не хватает средств");
        }
        account.setTotalAccount(account.getTotalAccount() - subtract);
    }

    public void addToAccount(Long id, Double add){
        if(add <= 0){
            throw new IllegalArgumentException("Сумма должна быть больше 0");
        }
        Account account = getAccountOrThrowException(id);
        account.setTotalAccount(account.getTotalAccount() + add);
    }

    public Account getAccountById(Long id){
        return getAccountOrThrowException(id);
    }

    public List<Account> getAllAccounts(){
        return accountRepository.findAll();
    }

    public void dropAccount(Long id){
        Account account = getAccountOrThrowException(id);
        accountRepository.delete(account);
    }

    private Account getAccountOrThrowException(Long id){
        return accountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Счет не найден"));
    }
}
