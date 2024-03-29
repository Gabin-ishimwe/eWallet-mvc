package com.ewallet.notification.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransactionRequestDto {
    private UUID fromAccount;
    private UUID toAccount;
    private Long amount;
    private TypeTransaction typeTransaction;
}