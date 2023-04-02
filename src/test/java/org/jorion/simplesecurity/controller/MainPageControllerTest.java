package org.jorion.simplesecurity.controller;

import org.jorion.simplesecurity.service.ProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ExtendedModelMap;

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