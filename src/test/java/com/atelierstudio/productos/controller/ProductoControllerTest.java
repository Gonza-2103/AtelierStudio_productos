package com.atelierstudio.productos.controller;

import com.atelierstudio.productos.util.ProductoDataFaker;
import com.atelierstudio.productos.dto.ProductoRequestDTO;
import com.atelierstudio.productos.exception.ResourceNotFoundException;
import com.atelierstudio.productos.service.ProductoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProductoService productoService;

    @Test
    @DisplayName("GET /api/productos - Debe retornar 200 OK con la lista de obras usando Faker")
    void testListarTodos() throws Exception {
        when(productoService.listarTodos()).thenReturn(ProductoDataFaker.mockListaResponseDTO());

        mockMvc.perform(get("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].titulo", is("Cuadro 'Mar y Playa'")))
                .andExpect(jsonPath("$[0].precio", is(45000)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].titulo", is("Retrato 'Caballero con Pipa'")));

        verify(productoService, times(1)).listarTodos();
    }

    @Test
    @DisplayName("GET /api/productos/{id} - Debe retornar 200 OK cuando la obra existe usando Faker")
    void testObtenerPorIdExitoso() throws Exception {
        when(productoService.obtenerPorId(1)).thenReturn(ProductoDataFaker.mockProductoResponseDTO());

        mockMvc.perform(get("/api/productos/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.titulo", is("Cuadro 'Mar y Playa'")))
                .andExpect(jsonPath("$.tecnica", is("Fotografía Artística")));

        verify(productoService, times(1)).obtenerPorId(1);
    }

    @Test
    @DisplayName("GET /api/productos/{id} - Debe retornar 404 NOT FOUND cuando la obra no existe")
    void testObtenerPorIdNoEncontrado() throws Exception {
        when(productoService.obtenerPorId(99))
                .thenThrow(new ResourceNotFoundException("Producto no encontrado con el ID: 99"));

        mockMvc.perform(get("/api/productos/{id}", 99)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.STATUS", is(404)))
                .andExpect(jsonPath("$.ERROR", is("Recurso no encontrado")))
                .andExpect(jsonPath("$.MENSAJE", is("Producto no encontrado con el ID: 99")));

        verify(productoService, times(1)).obtenerPorId(99);
    }

    @Test
    @DisplayName("GET /api/productos/tecnica/{tecnica} - Debe retornar 200 OK con obras filtradas")
    void testListarPorTecnica() throws Exception {
        when(productoService.listarPorTecnica("Fotografía Artística"))
                .thenReturn(List.of(ProductoDataFaker.mockProductoResponseDTO()));

        mockMvc.perform(get("/api/productos/tecnica/{tecnica}", "Fotografía Artística")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].tecnica", is("Fotografía Artística")));

        verify(productoService, times(1)).listarPorTecnica("Fotografía Artística");
    }

    @Test
    @DisplayName("POST /api/productos - Debe retornar 201 CREATED al enviar un DTO válido con Faker")
    void testCrearProductoExitoso() throws Exception {
        // AQUÍ SE USA: ProductoDataFaker.mockProductoRequestValido() y mockProductoResponseDTO()
        when(productoService.crear(any(ProductoRequestDTO.class))).thenReturn(ProductoDataFaker.mockProductoResponseDTO());

        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ProductoDataFaker.mockProductoRequestValido())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.titulo", is("Cuadro 'Mar y Playa'")));

        verify(productoService, times(1)).crear(any(ProductoRequestDTO.class));
    }

    @Test
    @DisplayName("POST /api/productos - Debe retornar 400 BAD REQUEST al fallar validaciones de Jakarta")
    void testCrearProductoValidacionFallida() throws Exception {
        // AQUÍ SE USA: ProductoDataFaker.mockProductoRequestInvalido()
        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ProductoDataFaker.mockProductoRequestInvalido())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.STATUS", is(400)))
                .andExpect(jsonPath("$.ERROR", is("Error de validación")))
                .andExpect(jsonPath("$.ERRORES.titulo").exists())
                .andExpect(jsonPath("$.ERRORES.precio").exists())
                .andExpect(jsonPath("$.ERRORES.stock").exists());

        verify(productoService, never()).crear(any(ProductoRequestDTO.class));
    }

    @Test
    @DisplayName("PUT /api/productos/{id} - Debe retornar 200 OK al actualizar una obra")
    void testActualizarProductoExitoso() throws Exception {
        when(productoService.actualizar(eq(1), any(ProductoRequestDTO.class))).thenReturn(ProductoDataFaker.mockProductoResponseDTO());

        mockMvc.perform(put("/api/productos/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ProductoDataFaker.mockProductoRequestValido())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.titulo", is("Cuadro 'Mar y Playa'")));

        verify(productoService, times(1)).actualizar(eq(1), any(ProductoRequestDTO.class));
    }

    @Test
    @DisplayName("DELETE /api/productos/{id} - Debe retornar 204 NO CONTENT al eliminar")
    void testEliminarProductoExitoso() throws Exception {
        doNothing().when(productoService).eliminar(1);

        mockMvc.perform(delete("/api/productos/{id}", 1))
                .andExpect(status().isNoContent());

        verify(productoService, times(1)).eliminar(1);
    }
}
