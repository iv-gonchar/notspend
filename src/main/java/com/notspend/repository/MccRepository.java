package com.notspend.repository;

import com.notspend.entity.Mcc;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

public interface MccRepository extends Repository<Mcc, Integer> {

    @Query("SELECT m.categoryName FROM Mcc AS m WHERE m.mccId = ?1")
    String getCategory(Integer id);
}
