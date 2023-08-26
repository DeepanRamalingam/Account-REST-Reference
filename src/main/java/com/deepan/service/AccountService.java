package com.deepan.service;

import com.deepan.exceptions.AccountNotFoundException;
import com.deepan.exceptions.TransactionAlreadyExist;
import com.deepan.model.Account;
import com.deepan.model.Transaction;
import com.deepan.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Service
public class AccountService {

    @Autowired
    private AccountRepository repo;


    public List<Account> allAccounts(String version){

        List<Account> accounts = repo.findAll();
        if(version != null){
            accounts = getAccounts(version);
        }
        return accounts;
    }

    private List<Account> getAccounts(String version) {

        List<Account> accounts;

        switch (version){

            case "2.0":
                accounts = new ArrayList<>();
                break;
            default:
                accounts = repo.findAll();
                break;

        }

        return accounts;
    }

    public Account addAccount(Account account) {
        Optional<Account> existingAccount = repo.findByAccountNumber(account.getAccountNumber());

        if (existingAccount.isPresent()) {
            throw new RuntimeException("Account already exists.");
        }

        account.setTransactions(new ArrayList<Transaction>());
        repo.save(account);
        return account;
    }


    public Transaction addNewTransaction(Transaction transaction, String accountNo) throws TransactionAlreadyExist, AccountNotFoundException {

        Optional<Account> existingAccount = repo.findByAccountNumber(accountNo);

        if (existingAccount.isPresent()) {

            Account account = existingAccount.get();
            List<Transaction> transactions =  account.getTransactions();

            if(transactions.contains(transaction)){
                throw new TransactionAlreadyExist();
            }
            transactions.add(transaction);
            repo.save(account);
            return transaction;

        }

        throw new AccountNotFoundException();
    }

    public List<Transaction> getTransactions(String accountNumber,String type, String sort, String order) throws AccountNotFoundException {

        Optional<Account> existingAccount = repo.findByAccountNumber(accountNumber);

        if(existingAccount.isPresent()){
            Account account = existingAccount.get();
            List<Transaction> transactions =  account.getTransactions();
            if(type != null){
                transactions = transactions.stream().filter(x->x.getTtype().equals(type)).toList();
            }
            if(sort != null && order != null){
                List<Transaction> sortedTransactions = new ArrayList<>(transactions);
                Comparator<Transaction> comparator = getComparatorForSortProperty(sort);

                if ("desc".equalsIgnoreCase(order)) {
                    Collections.sort(sortedTransactions, comparator.reversed());
                } else {
                    Collections.sort(sortedTransactions, comparator);
                }

                transactions = sortedTransactions;
            }
            return transactions;
        }

        throw new AccountNotFoundException();
    }

    private Comparator<Transaction> getComparatorForSortProperty(String sort) {
        Comparator<Transaction> comparator;

        switch (sort) {
            case "transactionId":
                comparator = Comparator.comparing(Transaction::getTid);
                break;
            case "amount":
                comparator = Comparator.comparing(Transaction::getAmount);
                break;
            case "date":
                comparator = Comparator.comparing(Transaction::getDate);
                break;
            case "ttype":
                comparator = Comparator.comparing(Transaction::getTtype);
                break;
            // Add more cases for other properties if needed
            default:
                comparator = Comparator.comparing(Transaction::getTid); // Default to sorting by transactionId
                break;
        }

        return comparator;
    }
}
