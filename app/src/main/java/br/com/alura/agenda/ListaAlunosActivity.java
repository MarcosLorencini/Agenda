package br.com.alura.agenda;

import android.Manifest;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Browser;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
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
                intetVaiProFormulario.putExtra("aluno", aluno); //pendura o aluno para ser mostrado no formulario pela etiqueta "aluno" o model aluno dever ser serializado
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
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;// estamos usando AdapterView(listaAlunos)
        final Aluno aluno = (Aluno) listaAlunos.getItemAtPosition(info.position);//devolve o aluno que esta na posicao que o menuInfo informou

        MenuItem itemLigar = menu.add("Ligar");

        itemLigar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                //verifica se possui a permissão de ligar
                if(ActivityCompat.checkSelfPermission(ListaAlunosActivity.this, Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED){//permissao dada para o usuario
                    //pedir para o usuario permitir a ligacao
                    //requestPermissions mostra a popup da permissão
                    //123 serve para diferencia qual é a permissão.
                    ActivityCompat.requestPermissions(ListaAlunosActivity.this,
                            new String[]{Manifest.permission.CALL_PHONE},123 );
                }else{
                    //o cliente já possui permissão
                    Intent intentLigar = new Intent(Intent.ACTION_CALL);
                    intentLigar.setData(Uri.parse("tel:"  + aluno.getTelefone()));
                    startActivity(intentLigar);

                }
                return false;
            }
        });



        MenuItem itemSMS = menu.add("Enviar SMS");
        Intent intentSMS = new Intent(Intent.ACTION_VIEW);
        intentSMS.setData(Uri.parse("sms:" + aluno.getTelefone()));//protocolo sms
        itemSMS.setIntent(intentSMS);

        MenuItem itemMapa = menu.add("Vizualizar no mapa");
        Intent intentMapa = new Intent(Intent.ACTION_VIEW);
        intentMapa.setData(Uri.parse("geo:0,0?q=" + aluno.getEndereco()));//protocolo geo
        itemMapa.setIntent(intentMapa);



        //mesmo efeito que o clique do "Deletar"
        MenuItem itemSite = menu.add("Visita site");//add no menu contexto
        Intent intentSite = new Intent(Intent.ACTION_VIEW);//Intent.ACTION_VIEW =  visualizar algo

        String site = aluno.getSite();
        if(!site.startsWith("http://")){
            site = "http://"+site;
        }
        intentSite.setData(Uri.parse(site));//parse de string para URI
        itemSite.setIntent(intentSite);



        MenuItem deletar = menu.add("Deletar");//add deletar no menu de contexto lista e cria uma referencia para ele
        deletar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() { //ouve o clique do deletar
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {//MenuItem menuItem é o item do menu de contexto no caso Deletar

                AlunoDAO dao = new AlunoDAO(ListaAlunosActivity.this);
                dao.deleta(aluno);
                dao.close();
                carregaLista();

                //Toast.makeText(ListaAlunosActivity.this, "Deletar o aluno "+ aluno.getNome(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });

    }

    //O cliente deu a permissão ou não da ligação ele vai chamar este método
   // @Override
   // public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
     //   super.onRequestPermissionsResult(requestCode, permissions, grantResults);
  //
  //  }
}
