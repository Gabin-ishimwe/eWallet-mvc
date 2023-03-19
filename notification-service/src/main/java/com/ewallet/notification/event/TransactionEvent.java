package com.ewallet.notification.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransactionEvent {
    private String message;
    private TransactionRequestDto transaction;
}
