package com.atelierstudio.productos.repository;

import com.atelierstudio.productos.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    // Busca una categoría por su nombre exacto (ej. "Pintura")
    Optional<Categoria> findByNombre(String nombre);

    // Verifica si ya existe una categoría con ese nombre antes de insertarla
    boolean existsByNombre(String nombre);
}