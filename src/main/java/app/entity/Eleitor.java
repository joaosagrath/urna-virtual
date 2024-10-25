package app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Eleitor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome completo é obrigatório")
    private String nomeCompleto;

    @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}", message = "CPF deve estar no formato 999.999.999-99")
    @Column(nullable = true)
    private String cpf; // Permitir nulo

    @NotBlank(message = "Profissão é obrigatória")
    private String profissao;

    // Regex para telefone celular com máscara: (99) 99999-9999
    @Pattern(regexp = "\\(\\d{2}\\) \\d{5}-\\d{4}", message = "Telefone celular deve estar no formato (99) 99999-9999")
    @NotBlank(message = "Telefone celular é obrigatório")
    private String telefoneCelular;

    // Regex para telefone fixo com máscara: (99) 9999-9999
    @Pattern(regexp = "\\(\\d{2}\\) \\d{4}-\\d{4}", message = "Telefone fixo deve estar no formato (99) 9999-9999")
    private String telefoneFixo;

    // Para o e-mail
    @Pattern(regexp = "^[\\w-_.+]*[\\w-_.]@[\\w]+[.][a-z]{2,3}$", message = "Endereço de e-mail inválido") 
    @Column(nullable = true)
    private String email;
    
    @Column(nullable = false)
    private String foto; // Campo para armazenar o caminho da foto

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusEleitor status;

    public enum StatusEleitor {
        APTO, INATIVO, BLOQUEADO, PENDENTE, VOTOU
    }
}