package com.integrateai.springaipractiseproject;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserTransactionRepository extends JpaRepository<UserTransaction, Integer> {
    UserTransaction findTopByUsernameOrderByTransactionDateDesc(String username);

    List<UserTransaction> findTop5ByUsernameOrderByTransactionDateDesc(String username);


}
