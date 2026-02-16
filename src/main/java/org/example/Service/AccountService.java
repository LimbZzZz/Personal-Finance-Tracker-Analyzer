package org.example.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.CustomException.AccountAlreadyExistException;
import org.example.CustomException.AccountNotFoundException;
import org.example.CustomException.InsufficientFundsException;
import org.example.CustomException.UserNotFoundException;
import org.example.DTO.Request.AccountDtoRequest;
import org.example.DTO.Response.AccountDtoResponse;
import org.example.Entity.Account;
import org.example.Entity.User;
import org.example.Repository.AccountRepository;
import org.example.Repository.UserRepository;
import org.example.Service.AOP.LogExecutionTime;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AccountService{
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    @LogExecutionTime
    public AccountDtoResponse createAccount(AccountDtoRequest request){
        if (accountRepository.existsByCardNumber(request.getCardNumber())){
            throw new AccountAlreadyExistException(request.getCardNumber());
        }

        Account newAccount = createAccountEntity(request);
        List<Account> accounts = accountRepository.findAll();
        if(accounts.isEmpty()){
            newAccount.setActive(true);
        }
        Account savedAccount = accountRepository.save(newAccount);
        return mapToAccountResponse(savedAccount);
    }

    private Account createAccountEntity(AccountDtoRequest request){
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException(request.getUserId()));
        Account accountEntity = new Account();
        accountEntity.setTotalAccount(request.getTotalAccount());
        accountEntity.setBank(request.getBank());
        accountEntity.setCardNumber(request.getCardNumber());
        accountEntity.setUser(user);
        return accountEntity;
    }

    private AccountDtoResponse mapToAccountResponse(Account account){
        AccountDtoResponse response = AccountDtoResponse.builder()
                .totalAccount(account.getTotalAccount())
                .bank(account.getBank())
                .cardNumber(account.getCardNumber())
                .userId(account.getUser().getId())
                .build();

        return response;
    }
    @LogExecutionTime
    public BigDecimal getCurrentBalanceFromOnceCard(Long id){
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));
        return account.getTotalAccount();
    }
    @LogExecutionTime
    public BigDecimal subtractFromAccount(BigDecimal subtract, String userEmail){
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException(userEmail));
        Account activeAccount = accountRepository.findByUserIdAndIsActiveTrue(user.getId())
                .orElseThrow(() -> new AccountNotFoundException("У пользователя нет активных счетов"));

        if(subtract == null){
            log.warn("Счет: {}. Сумма списания равна null", activeAccount.getCardNumber());
            throw new IllegalArgumentException("Сумма списания не может быть null");
        }

        if(subtract.signum() <= 0){
            log.warn("Счет: {}. Сумма списания не положительная: {}", activeAccount.getCardNumber(), subtract.signum());
            throw new IllegalArgumentException("Сумма должна быть положительной");
        }

        log.debug("Счет: Текущий баланс счета: {}руб", activeAccount.getTotalAccount());
        if(activeAccount.getTotalAccount().compareTo(subtract) < 0){
            log.warn("Счет: {}.Недостаточно средств на счете: Сумма списания: {}руб, Доступно: {}руб", activeAccount.getCardNumber(), subtract, activeAccount.getTotalAccount());
            throw new InsufficientFundsException(activeAccount.getTotalAccount());
        }

        activeAccount.setTotalAccount(activeAccount.getTotalAccount().subtract(subtract));
        accountRepository.save(activeAccount);
        return activeAccount.getTotalAccount();
    }
    @LogExecutionTime
    public List<AccountDtoResponse> findAllAccounts(){
        return accountRepository.findAll().stream()
                .map(this::mapToAccountResponse)
                .toList();
    }
    @LogExecutionTime
    public BigDecimal addToAccount(BigDecimal add, String userEmail){
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException(userEmail));
        Account activeAccount = accountRepository.findByUserIdAndIsActiveTrue(user.getId())
                .orElseThrow(() -> new AccountNotFoundException("У пользователя нет активных счетов"));

        if(add == null || add.compareTo(BigDecimal.ZERO) <= 0){
            log.error("Счет: {}. Сумма зачисления должна быть больше 0: Сумма зачисления: {}", activeAccount.getCardNumber(), add);
            throw new IllegalArgumentException("Сумма должна быть больше 0");
        }
        activeAccount.setTotalAccount(activeAccount.getTotalAccount().add(add));
        accountRepository.save(activeAccount);
        return activeAccount.getTotalAccount();
    }

    @LogExecutionTime
    public AccountDtoResponse findAccountById(Long id){
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));
        return mapToAccountResponse(account);
    }
    @LogExecutionTime
    public void delete(Long id) {
        log.debug("Удаление аккаунта с id: {}", id);
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));
        accountRepository.delete(account);
    }
    @LogExecutionTime
    public void changeActiveCard(Long id){
        List<Account> accounts = accountRepository.findAll();
        accounts.stream()
                .filter(Account::isActive)
                .findFirst()
                .ifPresent(active -> active.setActive(false));
        accounts.stream()
                .filter(item -> item.getId().equals(id))
                .findFirst()
                .ifPresent(active -> active.setActive(true));
        accountRepository.saveAll(accounts);
    }

}
