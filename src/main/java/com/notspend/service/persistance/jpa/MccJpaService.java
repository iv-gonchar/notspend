package com.notspend.service.persistance.jpa;

import com.notspend.repository.MccRepository;
import com.notspend.service.persistance.MccService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Profile({"default", "jpa"})
public class MccJpaService implements MccService {

    private final MccRepository mccRepository;

    @Override
    public String getCategoryByMccCode(Integer id) {
        return mccRepository.getCategory(id);
    }
}
