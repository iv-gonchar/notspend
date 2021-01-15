package com.notspend.repository;

import com.notspend.entity.Authority;
import org.springframework.data.repository.Repository;

public interface AuthorityRepository extends Repository<Authority, String> {

    Authority save(Authority authority);
}
