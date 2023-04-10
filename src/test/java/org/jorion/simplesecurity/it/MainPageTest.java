package org.jorion.simplesecurity.it;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;

/**
 * With {@code @SpringBootTest}, we start the full {@code ApplicationContext}.
 * <p>
 * Classes annotated with {@code @Component, @Service, @Repository, @Controller} will be converted into Beans
 * and placed into the application context.
 */
@SpringBootTest
@AutoConfigureMockMvc
class MainPageTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void main_unauthenticated_returns401()
            throws Exception {

        RequestBuilder request = MockMvcRequestBuilders.get("/main");
        MvcResult result = mvc.perform(request).andReturn();
        assertEquals(UNAUTHORIZED.value(), result.getResponse().getStatus());
    }

    @Test
    void main_withBasicAuthentication_returns200()
            throws Exception {

        RequestBuilder request = MockMvcRequestBuilders
                .get("/main")
                .with(httpBasic("john", "12345"));
        MvcResult result = mvc.perform(request).andReturn();
        assertEquals(OK.value(), result.getResponse().getStatus());
    }

    @Test
    @WithMockUser(username = "john", roles = "MEMBER")
    void main_withMockUser_returns200()
        throws Exception {

        RequestBuilder request = MockMvcRequestBuilders.get("/main");
        MvcResult result = mvc.perform(request).andReturn();
        assertEquals(OK.value(), result.getResponse().getStatus());
    }
}