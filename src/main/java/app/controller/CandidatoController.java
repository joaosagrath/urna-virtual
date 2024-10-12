package app.controller;

import app.entity.Candidato;
import app.service.CandidatoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/candidatos")
public class CandidatoController {

    @Autowired
    private CandidatoService candidatoService;

    // Endpoint para criar
    @PostMapping("/salvar")
    public ResponseEntity<Candidato> salvarCandidato(@RequestBody Candidato candidato) {
        Candidato novoCandidato = candidatoService.salvarCandidato(candidato);
        return ResponseEntity.ok(novoCandidato);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Candidato> editarCandidato(@PathVariable Long id, @RequestBody Candidato novosDados) {
        Candidato candidatoAtualizado = candidatoService.editarCandidato(id, novosDados);
        return ResponseEntity.ok(candidatoAtualizado);
    }
    
    // Endpoint para listar todos candidatos
    @GetMapping("/todos")
    public ResponseEntity<List<Candidato>> listarTodosCandidatos() {
        List<Candidato> candidatos = candidatoService.getAllcandidatos();
        return ResponseEntity.ok(candidatos);
    }

    // Endpoint para listar candidatos ativos
    @GetMapping("/ativos")
    public ResponseEntity<List<Candidato>> listarCandidatosAtivos() {
        List<Candidato> candidatosAtivos = candidatoService.getCandidatosAtivos();
        return ResponseEntity.ok(candidatosAtivos);
    }

    // Endpoint para inativar um candidato
    @PutMapping("/inativar/{id}")
    public ResponseEntity<Void> inativarCandidato(@PathVariable Long id) {
        candidatoService.inativarCandidato(id);
        return ResponseEntity.noContent().build();
    }
}
