package app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.entity.Eleitor;
import app.repository.EleitorRepository;

@Service
public class EleitorService {

    @Autowired
    private EleitorRepository eleitorRepository;

    // Método para salvar ou atualizar um eleitor
    public Eleitor salvarEleitor(Eleitor eleitor) {
        if (eleitor.getCpf() == null || eleitor.getEmail() == null) {
            eleitor.setStatus(Eleitor.StatusEleitor.PENDENTE);
        } else {
            eleitor.setStatus(Eleitor.StatusEleitor.APTO);
        }
        return eleitorRepository.save(eleitor);
    }
    
    // Método para editar dados de um eleitor
    public Eleitor editarEleitor(Long eleitorId, Eleitor novosDados) {
        Eleitor eleitor = eleitorRepository.findById(eleitorId)
                .orElseThrow(() -> new RuntimeException("Eleitor não encontrado"));

        // Atualizar os campos com os novos dados
        eleitor.setNomeCompleto(novosDados.getNomeCompleto());
        eleitor.setStatus(novosDados.getStatus());

        // Salvar as alterações no banco de dados
        return eleitorRepository.save(eleitor);
    }

    // Método para listar eleitores ativos
    public List<Eleitor> getEleitoresAtivos() {
        return eleitorRepository.findByStatus(Eleitor.StatusEleitor.APTO);
    }

    // Método para inativar um eleitor
    public void inativarEleitor(Long id) {
        Optional<Eleitor> eleitor = eleitorRepository.findById(id);
        if (eleitor.isPresent()) {
            Eleitor e = eleitor.get();
            if (e.getStatus() == Eleitor.StatusEleitor.VOTOU) {
                throw new IllegalStateException("Usuário já votou. Não foi possível inativá-lo");
            }
            e.setStatus(Eleitor.StatusEleitor.INATIVO);
            eleitorRepository.save(e);
        }
    }
}
