package app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.entity.Eleitor;

@Repository
public interface EleitorRepository extends JpaRepository<Eleitor, Long> {
    // Buscar apenas eleitores com status ativo
    List<Eleitor> findByStatus(Eleitor.StatusEleitor status);
}
