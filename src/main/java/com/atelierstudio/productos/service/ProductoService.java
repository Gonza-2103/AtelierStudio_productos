package com.atelierstudio.productos.service;

import com.atelierstudio.productos.assembler.ProductoModelAssembler;
import com.atelierstudio.productos.dto.ProductoRequestDTO;
import com.atelierstudio.productos.dto.ProductoResponseDTO;
import com.atelierstudio.productos.model.Producto;
import com.atelierstudio.productos.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final ProductoModelAssembler productoAssembler;

    //1. Listar todas las obras para el catálogo y administración
    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> listarTodos() {
        log.info("Consultando todos los productos del catálogo de AtelierStudio");
        List<Producto> productos = productoRepository.findAll();
        log.debug("Se encontraron {} productos", productos.size());
        return productoAssembler.toResponseDTOList(productos);
    }

    //2. Obtener una obra específica por su ID
    @Transactional(readOnly = true)
    public ProductoResponseDTO obtenerPorId(Integer id) {
        log.info("Buscando producto con ID: {}", id);
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Producto con ID {} no fue encontrado", id);
                    return new RuntimeException("Producto no encontrado con el ID: " + id);
                });
        return productoAssembler.toResponseDTO(producto);
    }

    //3. Filtrar obras por técnica artística
    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> listarPorTecnica(String tecnica) {
        log.info("Filtrando productos por técnica: {}", tecnica);
        List<Producto> productosFiltrados = productoRepository.findAll().stream()
                .filter(p -> p.getTecnica() != null && p.getTecnica().equalsIgnoreCase(tecnica))
                .toList();
        log.debug("Se encontraron {} productos con la técnica '{}'", productosFiltrados.size(), tecnica);
        return productoAssembler.toResponseDTOList(productosFiltrados);
    }

    //4. Crear un nuevo producto
    @Transactional
    public ProductoResponseDTO crear(ProductoRequestDTO request) {
        log.info("Registrando nueva obra: '{}' del artista: '{}'", request.getTitulo(), request.getAutor());
        Producto nuevoProducto = productoAssembler.toEntity(request);
        Producto guardado = productoRepository.save(nuevoProducto);
        log.info("Obra registrada exitosamente con ID: {}", guardado.getId());
        return productoAssembler.toResponseDTO(guardado);
    }

    //5. Actualizar los datos y stock de una obra existente
    @Transactional
    public ProductoResponseDTO actualizar(Integer id, ProductoRequestDTO request) {
        log.info("Iniciando actualización para el producto con ID: {}", id);
        Producto productoExistente = productoRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se pudo actualizar. Obra con ID {} no encontrada", id);
                    return new RuntimeException("No se puede actualizar. Producto no encontrado con ID: " + id);
                });

        productoExistente.setTitulo(request.getTitulo());
        productoExistente.setDescripcion(request.getDescripcion());
        productoExistente.setTecnica(request.getTecnica());
        productoExistente.setMedidas(request.getMedidas());
        productoExistente.setPrecio(request.getPrecio());
        productoExistente.setStock(request.getStock());
        productoExistente.setImagen(request.getImagen());
        productoExistente.setAutor(request.getAutor());

        Producto actualizado = productoRepository.save(productoExistente);
        log.info("Producto con ID {} actualizado correctamente", id);
        return productoAssembler.toResponseDTO(actualizado);
    }

    //6. Eliminar un producto del inventario
    @Transactional
    public void eliminar(Integer id) {
        log.info("Solicitud para eliminar producto con ID: {}", id);
        if (!productoRepository.existsById(id)) {
            log.warn("No se pudo eliminar. Obra con ID {} no encontrada", id);
            throw new RuntimeException("No se puede eliminar. Producto no encontrado con ID: " + id);
        }

        productoRepository.deleteById(id);
        log.info("Producto con ID {} eliminado satisfactoriamente", id);
    }
}
