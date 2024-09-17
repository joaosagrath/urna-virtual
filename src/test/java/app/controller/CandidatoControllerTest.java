package app.controller;

import app.entity.Candidato;
import app.service.CandidatoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CandidatoController.class)
public class CandidatoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CandidatoService candidatoService;

    @Autowired
    private ObjectMapper objectMapper;  // Para converter objetos em JSON e vice-versa

    private Candidato candidato;

    @BeforeEach
    void setUp() {
        candidato = new Candidato();
        candidato.setId(1L);
        candidato.setNomeCompleto("Maria Souza");
        candidato.setNumero(10);
        candidato.setCargo("Prefeito");
        candidato.setStatus(Candidato.StatusCandidato.ATIVO);
        candidato.setVotosTotais(1000);
    }

    @Test
    void SalvarCandidato() throws Exception {
        when(candidatoService.salvarCandidato(Mockito.any(Candidato.class))).thenReturn(candidato);

        mockMvc.perform(post("/api/candidatos/salvar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(candidato)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomeCompleto").value("Maria Souza"))
                .andExpect(jsonPath("$.numero").value(10))
                .andExpect(jsonPath("$.cargo").value("Prefeito"));
    }

    @Test
    void EditarCandidato() throws Exception {
        Candidato atualizado = new Candidato();
        atualizado.setId(1L);
        atualizado.setNomeCompleto("Maria Souza Atualizada");
        atualizado.setNumero(20);
        atualizado.setCargo("Governador");
        atualizado.setStatus(Candidato.StatusCandidato.ATIVO);
        atualizado.setVotosTotais(2000);

        when(candidatoService.editarCandidato(Mockito.anyLong(), Mockito.any(Candidato.class))).thenReturn(atualizado);

        mockMvc.perform(put("/api/candidatos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(atualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomeCompleto").value("Maria Souza Atualizada"))
                .andExpect(jsonPath("$.numero").value(20))
                .andExpect(jsonPath("$.cargo").value("Governador"));
    }

    @Test
    void ListarCandidatosAtivos() throws Exception {
        when(candidatoService.getCandidatosAtivos()).thenReturn(List.of(candidato));

        mockMvc.perform(get("/api/candidatos/ativos")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nomeCompleto").value("Maria Souza"))
                .andExpect(jsonPath("$[0].numero").value(10));
    }

    @Test
    void InativarCandidato() throws Exception {
        mockMvc.perform(put("/api/candidatos/inativar/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
