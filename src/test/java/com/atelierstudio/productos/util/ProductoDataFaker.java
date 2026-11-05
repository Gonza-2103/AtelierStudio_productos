package com.atelierstudio.productos.util;

import com.atelierstudio.productos.dto.ProductoRequestDTO;
import com.atelierstudio.productos.dto.ProductoResponseDTO;
import com.atelierstudio.productos.model.Producto;

import java.util.List;

public final class ProductoDataFaker {
    private ProductoDataFaker() {
        // Constructor privado para clase utilitaria
    }

    //1. Entidad Producto completa válida (Mock de Base de Datos)
    public static Producto mockProductoMarYPlaya() {
        return Producto.builder()
                .id(1)
                .titulo("Cuadro 'Mar y Playa'")
                .descripcion("Fotografía aérea en plano cenital que captura el contraste entre las aguas turquesas del océano y la orilla de arena blanca...")
                .tecnica("Fotografía Artística")
                .medidas("60x40 cm")
                .precio(45000)
                .stock(10)
                .imagen("/img/mar-y-playa.jpg")
                .autor("Artista Local")
                .build();
    }

    public static Producto mockProductoCaballeroConPipa() {
        return Producto.builder()
                .id(2)
                .titulo("Retrato 'Caballero con Pipa'")
                .descripcion("Pintura al óleo de estilo expresivo que retrata de perfil a un marinero o pescador de mirada reflexiva...")
                .tecnica("Pintura al Óleo")
                .medidas("50x70 cm")
                .precio(60000)
                .stock(8)
                .imagen("/img/caballero-pipa.jpg")
                .autor("Artista Local")
                .build();
    }

    //2. Lista de entidades para pruebas de listado o filtrado
    public static List<Producto> mockListaProductos() {
        return List.of(
                mockProductoMarYPlaya(),
                mockProductoCaballeroConPipa(),
                Producto.builder()
                        .id(3)
                        .titulo("Cuadro 'Paisaje de Montaña'")
                        .descripcion("Pintura paisajística tradicional al óleo que muestra una cordillera alpina nevada reflejada en un lago...")
                        .tecnica("Pintura al Óleo")
                        .medidas("80x60 cm")
                        .precio(48000)
                        .stock(6)
                        .imagen("/img/paisaje-montana.jpg")
                        .autor("Artista Local")
                        .build(),
                Producto.builder()
                        .id(4)
                        .titulo("Retrato 'Gato entre Sombras'")
                        .descripcion("Acuarela luminosa que ilustra a un gato atigrado de pelaje naranja y blanco...")
                        .tecnica("Acuarela y Técnica Mixta")
                        .medidas("40x50 cm")
                        .precio(72000)
                        .stock(6)
                        .imagen("/img/gato-sombras.jpg")
                        .autor("Artista Local")
                        .build()
        );
    }

    //3. RequestDTO válido para pruebas de POST y PUT
    public static ProductoRequestDTO mockProductoRequestValido() {
        return ProductoRequestDTO.builder()
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

    //4. RequestDTO inválido para probar el GlobalExceptionHandler (error 400 Bad Request)
    public static ProductoRequestDTO mockProductoRequestInvalido() {
        return ProductoRequestDTO.builder()
                .titulo("")               // @NotBlank violado
                .descripcion("Corta")     // @Size(min=10) violado
                .tecnica("")              // @NotBlank violado
                .medidas("")              // @NotBlank violado
                .precio(0)                // @Min(1) / @Positive violado
                .stock(-1)                // @Min(0) violado
                .imagen("")               // @NotBlank violado
                .autor("")                // @NotBlank violado
                .build();
    }

    //5. ResponseDTO esperado que devuelve el Assembler y el Service
    public static ProductoResponseDTO mockProductoResponseDTO() {
        return ProductoResponseDTO.builder()
                .id(1)
                .titulo("Cuadro 'Mar y Playa'")
                .descripcion("Fotografía aérea en plano cenital que captura el contraste entre las aguas turquesas del océano y la orilla de arena blanca...")
                .tecnica("Fotografía Artística")
                .medidas("60x40 cm")
                .precio(45000)
                .stock(10)
                .imagen("/img/mar-y-playa.jpg")
                .autor("Artista Local")
                .build();
    }

    //6. Lista de ResponseDTOs
    public static List<ProductoResponseDTO> mockListaResponseDTO() {
        return List.of(
                mockProductoResponseDTO(),
                ProductoResponseDTO.builder()
                        .id(2)
                        .titulo("Retrato 'Caballero con Pipa'")
                        .descripcion("Pintura al óleo de estilo expresivo que retrata de perfil a un marinero o pescador de mirada reflexiva...")
                        .tecnica("Pintura al Óleo")
                        .medidas("50x70 cm")
                        .precio(60000)
                        .stock(8)
                        .imagen("/img/caballero-pipa.jpg")
                        .autor("Artista Local")
                        .build()
        );
    }
}
