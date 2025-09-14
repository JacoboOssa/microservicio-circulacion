package co.analisys.biblioteca.controller;

import co.analisys.biblioteca.model.LibroId;
import co.analisys.biblioteca.model.Prestamo;
import co.analisys.biblioteca.model.PrestamoId;
import co.analisys.biblioteca.model.UsuarioId;
import co.analisys.biblioteca.service.CirculacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/circulacion")
public class CirculacionController {

    @Autowired
    private CirculacionService circulacionService;

    @Operation(
            summary = "Prestar un libro",
            description = "Permite a un bibliotecario registrar el préstamo de un libro a un usuario."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Préstamo registrado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Parámetros inválidos", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado (solo ROLE_LIBRARIAN)", content = @Content)
    })
    @PostMapping("/prestar")
    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    public void prestarLibro(
            @Parameter(description = "ID del usuario que solicita el préstamo", required = true)
            @RequestParam String usuarioId,
            @Parameter(description = "ID del libro que se va a prestar", required = true)
            @RequestParam String libroId) {
        circulacionService.prestarLibro(new UsuarioId(usuarioId), new LibroId(libroId));
    }

    @Operation(
            summary = "Devolver un libro",
            description = "Permite a un bibliotecario registrar la devolución de un libro previamente prestado."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Devolución registrada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Parámetros inválidos", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado (solo ROLE_LIBRARIAN)", content = @Content)
    })
    @PostMapping("/devolver")
    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    public void devolverLibro(
            @Parameter(description = "ID del préstamo que se va a cerrar", required = true)
            @RequestParam String prestamoId) {
        circulacionService.devolverLibro(new PrestamoId(prestamoId));
    }

    @Operation(
            summary = "Obtener todos los préstamos",
            description = "Devuelve una lista con todos los préstamos activos o históricos registrados en el sistema. " +
                    "Disponible para usuarios y bibliotecarios."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de préstamos obtenido exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Prestamo.class))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado (requiere ROLE_USER o ROLE_LIBRARIAN)", content = @Content)
    })
    @GetMapping("/prestamos")
    @PreAuthorize("hasAnyRole('ROLE_LIBRARIAN', 'ROLE_USER')")
    public List<Prestamo> obtenerTodosPrestamos() {
        return circulacionService.obtenerTodosPrestamos();
    }

    @Operation(
            summary = "Estado público del servicio de circulación",
            description = "Endpoint público que permite verificar si el servicio de circulación está en funcionamiento."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "El servicio responde correctamente",
                    content = @Content(mediaType = "text/plain"))
    })
    @GetMapping("/public/status")
    public String getPublicStatus() {
        return "El servicio de circulación está funcionando correctamente";
    }
}
