package com.notspend.service;

import com.notspend.entity.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    void addCategory(Category category);
    List<Category> getAllExpenseCategories();
    List<Category> getAllIncomeCategories();
    Optional<Category> getCategory(int id);
    void deleteCategoryById(int id);
    void updateCategory(Category category);

    boolean isCategoryNameExist(Category category);
    boolean isCategoryHaveRelations(int id);
    int replaceCategoryInAllExpenses(int fromCategoryId, int toCategoryId);

}
