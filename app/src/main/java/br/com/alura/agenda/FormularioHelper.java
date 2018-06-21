package br.com.alura.agenda;

import android.widget.EditText;
import android.widget.RatingBar;

import br.com.alura.agenda.modelo.Aluno;

public class FormularioHelper {

    private final EditText campoNome;
    private final EditText campoEndereco;
    private final EditText campoTelefone;
    private final EditText campoSite;
    private final RatingBar campoNota;

    private Aluno aluno;

    //inserindo um novo aluno
    //mapeia os id´s dos campos da tela para uma variavel
    public FormularioHelper(FormularioActivity activity){
         campoNome = activity.findViewById(R.id.formulario_nome);
         campoEndereco = activity.findViewById(R.id.formulario_endereco);
         campoTelefone = activity.findViewById(R.id.formulario_telefone);
         campoSite = activity.findViewById(R.id.formulario_site);
         campoNota = activity.findViewById(R.id.formulario_nota);
         aluno = new Aluno();//aluno novo nova instancia ainda não tem Id
    }
    //pega o aluno da tela
    public Aluno pegarAluno() {
        aluno.setNome(campoNome.getText().toString());
        aluno.setEndereco(campoEndereco.getText().toString());
        aluno.setTelefone(campoTelefone.getText().toString());
        aluno.setSite(campoSite.getText().toString());
        aluno.setNota(Double.valueOf(campoNota.getProgress()));
        return aluno;
    }
    //pega o aluno e joga na tela
    public void preecheFormulario(Aluno aluno) {
        campoNome.setText(aluno.getNome());
        campoEndereco.setText(aluno.getEndereco());
        campoTelefone.setText(aluno.getTelefone());
        campoSite.setText(aluno.getSite());
        campoNota.setProgress(aluno.getNota().intValue());//double converte para int
        this.aluno = aluno;//atualiza o aluno, o id está aqui, para depois conseguir pegar o aluno pelo metodos pegarAluno()


    }
}
