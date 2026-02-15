package org.example.Service;

import jakarta.transaction.Transactional;
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
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class AccountService extends BaseService<Account, Long>{
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public AccountService(AccountRepository accountRepository, UserRepository userRepository) {
        super(accountRepository);
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

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
    public BigDecimal getCurrentBalanceFromOnceCard(Long id){
        return findById(id).getTotalAccount();
    }

    public BigDecimal subtractFromAccount(BigDecimal subtract){
        log.info("Операция списания со счета началась");
        long currentTime = System.currentTimeMillis();

        Account activeAccount = accountRepository.findByIsActiveTrue()
                .orElseThrow(RuntimeException::new);

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
        log.info("Счет: {}.Операция списания успешно завершилась" +
                "\nВремя выполнения операции: {}мс", activeAccount.getCardNumber(), (System.currentTimeMillis() - currentTime));
        return activeAccount.getTotalAccount();
    }

    public List<AccountDtoResponse> findAllAccounts(){
        return accountRepository.findAll().stream()
                .map(this::mapToAccountResponse)
                .toList();
    }

    public BigDecimal addToAccount(BigDecimal add){
        log.info("Операция зачисления на счет началась");
        long currentTime = System.currentTimeMillis();

        Account activeAccount = accountRepository.findByIsActiveTrue()
                .orElseThrow(RuntimeException::new);

        if(add == null || add.compareTo(BigDecimal.ZERO) <= 0){
            log.error("Счет: {}. Сумма зачисления должна быть больше 0: Сумма зачисления: {}", activeAccount.getCardNumber(), add);
            throw new IllegalArgumentException("Сумма должна быть больше 0");
        }
        activeAccount.setTotalAccount(activeAccount.getTotalAccount().add(add));
        accountRepository.save(activeAccount);
        log.info("Счет: {}. Операция зачисления на счет успешно завершена" +
                "\nВремя выполнения операции: {}мс",activeAccount.getCardNumber() , (System.currentTimeMillis() - currentTime));
        return activeAccount.getTotalAccount();
    }

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
