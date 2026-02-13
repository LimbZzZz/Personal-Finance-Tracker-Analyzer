package org.example.Service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.example.CustomException.AccountAlreadyExistException;
import org.example.CustomException.InsufficientFundsException;
import org.example.Entity.Account;
import org.example.Repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
@Transactional
public class AccountService extends BaseService<Account, Long>{
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        super(accountRepository);
        this.accountRepository = accountRepository;
    }

    public Account createAccount(Account account){
        if (accountRepository.findById(account.getId()).isPresent()){
            throw new AccountAlreadyExistException(account.getCardNumber());
        }
        return accountRepository.save(account);
    }
    public BigDecimal getCurrentBalanceFromOnceCard(Long id){
        return findById(id).getTotalAccount();
    }

    public BigDecimal subtractFromAccount(Long id, BigDecimal subtract){
        log.info("Операция списания со счета {} началась", id);
        long currentTime = System.currentTimeMillis();

        if(subtract.signum() <= 0){
            log.warn("Счет: {}. Сумма списания не положительная: {}", id, subtract.signum());
            throw new IllegalArgumentException("Сумма должна быть положительной");
        }
        if(subtract == null){
            log.warn("Счет: {}. Сумма списания равна null", id);
            throw new IllegalArgumentException("Сумма списания не может быть null");
        }
        Account account = findById(id);
        log.debug("Счет: Текущий баланс счета: {}руб", account.getTotalAccount());
        if(account.getTotalAccount().compareTo(subtract) < 0){
            log.warn("Счет: {}.Недостаточно средств на счете: Сумма списания: {}руб, Доступно: {}руб", id, subtract, account.getTotalAccount());
            throw new InsufficientFundsException(account.getTotalAccount());
        }
        account.setTotalAccount(account.getTotalAccount().subtract(subtract));
        accountRepository.save(account);
        log.info("Счет: {}.Операция списания успешно завершилась" +
                "\nВремя выполнения операции: {}мс", id, (System.currentTimeMillis() - currentTime));
        return account.getTotalAccount();
    }

    public BigDecimal addToAccount(Long id, BigDecimal add){
        log.info("Операция зачисления на счет {} началась", id);
        long currentTime = System.currentTimeMillis();
        if(add == null || add.compareTo(BigDecimal.ZERO) <= 0){
            log.error("Счет: {}. Сумма зачисления должна быть больше 0: Сумма зачисления: {}", id, add);
            throw new IllegalArgumentException("Сумма должна быть больше 0");
        }
        Account account = findById(id);
        account.setTotalAccount(account.getTotalAccount().add(add));
        accountRepository.save(account);
        log.info("Счет: {}. Операция зачисления на счет успешно завершена" +
                "\nВремя выполнения операции: {}мс", id, (System.currentTimeMillis() - currentTime));
        return account.getTotalAccount();
    }
}
