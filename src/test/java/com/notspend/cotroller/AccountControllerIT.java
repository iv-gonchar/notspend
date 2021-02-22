package com.notspend.cotroller;

import org.junit.jupiter.api.Disabled;
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
class AccountControllerIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AccountController accountController;

    @Test
    void addAccount() throws Exception {
        mockMvc.perform(get("/account/add").with(user("demo").password("demo")))
                .andExpect(status().isOk())
                .andExpect(view().name("account/add"))
                .andExpect(model().attribute("account", hasProperty("id", equalTo(0))))
                .andExpect(model().attribute("account", hasProperty("type", nullValue())))
                .andExpect(model().attribute("currencies", hasSize(112)));
    }

    @Test
    @Transactional
    void processAddAccountForm() throws Exception {
        mockMvc.perform(post("/account/addProcess")
                .with(user("demo").password("demo"))
                .with(csrf())
                .param("type", "NewAccount")
                .param("summary", "0.0")
                .param("description", "New account description")
                .param("code", "USD"))
                .andExpect(status().isOk())
                .andExpect(view().name("success"));
    }

    @Test
    void listAccounts() throws Exception {
        mockMvc.perform(get("/account/all")
                .with(user("demo").password("demo")))
                .andExpect(status().isOk())
                .andExpect(view().name("account/all"))
                .andExpect(model().attribute("accounts", hasSize(3)));
    }

    @Test
    void updateAccount() throws Exception {
        mockMvc.perform(get("/account/update")
                .with(user("demo").password("demo"))
                .param("accountId", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("account/update"))
                .andExpect(model().attribute("account", hasProperty("id", equalTo(1))))
                .andExpect(model().attribute("account", hasProperty("type", equalTo("Cash"))))
                .andExpect(model().attribute("account", hasProperty("summary", equalTo(60923.50))))
                .andExpect(model().attribute("account", hasProperty("description", equalTo("Cash in wallet"))));
    }

    @Test
    @Transactional
    void updateProcess() throws Exception {
        mockMvc.perform(post("/account/updateprocess")
                .with(user("demo").password("demo"))
                .with(csrf())
                .param("id", "1")
                .param("type", "CashUpdated")
                .param("description", "Updated Description"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("all"));
    }

    @Test
    @Transactional
    void deleteAccount() throws Exception {
        mockMvc.perform(get("/account/delete")
                .with(user("demo").password("demo"))
                .param("accountId", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("account/cantdelete"))
                .andExpect(model().attribute("accountToDelete", notNullValue()))
                .andExpect(model().attribute("accounts", notNullValue()))
                .andExpect(model().attribute("account", notNullValue()));
    }

    @Test
    @Transactional
    void transferToOtherAccountAndDelete() throws Exception {
        mockMvc.perform(post("/account/transferToExistAccount")
                .with(user("demo").password("demo"))
                .with(csrf())
                .param("accountId", "1")
                .param("accountToDelete", "2"))
                .andExpect(status().isOk())
                .andExpect(view().name("success"));
    }

    @Test
    @Disabled("it depends on currency exchange rate")
    void transferMoneyBetweenAccounts() throws Exception {
        mockMvc.perform(get("/account/transfer")
                .with(user("demo").password("demo")))
                .andExpect(status().isOk())
                .andExpect(view().name("account/transfer"))
                .andExpect(model().attribute("accounts", hasSize(3)))
                .andExpect(model().attribute("allMoneySummary", equalTo(653405.085481)));
    }

}