package app.controller;

import app.entity.Candidato;
import app.entity.Eleitor;
import app.service.EleitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/eleitores")
@CrossOrigin("*")
public class EleitorController {

    @Autowired
    private EleitorService eleitorService;

    // Endpoint para criar um eleitor
    @PostMapping("/salvar")
    public ResponseEntity<Eleitor> salvarEleitor(@RequestBody Eleitor eleitor) {
        Eleitor novoEleitor = eleitorService.salvarEleitor(eleitor);
        return ResponseEntity.ok(novoEleitor);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Eleitor> editarEleitor(@PathVariable Long id, @RequestBody Eleitor novosDados) {
        Eleitor eleitorAtualizado = eleitorService.editarEleitor(id, novosDados);
        return ResponseEntity.ok(eleitorAtualizado);
    }
    
    // endpont para listar todos os eleitores
    @GetMapping("/todos")
    public ResponseEntity<List<Eleitor>> listarTodosEleitores() {
        List<Eleitor> candidatos = eleitorService.getAlleleitores();
        return ResponseEntity.ok(candidatos);
    }

    // Endpoint para listar eleitores ativos
    @GetMapping("/ativos")
    public ResponseEntity<List<Eleitor>> listarEleitoresAtivos() {
        List<Eleitor> eleitoresAtivos = eleitorService.getEleitoresAtivos();
        return ResponseEntity.ok(eleitoresAtivos);
    }

    // Endpoint para inativar um eleitor
    @PutMapping("/inativar/{id}")
    public ResponseEntity<Void> inativarEleitor(@PathVariable Long id) {
        eleitorService.inativarEleitor(id);
        return ResponseEntity.noContent().build();
    }
}
