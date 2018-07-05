package br.com.alura.agenda;

import android.content.Intent;
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

        List<String> topicosPort = Arrays.asList("Sujeito", "Objeto direto", "Objeto Indireto");
        Prova provaPortugues = new Prova("Portugues","25/05/2016", topicosPort);

        List<String> topicosMat = Arrays.asList("Equação de Segundo grau", "Trigonometria");
        Prova provaMatematica = new Prova("Matematica", "27/05/2016", topicosMat);

        List<Prova> provas = Arrays.asList(provaPortugues, provaMatematica);

        //fazer com que as provas aparecam na nossa lista(view), colocar um obj dentro da lista(view) criar uma view
        //this = contexto
        //android.R.layout.simple_list_item_1 como mostrar os dados na tela
        //provas = lista
        ArrayAdapter<Prova> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, provas);

        //pegar a ref da lista e utilizar o adapter
        ListView lista = (ListView) findViewById(R.id.provas_lista);
        //lista utiliza o adapter
        lista.setAdapter(adapter);

        //detectar o click de dentro da lista
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                //posicao clicada
                Prova prova  = (Prova) parent.getItemAtPosition(position);
                Toast.makeText(ProvasActivity.this, "Clicou na prova de " + prova, Toast.LENGTH_SHORT).show();
                //vai do link da materia para a tela de detalhe
                Intent vaiParaDetalhes = new Intent(ProvasActivity.this, DetalheresProvaActivity.class);
                //envia as informações da prova para detalhes, deve implementar Serializable a classe Prova para poder passar os dados
                vaiParaDetalhes.putExtra("prova", prova);

                startActivity(vaiParaDetalhes);

            }
        });



    }
}
