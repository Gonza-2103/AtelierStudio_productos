package com.atelierstudio.productos.controller;

import com.atelierstudio.productos.dto.ProductoRequestDTO;
import com.atelierstudio.productos.dto.ProductoResponseDTO;
import com.atelierstudio.productos.service.ProductoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/productos")
@RequiredArgsConstructor
@Tag(name = "Productos v1", description = "Gestión del catálogo de obras y productos de AtelierStudio")
public class ProductoController {

    private final ProductoService productoService;

    //1. Obtener todos los productos (Catálogo general)
    @GetMapping
    public ResponseEntity<List<ProductoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(productoService.listarTodos());
    }

    //2. Obtener producto por ID (Detalle de la obra / Modal)
    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(productoService.obtenerPorId(id));
    }

    //3. Filtrar por técnica artística (Filtros del catálogo en React)
    /*@GetMapping("/tecnica/{tecnica}")
    public ResponseEntity<List<ProductoResponseDTO>> listarPorTecnica(@PathVariable String tecnica) {
        List<ProductoResponseDTO> productos = productoService.listarPorTecnica(tecnica);
        return ResponseEntity.ok(productos);
    }*/

    //4. Crear nuevo producto (Formulario de administración)
    @PostMapping
    public ResponseEntity<ProductoResponseDTO> crear(@Valid @RequestBody ProductoRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.crear(request));
    }

    //5. Actualizar producto existente (Edición en panel admin)
    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> actualizar(@PathVariable Integer id, @Valid @RequestBody ProductoRequestDTO request) {
        return ResponseEntity.ok(productoService.actualizar(id, request));
    }

    //6. Eliminar producto por ID (Borrado desde la tabla de productos)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
