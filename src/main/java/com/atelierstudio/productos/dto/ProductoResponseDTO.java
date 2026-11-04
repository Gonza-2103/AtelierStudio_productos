package com.atelierstudio.productos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductoResponseDTO {

    private Integer id;
    private String titulo;
    private String descripcion;
    private String tecnica;
    private String medidas;
    private Double precio;
    private Integer stock;
    private String imagen;
    private String autor;
}
