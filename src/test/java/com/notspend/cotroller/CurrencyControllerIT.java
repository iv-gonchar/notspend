package com.notspend.cotroller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@MvcTest
class CurrencyControllerIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    CurrencyController currencyController;

    @Test
    void listCurrency() throws Exception {
        mockMvc.perform(get("/currency/all").with(user("demo").password("demo")))
                .andExpect(status().isOk())
                .andExpect(view().name("currency/all"))
                .andExpect(model().attribute("currencies", hasSize(2)))
                .andExpect(model().attribute("currencies", hasItem(hasProperty("code", equalTo("UAH")))))
                .andExpect(model().attribute("currencies", hasItem(hasProperty("code", equalTo("USD")))));
    }
}