package app.entity;

import lombok.Data;

import java.util.List;

@Data
public class Apuracao {

    private int totalVotos;
    private List<Candidato> candidatosPrefeito;
    private List<Candidato> candidatosVereador;
}
