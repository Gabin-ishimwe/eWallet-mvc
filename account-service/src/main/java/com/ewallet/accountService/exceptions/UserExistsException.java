package com.ewallet.accountService.exceptions;

public class UserExistsException extends Exception {
    public UserExistsException(String message) {
        super(message);
    }

}
