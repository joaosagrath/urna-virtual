package app.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import app.entity.Eleitor;
import app.repository.EleitorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class EleitorServiceTest {

    @InjectMocks
    private EleitorService eleitorService;

    @Mock
    private EleitorRepository eleitorRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testInativarEleitor_EleitorExistenteENaoVotou() {
        // Mock de um eleitor existente que não votou
        Long eleitorId = 1L;
        Eleitor eleitor = new Eleitor();
        eleitor.setId(eleitorId);
        eleitor.setStatus(Eleitor.StatusEleitor.APTO);

        // Simula o retorno do eleitor quando findById é chamado
        when(eleitorRepository.findById(eleitorId)).thenReturn(Optional.of(eleitor));

        // Executa o método
        eleitorService.inativarEleitor(eleitorId);

        // Verifica se o status do eleitor foi alterado para INATIVO
        assertEquals(Eleitor.StatusEleitor.INATIVO, eleitor.getStatus());

        // Verifica se o método save foi chamado com o eleitor inativado
        verify(eleitorRepository, times(1)).save(eleitor);
    }

    @Test
    void testInativarEleitor_EleitorExistenteEVotou() {
        // Mock de um eleitor existente que já votou
        Long eleitorId = 1L;
        Eleitor eleitor = new Eleitor();
        eleitor.setId(eleitorId);
        eleitor.setStatus(Eleitor.StatusEleitor.VOTOU);

        // Simula o retorno do eleitor quando findById é chamado
        when(eleitorRepository.findById(eleitorId)).thenReturn(Optional.of(eleitor));

        // Executa o método e verifica se a exceção é lançada
        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
            eleitorService.inativarEleitor(eleitorId);
        });

        // Verifica se a mensagem da exceção está correta
        assertEquals("Usuário já votou. Não foi possível inativá-lo", thrown.getMessage());

        // Verifica se o método save nunca foi chamado, já que não é permitido inativar o eleitor
        verify(eleitorRepository, never()).save(any(Eleitor.class));
    }

    @Test
    void testInativarEleitor_EleitorNaoExistente() {
        // Mock para um eleitor não existente
        Long eleitorId = 1L;

        // Simula o retorno de Optional.empty() quando findById é chamado
        when(eleitorRepository.findById(eleitorId)).thenReturn(Optional.empty());

        // Executa o método
        eleitorService.inativarEleitor(eleitorId);

        // Verifica se o método save nunca foi chamado, já que o eleitor não existe
        verify(eleitorRepository, never()).save(any(Eleitor.class));
    }

    @Test
    void testGetEleitoresAtivos() {
        // Mock de eleitores ativos
        Eleitor eleitor1 = new Eleitor();
        eleitor1.setStatus(Eleitor.StatusEleitor.APTO);

        Eleitor eleitor2 = new Eleitor();
        eleitor2.setStatus(Eleitor.StatusEleitor.APTO);

        List<Eleitor> eleitoresAtivos = Arrays.asList(eleitor1, eleitor2);

        // Simula o retorno da lista de eleitores ativos quando findByStatus é chamado
        when(eleitorRepository.findByStatus(Eleitor.StatusEleitor.APTO)).thenReturn(eleitoresAtivos);

        // Executa o método
        List<Eleitor> result = eleitorService.getEleitoresAtivos();

        // Verifica se a lista retornada é a esperada
        assertEquals(2, result.size());
        assertTrue(result.contains(eleitor1));
        assertTrue(result.contains(eleitor2));
    }
    
    @Test
    void testSalvarEleitor_ComCpfEmail() {
        // Cria um eleitor com CPF e email
        Eleitor eleitor = new Eleitor();
        eleitor.setCpf("12345678900");
        eleitor.setEmail("test@example.com");
        
        // Simula o retorno do eleitor salvo
        when(eleitorRepository.save(eleitor)).thenReturn(eleitor);

        // Executa o método
        Eleitor result = eleitorService.salvarEleitor(eleitor);

        // Verifica se o status foi corretamente definido como APTO
        assertEquals(Eleitor.StatusEleitor.APTO, result.getStatus());
        // Verifica se o método save foi chamado uma vez com o eleitor
        verify(eleitorRepository, times(1)).save(eleitor);
    }

    @Test
    void SalvarEleitor_SemCpfEmail() {
        // Cria um eleitor sem CPF e email
        Eleitor eleitor = new Eleitor();
        eleitor.setCpf(null);
        eleitor.setEmail(null);

        // Simula o retorno do eleitor salvo
        when(eleitorRepository.save(eleitor)).thenReturn(eleitor);

        // Executa o método
        Eleitor result = eleitorService.salvarEleitor(eleitor);

        // Verifica se o status foi corretamente definido como PENDENTE
        assertEquals(Eleitor.StatusEleitor.PENDENTE, result.getStatus());
        // Verifica se o método save foi chamado uma vez com o eleitor
        verify(eleitorRepository, times(1)).save(eleitor);
    }

    @Test
    void EditarEleitor_EleitorExistente() {
        Long eleitorId = 1L;
        Eleitor eleitorExistente = new Eleitor();
        eleitorExistente.setId(eleitorId);
        eleitorExistente.setNomeCompleto("Nome Antigo");
        eleitorExistente.setStatus(Eleitor.StatusEleitor.PENDENTE);

        Eleitor novosDados = new Eleitor();
        novosDados.setNomeCompleto("Nome Novo");
        novosDados.setStatus(Eleitor.StatusEleitor.APTO);

        // Simula o retorno do eleitor existente quando findById é chamado
        when(eleitorRepository.findById(eleitorId)).thenReturn(Optional.of(eleitorExistente));
        // Simula o retorno do eleitor atualizado quando salvo
        when(eleitorRepository.save(any(Eleitor.class))).thenReturn(novosDados);

        // Executa o método
        Eleitor result = eleitorService.editarEleitor(eleitorId, novosDados);

        // Verifica se os dados foram atualizados corretamente
        assertEquals("Nome Novo", result.getNomeCompleto());
        assertEquals(Eleitor.StatusEleitor.APTO, result.getStatus());
        // Verifica se o método save foi chamado com o eleitor atualizado
        verify(eleitorRepository, times(1)).save(eleitorExistente);
    }

    @Test
    void testEditarEleitor_EleitorNaoExistente() {
        Long eleitorId = 1L;
        Eleitor novosDados = new Eleitor();
        novosDados.setNomeCompleto("Nome Novo");

        // Simula o retorno de Optional.empty() quando findById é chamado
        when(eleitorRepository.findById(eleitorId)).thenReturn(Optional.empty());

        // Executa o método e verifica se a exceção é lançada
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            eleitorService.editarEleitor(eleitorId, novosDados);
        });

        // Verifica se a mensagem da exceção está correta
        assertEquals("Eleitor não encontrado", thrown.getMessage());

        // Verifica se o método save nunca foi chamado
        verify(eleitorRepository, never()).save(any(Eleitor.class));
    }
}
