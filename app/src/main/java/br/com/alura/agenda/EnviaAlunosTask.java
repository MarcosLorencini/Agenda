package br.com.alura.agenda;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.List;

import br.com.alura.agenda.converter.AlunoConverter;
import br.com.alura.agenda.dao.AlunoDAO;
import br.com.alura.agenda.modelo.Aluno;


//Void indica que o metodo doInBackground não está recebendo parametro algum, porem receber parametros do execute("String",1,2) new EnviaAlunosTask(this).execute();
//Void indica que não está sendo usado no decorrer da execução
//String parametro do onPostExecute
public class EnviaAlunosTask extends AsyncTask<Void, Void, String> {

    private Context context;
    private ProgressDialog dialog;

    public EnviaAlunosTask(Context context) {
        this.context = context;
    }


    //antes de começar o doInBackground que é execultado na thread principal para mostrar a barra de execução
    //true o tempo que mostra é infinito
    //true o usuário pode cancelar a amostra da barra
    @Override
    protected void onPreExecute() {
        dialog = ProgressDialog.show(context, "Aguarde","Enviando alunos...",true, true);

    }

    //tudo que iremos realizar na thread secundária
    @Override
    protected String doInBackground(Void... params) {

        //converte os dados para json antes de enviar para nuvem
        AlunoDAO dao = new AlunoDAO(context);
        List<Aluno> alunos = dao.buscaAlunos();
        dao.close();
        AlunoConverter conversor = new AlunoConverter();
        String json = conversor.converteParaJSON(alunos);

        WebClient client = new WebClient();
        String resposta = client.post(json);
        return resposta;//envia o parametro abixo do metodo onPostExecute, para a thread principal(onPostExecute)
    }

    //thread principal do android(onPostExecute) vai ser excultado depois da thread secundaria(doInBackground),
    @Override
    protected void onPostExecute(String resposta) {
        dialog.dismiss();//tira o dialog
       Toast.makeText(context, resposta,  Toast.LENGTH_LONG).show();
        super.onPostExecute(resposta);
    }
}
