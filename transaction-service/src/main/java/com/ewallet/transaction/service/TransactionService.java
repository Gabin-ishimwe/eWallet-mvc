package com.ewallet.transaction.service;

import com.ewallet.transaction.dto.TransactionRequestDto;
import com.ewallet.transaction.dto.TransactionResponseDto;
import com.ewallet.transaction.entity.Transaction;
import com.ewallet.transaction.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    public TransactionResponseDto commitTransaction(TransactionRequestDto transactionRequestDto) {
        Transaction transaction = Transaction.builder()
                .typeTransaction(transactionRequestDto.getTypeTransaction())
                .fromAccountNumber(transactionRequestDto.getFromAccount())
                .toAccountNumber(transactionRequestDto.getToAccount())
                .transactionTime(new Date(System.currentTimeMillis()))
                .amount(transactionRequestDto.getAmount())
                .build();

        Transaction commitedTransaction = transactionRepository.save(transaction);
        return new TransactionResponseDto("Transaction Successfully committed", List.of(commitedTransaction));
    }


//    public TransactionResponseDto getAllUserTransaction() throws NotFoundException {
//        UserDetails authUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        User findUser = userRepository.findByEmail(authUser.getUsername());
//        if(findUser == null) {
//            throw new NotFoundException("User not found");
//        }
//        return new TransactionResponseDto("User's Transactions", findUser.getTransactions());
//    }
//
//    public TransactionResponseDto getOneUserTransaction(Long transactionId) throws NotFoundException {
//        UserDetails authUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        User findUser = userRepository.findByEmail(authUser.getUsername());
//        if(findUser == null) {
//            throw new NotFoundException("User not found");
//        }
//        return new TransactionResponseDto("One Transaction", findUser.getTransactions().stream().filter(transaction -> Objects.equals(transaction.getId(), transactionId)).toList());
//    }
}
