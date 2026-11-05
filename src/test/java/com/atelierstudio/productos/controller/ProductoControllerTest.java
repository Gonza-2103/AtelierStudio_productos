package com.atelierstudio.productos.controller;

import com.atelierstudio.productos.dto.ProductoRequestDTO;
import com.atelierstudio.productos.dto.ProductoResponseDTO;
import com.atelierstudio.productos.exception.GlobalExceptionHandler;
import com.atelierstudio.productos.exception.ResourceNotFoundException;
import com.atelierstudio.productos.service.ProductoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;

import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductoController.class)
@Import(GlobalExceptionHandler.class)
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProductoService productoService;

    private ProductoResponseDTO responseDTO;
    private ProductoRequestDTO requestValidoDTO;

    @BeforeEach
    void setUp() {
        responseDTO = ProductoResponseDTO.builder()
                .id(1)
                .titulo("Cuadro 'Mar y Playa'")
                .descripcion("Fotografía aérea en plano cenital que captura el contraste entre las aguas turquesas del océano y la orilla de arena blanca...")
                .tecnica("Fotografía Artística")
                .medidas("60x40 cm")
                .precio(45000)
                .stock(5)
                .imagen("/img/mar-y-playa.jpg")
                .autor("Artista Local")
                .build();

        requestValidoDTO = ProductoRequestDTO.builder()
                .titulo("Cuadro 'Mar y Playa'")
                .descripcion("Fotografía aérea en plano cenital que captura el contraste entre las aguas turquesas del océano y la orilla de arena blanca...")
                .tecnica("Fotografía Artística")
                .medidas("60x40 cm")
                .precio(45000)
                .stock(5)
                .imagen("/img/mar-y-playa.jpg")
                .autor("Artista Local")
                .build();
    }

    @Test
    @DisplayName("GET /api/productos - Debe retornar 200 OK con la lista de obras")
    void testListarTodos() throws Exception {
        when(productoService.listarTodos()).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].titulo", is("Cuadro 'Mar y Playa'")))
                .andExpect(jsonPath("$[0].precio", is(45000)));

        verify(productoService, times(1)).listarTodos();
    }

    @Test
    @DisplayName("GET /api/productos/{id} - Debe retornar 200 OK cuando la obra existe")
    void testObtenerPorIdExitoso() throws Exception {
        when(productoService.obtenerPorId(1)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/productos/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.titulo", is("Cuadro 'Mar y Playa'")))
                .andExpect(jsonPath("$.tecnica", is("Fotografía Artística")));

        verify(productoService, times(1)).obtenerPorId(1);
    }

    @Test
    @DisplayName("GET /api/productos/{id} - Debe retornar 404 NOT FOUND con estructura de GlobalExceptionHandler cuando no existe")
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
    @DisplayName("GET /api/productos/tecnica/{tecnica} - Debe retornar 200 OK con las obras filtradas")
    void testListarPorTecnica() throws Exception {
        when(productoService.listarPorTecnica("Fotografía Artística")).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/api/productos/tecnica/{tecnica}", "Fotografía Artística")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].tecnica", is("Fotografía Artística")));

        verify(productoService, times(1)).listarPorTecnica("Fotografía Artística");
    }

    @Test
    @DisplayName("POST /api/productos - Debe retornar 201 CREATED al registrar una obra válida")
    void testCrearProductoExitoso() throws Exception {
        when(productoService.crear(any(ProductoRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestValidoDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.titulo", is("Cuadro 'Mar y Playa'")));

        verify(productoService, times(1)).crear(any(ProductoRequestDTO.class));
    }

    @Test
    @DisplayName("POST /api/productos - Debe retornar 400 BAD REQUEST con mapa de errores al fallar Jakarta Validation")
    void testCrearProductoValidacionFallida() throws Exception {
        ProductoRequestDTO requestInvalido = ProductoRequestDTO.builder()
                .titulo("") // @NotBlank violado
                .descripcion("Corta") // @Size min 10 violado
                .tecnica("")
                .medidas("")
                .precio(0) // @Min(1) violado
                .stock(-1) // @Min(0) violado
                .imagen("")
                .autor("")
                .build();

        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestInvalido)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.STATUS", is(400)))
                .andExpect(jsonPath("$.ERROR", is("Error de validación")))
                .andExpect(jsonPath("$.ERRORES.titulo").exists())
                .andExpect(jsonPath("$.ERRORES.precio").exists())
                .andExpect(jsonPath("$.ERRORES.stock").exists());

        verify(productoService, never()).crear(any(ProductoRequestDTO.class));
    }

    @Test
    @DisplayName("PUT /api/productos/{id} - Debe retornar 200 OK al actualizar una obra existente")
    void testActualizarProductoExitoso() throws Exception {
        when(productoService.actualizar(eq(1), any(ProductoRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(put("/api/productos/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestValidoDTO)))
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
