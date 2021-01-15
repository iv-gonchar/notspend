package com.notspend.service.jpa;

import com.notspend.repository.MccRepository;
import com.notspend.service.MccService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MccJpaService implements MccService {

    private final MccRepository mccRepository;

    @Override
    public String getCategoryByMccCode(Integer id) {
        return mccRepository.getCategory(id);
    }
}
