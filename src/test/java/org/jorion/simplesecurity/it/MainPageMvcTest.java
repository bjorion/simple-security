package org.jorion.simplesecurity.it;

import org.jorion.simplesecurity.service.ProductService;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

/**
 * With {@code @WebMvcTest}, we create the {@code @ApplicationContext} with a limited number of beans
 * (only those related to the Web Layer).
 * <p>
 * The {@code @WebMvcTest} annotation is used to test the Web Layer only.
 * If the controller has a dependency on a {@code @Service} object, you'll need to mock this
 * dependency using a {@code @MockBean} annotation.
 */
// @WebMvcTest(controllers = MainPageController.class)
public class MainPageMvcTest {

    // @Autowired
    private MockMvc mvc;

    // @MockBean
    private ProductService productService;

    // @Test
    void main_unauthenticated_returns401()
            throws Exception {

        Mockito.when(productService.findAll()).thenReturn(List.of());

        RequestBuilder request = MockMvcRequestBuilders.get("/main");
        MvcResult result = mvc.perform(request).andReturn();
        assertEquals(UNAUTHORIZED.value(), result.getResponse().getStatus());
    }
}
