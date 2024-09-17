package app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.entity.Candidato;
import app.entity.Candidato.StatusCandidato;

@Repository
public interface CandidatoRepository extends JpaRepository<Candidato, Long> {
    // Buscar apenas candidatos com status ativo
    List<Candidato> findByStatus(StatusCandidato status);
}

