package app.controller;

import app.entity.Apuracao;
import app.service.VotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/votacao")
@CrossOrigin("*")
public class VotacaoController {

    @Autowired
    private VotoService votoService;

    // Endpoint para apurar os votos
    @GetMapping("/apuracao")
    public ResponseEntity<Apuracao> apurarVotos() {
        Apuracao apuracao = votoService.realizarApuracao();
        return ResponseEntity.ok(apuracao);
    }
}
