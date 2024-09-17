package app.repository;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import app.entity.Candidato;

@SpringBootTest
@Transactional
public class CandidatoRepositoryTest {

    @Autowired
    private CandidatoRepository candidatoRepository;

    @Test
    void SalvarCandidatoComSucesso() {
        // Setup
        Candidato candidato = new Candidato();
        candidato.setNomeCompleto("Maria");
        candidato.setNumero(1234);
        candidato.setCargo("Vereador");
        candidato.setStatus(Candidato.StatusCandidato.ATIVO);

        // Execução
        Candidato salvo = candidatoRepository.save(candidato);

        // Verificação
        assertThat(salvo.getId()).isNotNull();
        assertThat(salvo.getNomeCompleto()).isEqualTo("Maria");
    }
}
