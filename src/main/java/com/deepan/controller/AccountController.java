package com.deepan.controller;


import com.deepan.dto.Response;
import com.deepan.exceptions.AccountNotFoundException;
import com.deepan.exceptions.TransactionAlreadyExist;
import com.deepan.model.Account;
import com.deepan.model.Transaction;
import com.deepan.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1")
public class AccountController {

    @Autowired
    private AccountService service;

    @GetMapping("/test")
    public String testMethod() {

        return "successfull request";
    }

    @GetMapping(value = "/accounts")
    public ResponseEntity<List<Account>> getAllAccounts(@RequestHeader(name = "x-api-version",required = false) String version) {

        return new ResponseEntity<List<Account>>(service.allAccounts(version), HttpStatus.OK);
    }

    @PostMapping(value = "/accounts")
    public ResponseEntity<?> addAccount(@RequestBody Account account) {

        ResponseEntity<?> responseEntity;

        try {
            Account account1 = service.addAccount(account);
            responseEntity = new ResponseEntity<Account>(account, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            responseEntity = new ResponseEntity<String>("Duplicate Account", HttpStatus.CONFLICT);
        }

        return responseEntity;
    }

    @PostMapping(value = "/accounts/{accNo}/transactions")
    public ResponseEntity<?> addTransaction(@RequestBody Transaction transaction, @PathVariable("accNo") String accountNumber ) {

        ResponseEntity<?> responseEntity;

        try {
            Transaction transaction1 = service.addNewTransaction(transaction,accountNumber);
            responseEntity = new ResponseEntity<Response>(new Response("Success", "Transaction added successfully"), HttpStatus.CREATED);
            System.out.println("try block executed");
        }

        catch (TransactionAlreadyExist e) {
            Response response = new Response("Failed","Transaction Already Present");
            responseEntity = new ResponseEntity<Response>(response, HttpStatus.CONFLICT);
            System.out.println("transaction already exist");
        }
        catch (AccountNotFoundException e) {
            responseEntity = new ResponseEntity<Response>(new Response("Failed","Account not found"), HttpStatus.NOT_FOUND);
            System.out.println("transaction already exist");
        }

        System.out.println("response is returned");
        return responseEntity;
    }

    @GetMapping(value = "/accounts/{accNo}/transactions")
    public ResponseEntity<?> getTransactions(@PathVariable("accNo") String accountNumber,
                                             @RequestParam(name = "type",required = false) String type,
                                             @RequestParam(name = "sort",required = false) String sort,
                                             @RequestParam(name = "order",required = false) String order
                                             ) {

        ResponseEntity<?> responseEntity;

        try {
            List<Transaction> transactionList = service.getTransactions(accountNumber,type,sort,order);
            responseEntity = new ResponseEntity<List<Transaction>>(transactionList, HttpStatus.CREATED);
        }
        catch (AccountNotFoundException e) {
            responseEntity = new ResponseEntity<Response>(new Response("Failed","Account not found"), HttpStatus.NOT_FOUND);
        }

        System.out.println("response is returned");
        return responseEntity;
    }

}
//jzcl