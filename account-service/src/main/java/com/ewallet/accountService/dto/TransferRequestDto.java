package com.ewallet.accountService.dto;

import com.ewallet.userService.validation.uuidValidation.Uuid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransferRequestDto {

    @Min(value = 0)
    private Long amount;

    @Uuid
    private UUID senderAccount;

    @Uuid
    private UUID receiverAccount;
}
