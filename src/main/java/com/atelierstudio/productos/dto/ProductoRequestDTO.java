package com.atelierstudio.productos.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductoRequestDTO {

    @NotBlank(message = "El título de la obra es obligatorio")
    @Size(min = 3, max = 150, message = "El título debe tener entre 3 y 150 caracteres")
    private String titulo;

    @NotBlank(message = "La descripción no puede estar vacía")
    @Size(min = 10, max = 1000, message = "La descripción debe tener entre 10 y 1000 caracteres")
    private String descripcion;

    @NotBlank(message = "La técnica artística es obligatoria")
    private String tecnica;

    @NotBlank(message = "Las medidas de la obra son obligatorias")
    private String medidas;

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser mayor a cero")
    @Min(value = 1, message = "El precio mínimo debe ser 1")
    private Integer precio;

    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser un número negativo")
    private Integer stock;

    @NotBlank(message = "La URL o ruta de la imagen es obligatoria")
    private String imagen;

    @NotBlank(message = "El nombre del autor o artista es obligatorio")
    private String autor;
}
