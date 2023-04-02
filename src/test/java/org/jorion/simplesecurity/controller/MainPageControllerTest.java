package org.jorion.simplesecurity.controller;

import jakarta.persistence.EntityManagerFactory;
import org.jorion.simplesecurity.service.ProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MainPageControllerTest {

    @Mock
    private ProductService service;

    @InjectMocks
    private MainPageController controller;

    @Test
    void main_ok()
            throws Exception {

        when(service.findAll()).thenReturn(List.of());

        ExtendedModelMap model = new ExtendedModelMap();
        String response = controller.main(null, model);
        Assertions.assertEquals("main.html", response);
    }
}