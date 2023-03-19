package com.ewallet.accountService.services;

import com.ewallet.accountService.dto.*;
import com.ewallet.accountService.entity.Account;
import com.ewallet.accountService.event.TransactionEvent;
import com.ewallet.accountService.exceptions.NotFoundException;
import com.ewallet.accountService.kafka.AccountEvent;
import com.ewallet.accountService.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.UUID;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private WebClient.Builder webClient;

    @Autowired
    private AccountEvent accountEvent;

    @Autowired
    private KafkaTemplate<String, TransactionEvent> kafkaTemplate;

    public AccountResponseDto createAccount() {
        Account account = Account.builder()
                .account_number(UUID.randomUUID())
                .balance(0L)
                .build();

        Account createdAccount = accountRepository.save(account);
        return new AccountResponseDto("Account created Successfully", createdAccount);
    }

    @Transactional
    public AccountResponseDto depositMoney(AccountRequestDto accountRequestDto) throws NotFoundException {
        Account findAccount = accountRepository.findById(accountRequestDto.getAccountNumber()).orElseThrow(() -> new NotFoundException("Account number not found"));
        Long newBalance = findAccount.getBalance() + accountRequestDto.getMoney();
        findAccount.setBalance(newBalance);
        Account depositAccount = accountRepository.save(findAccount);

        TransactionRequestDto depositTransaction = TransactionRequestDto.builder()
                .toAccount(findAccount.getAccount_number())
                .typeTransaction(TypeTransaction.DEPOSIT)
                .amount(accountRequestDto.getMoney())
                .build(); ;
        System.out.println("before===============================");
        // call transaction service
        TransactionResponseDto transactionResponseDto = webClient.build().post()
                .uri("http://localhost:8083/api/v1/transaction")
                .bodyValue(depositTransaction)
                .retrieve()
                .bodyToMono(TransactionResponseDto.class)
                .block();
        System.out.println("after=================================");
        // notification
        // kafkaTemplate.send("transactionNotification", new TransactionEvent("deposit transaction", depositTransaction));
        accountEvent.sendMessage(new TransactionEvent("deposit transaction", depositTransaction));


        return new AccountResponseDto(
                "Amount deposited Successful",
                depositAccount
        );
    }

    @Transactional
    public AccountResponseDto withdrawMoney(AccountRequestDto accountRequestDto) throws NotFoundException {
        Account findAccount = accountRepository.findById(accountRequestDto.getAccountNumber()).orElseThrow(() -> new NotFoundException("Account number not found"));
        long remainingBalance = findAccount.getBalance() - accountRequestDto.getMoney();
        if(remainingBalance < 0) {
            throw new IllegalArgumentException("You don't have enough sufficient funds");
        }
        findAccount.setBalance(remainingBalance);
        Account withrawAccount = accountRepository.save(findAccount);

        TransactionRequestDto withdrawTransaction = TransactionRequestDto.builder()
                .toAccount(findAccount.getAccount_number())
                .typeTransaction(TypeTransaction.WITHDRAW)
                .amount(accountRequestDto.getMoney())
                .build(); ;

        // call transaction service
        TransactionResponseDto transactionResponseDto = webClient.build().post()
                .uri("http://account-service/api/v1/transaction")
                .bodyValue(withdrawTransaction)
                .retrieve()
                .bodyToMono(TransactionResponseDto.class)
                .block();

        // notification
        // kafkaTemplate.send("transactionNotification", new TransactionEvent("deposit transaction", withdrawTransaction));
        accountEvent.sendMessage(new TransactionEvent("withdraw transaction", withdrawTransaction));

        return new AccountResponseDto(
                "Amount withdrawn Successful",
                withrawAccount
        );
    }

    @Transactional
    public AccountResponseDto transferMoney(TransferRequestDto transferRequestDto) throws NotFoundException {
        Account senderAccount = accountRepository.findById(transferRequestDto.getSenderAccount()).orElseThrow(()-> new NotFoundException("Sender account not found"));
        Account receiveAccount = accountRepository.findById(transferRequestDto.getReceiverAccount()).orElseThrow(() -> new NotFoundException("Receiver account not found"));

        long newBalance = senderAccount.getBalance() - transferRequestDto.getAmount();
        if(newBalance < 0) {
            throw new IllegalArgumentException("You don't have enough sufficient funds");
        }
        senderAccount.setBalance(newBalance);
        Account transferAccount = accountRepository.save(senderAccount);

        // receiver's account
        // adding the amount
        receiveAccount.setBalance(receiveAccount.getBalance() + transferRequestDto.getAmount());
        accountRepository.save(receiveAccount);

        // registering transaction
        TransactionRequestDto withdrawTransaction = TransactionRequestDto.builder()
                .toAccount(receiveAccount.getAccount_number())
                .fromAccount(senderAccount.getAccount_number())
                .typeTransaction(TypeTransaction.TRANSFER)
                .amount(transferRequestDto.getAmount())
                .build(); ;

        // call transaction service
        TransactionResponseDto transactionResponseDto = webClient.build().post()
                .uri("http://transaction-service/api/v1/transaction")
                .bodyValue(withdrawTransaction)
                .retrieve()
                .bodyToMono(TransactionResponseDto.class)
                .block();
        // notification
        // kafkaTemplate.send("transactionNotification", new TransactionEvent("deposit transaction", withdrawTransaction));
        accountEvent.sendMessage(new TransactionEvent("transaction transaction", withdrawTransaction));

        return new AccountResponseDto("Amount Transferred Successfully ",  transferAccount);
    }

//    public AccountResponseDto balanceAccount() throws NotFoundException {
//        UserDetails authUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        User findUser = userRepository.findByEmail(authUser.getUsername());
//        if(findUser == null) {
//            throw new NotFoundException("User not found");
//        }
//        Account userAccount = findUser.getAccount();
//
//        return new AccountResponseDto("Account Balance", userAccount);
//    }
}
