package app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.entity.Eleitor;
import app.repository.EleitorRepository;
import jakarta.validation.Valid;

@Service
public class EleitorService {

    @Autowired
    private EleitorRepository eleitorRepository;

    public Eleitor salvarEleitor(@Valid Eleitor eleitor) {
        boolean cpfVazio = eleitor.getCpf() == null || eleitor.getCpf().isEmpty();
        boolean emailVazio = eleitor.getEmail() == null || eleitor.getEmail().isEmpty();

        // Verifica se CPF ou e-mail estão vazios e define o status como PENDENTE em ambos os casos
        if (cpfVazio && emailVazio) {
            // Caso ambos CPF e e-mail estejam vazios
            eleitor.setCpf(null);   // Define o CPF como null
            eleitor.setEmail(null); // Define o e-mail como null
            eleitor.setStatus(Eleitor.StatusEleitor.PENDENTE); // Define o status como PENDENTE
        } else if (cpfVazio) {
            // Caso apenas o CPF esteja vazio
            eleitor.setCpf(null);   // Define o CPF como null
            eleitor.setStatus(Eleitor.StatusEleitor.PENDENTE); // Define o status como PENDENTE
        } else if (emailVazio) {
            // Caso apenas o e-mail esteja vazio
            eleitor.setEmail(null); // Define o e-mail como null
            eleitor.setStatus(Eleitor.StatusEleitor.PENDENTE); // Define o status como PENDENTE
        } else {
            // Caso ambos CPF e e-mail estejam preenchidos
            eleitor.setStatus(Eleitor.StatusEleitor.APTO); // Define o status como APTO
        }

        // Salva ou atualiza o eleitor no banco de dados
        return eleitorRepository.save(eleitor);
    }
    
    public Eleitor findByCpf(String cpf) {
    	Eleitor eleitor = eleitorRepository.findByCpf(cpf).get();
    	return eleitor;
    }
    
    public Eleitor findById(Long id) {
    	Eleitor eleitor = eleitorRepository.findById(id).get();
    	return eleitor;
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
    
    // Método para listar todos os eleitores
    public List<Eleitor> getAlleleitores(){
    	return eleitorRepository.findAll();
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
