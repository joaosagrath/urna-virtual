package app.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import app.entity.Apuracao;
import app.entity.Candidato;
import app.entity.Eleitor;
import app.entity.Voto;
import app.repository.CandidatoRepository;
import app.repository.EleitorRepository;
import app.repository.VotoRepository;

@SpringBootTest
public class VotoServiceTest {

    @Mock
    private EleitorRepository eleitorRepository;

    @Mock
    private CandidatoRepository candidatoRepository;

    @Mock
    private VotoRepository votoRepository;

    @InjectMocks
    private VotoService votoService;

    @Test
    void EleitorInaptoParaVotacao() {
        // Setup
        Eleitor eleitor = new Eleitor();
        eleitor.setStatus(Eleitor.StatusEleitor.VOTOU);

        when(eleitorRepository.findById(1L)).thenReturn(Optional.of(eleitor));

        Voto voto = new Voto();

        // Verifica exceção lançada pela regra de negócio
        Exception exception = assertThrows(RuntimeException.class, () -> {
            votoService.votar(1L, voto);
        });

        assertThat(exception.getMessage()).isEqualTo("Eleitor inapto para votação");
    }

    @Test
    void VotarComSucesso() {
        // Setup
        Eleitor eleitor = new Eleitor();
        eleitor.setStatus(Eleitor.StatusEleitor.APTO);

        Candidato candidatoPrefeito = new Candidato();
        candidatoPrefeito.setId(1L);
        candidatoPrefeito.setStatus(Candidato.StatusCandidato.ATIVO);

        Candidato candidatoVereador = new Candidato();
        candidatoVereador.setId(2L);
        candidatoVereador.setStatus(Candidato.StatusCandidato.ATIVO);

        Voto voto = new Voto();
        voto.setCandidatoPrefeito(candidatoPrefeito);
        voto.setCandidatoVereador(candidatoVereador);

        when(eleitorRepository.findById(1L)).thenReturn(Optional.of(eleitor));
        when(candidatoRepository.findById(1L)).thenReturn(Optional.of(candidatoPrefeito));
        when(candidatoRepository.findById(2L)).thenReturn(Optional.of(candidatoVereador));

        // Execução
        String hash = votoService.votar(1L, voto);

        // Verificação
        assertThat(hash).isNotNull();
        verify(eleitorRepository).save(eleitor); // Verifica se o status foi salvo
        verify(votoRepository).save(voto);
    }

    @Test
    void CandidatoInvalido() {
        // Setup
        Eleitor eleitor = new Eleitor();
        eleitor.setStatus(Eleitor.StatusEleitor.APTO);

        Voto voto = new Voto();
        Candidato candidatoPrefeito = new Candidato();
        candidatoPrefeito.setId(1L);

        voto.setCandidatoPrefeito(candidatoPrefeito);

        when(eleitorRepository.findById(1L)).thenReturn(Optional.of(eleitor));
        when(candidatoRepository.findById(1L)).thenReturn(Optional.empty());

        // Verifica se exceção é lançada
        Exception exception = assertThrows(RuntimeException.class, () -> {
            votoService.votar(1L, voto);
        });

        assertThat(exception.getMessage()).isEqualTo("Candidato(s) inválido(s). Refaça a requisição!");
    }
    
    @Test
    void RealizarApuracao() {
        // Mock da lista de candidatos ativos
        List<Candidato> candidatos = new ArrayList<>();
        Candidato candidato1 = new Candidato();
        candidato1.setId(1L);
        candidato1.setCargo("Prefeito");
        candidato1.setStatus(Candidato.StatusCandidato.ATIVO);
        
        Candidato candidato2 = new Candidato();
        candidato2.setId(2L);
        candidato2.setCargo("Vereador");
        candidato2.setStatus(Candidato.StatusCandidato.ATIVO);

        candidatos.add(candidato1);
        candidatos.add(candidato2);

        // Simular o retorno dos candidatos ativos
        when(candidatoRepository.findByStatus(Candidato.StatusCandidato.ATIVO)).thenReturn(candidatos);

        // Simular contagem de votos
        when(votoRepository.countByCandidatoPrefeitoId(1L)).thenReturn(100); // Prefeito
        when(votoRepository.countByCandidatoVereadorId(2L)).thenReturn(200); // Vereador
        when(votoRepository.count()).thenReturn(300L);  // Total de votos

        // Executar o método
        Apuracao apuracao = votoService.realizarApuracao();

        // Verificações
        assertNotNull(apuracao);
        assertEquals(300, apuracao.getTotalVotos());  // Verifica total de votos

        // Verifica se os candidatos foram corretamente separados e os votos atribuídos
        assertEquals(1, apuracao.getCandidatosPrefeito().size());
        assertEquals(1, apuracao.getCandidatosVereador().size());

        // Verifica se o candidato a prefeito tem os votos atribuídos
        Candidato prefeito = apuracao.getCandidatosPrefeito().get(0);
        assertEquals(100, prefeito.getVotosTotais());

        // Verifica se o candidato a vereador tem os votos atribuídos
        Candidato vereador = apuracao.getCandidatosVereador().get(0);
        assertEquals(200, vereador.getVotosTotais());

        // Verifica se os métodos corretos foram chamados
        verify(candidatoRepository, times(1)).findByStatus(Candidato.StatusCandidato.ATIVO);
        verify(votoRepository, times(1)).countByCandidatoPrefeitoId(1L);
        verify(votoRepository, times(1)).countByCandidatoVereadorId(2L);
        verify(votoRepository, times(1)).count();
    }
}
