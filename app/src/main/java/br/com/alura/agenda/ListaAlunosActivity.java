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

        //capturar o click na lista
        //lista é a lista que foi clicada
        //item que foi clicado
        //posicaoItem posicao do item clicado
        //idItem id do item clicado
        listaAlunos.setOnItemClickListener(new AdapterView.OnItemClickListener() {//  trata o click no item
            @Override
            public void onItemClick(AdapterView<?> lista, View item, int posicaoItem, long idItem) {
                Aluno aluno = (Aluno) listaAlunos.getItemAtPosition(posicaoItem);
                //Toast.makeText(ListaAlunosActivity.this, "Aluno "+aluno.getNome()+" clicado", Toast.LENGTH_SHORT).show();
                Intent intetVaiProFormulario = new Intent(ListaAlunosActivity.this, FormularioActivity.class);//vai para o fom de cadastro
                //transf o aluno de uma activity para outra
                intetVaiProFormulario.putExtra("aluno", aluno); //pendura o aluno para ser mostrado no formulario pela etiqueta "aluno"
                 startActivity(intetVaiProFormulario);
            }
        });
        /*
        // OU PODE USAR UM CLICK LONGO
        listaAlunos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(ListaAlunosActivity.this, "Clique longo", Toast.LENGTH_SHORT).show();
                //se devolve um true, ninguem mais vai poder tratar este clique. só vai mostrar o Toast
                //se devolver um false passa para frente para se alguém estiver interessado. Vai mostrar o Toast e o Menu de contexto
                return true;
            }
        });
        */
        
        Button novoAluno = findViewById(R.id.novo_aluno);
        novoAluno.setOnClickListener(new View.OnClickListener() {// trata o click em qualquer lugar da lista
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
