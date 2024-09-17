package app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "candidato")
@Data
@NoArgsConstructor
public class Candidato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nomeCompleto;

    @Column(nullable = false)
    private int numero;

    @Column(nullable = false)
    private String cargo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusCandidato status;

    @Column
    private Integer votosTotais; // Campo para armazenar o total de votos

    // Enum para representar o status do candidato
    public enum StatusCandidato {
        ATIVO, INATIVO
    }

    // Método para definir o total de votos
    public void setVotosTotais(Integer totalVotos) {
        this.votosTotais = totalVotos;
    }

    // Método para obter o total de votos (se necessário)
    public int getVotosTotais() {
        return votosTotais != null ? votosTotais : 0;
    }
}
