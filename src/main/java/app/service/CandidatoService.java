package app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.repository.CandidatoRepository;

import app.entity.Candidato;

@Service
public class CandidatoService {

    @Autowired
    private CandidatoRepository candidatoRepository;

    // Método para salvar um candidato
    public Candidato salvarCandidato(Candidato candidato) {
        candidato.setStatus(Candidato.StatusCandidato.ATIVO);
        return candidatoRepository.save(candidato);
    }
    
    // Método para editar dados de um candidato
    public Candidato editarCandidato(Long candidatoId, Candidato novosDados) {
        Candidato candidato = candidatoRepository.findById(candidatoId)
                .orElseThrow(() -> new RuntimeException("Candidato não encontrado"));

        // Atualizar os campos com os novos dados
        candidato.setNomeCompleto(novosDados.getNomeCompleto());
        candidato.setNumero(novosDados.getNumero());
        candidato.setCargo(novosDados.getCargo());
        candidato.setStatus(novosDados.getStatus());

        // Salvar as alterações no banco de dados
        return candidatoRepository.save(candidato);
    }
    
    // Método para listar todos os candidatos
    public List<Candidato> getAllcandidatos(){
    	return candidatoRepository.findAll();
    }

    // Método para listar candidatos ativos
    public List<Candidato> getCandidatosAtivos() {
        return candidatoRepository.findByStatus(Candidato.StatusCandidato.ATIVO);
    }

    // Método para inativar um candidato
    public void inativarCandidato(Long id) {
        Optional<Candidato> candidato = candidatoRepository.findById(id);
        if (candidato.isPresent()) {
            Candidato c = candidato.get();
            c.setStatus(Candidato.StatusCandidato.INATIVO);
            candidatoRepository.save(c);
        }
    }
}
