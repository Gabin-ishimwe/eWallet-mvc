package com.ewallet.accountService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TransactionResponseDto {
    private String message;

    private List<Transaction> transactions;
}
