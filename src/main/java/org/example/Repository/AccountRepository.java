package org.example.Repository;

import org.example.Entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    boolean existsByCardNumber(String cardNumber);
    Optional<Account> findByIsActiveTrue();
    Optional<Account> findByUserIdAndIsActiveTrue(Long userId);
}
