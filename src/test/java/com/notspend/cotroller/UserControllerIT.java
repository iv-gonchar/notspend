package com.notspend.cotroller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@MvcTest
class UserControllerIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserController userController;

    @Test
    void register() throws Exception {
        mockMvc.perform(
                get("/user/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/register"))
                .andExpect(model().attribute("user", hasProperty("username", nullValue())))
                .andExpect(model().attribute("user", hasProperty("email", nullValue())))
                .andExpect(model().attribute("user", hasProperty("name", nullValue())))
                .andExpect(model().attribute("user", hasProperty("surname", nullValue())))
                .andExpect(model().attribute("user", hasProperty("enabled", equalTo(false))));
    }

    @Test
    @Transactional
    void registerProcessSuccessfully() throws Exception {
        mockMvc.perform(
                post("/user/registerprocess").with(csrf().asHeader())
                        .param("username", "newUser")
                        .param("email", "correct@email.com")
                        .param("name", "CorrectName")
                        .param("surname", "CorrectSurname")
                        .param("password", "securepassword")
                        .param("passwordSecondTime", "securepassword"))
                .andExpect(status().isOk())
                .andExpect(view().name("/login"));
    }

    @Test
    @Transactional
    void registerProcessEmptyUsername() throws Exception {
        mockMvc.perform(
                post("/user/registerprocess").with(csrf().asHeader())
                        .param("email", "correct@email.com")
                        .param("name", "CorrectName")
                        .param("surname", "CorrectSurname")
                        .param("password", "securepassword")
                        .param("passwordSecondTime", "securepassword"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/register"));
    }

    @Test
    @Transactional
    void registerProcessExistentUsername() throws Exception {
        mockMvc.perform(
                post("/user/registerprocess").with(csrf().asHeader())
                        .param("username", "demo")
                        .param("email", "correct@email.com")
                        .param("name", "CorrectName")
                        .param("surname", "CorrectSurname")
                        .param("password", "securepassword")
                        .param("passwordSecondTime", "securepassword"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/register"))
                .andExpect(model().attribute("userexist", equalTo("User with username demo is already register")));
    }

    @Test
    @Transactional
    void registerProcessRepeatedPasswordIsDifferent() throws Exception {
        mockMvc.perform(
                post("/user/registerprocess").with(csrf().asHeader())
                        .param("username", "newUser")
                        .param("email", "correct@email.com")
                        .param("name", "CorrectName")
                        .param("surname", "CorrectSurname")
                        .param("password", "securepassword")
                        .param("passwordSecondTime", "differentpassword"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("register?passwordSecondTime=differentpassword"));
    }

    @Test
    void forgetPassword() throws Exception {
        mockMvc.perform(
                get("/user/forgetpassword"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/forgetpassword"));
    }
}