package com.notspend.cotroller;

import com.notspend.repository.RepositoryTestsInitializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@AutoConfigureDataJpa
@ContextConfiguration(classes = ControllerTestConfig.class, initializers = RepositoryTestsInitializer.class)
@TestPropertySource(locations = "classpath:application.properties")
@WebAppConfiguration
class CategoryControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    CategoryController categoryController;

    @BeforeEach
    void setUp() {
    }

    @Test
    void listExpenseCategories() throws Exception {
        mockMvc.perform(
                get("/category/allexpense")
                        .header(HttpHeaders.AUTHORIZATION,
                                "Basic " + Base64Utils.encodeToString("demo:demo".getBytes())))
                .andExpect(status().isOk())
                .andExpect(view().name("category/all"))
                .andExpect(model().attribute("categories", hasSize(9)))
                .andExpect(model().attribute("categories", everyItem(hasProperty("income", is(false)))))
                .andExpect(model().attribute("categoryType", equalTo("expense")));
    }

    @Test
    void listIncomeCategories() throws Exception {
        mockMvc.perform(
                get("/category/allincome")
                        .header(HttpHeaders.AUTHORIZATION,
                                "Basic " + Base64Utils.encodeToString("demo:demo".getBytes())))
                .andExpect(status().isOk())
                .andExpect(view().name("category/all"))
                .andExpect(model().attribute("categories", hasSize(4)))
                .andExpect(model().attribute("categories", everyItem(hasProperty("income", is(true)))))
                .andExpect(model().attribute("categoryType", equalTo("income")));
    }

    @Test
    void addCategory() throws Exception {
        mockMvc.perform(
                get("/category/add")
                        .header(HttpHeaders.AUTHORIZATION,
                                "Basic " + Base64Utils.encodeToString("demo:demo".getBytes())))
                .andExpect(status().isOk())
                .andExpect(view().name("category/add"))
                .andExpect(model().attribute("category", hasProperty("id", is(0))))
                .andExpect(model().attribute("category", hasProperty("name", nullValue())))
                .andExpect(model().attribute("category", hasProperty("description", nullValue())))
                .andExpect(model().attribute("category", hasProperty("user", nullValue())))
                .andExpect(model().attribute("category", hasProperty("income", is(false))));
    }

    @Test
    // Due to this test method marked as transactional entity is not stored in database.
    // Instead transaction is rollbacked after test
    @Transactional
    void processAddCategoryFormSuccessfully() throws Exception {
        mockMvc.perform(
                post("/category/addProcess")
                        .with(csrf().asHeader())
                        .param("name", "TestCategory")
                        .param("description", "Test category description")
                        .param("income", "false")
                        .header(HttpHeaders.AUTHORIZATION,
                                "Basic " + Base64Utils.encodeToString("demo:demo".getBytes())))
                .andExpect(status().isOk())
                .andExpect(view().name("success"));
    }

    @Test
    @Transactional
    void processAddCategoryFormExistingCategoryReturnsNameexistView() throws Exception {
        mockMvc.perform(
                post("/category/addProcess")
                        .with(csrf().asHeader())
                        .param("name", "Salary")
                        .header(HttpHeaders.AUTHORIZATION,
                                "Basic " + Base64Utils.encodeToString("demo:demo".getBytes())))
                .andExpect(status().isOk())
                .andExpect(view().name("category/nameexist"))
                .andExpect(model().attribute("category", hasProperty("name", equalTo("Salary"))));
    }

    @Test
    @Transactional
    void processAddCategoryFormEmptyName() throws Exception {
        mockMvc.perform(
                post("/category/addProcess")
                        .with(csrf().asHeader())
                        .param("name", "")
                        .header(HttpHeaders.AUTHORIZATION,
                                "Basic " + Base64Utils.encodeToString("demo:demo".getBytes())))
                .andExpect(status().isOk())
                .andExpect(view().name("category/add"));
    }

    @Test
    @Transactional
    void deleteCategorySuccessfully() throws Exception {
        mockMvc.perform(
                get("/category/delete")
                        .with(csrf().asHeader())
                        .param("categoryId", "11")
                        .header(HttpHeaders.AUTHORIZATION,
                                "Basic " + Base64Utils.encodeToString("demo:demo".getBytes())))
                .andExpect(status().isOk())
                .andExpect(view().name("success"));
    }

    @Test
    @Transactional
    @Disabled("Controller always returns list of expense categories, but should return list of categories depending on current category")
    void deleteCategoryWithRelatedExpenses() throws Exception {
        mockMvc.perform(
                get("/category/delete")
                        .with(csrf().asHeader())
                        .param("categoryId", "1")
                        .header(HttpHeaders.AUTHORIZATION,
                                "Basic " + Base64Utils.encodeToString("demo:demo".getBytes())))
                .andExpect(status().isOk())
                .andExpect(view().name("category/cantdelete"))
                .andExpect(model().attribute("categoryToDelete", hasProperty("id", equalTo(1))))
                .andExpect(model().attribute("categories", hasSize(4)))
                .andExpect(model().attribute("category", hasProperty("id", equalTo(0))));
    }

    @Test
    @Transactional
    @Disabled("Fails parameter mapping in controller method")
    void deleteCategoryWithCategoryIdAbsent() throws Exception {
        mockMvc.perform(
                get("/category/delete")
                        .with(csrf().asHeader())
                        .header(HttpHeaders.AUTHORIZATION,
                                "Basic " + Base64Utils.encodeToString("demo:demo".getBytes())))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    @Disabled("throws NoSuchElementException - should be fixed to provide meaningful error message")
    void deleteCategoryWithNonExistentCategoryId() throws Exception {
        mockMvc.perform(
                get("/category/delete")
                        .with(csrf().asHeader())
                        .param("categoryId", "123")
                        .header(HttpHeaders.AUTHORIZATION,
                                "Basic " + Base64Utils.encodeToString("demo:demo".getBytes())))
                .andExpect(status().isOk());
    }

    @Test
    void updateCategorySuccessfully() throws Exception {
        mockMvc.perform(
                get("/category/update")
                        .with(csrf().asHeader())
                        .param("categoryId", "1")
                        .header(HttpHeaders.AUTHORIZATION,
                                "Basic " + Base64Utils.encodeToString("demo:demo".getBytes())))
                .andExpect(status().isOk())
                .andExpect(view().name("category/update"))
                .andExpect(model().attribute("category", hasProperty("id", equalTo(1))))
                .andExpect(model().attribute("category", hasProperty("name", equalTo("Salary"))))
                .andExpect(model().attribute("category", hasProperty("description", equalTo("Salary category"))));
    }

    @Test
    @Disabled("throws NoSuchElementException - should be fixed to provide meaningful error message")
    void updateCategoryNonExistingCategory() throws Exception {
        mockMvc.perform(
                get("/category/update")
                        .with(csrf().asHeader())
                        .param("categoryId", "123")
                        .header(HttpHeaders.AUTHORIZATION,
                                "Basic " + Base64Utils.encodeToString("demo:demo".getBytes())))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    void updateProcessIncomeCategory() throws Exception {
        mockMvc.perform(
                post("/category/updateprocess")
                        .with(csrf().asHeader())
                        .param("id", "1")
                        .param("income", "true")
                        .param("name", "SalaryUpdated")
                        .param("description", "UpdatedDescription")
                        .header(HttpHeaders.AUTHORIZATION,
                                "Basic " + Base64Utils.encodeToString("demo:demo".getBytes())))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("allincome"));
    }

    @Test
    @Transactional
    void updateProcessExpenseCategory() throws Exception {
        mockMvc.perform(
                post("/category/updateprocess")
                        .with(csrf().asHeader())
                        .param("id", "6")
                        .param("income", "false")
                        .param("name", "FoodUpdated")
                        .param("description", "UpdatedDescription")
                        .header(HttpHeaders.AUTHORIZATION,
                                "Basic " + Base64Utils.encodeToString("demo:demo".getBytes())))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("allexpense"));
    }

    @Test
    @Transactional
    @Disabled("Transform ConstraintViolationException and return error page")
    void updateProcessIdAbsent() throws Exception {
        mockMvc.perform(
                post("/category/updateprocess")
                        .with(csrf().asHeader())
                        .header(HttpHeaders.AUTHORIZATION,
                                "Basic " + Base64Utils.encodeToString("demo:demo".getBytes())))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("allexpense"));
    }

    @Test
    @Transactional
    @Disabled("Bug should be fixed. updateProcess() allows to save non existing category")
    void updateProcessNonExistingCategory() throws Exception {
        mockMvc.perform(
                post("/category/updateprocess")
                        .with(csrf().asHeader())
                        .param("id", "123")
                        .param("name", "NotExisting")
                        .header(HttpHeaders.AUTHORIZATION,
                                "Basic " + Base64Utils.encodeToString("demo:demo".getBytes())))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("allexpense"));
    }

    @Test
    @Transactional
    void transferToOtherCategoryAndDeleteSuccessfully() throws Exception {
        mockMvc.perform(
                post("/category/transferToExistCategory")
                        .with(csrf().asHeader())
                        .param("categoryId", "6")
                        .param("categoryToDelete", "7")
                        .header(HttpHeaders.AUTHORIZATION,
                                "Basic " + Base64Utils.encodeToString("demo:demo".getBytes())))
                .andExpect(status().isOk())
                .andExpect(view().name("success"));
    }

    @Test
    @Transactional
    void transferToOtherCategoryAndDeleteSameId() throws Exception {
        mockMvc.perform(
                post("/category/transferToExistCategory")
                        .with(csrf().asHeader())
                        .param("categoryId", "6")
                        .param("categoryToDelete", "6")
                        .header(HttpHeaders.AUTHORIZATION,
                                "Basic " + Base64Utils.encodeToString("demo:demo".getBytes())))
                .andExpect(status().isOk())
                .andExpect(view().name("success"));
    }

    @Test
    @Transactional
    @Disabled("Fix bug - should return error page")
    void transferToOtherCategoryAndDeleteNonExistentId() throws Exception {
        mockMvc.perform(
                post("/category/transferToExistCategory")
                        .with(csrf().asHeader())
                        .param("categoryId", "6")
                        .param("categoryToDelete", "123")
                        .header(HttpHeaders.AUTHORIZATION,
                                "Basic " + Base64Utils.encodeToString("demo:demo".getBytes())))
                .andExpect(status().isOk())
                .andExpect(view().name("success"));
    }

    @Test
    @Transactional
    void transferToNewCategoryAndDeleteSuccessfully() throws Exception {
        mockMvc.perform(
                post("/category/transferToNewCategory")
                        .with(csrf().asHeader())
                        .param("name", "Nutrition")
                        .param("description", "Category to replace food")
                        .param("income", "false")
                        .param("categoryToDelete", "6")
                        .header(HttpHeaders.AUTHORIZATION,
                                "Basic " + Base64Utils.encodeToString("demo:demo".getBytes())))
                .andExpect(status().isOk())
                .andExpect(view().name("success"));
    }
}