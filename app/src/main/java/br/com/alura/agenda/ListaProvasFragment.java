package br.com.alura.agenda;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

import br.com.alura.agenda.modelo.Prova;


//representa um pedaço da tela tanto da parte visual como o comportamento
public class ListaProvasFragment extends Fragment {

    //chamado quando o frag entrar na tela para dentro de uma activity e construir a view deste fragment
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //criar a view q representa o frag
        //container onde vai inflar o frag, onde vai coloca o frag
        //false não ficar pindurado como filho do container
        // view devolve a view que acabou de montar, inflou o layout de devolveu ela montada
        View view = inflater.inflate(R.layout.fragment_lista_provas, container, false);
        //vamos popular os campos desta view
        //procurar os componentes não no frag é na view que está montando para este frag

        List<String> topicosPort = Arrays.asList("Sujeito", "Objeto direto", "Objeto Indireto");
        Prova provaPortugues = new Prova("Portugues","25/05/2016", topicosPort);

        List<String> topicosMat = Arrays.asList("Equação de Segundo grau", "Trigonometria");
        Prova provaMatematica = new Prova("Matematica", "27/05/2016", topicosMat);

        List<Prova> provas = Arrays.asList(provaPortugues, provaMatematica);

        //fazer com que as provas aparecam na nossa lista(view), colocar um obj dentro da lista(view) criar uma view
        //this = contexto
        //android.R.layout.simple_list_item_1 como mostrar os dados na tela
        //provas = lista
        //getContext() conexto que está associado ao frag, pois o frag não possui contexto
        ArrayAdapter<Prova> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, provas);

        //pegar a ref da lista e utilizar o adapter
        //view procurar os componentes não no frag é na view que está montando para este frag
        ListView lista = (ListView) view.findViewById(R.id.provas_lista);
        //lista utiliza o adapter
        lista.setAdapter(adapter);

        //detectar o click de dentro da lista
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                //posicao clicada
                Prova prova  = (Prova) parent.getItemAtPosition(position);
                Toast.makeText(getContext(), "Clicou na prova de " + prova, Toast.LENGTH_SHORT).show();

                //A activity vai ficar responsavel pela troca  dos fragment
                //substituir o frag(lista de matérias) pelo frag de detalhes de cada matéria
                //recupera ref para activity para ela conseguir selecionar a prova da materia x
                ProvasActivity provasActivity = (ProvasActivity) getActivity();
                provasActivity.selecionaPorva(prova);
          }
        });

        return view;
    }
}
