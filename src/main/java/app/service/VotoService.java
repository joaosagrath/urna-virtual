package app.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import app.entity.Apuracao;
import app.entity.Candidato;
import app.entity.Eleitor;
import app.entity.Voto;
import app.repository.CandidatoRepository;
import app.repository.EleitorRepository;
import app.repository.VotoRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VotoService {

    private final EleitorRepository eleitorRepository;
    private final CandidatoRepository candidatoRepository;
    private final VotoRepository votoRepository;

    // Método para votar
    public String votar(Long eleitorId, Voto voto) {
        Eleitor eleitor = eleitorRepository.findById(eleitorId)
                .orElseThrow(() -> new RuntimeException("Eleitor não encontrado"));

        // Verificar se o eleitor está apto a votar
        if (eleitor.getStatus() != Eleitor.StatusEleitor.APTO) {
            throw new RuntimeException("Eleitor inapto para votação");
        }

        // Verificar se os candidatos são válidos
        validarCandidatos(voto);

        // Atualizar status do eleitor para VOTOU
        eleitor.setStatus(Eleitor.StatusEleitor.VOTOU);
        eleitorRepository.save(eleitor);

        // Configurar data e hora da votação
        voto.setDataHora(LocalDateTime.now());

        // Gerar hash de votação
        String hash = UUID.randomUUID().toString();
        voto.setHash(hash);

        // Persistir o voto
        votoRepository.save(voto);

        return hash;
    }

    // Método para realizar a apuração
    public Apuracao realizarApuracao() {
        Apuracao apuracao = new Apuracao();

        // Recuperar candidatos ativos
        List<Candidato> candidatos = candidatoRepository.findByStatus(Candidato.StatusCandidato.ATIVO);

        // Separar candidatos para prefeito e vereador
        List<Candidato> candidatosPrefeito = new ArrayList<>();
        List<Candidato> candidatosVereador = new ArrayList<>();

        for (Candidato candidato : candidatos) {
            if ("Prefeito".equalsIgnoreCase(candidato.getCargo())) {
                candidatosPrefeito.add(candidato);
            } else if ("Vereador".equalsIgnoreCase(candidato.getCargo())) {
                candidatosVereador.add(candidato);
            }
        }

        // Contar votos por candidato e atribuir a apuração
        for (Candidato candidato : candidatosPrefeito) {
            int totalVotos = votoRepository.countByCandidatoPrefeitoId(candidato.getId());
            candidato.setVotosTotais(totalVotos);
        }

        for (Candidato candidato : candidatosVereador) {
            int totalVotos = votoRepository.countByCandidatoVereadorId(candidato.getId());
            candidato.setVotosTotais(totalVotos);
        }

        // Ordenar candidatos por total de votos
        candidatosPrefeito.sort(Comparator.comparingInt(Candidato::getVotosTotais).reversed());
        candidatosVereador.sort(Comparator.comparingInt(Candidato::getVotosTotais).reversed());

        apuracao.setTotalVotos((int) votoRepository.count());
        apuracao.setCandidatosPrefeito(candidatosPrefeito);
        apuracao.setCandidatosVereador(candidatosVereador);

        return apuracao;
    }

    // Método auxiliar para validar se candidatos são válidos
    private void validarCandidatos(Voto voto) {
        if (voto.getCandidatoPrefeito() == null || voto.getCandidatoVereador() == null) {
            throw new RuntimeException("Candidato(s) inválido(s). Refaça a requisição!");
        }

        Optional<Candidato> candidatoPrefeito = candidatoRepository.findById(voto.getCandidatoPrefeito().getId());
        Optional<Candidato> candidatoVereador = candidatoRepository.findById(voto.getCandidatoVereador().getId());

        if (candidatoPrefeito.isEmpty() || candidatoPrefeito.get().getStatus() != Candidato.StatusCandidato.ATIVO) {
            throw new RuntimeException("O candidato escolhido para prefeito não é válido. Refaça a requisição!");
        }

        if (candidatoVereador.isEmpty() || candidatoVereador.get().getStatus() != Candidato.StatusCandidato.ATIVO) {
            throw new RuntimeException("O candidato escolhido para vereador não é válido. Refaça a requisição!");
        }
    }
}
