package app.controller;

import app.entity.Eleitor;
import app.service.EleitorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

@WebMvcTest(EleitorController.class)
public class EleitorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EleitorService eleitorService;

    @Autowired
    private ObjectMapper objectMapper;  // Para converter objetos em JSON e vice-versa

    private Eleitor eleitor;

    @BeforeEach
    void setUp() {
        eleitor = new Eleitor();
        eleitor.setId(1L);
        eleitor.setNomeCompleto("João da Silva");
        eleitor.setCpf("12345678901");
        eleitor.setProfissao("Engenheiro");
        eleitor.setTelefoneCelular("11987654321");
        eleitor.setTelefoneFixo("1134567890");
        eleitor.setEmail("joao.silva@example.com");
        eleitor.setStatus(Eleitor.StatusEleitor.APTO);
    }

    @Test
    void SalvarEleitor() throws Exception {
        when(eleitorService.salvarEleitor(Mockito.any(Eleitor.class))).thenReturn(eleitor);

        mockMvc.perform(post("/api/eleitores/salvar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eleitor)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomeCompleto").value("João da Silva"))
                .andExpect(jsonPath("$.cpf").value("12345678901"));
    }

    @Test
    void EditarEleitor() throws Exception {
        Eleitor atualizado = new Eleitor();
        atualizado.setId(1L);
        atualizado.setNomeCompleto("João da Silva Atualizado");
        atualizado.setCpf("12345678901");
        atualizado.setProfissao("Engenheiro");
        atualizado.setTelefoneCelular("11987654321");
        atualizado.setTelefoneFixo("1134567890");
        atualizado.setEmail("joao.silva@example.com");
        atualizado.setStatus(Eleitor.StatusEleitor.APTO);

        when(eleitorService.editarEleitor(Mockito.anyLong(), Mockito.any(Eleitor.class))).thenReturn(atualizado);

        mockMvc.perform(put("/api/eleitores/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(atualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomeCompleto").value("João da Silva Atualizado"));
    }

    @Test
    void ListarEleitoresAtivos() throws Exception {
        when(eleitorService.getEleitoresAtivos()).thenReturn(List.of(eleitor));

        mockMvc.perform(get("/api/eleitores/ativos")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nomeCompleto").value("João da Silva"));
    }

    @Test
    void InativarEleitor() throws Exception {
        mockMvc.perform(put("/api/eleitores/inativar/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
