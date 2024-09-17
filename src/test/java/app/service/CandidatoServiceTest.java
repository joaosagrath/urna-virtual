package app.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import app.entity.Candidato;
import app.repository.CandidatoRepository;

@SpringBootTest
public class CandidatoServiceTest {

    @Mock
    private CandidatoRepository candidatoRepository;

    @InjectMocks
    private CandidatoService candidatoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    void EditarCandidatoComSucesso() {
        // Setup
        Candidato candidatoExistente = new Candidato();
        candidatoExistente.setId(1L);
        candidatoExistente.setNomeCompleto("João");

        Candidato novosDados = new Candidato();
        novosDados.setNomeCompleto("João Atualizado");

        when(candidatoRepository.findById(1L)).thenReturn(Optional.of(candidatoExistente));

        // Execução
        candidatoService.editarCandidato(1L, novosDados);

        // Verificação
        assertThat(candidatoExistente.getNomeCompleto()).isEqualTo("João Atualizado");
        verify(candidatoRepository).save(candidatoExistente);
    }

    @Test
    void CandidatoNaoEncontrado() {
        // Setup
        when(candidatoRepository.findById(1L)).thenReturn(Optional.empty());

        Candidato novosDados = new Candidato();

        // Verificação de exceção
        Exception exception = assertThrows(RuntimeException.class, () -> {
            candidatoService.editarCandidato(1L, novosDados);
        });

        assertThat(exception.getMessage()).isEqualTo("Candidato não encontrado");
    }
    
    @Test
    void InativarCandidato_CandidatoExistente() {
        // Mock de um candidato existente
        Long candidatoId = 1L;
        Candidato candidato = new Candidato();
        candidato.setId(candidatoId);
        candidato.setStatus(Candidato.StatusCandidato.ATIVO);

        // Simula o retorno do candidato quando findById é chamado
        when(candidatoRepository.findById(candidatoId)).thenReturn(Optional.of(candidato));

        // Executa o método
        candidatoService.inativarCandidato(candidatoId);

        // Verifica se o status do candidato foi alterado para INATIVO
        assertEquals(Candidato.StatusCandidato.INATIVO, candidato.getStatus());

        // Verifica se o método save foi chamado com o candidato inativado
        verify(candidatoRepository, times(1)).save(candidato);
    }

    @Test
    void InativarCandidato_CandidatoNaoExistente() {
        // Mock para um candidato não existente
        Long candidatoId = 1L;

        // Simula o retorno de Optional.empty() quando findById é chamado
        when(candidatoRepository.findById(candidatoId)).thenReturn(Optional.empty());

        // Executa o método
        candidatoService.inativarCandidato(candidatoId);

        // Verifica se o método save nunca foi chamado, já que o candidato não existe
        verify(candidatoRepository, never()).save(any(Candidato.class));
    }
}
