package com.atelierstudio.productos.service;

import com.atelierstudio.productos.assembler.ProductoModelAssembler;
import com.atelierstudio.productos.dto.ProductoResponseDTO;
import com.atelierstudio.productos.exception.ResourceNotFoundException;
import com.atelierstudio.productos.model.Producto;
import com.atelierstudio.productos.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private ProductoModelAssembler productoAssembler;

    @InjectMocks
    private ProductoService productoService;

    private Producto producto;
    private ProductoResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        producto = Producto.builder()
                .id(1)
                .titulo("Cuadro 'Mar y Playa'")
                .tecnica("Fotografía Artística")
                .precio(45000)
                .stock(10)
                .build();

        responseDTO = ProductoResponseDTO.builder()
                .id(1)
                .titulo("Cuadro 'Mar y Playa'")
                .tecnica("Fotografía Artística")
                .precio(45000)
                .stock(10)
                .build();
    }

    @Test
    void testObtenerPorIdExitoso() {
        when(productoRepository.findById(1)).thenReturn(Optional.of(producto));
        when(productoAssembler.toResponseDTO(producto)).thenReturn(responseDTO);

        ProductoResponseDTO resultado = productoService.obtenerPorId(1);

        assertNotNull(resultado);
        assertEquals("Cuadro 'Mar y Playa'", resultado.getTitulo());
        verify(productoRepository, times(1)).findById(1);
    }

    @Test
    void testObtenerPorIdNoEncontradoLanzaExcepcion() {
        when(productoRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productoService.obtenerPorId(99));
        verify(productoRepository, times(1)).findById(99);
    }

    @Test
    void testListarPorTecnicaUsaMetodoRepositorio() {
        when(productoRepository.findByTecnicaIgnoreCase("Fotografía Artística")).thenReturn(List.of(producto));
        when(productoAssembler.toResponseDTOList(List.of(producto))).thenReturn(List.of(responseDTO));

        List<ProductoResponseDTO> resultado = productoService.listarPorTecnica("Fotografía Artística");

        assertEquals(1, resultado.size());
        verify(productoRepository, times(1)).findByTecnicaIgnoreCase("Fotografía Artística");
    }
}
