package com.ewallet.transaction.controller;

import com.ewallet.transaction.dto.TransactionRequestDto;
import com.ewallet.transaction.dto.TransactionResponseDto;
import com.ewallet.transaction.service.TransactionService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/transaction")
@Api(tags = "Transaction")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public TransactionResponseDto commitTransaction(@RequestBody TransactionRequestDto transactionRequestDto) {
       return transactionService.commitTransaction(transactionRequestDto);
    }

//    @GetMapping
////    @PreAuthorize("hasRole('USER')")
//    @ApiOperation(
//            value = "User's transaction",
//            notes = "API to get all transactions of user"
//    )
//    public TransactionResponseDto getAllTransactions() throws NotFoundException {
//        return transactionService.getAllUserTransaction();
//    }
//
//    @GetMapping("/{transactionId}")
////    @PreAuthorize("hasRole('USER')")
//    @ApiOperation(
//            value = "User's transaction",
//            notes = "API to get one transaction by Id of user"
//    )
//    public TransactionResponseDto getOneTransactions(@PathVariable Long transactionId) throws NotFoundException {
//        return transactionService.getOneUserTransaction(transactionId);
//    }
}
