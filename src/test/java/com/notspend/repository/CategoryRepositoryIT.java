package com.notspend.repository;

import com.notspend.entity.Category;
import com.notspend.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.persistence.EntityManager;
import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.*;

@RepositoryTest
class CategoryRepositoryIT {

    private static final String TEST_USERNAME = "demo";

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private EntityManager entityManager;

    @Test
    void getByIncomeAndUserUsernameIncomeCategories() {
        List<Category> categories = categoryRepository.getByIncomeAndUserUsername(true, TEST_USERNAME);
        assertSoftly(softly -> {
            softly.assertThat(categories.size()).isEqualTo(4);
            softly.assertThat(categories)
                    .extracting(Category::getUser)
                    .extracting(User::getUsername)
                    .allMatch(TEST_USERNAME::equals);
            softly.assertThat(categories).allMatch(Category::isIncome);
        });
    }

    @Test
    void getByIncomeAndUserUsernameOutcomeCategories() {
        List<Category> categories = categoryRepository.getByIncomeAndUserUsername(false, TEST_USERNAME);
        assertSoftly(softly -> {
            softly.assertThat(categories.size()).isEqualTo(9);
            softly.assertThat(categories)
                    .extracting(Category::getUser)
                    .extracting(User::getUsername)
                    .allMatch(TEST_USERNAME::equals);
            softly.assertThat(categories).noneMatch(Category::isIncome);
        });
    }

    @Test
    void getByIncomeAndNonExistentUserReturnsEmptyList() {
        List<Category> categories = categoryRepository.getByIncomeAndUserUsername(true, "non_existent_user");
        assertThat(categories).isEmpty();
    }

    @Test
    void getByNameAndUserUsername() {
        Category salary = categoryRepository.getByNameAndUserUsername("Salary", TEST_USERNAME).get();
        assertEquals("Salary", salary.getName());
    }

    @Test
    void getByNonExistentNameAndUserUsernameReturnsEmptyOptional() {
        assertThat(categoryRepository.getByNameAndUserUsername("non_existent", TEST_USERNAME)).isEmpty();
    }

    @Test
    void existsByNameAndUserUsername() {
        assertTrue(categoryRepository.existsByNameAndUserUsername("Salary", TEST_USERNAME));
    }

    @Test
    void existsByNameAndUserUsernameReturnsFalseIfUserNotExist() {
        assertFalse(categoryRepository.existsByNameAndUserUsername("Salary", "non_existent"));
    }

    @Test
    void getByIdAndUserUsername() {
        Category salary = categoryRepository.getByNameAndUserUsername("Salary", TEST_USERNAME).get();
        Category salaryById = categoryRepository.getByIdAndUserUsername(salary.getId(), TEST_USERNAME).get();
        assertEquals(salary, salaryById);
    }

    @Test
    void getAllByUsername() {
        List<Category> categories = categoryRepository.getAllByUserUsername(TEST_USERNAME);
        assertSoftly(softly -> {
            softly.assertThat(categories.size()).isEqualTo(13);
            softly.assertThat(categories)
                    .extracting(Category::getUser)
                    .extracting(User::getUsername)
                    .allMatch(TEST_USERNAME::equals);
        });
    }

    @Test
    void getAllByNonExistentUsernameReturnsEmptyList() {
        List<Category> categories = categoryRepository.getAllByUserUsername("non_existent");
        assertThat(categories).isEmpty();
    }

    @Test
    void saveCategoryWithoutNameThrowsException() {
        Category category = new Category();
        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> {
                    categoryRepository.save(category);
                    entityManager.flush();
                })
                .withMessageStartingWith("Validation failed for classes [com.notspend.entity.Category] during persist time");
    }

    // TODO apply @NotNull annotation on Category.user as such constraint exist on database table and rewrite test
    @Test
    void saveCategoryWithoutUserThrowsException() {
        Category category = new Category();
        category.setName("NewCategory");
        assertThatExceptionOfType(DataIntegrityViolationException.class)
                .isThrownBy(() -> {
                    categoryRepository.save(category);
                    entityManager.flush();
                })
                .havingRootCause()
                .withMessage("Column 'username' cannot be null");
    }

    @Test
    void saveCorrectCategory() {
        String expectedCategoryName = "NewCategory";

        User testUser = userRepository.getByUsername(TEST_USERNAME).get();
        Category category = new Category();
        category.setName(expectedCategoryName);
        category.setUser(testUser);
        categoryRepository.save(category);
        entityManager.flush();
        String savedCategoryName = jdbcTemplate.queryForObject(
                "SELECT name FROM category WHERE name = ?", new Object[]{expectedCategoryName}, String.class);
        assertEquals(expectedCategoryName, savedCategoryName);
    }

    @Test
    void deleteByIdAndUserUsernameThrowsExceptionDueToConstraintOnExpense() {
        Integer salaryCategoryId = jdbcTemplate.queryForObject(
                "SELECT category_id FROM category WHERE name = ?", new Object[]{"Salary"}, Integer.class);

        assertThatExceptionOfType(DataIntegrityViolationException.class)
                .isThrownBy(() -> {
                    categoryRepository.deleteByIdAndUserUsername(salaryCategoryId, TEST_USERNAME);
                    // next line is not correct in scope of this test, but it is here to show an example
                    // that flush() is done also before get/find queries
                    categoryRepository.getByIdAndUserUsername(salaryCategoryId, TEST_USERNAME);
                })
                .havingRootCause()
                .withMessageStartingWith("Cannot delete or update a parent row: a foreign key constraint fails");
    }
}