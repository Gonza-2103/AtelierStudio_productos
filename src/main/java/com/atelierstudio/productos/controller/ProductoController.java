package com.atelierstudio.productos.controller;

import com.atelierstudio.productos.dto.ProductoRequestDTO;
import com.atelierstudio.productos.dto.ProductoResponseDTO;
import com.atelierstudio.productos.service.ProductoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    //1. Obtener todos los productos (Catálogo general)
    @GetMapping
    public ResponseEntity<List<ProductoResponseDTO>> listarTodos() {
        List<ProductoResponseDTO> productos = productoService.listarTodos();
        return ResponseEntity.ok(productos);
    }

    //2. Obtener producto por ID (Detalle de la obra / Modal)
    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> obtenerPorId(@PathVariable Integer id) {
        ProductoResponseDTO producto = productoService.obtenerPorId(id);
        return ResponseEntity.ok(producto);
    }

    //3. Filtrar por técnica artística (Filtros del catálogo en React)
    @GetMapping("/tecnica/{tecnica}")
    public ResponseEntity<List<ProductoResponseDTO>> listarPorTecnica(@PathVariable String tecnica) {
        List<ProductoResponseDTO> productos = productoService.listarPorTecnica(tecnica);
        return ResponseEntity.ok(productos);
    }

    //4. Crear nuevo producto (Formulario de administración)
    @PostMapping
    public ResponseEntity<ProductoResponseDTO> crear(@Valid @RequestBody ProductoRequestDTO request) {
        ProductoResponseDTO nuevoProducto = productoService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProducto);
    }

    //5. Actualizar producto existente (Edición en panel admin)
    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody ProductoRequestDTO request) {
        ProductoResponseDTO productoActualizado = productoService.actualizar(id, request);
        return ResponseEntity.ok(productoActualizado);
    }

    //6. Eliminar producto por ID (Borrado desde la tabla de productos)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
