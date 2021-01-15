package com.notspend.service.jpa;

import com.notspend.entity.Authority;
import com.notspend.repository.AuthorityRepository;
import com.notspend.service.AuthorityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorityJpaService implements AuthorityService {

    private final AuthorityRepository authorityRepository;

    @Override
    public void saveAuthority(Authority authority) {
        authorityRepository.save(authority);
    }
}
