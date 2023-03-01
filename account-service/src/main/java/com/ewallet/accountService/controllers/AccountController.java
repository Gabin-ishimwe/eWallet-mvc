package com.ewallet.accountService.controllers;

import com.ewallet.accountService.dto.AccountRequestDto;
import com.ewallet.accountService.dto.AccountResponseDto;
import com.ewallet.accountService.dto.TransferRequestDto;
import com.ewallet.accountService.exceptions.NotFoundException;
import com.ewallet.accountService.services.AccountService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/account")
@Api(tags = "Account")
@EnableTransactionManagement
public class AccountController {
    @Autowired
    private AccountService accountService;

//    @GetMapping("/balance")
//    @PreAuthorize("hasRole('USER')")
//    @ApiOperation(
//            value = "User Balance Account",
//            notes = "API for user to get balance money on their account"
//    )
//    public AccountResponseDto balanceMoney() throws NotFoundException {
//        return accountService.balanceAccount();
//    }
    @PostMapping
    @CircuitBreaker(name = "accountService", fallbackMethod = "fallbackCreateMethod")
    public AccountResponseDto createAccount() {
        System.out.println("Account created-----");
        return accountService.createAccount();
    }

    public ResponseEntity<?> fallbackCreateMethod(RuntimeException e) throws Exception {
        throw new Exception("Create account service malfunctioning, Try again later !!!");
    }

    @PostMapping("/deposit")
    @CircuitBreaker(name = "accountService", fallbackMethod = "fallbackDepositMethod")
    @ApiOperation(
            value = "User deposit money",
            notes = "API for user to deposit money on their account"
    )
    public AccountResponseDto depositMoney(@RequestBody @Valid AccountRequestDto accountRequestDto) throws NotFoundException {
        return accountService.depositMoney(accountRequestDto);
    }

    public ResponseEntity<?> fallbackDepositMethod(RuntimeException e) throws Exception {
        throw new Exception("Deposit service malfunctioning, Try again later !!!");
    }

    @PostMapping("/withdraw")
    @ApiOperation(
            value = "User withdraw money",
            notes = "API for user to withdraw money on their account"
    )
    public AccountResponseDto withdrawMoney(@RequestBody @Valid AccountRequestDto accountRequestDto) throws NotFoundException {
        return accountService.withdrawMoney(accountRequestDto);
    }

    @PostMapping("/transfer")
    @ApiOperation(
            value = "User transfer money",
            notes = "API for user to transfer money on other account"
    )
    public AccountResponseDto transferMoney(@RequestBody @Valid TransferRequestDto transferRequestDto) throws NotFoundException {
        return accountService.transferMoney(transferRequestDto);
    }
}
