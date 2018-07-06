package br.com.alura.agenda;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

import br.com.alura.agenda.modelo.Prova;

public class ProvasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provas);

        //vamos instancia o frag e colocar na "frame" activity_provas.xml
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        //pega o pega o FrameLayout na tela activity_provas.xml e substitui pelo nosso frag
        //abre um transação
         FragmentTransaction tx = supportFragmentManager.beginTransaction();
         //consultar modo landscape ou retrato nos arquivos bools.xml

        //subistitui o frame  pelo frag no modo landiscape ou retrato
        tx.replace(R.id.frame_principal, new ListaProvasFragment());
        //verifica se está no modo paisagem
         if(estaNoModPaisagem()){
             tx.replace(R.id.frame_secudario, new DetalhesProvaFragment());
         }

        tx.commit();



    }

    private boolean estaNoModPaisagem() {
        return getResources().getBoolean(R.bool.modoPaisagem);
    }

    public void selecionaPorva(Prova prova) {
        FragmentManager manager = getSupportFragmentManager();
        //se não estiver no modo paisagem troca o frame principal(lista de materias) pelos detalheres das materias
        if(!estaNoModPaisagem()){
            // startActivity(vaiParaDetalhes);
            FragmentTransaction tx = manager.beginTransaction();

            DetalhesProvaFragment detalheFragment = new DetalhesProvaFragment();
            //vamos passar parametros da prova para o frag
            //Bundle é tipo uma caixinha onde se pode colocar varios obj dentro
            Bundle parametros = new Bundle();
            //tipo chave e valor, e do outro lado recupera como chave
            //coloca a prova dentro do bundle
            parametros.putSerializable("prova",prova);
            //passa o bundle para dentro do fragment
            detalheFragment.setArguments(parametros);
            //vai do link da materia para a tela de detalhe(substitui o frag por outro)
            tx.replace(R.id.frame_principal, detalheFragment);
            //quando clica do botao de back do celular ele volta para o Frag anterior que a lista de materia e não mata a activity
            //null ser para marcar a transacao e poder procurar a transacao e voltar para um ponto especifico
            tx.addToBackStack(null);

            tx.commit();
        }else{
           //pega o fragment detalhes e populado(vai ficar dois frag na tela das materias e os detalhes)
            DetalhesProvaFragment detalhesProvaFragment = (DetalhesProvaFragment) manager.findFragmentById(R.id.frame_secudario);
            detalhesProvaFragment.populaCampos(prova);

        }


    }
}
