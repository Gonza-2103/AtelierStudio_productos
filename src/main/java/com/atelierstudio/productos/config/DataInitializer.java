package com.atelierstudio.productos.config;

import com.atelierstudio.productos.model.Categoria;
import com.atelierstudio.productos.model.Producto;
import com.atelierstudio.productos.repository.CategoriaRepository;
import com.atelierstudio.productos.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    @Override
    public void run(String... args) {

        //1. Crear y guardar las 3 categorías reales mostradas en AtelierStudioReact
        Categoria pinturaOleo = obtenerOCrearCategoria(
                "Pintura al Óleo",
                "Obras pictóricas tradicionales y expresivas realizadas al óleo sobre lienzo."
        );

        Categoria acuarelaMixta = obtenerOCrearCategoria(
                "Acuarela y Técnica Mixta",
                "Ilustraciones luminosas combinando pigmentos al agua y técnicas mixtas."
        );

        Categoria fotografiaArtistica = obtenerOCrearCategoria(
                "Fotografía Artística",
                "Capturas fotográficas de autor en edición limitada y alta resolución."
        );

        //2. Validar si ya existen productos para no duplicarlos al reiniciar
        if (productoRepository.count() > 0) {
            log.info("DataInitializer: El catálogo de AtelierStudio ya contiene datos. Omitiendo carga inicial.");
            return;
        }

        //3. Cargar los productos que tiene el local
        List<Producto> productosIniciales = List.of(
                Producto.builder()
                        .titulo("Cuadro 'Paisaje de Montaña'")
                        .descripcion("Pintura paisajística tradicional al óleo que muestra una cordillera alpina nevada reflejada en un lago...")
                        .precio(48000)
                        .stock(10)
                        .tecnica("Pintura al Óleo")
                        .imagen("/img/paisaje-montana.jpg")
                        .categoria(pinturaOleo)
                        .build(),

                Producto.builder()
                        .titulo("Retrato 'Gato entre Sombras'")
                        .descripcion("Acuarela luminosa que ilustra a un gato atigrado de pelaje naranja y blanco...")
                        .precio(72000)
                        .stock(10)
                        .tecnica("Acuarela y Técnica Mixta")
                        .imagen("/img/gato-sombras.jpg")
                        .categoria(acuarelaMixta)
                        .build(),

                Producto.builder()
                        .titulo("Cuadro 'Mar y Playa'")
                        .descripcion("Fotografía aérea en plano cenital que captura el contraste entre las aguas turquesas del océano y la orilla de arena blanca...")
                        .precio(45000)
                        .stock(10)
                        .tecnica("Fotografía Artística")
                        .imagen("/img/mar-playa.jpg")
                        .categoria(fotografiaArtistica)
                        .build(),

                Producto.builder()
                        .titulo("Retrato 'Caballero con Pipa'")
                        .descripcion("Pintura al óleo de estilo expresivo que retrata de perfil a un marinero o pescador de mirada reflexiva...")
                        .precio(60000)
                        .stock(10)
                        .tecnica("Pintura al Óleo")
                        .imagen("/img/caballero-pipa.jpg")
                        .categoria(pinturaOleo)
                        .build()
        );

        productoRepository.saveAll(productosIniciales);
        log.info("DataInitializer: Categorías y {} productos obligatorios de AtelierStudio cargados correctamente.", productosIniciales.size());
    }

    //Busca la categoría por nombre; si no existe en MySQL, la crea y retorna con su ID
    private Categoria obtenerOCrearCategoria(String nombre, String descripcion) {
        return categoriaRepository.findByNombre(nombre)
                .orElseGet(() -> categoriaRepository.save(
                        Categoria.builder()
                                .nombre(nombre)
                                .descripcion(descripcion)
                                .build()
                ));
    }
}