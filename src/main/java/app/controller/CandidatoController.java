package app.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import app.entity.Candidato;
import app.service.CandidatoService;
import jakarta.persistence.criteria.Path;

@RestController
@RequestMapping("/api/candidatos")
@CrossOrigin("*")
public class CandidatoController {

    @Autowired
    private CandidatoService candidatoService;
    
    /*
    // Endpoint para criar
    @PostMapping("/salvar")
    public ResponseEntity<Candidato> salvarCandidato(@RequestBody Candidato candidato) {
        
    	Candidato novoCandidato = candidatoService.salvarCandidato(candidato);
        return ResponseEntity.ok(novoCandidato);
    }*/
    
    @PostMapping(value = "/salvar", consumes = {"multipart/form-data"})
    public ResponseEntity<Candidato> salvarCandidato(
            @RequestPart("candidato") Candidato candidato,
            @RequestPart(value = "foto", required = false) MultipartFile foto) {
        
        try {
            // Processar a foto se estiver presente
            if (foto != null && !foto.isEmpty()) {
                String diretorioFotos = "fotoCandidato/";
                String nomeArquivo = UUID.randomUUID().toString() + "_" + foto.getOriginalFilename();
                java.nio.file.Path caminhoArquivo = Paths.get(diretorioFotos + nomeArquivo);

                // Salva a foto no servidor
                Files.copy(foto.getInputStream(), caminhoArquivo);

                // Define o nome da foto no candidato
                candidato.setFoto(nomeArquivo);
            }

            // Salva o candidato no banco de dados
            Candidato novoCandidato = candidatoService.salvarCandidato(candidato);
            return ResponseEntity.ok(novoCandidato);
            
        } catch (IOException e) {
            e.printStackTrace(); // Registra a exceção no console
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } catch (Exception e) {
            e.printStackTrace(); // Registra qualquer outra exceção
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
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
