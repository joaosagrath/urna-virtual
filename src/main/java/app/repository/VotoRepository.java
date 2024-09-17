package app.repository;

import app.entity.Voto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VotoRepository extends JpaRepository<Voto, Long> {

    @Query("SELECT COUNT(v) FROM Voto v WHERE v.candidatoPrefeito.id = :candidatoId")
    int countByCandidatoPrefeitoId(@Param("candidatoId") Long candidatoId);

    @Query("SELECT COUNT(v) FROM Voto v WHERE v.candidatoVereador.id = :candidatoId")
    int countByCandidatoVereadorId(@Param("candidatoId") Long candidatoId);
}
