package com.notspend.repository;

import com.notspend.entity.Account;

public interface AccountRepository extends CrudByUserRepository<Account, Integer> {

    // todo restrict by user
    void delete(Account account);
}
