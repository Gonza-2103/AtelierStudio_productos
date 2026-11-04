package com.atelierstudio.productos.repository;

import com.atelierstudio.productos.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    // Método de búsqueda personalizado por técnica artística (útil para AtelierStudio)
    List<Producto> findByTecnicaIgnoreCase(String tecnica);
}
