package br.com.alura.agenda;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import br.com.alura.agenda.dao.AlunoDAO;
import br.com.alura.agenda.modelo.Aluno;

public class FormularioActivity extends AppCompatActivity {

    private FormularioHelper helper;

    @Override//cria o formulario
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);

        helper = new FormularioHelper(this);

        //recuperar os dados que vieram da listaAlunosActivity
        Intent intent = getIntent();
        Aluno aluno = (Aluno) intent.getSerializableExtra("aluno");//etiqueta "aluno" da listaAluno
        //verifica se existe o aluno para ser preenchido no form
        if(aluno != null){
            helper.preecheFormulario(aluno);
        }


    }
    //criação do menu
    //passa um Menu vazio para ser criado
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_formulario, menu);//transforma o xml em um menu
        return super.onCreateOptionsMenu(menu);
    }

    //metodo chamado sempre que clica em algum item do menu
    //botao cheque no campo sup direito
    //passa o item que foi clicado
    //usado quando usa o xml
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //faz o switch para ver o que for clicado pelo id
        switch (item.getItemId()){
            case R.id.menu_formulario_ok:
                Aluno aluno = helper.pegarAluno();//aluno novo o id estará vazio
                AlunoDAO dao = new AlunoDAO(this); //contexto da nossa activit

                if(aluno.getId() != null){
                    dao.altera(aluno);
                }else{
                    dao.insere(aluno);
                }

                dao.close();// ganhamos do SQLiteHelper

                Toast.makeText(FormularioActivity.this,"Aluno "+aluno.getNome()+" salvo com sucesso!", Toast.LENGTH_SHORT).show();

                finish();
            break;
        }
        return super.onOptionsItemSelected(item);
    }
}
