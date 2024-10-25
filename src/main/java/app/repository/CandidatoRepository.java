package app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.entity.Candidato;
import app.entity.Candidato.StatusCandidato;

@Repository
public interface CandidatoRepository extends JpaRepository<Candidato, Long> {
    
	// Buscar todos os candidatos
	List<Candidato> findAll();
	
	// Buscar apenas candidatos com status ativo
    List<Candidato> findByStatus(StatusCandidato status);

	Optional<Candidato> findByNumero(int numero);
}

