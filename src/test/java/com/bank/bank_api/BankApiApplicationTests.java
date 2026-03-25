package com.bank.bank_api;

import com.bank.bank_api.exception.AccountNotFoundException;
import com.bank.bank_api.exception.InsufficientBalanceException;
import com.bank.bank_api.model.Account;
import com.bank.bank_api.model.Transaction;
import com.bank.bank_api.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BankApiApplicationTests {

    @Autowired
    private AccountService accountService;

    @Test
    void testCreateAccount() {
        Account account = new Account("Test User", "SAVINGS", 5000.0);
        Account saved = accountService.createAccount(account);

        assertNotNull(saved);
        assertNotNull(saved.getId());
        assertEquals("Test User", saved.getAccountHolderName());
        assertEquals(5000.0, saved.getBalance());
    }

    @Test
    void testDeposit() {
        Account account = new Account("Deposit User", "SAVINGS", 1000.0);
        Account saved = accountService.createAccount(account);

        Account updated = accountService.deposit(saved.getId(), 500.0);

        assertEquals(1500.0, updated.getBalance());
    }

    @Test
    void testWithdrawInsufficientBalance() {
        Account account = new Account("Poor User", "SAVINGS", 100.0);
        Account saved = accountService.createAccount(account);

        assertThrows(InsufficientBalanceException.class, () -> {
            accountService.withdraw(saved.getId(), 500.0);
        });
    }

    @Test
    void testTransfer() {
        Account from = accountService.createAccount(new Account("Sender", "SAVINGS", 5000.0));
        Account to = accountService.createAccount(new Account("Receiver", "SAVINGS", 1000.0));

        accountService.transfer(from.getId(), to.getId(), 2000.0);

        Account updatedFrom = accountService.getAccountById(from.getId());
        Account updatedTo = accountService.getAccountById(to.getId());

        assertEquals(3000.0, updatedFrom.getBalance());
        assertEquals(3000.0, updatedTo.getBalance());
    }

    @Test
    void testAccountNotFound() {
        assertThrows(AccountNotFoundException.class, () -> {
            accountService.getAccountById(99999L);
        });
    }

    @Test
    void testTransactionHistory() {
        Account account = accountService.createAccount(new Account("History User", "SAVINGS", 5000.0));
        accountService.deposit(account.getId(), 1000.0);
        accountService.withdraw(account.getId(), 500.0);

        List<Transaction> transactions = accountService.getTransactionsByAccountId(account.getId());

        assertEquals(2, transactions.size());
    }
}
