package com.bank.bank_api.service;

import com.bank.bank_api.model.Account;
import com.bank.bank_api.model.Transaction;

import java.util.List;

public interface AccountService {
    Account createAccount(Account account);
    List<Account> getAllAccounts();
    Account getAccountById(Long id);
    Account updateAccount(Long id, Account accountDetails);
    void deleteAccount(Long id);
    Account deposit(Long id, Double amount);
    Account withdraw(Long id, Double amount);
    String transfer(Long fromId, Long toId, Double amount);
    List<Transaction> getTransactionsByAccountId(Long accountId);
}
