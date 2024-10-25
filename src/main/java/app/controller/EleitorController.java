package app.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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

import app.entity.Eleitor;
import app.service.EleitorService;

@RestController
@RequestMapping("/api/eleitores")
@CrossOrigin(origins = "http://localhost:4200") 
public class EleitorController {

    @Autowired
    private EleitorService eleitorService;
    
    /*
    // Endpoint para criar um eleitor
    @PostMapping("/salvar")
    public ResponseEntity<Eleitor> salvarEleitor(@RequestBody Eleitor eleitor) {
        Eleitor novoEleitor = eleitorService.salvarEleitor(eleitor);
        return ResponseEntity.ok(novoEleitor);
    }*/
    
    @PostMapping(value = "/salvar", consumes = {"multipart/form-data"})
    public ResponseEntity<Eleitor> salvarEleitor(
            @RequestPart("eleitor") Eleitor eleitor,
            @RequestPart(value = "foto", required = false) MultipartFile foto) {

    	try {

            // Processar a foto se estiver presente
            if (foto != null && !foto.isEmpty()) {
                String diretorioFotos = "c:/fotos/fotoEleitor/"; // Altere o diretório se necessário
                String nomeArquivo = UUID.randomUUID().toString() + "_" + foto.getOriginalFilename();
                java.nio.file.Path caminhoArquivo = Paths.get(diretorioFotos + nomeArquivo);

                // Salva a foto no servidor
                Files.copy(foto.getInputStream(), caminhoArquivo);

                // Define o nome da foto no eleitor
                eleitor.setFoto(nomeArquivo);
            }

            // Salva o eleitor no banco de dados
            Eleitor novoEleitor = eleitorService.salvarEleitor(eleitor);
            return ResponseEntity.ok(novoEleitor);

        } catch (IOException e) {
            e.printStackTrace(); // Registra a exceção no console
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } catch (Exception e) {
            e.printStackTrace(); // Registra qualquer outra exceção
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    

    
    @PutMapping("/{id}")
    public ResponseEntity<Eleitor> editarEleitor(@PathVariable Long id, @RequestBody Eleitor novosDados) {
        Eleitor eleitorAtualizado = eleitorService.editarEleitor(id, novosDados);
        return ResponseEntity.ok(eleitorAtualizado);
    }
    
    @GetMapping("/findById/{id}")
    public ResponseEntity<Eleitor> findById(@PathVariable Long id){
    	try {
    		Eleitor eleitor = this.eleitorService.findById(id);
			return new ResponseEntity<Eleitor>(eleitor, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
    }
    
    @GetMapping("/findByCpf/{cpf}")
    public ResponseEntity<Eleitor> findByCpf(@PathVariable String cpf){
    	try {
    		Eleitor eleitor = this.eleitorService.findByCpf(cpf);
			return new ResponseEntity<Eleitor>(eleitor, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
    }
    
    // endpont para listar todos os eleitores
    @GetMapping("/findAll")
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
