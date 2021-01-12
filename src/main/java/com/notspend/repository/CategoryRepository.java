package com.notspend.repository;

import com.notspend.entity.Category;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends CrudRepository<Category, Integer> {

    /**
     * Returns all income categories for {@code true} argument and
     * expense categories for {@code false}
     *
     * @param isIncome denotes whether income or expense catefories should be returned
     * @return income or expense categories
     */
    List<Category> findByIncome(boolean isIncome);

    Optional<Category> findByName(String name);
}
