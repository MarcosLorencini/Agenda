package br.com.alura.agenda;

import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import br.com.alura.agenda.dao.AlunoDAO;
import br.com.alura.agenda.modelo.Aluno;

public class ListaAlunosActivity extends AppCompatActivity {

    private ListView listaAlunos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {//chama somente na primeira criacao da activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_alunos);

        listaAlunos = (ListView) findViewById(R.id.lista_alunos);
        
        Button novoAluno = findViewById(R.id.novo_aluno);
        novoAluno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentVaiProFormulario = new Intent(ListaAlunosActivity.this, FormularioActivity.class);
                startActivity(intentVaiProFormulario);

            }
        });

        //registrando a lista no menu de contexto
        registerForContextMenu(listaAlunos);

    }

    private void carregaLista() {
        AlunoDAO dao = new AlunoDAO(this);//contexto activite
        List<Aluno> alunos = dao.buscaAlunos();
        dao.close();// ganhamos do SQLiteHelper


        ArrayAdapter<Aluno> adapter = new ArrayAdapter<Aluno>(this,android.R.layout.simple_list_item_1,alunos );
        listaAlunos.setAdapter(adapter);
    }

    //quando pausar a activity ListaAlunos para adicionar mais um aluno volta para este método com o aluno gravado por ultimo.
    @Override
    protected void onResume() {
        super.onResume();
        carregaLista();
    }

    //recebe um ContextMenu vazio e popula ele
    //menuInfo diz qual item da lista que foi clicado para gerar o menu de contexto
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, final ContextMenu.ContextMenuInfo menuInfo) {
        MenuItem deletar = menu.add("Deletar");//colocar deletar no menu de contexto lista e cria uma referencia para ele
        deletar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() { //ouve o clique do deletar
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {//MenuItem menuItem é o item do menu de contexto no caso Deletar
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;// estamos usando AdapterView(listaAlunos)
                Aluno aluno = (Aluno) listaAlunos.getItemAtPosition(info.position);//devolve o aluno que esta na posicao que o menuInfo informou
                AlunoDAO dao = new AlunoDAO(ListaAlunosActivity.this);
                dao.deleta(aluno);
                dao.close();
                carregaLista();

                //Toast.makeText(ListaAlunosActivity.this, "Deletar o aluno "+ aluno.getNome(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });

    }


}
