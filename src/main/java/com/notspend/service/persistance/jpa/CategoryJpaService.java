package com.notspend.service.persistance.jpa;

import com.notspend.entity.Category;
import com.notspend.repository.CategoryRepository;
import com.notspend.repository.ExpenseRepository;
import com.notspend.service.persistance.CategoryService;
import com.notspend.service.persistance.UserService;
import com.notspend.util.SecurityUserHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Profile({"default", "jpa"})
public class CategoryJpaService implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final ExpenseRepository expenseRepository;

    private final UserService userService;

    @Override
    public void addCategory(Category category) {
        validateCategoryOwner(category);
        categoryRepository.save(category);
    }

    @Override
    public List<Category> getAllExpenseCategories() {
        return categoryRepository.getByIncomeAndUserUsername(false, userService.currentUser());
    }

    @Override
    public List<Category> getAllIncomeCategories() {
        return categoryRepository.getByIncomeAndUserUsername(true, userService.currentUser());
    }

    @Override
    public Category getCategory(int id) {
        return categoryRepository.getByIdAndUserUsername(id, userService.currentUser()).orElseThrow(
                () -> new NoSuchElementException("There is no category with id " + id + " in repository" +
                        "for current user " + SecurityUserHandler.getCurrentUser())
        );
    }

    @Override
    public Category getCategory(String name) {
        return categoryRepository.getByNameAndUserUsername(name, userService.currentUser()).orElseThrow(
                () -> new NoSuchElementException("There is no category with name " + name + " in repository" +
                        "for current user " + SecurityUserHandler.getCurrentUser())
        );
    }

    @Override
    @Transactional
    public void deleteCategoryById(int id) {
        categoryRepository.deleteByIdAndUserUsername(id, userService.currentUser());
    }

    @Override
    public void updateCategory(Category category) {
        validateCategoryOwner(category);
        categoryRepository.save(category);
    }

    @Override
    public boolean isCategoryNameExist(Category category) {
        return categoryRepository.existsByNameAndUserUsername(category.getName(), userService.currentUser());
    }

    @Override
    public boolean isCategoryHaveRelations(int id) {
        return expenseRepository.existsByCategoryIdAndUserUsername(id, userService.currentUser());
    }

    @Override
    public int replaceCategoryInAllExpenses(int oldCategoryId, int newCategoryId) {
        Category oldCategory = getCategory(oldCategoryId);
        Category newCategory = getCategory(newCategoryId);
        int updated = expenseRepository.updateCategoryInExpenses(oldCategory, newCategory, userService.currentUser());
        deleteCategoryById(oldCategoryId);
        return updated;
    }

    private void validateCategoryOwner(Category category) {
        if (!userService.currentUser().equals(category.getUser().getUsername())) {
            throw new IllegalArgumentException("Operation with another user's category is prohibited");
        }
    }
}
