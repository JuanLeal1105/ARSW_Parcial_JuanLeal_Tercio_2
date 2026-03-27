package edu.eci.arsw.blueprints.controllers;

import edu.eci.arsw.blueprints.controllers.dto.ResponseDTO;
import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
import edu.eci.arsw.blueprints.services.BlueprintsServices;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/blueprints")
public class BlueprintsAPIController {
    private final BlueprintsServices services;


    public BlueprintsAPIController(BlueprintsServices services) {
        this.services = services;
    }

    // GET /blueprints
    @GetMapping
    public ResponseEntity<ResponseDTO<Set<Blueprint>>> getAll() {
        try{
            Set<Blueprint> list = services.getAllBlueprints();
            return ResponseEntity.ok(ResponseDTO.success(list, "Datos traidos", 200, HttpStatus.OK));
        }catch (BlueprintNotFoundException e){
            return ResponseEntity.ok(ResponseDTO.error("Error al traer planos pa", 400, HttpStatus.NOT_FOUND));
        }

    }

    // GET /blueprints/{author}
    @GetMapping("/{author}")
    public ResponseEntity<ResponseDTO<Set<Blueprint>>> byAuthor(@PathVariable String author) {
        try {
            Set<Blueprint> setsitos = services.getBlueprintsByAuthor(author);
            return ResponseEntity.ok(ResponseDTO.success(setsitos, "Planos encontrados", 200, HttpStatus.OK));
        } catch (BlueprintNotFoundException e) {
            return ResponseEntity.ok(ResponseDTO.error("Planos no encontrados pa", 404, HttpStatus.NOT_FOUND));
        }
    }

    // GET /blueprints/{author}/{bpname}
    @GetMapping("/{author}/{bpname}")
    public ResponseEntity<ResponseDTO<Blueprint>> byAuthorAndName(@PathVariable String author, @PathVariable String bpname) {
        try {
            Blueprint planito = services.getBlueprint(author, bpname);
            return ResponseEntity.ok(ResponseDTO.success(planito, "Plano encontrado",200, HttpStatus.FOUND));
        } catch (BlueprintNotFoundException e) {
            return ResponseEntity.ok(ResponseDTO.error("Plano no encontrado pa",404, HttpStatus.NOT_FOUND));
        }
    }

    // POST /blueprints
    @PostMapping
    public ResponseEntity<ResponseDTO<Blueprint>> add(@Valid @RequestBody NewBlueprintRequest req) {
        try {
            Blueprint bp = new Blueprint(req.author(), req.name(), req.points());
            services.addNewBlueprint(bp);
            return ResponseEntity.ok(ResponseDTO.success(bp, "Plano creado con éxito", 201, HttpStatus.CREATED));
        } catch (BlueprintPersistenceException e) {
            return ResponseEntity.ok(ResponseDTO.error("Plano no se pudo crear pa", 400, HttpStatus.BAD_REQUEST));
        }
    }

    // PUT /blueprints/{author}/{bpname}/points
    @PutMapping("/{author}/{bpname}/points")
    public ResponseEntity<ResponseDTO<Void>> addPoint(@PathVariable String author, @PathVariable String bpname,
                                      @RequestBody Point p) {
        try {
            services.addPoint(author, bpname, p.x(), p.y());
            return ResponseEntity.ok(ResponseDTO.success(null, "Punto añadido con éxito", 202, HttpStatus.ACCEPTED));
        } catch (BlueprintNotFoundException e) {
            return ResponseEntity.ok(ResponseDTO.error("Punto no se puede añadir pa",400, HttpStatus.BAD_REQUEST));
        }
    }
    public record NewBlueprintRequest(
            @NotBlank String author,
            @NotBlank String name,
            @Valid java.util.List<Point> points
    ) { }
}
