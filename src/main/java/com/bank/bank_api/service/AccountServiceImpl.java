package com.bank.bank_api.service;

import com.bank.bank_api.exception.AccountNotFoundException;
import com.bank.bank_api.exception.InsufficientBalanceException;
import com.bank.bank_api.model.Account;
import com.bank.bank_api.model.Transaction;
import com.bank.bank_api.repository.AccountRepository;
import com.bank.bank_api.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public Account getAccountById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));
    }

    @Override
    public Account updateAccount(Long id, Account accountDetails) {
        Account account = getAccountById(id);
        account.setAccountHolderName(accountDetails.getAccountHolderName());
        account.setAccountType(accountDetails.getAccountType());
        return accountRepository.save(account);
    }

    @Override
    public void deleteAccount(Long id) {
        Account account = getAccountById(id);
        accountRepository.delete(account);
    }

    @Override
    public Account deposit(Long id, Double amount) {
        Account account = getAccountById(id);
        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);

        Transaction transaction = new Transaction("DEPOSIT", amount, null, id);
        transactionRepository.save(transaction);

        return account;
    }

    @Override
    public Account withdraw(Long id, Double amount) {
        Account account = getAccountById(id);

        if (account.getBalance() < amount) {
            throw new InsufficientBalanceException(
                "Insufficient balance. Available: " + account.getBalance() + ", Requested: " + amount
            );
        }

        account.setBalance(account.getBalance() - amount);
        accountRepository.save(account);

        Transaction transaction = new Transaction("WITHDRAWAL", amount, id, null);
        transactionRepository.save(transaction);

        return account;
    }

    @Override
    public String transfer(Long fromId, Long toId, Double amount) {
        Account fromAccount = getAccountById(fromId);
        Account toAccount = getAccountById(toId);

        if (fromAccount.getBalance() < amount) {
            throw new InsufficientBalanceException(
                "Insufficient balance. Available: " + fromAccount.getBalance() + ", Requested: " + amount
            );
        }

        fromAccount.setBalance(fromAccount.getBalance() - amount);
        toAccount.setBalance(toAccount.getBalance() + amount);

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        Transaction transaction = new Transaction("TRANSFER", amount, fromId, toId);
        transactionRepository.save(transaction);

        return "Transfer successful. " + amount + " transferred from Account " + fromId + " to Account " + toId;
    }

    @Override
    public List<Transaction> getTransactionsByAccountId(Long accountId) {
        return transactionRepository.findByFromAccountIdOrToAccountId(accountId, accountId);
    }
}
