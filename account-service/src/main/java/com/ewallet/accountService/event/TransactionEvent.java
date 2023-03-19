package com.ewallet.accountService.event;

import com.ewallet.accountService.dto.TransactionRequestDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransactionEvent {
    private String message;
    private TransactionRequestDto transaction;
}
