package br.com.alura.agenda.tasks;

import android.os.AsyncTask;

import br.com.alura.agenda.WebClient;
import br.com.alura.agenda.converter.AlunoConverter;
import br.com.alura.agenda.modelo.Aluno;

public class InsereAlunoTask extends AsyncTask {
    private final Aluno aluno;

    public InsereAlunoTask(Aluno aluno) {
        this.aluno = aluno;
    }

    //
    @Override
    protected Object doInBackground(Object[] objects) {
        //pega o objeto aluno e transf em json
        String json = new AlunoConverter().converteParaJSONCompleto(aluno);
        //classe q realiza a conexao entre o app e o servidor
        new WebClient().insere(json);
        return null;
    }
}
