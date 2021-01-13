package com.notspend.repository;

import com.notspend.entity.Category;
import com.notspend.entity.User;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends CrudByUserRepository<Category, Integer> {

    /**
     * Returns all income categories for {@code true} argument and
     * expense categories for {@code false}
     *
     * @param isIncome denotes whether income or expense catefories should be returned
     * @return income or expense categories
     */
    List<Category> findByIncomeAndUser(boolean isIncome, User user);

    Optional<Category> findByNameAndUser(String name, User user);
}
