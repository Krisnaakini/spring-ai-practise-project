package com.integrateai.springaipractiseproject;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserTransactionRepository extends JpaRepository<UserTransaction, Integer> {
    UserTransaction findByUsername(String name);
}
