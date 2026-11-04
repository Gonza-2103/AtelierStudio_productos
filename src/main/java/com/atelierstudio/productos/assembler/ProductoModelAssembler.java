package com.atelierstudio.productos.assembler;


import com.atelierstudio.productos.dto.ProductoRequestDTO;
import com.atelierstudio.productos.dto.ProductoResponseDTO;
import com.atelierstudio.productos.model.Producto;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class ProductoModelAssembler {

    /* 1. Transforma un ProductoRequestDTO (enviado desde el formulario de React)
       en una entidad Producto para persistencia o almacenamiento en memoria. */

    public Producto toEntity(ProductoRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        return Producto.builder()
                .titulo(dto.getTitulo())
                .descripcion(dto.getDescripcion())
                .tecnica(dto.getTecnica())
                .medidas(dto.getMedidas())
                .precio(dto.getPrecio())  //Integer en CLP
                .stock(dto.getStock())
                .imagen(dto.getImagen())
                .autor(dto.getAutor())
                .build();
    }


    /* 2. Transforma una entidad Producto en un ProductoResponseDTO para enviar
       al catálogo, modal de detalles o tabla administrativa de React.  */

    public ProductoResponseDTO toResponseDTO(Producto entity) {
        if (entity == null) {
            return null;
        }

        return ProductoResponseDTO.builder()
                .id(entity.getId())
                .titulo(entity.getTitulo())
                .descripcion(entity.getDescripcion())
                .tecnica(entity.getTecnica())
                .medidas(entity.getMedidas())
                .precio(entity.getPrecio())
                .stock(entity.getStock())
                .imagen(entity.getImagen())
                .autor(entity.getAutor())
                .build();
    }


    /* 3. Convierte una lista completa de entidades a una lista de ProductoResponseDTO
       para el renderizado del catálogo o la grilla de administración. */

    public List<ProductoResponseDTO> toResponseDTOList(List<Producto> entities) {
        if (entities == null || entities.isEmpty()) {
            return Collections.emptyList();
        }

        return entities.stream()
                .map(this::toResponseDTO)
                .toList();
    }
}
