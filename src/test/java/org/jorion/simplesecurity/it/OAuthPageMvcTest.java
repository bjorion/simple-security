package org.jorion.simplesecurity.it;

import org.jorion.simplesecurity.controller.OAuthController;
import org.jorion.simplesecurity.service.JwtTokenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;

@WebMvcTest
@ContextConfiguration(classes = OAuthController.class)
public class OAuthPageMvcTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private JwtTokenService tokenService;

    // with @WithMockUser, we have a 302 to gitHub (not sure why?)
    @Test
    @WithMockUser(username = "john", roles = "MEMBER")
    void createAccessToken_withMockUser_returnsJwt()
            throws Exception {

        final String token = "abc.def.ghi";
        when(tokenService.generateToken(any())).thenReturn(token);

        RequestBuilder request = MockMvcRequestBuilders
                .post("/token")
                .with(httpBasic("john", "12345"))
                .with(csrf());
        MvcResult result = mvc.perform(request).andReturn();
        assertEquals(OK.value(), result.getResponse().getStatus());
        assertEquals(token, result.getResponse().getContentAsString());
    }

    @Test
    @WithMockUser(username = "john", roles = "MEMBER")
    void user_withMockUser_returnsUserInfo()
            throws Exception {

        RequestBuilder request = MockMvcRequestBuilders.get("/user");
        MvcResult result = mvc.perform(request).andReturn();
        assertEquals(OK.value(), result.getResponse().getStatus());
        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains("Username=john"));
    }
}
