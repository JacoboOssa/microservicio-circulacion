package co.analisys.biblioteca.client;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;


@FeignClient(name = "circulacion", url = "http://localhost:8082")
public interface CatalogoClient {


    @GetMapping("/libros/{libroId}/disponible")
    Boolean isLibroDisponible(@PathVariable("libroId") String id);

    @PutMapping("/libros/{libroId}/disponibilidad")
    void actualizarDisponibilidad(@PathVariable("libroId") String id,@RequestBody Boolean disponible);

}
