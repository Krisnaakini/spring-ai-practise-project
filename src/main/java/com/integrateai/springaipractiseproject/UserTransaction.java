package com.integrateai.springaipractiseproject;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class UserTransaction {

    @Id
    private int id;

    private String username;
    private String transaction;
    private double balance;
    private LocalDateTime transactionDate;
}
