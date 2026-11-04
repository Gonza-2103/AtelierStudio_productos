package com.atelierstudio.productos.config;

import com.atelierstudio.productos.model.Producto;
import com.atelierstudio.productos.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final ProductoRepository productoRepository;

    @Override
    public void run(String... args) throws Exception {

        //Evita duplicar datos si la base de datos ya contiene registros
        if (productoRepository.count() > 0) {
            log.info("DataInitializer: El catálogo de AtelierStudio ya contiene datos. Omitiendo carga inicial.");
            return;
        }

        log.info("DataInitializer: Inicializando catálogo de obras de arte para AtelierStudio...");

        List<Producto> catalogoInicial = List.of(
                Producto.builder()
                        .titulo("Cuadro 'Mar y Playa'")
                        .descripcion("Fotografía aérea que captura el contraste entre el turquesa del océano y la arena blanca...")
                        .tecnica("Fotografía Artística")
                        .medidas("60x40 cm")
                        .precio(45000)
                        .stock(10)
                        .imagen("/img/mar-y-playa.jpg")
                        .autor("Artista Local")
                        .build(),

                Producto.builder()
                        .titulo("Retrato 'Caballero con Pipa'")
                        .descripcion("Pintura al óleo de estilo expresivo que retrata a un marinero de mirada reflexiva...")
                        .tecnica("Pintura al Óleo")
                        .medidas("50x70 cm")
                        .precio(60000)
                        .stock(8)
                        .imagen("/img/caballero-pipa.jpg")
                        .autor("Artista Local")
                        .build(),

                Producto.builder()
                        .titulo("Cuadro 'Paisaje de Montaña'")
                        .descripcion("Pintura paisajística al óleo que muestra una cordillera nevada reflejada en un lago...")
                        .tecnica("Pintura al Óleo")
                        .medidas("80x60 cm")
                        .precio(48000)
                        .stock(6)
                        .imagen("/img/paisaje-montana.jpg")
                        .autor("Artista Local")
                        .build(),

                Producto.builder()
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

        productoRepository.saveAll(catalogoInicial);
        log.info("DataInitializer: Se han precargado exitosamente {} obras de arte en AtelierStudio.", catalogoInicial.size());
    }
}
