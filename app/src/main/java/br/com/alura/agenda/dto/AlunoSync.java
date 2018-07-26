package br.com.alura.agenda.dto;


import java.util.List;

import br.com.alura.agenda.modelo.Aluno;

//classe que representa dos doados devolvidos pelo banco
public class AlunoSync {

    private List<Aluno> alunos;
    private String momentoDaUltimaModificacao;

    public List<Aluno> getAlunos() {
        return alunos;
    }

    public String getMomentoDaUltimaModificacao() {
        return momentoDaUltimaModificacao;
    }
}
