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

    @Pattern(regexp = "\\d{11}", message = "CPF deve ter 11 dígitos") // Ajuste a regex conforme a necessidade
    private String cpf;

    @NotBlank(message = "Profissão é obrigatória")
    private String profissao;

    @Pattern(regexp = "\\d{10,11}", message = "Telefone celular deve ter entre 10 e 11 dígitos") // Ajuste a regex conforme a necessidade
    @NotBlank(message = "Telefone celular é obrigatório")
    private String telefoneCelular;

    @Pattern(regexp = "\\d{10,11}", message = "Telefone fixo deve ter entre 10 e 11 dígitos") // Ajuste a regex conforme a necessidade
    private String telefoneFixo;

    @Pattern(regexp = "^[\\w-_.+]*[\\w-_.]@[\\w]+[.][a-z]{2,3}$", message = "Endereço de e-mail inválido") // Regex básica para e-mail
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusEleitor status;

    public enum StatusEleitor {
        APTO, INATIVO, BLOQUEADO, PENDENTE, VOTOU
    }
}
