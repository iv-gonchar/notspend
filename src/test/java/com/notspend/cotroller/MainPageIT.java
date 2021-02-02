package com.notspend.cotroller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@MvcTest
class MainPageIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    MainPage mainPage;

    @Test
    // Can't test exact values as they are date dependent
    void getMainPage() throws Exception {
        mockMvc.perform(
                get("/").with(user("demo").password("demo")))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(request().sessionAttribute("username", equalTo("demo")))
                .andExpect(request().sessionAttribute("totalSum", notNullValue()))
                .andExpect(request().sessionAttribute("spendCurrentMonth", notNullValue()))
                .andExpect(request().sessionAttribute("earnCurrentMonth", notNullValue()))
                .andExpect(request().sessionAttribute("income", notNullValue()))
                .andExpect(request().sessionAttribute("months", notNullValue()))
                .andExpect(request().sessionAttribute("expensePerDay", notNullValue()))
                .andExpect(request().sessionAttribute("days", notNullValue()))
                .andExpect(request().sessionAttribute("currencyValues", hasKey("USD")))
                .andExpect(request().sessionAttribute("currencyValues", hasKey("UAH")));
    }
}