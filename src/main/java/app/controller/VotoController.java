package app.controller;

import app.entity.Apuracao;
import app.entity.Voto;
import app.service.VotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/votos")
public class VotoController {

    @Autowired
    private VotoService votoService;

    // Endpoint para registrar um voto
    @PostMapping("/{eleitorId}")
    public ResponseEntity<String> votar(@PathVariable Long eleitorId, @RequestBody Voto voto) {
        try {
            String hash = votoService.votar(eleitorId, voto);
            return ResponseEntity.ok("Voto registrado com sucesso! Hash de votação: " + hash);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Endpoint para realizar a apuração
    @GetMapping("/apuracao")
    public ResponseEntity<Apuracao> apurarVotos() {
        Apuracao apuracao = votoService.realizarApuracao();
        return ResponseEntity.ok(apuracao);
    }
}
