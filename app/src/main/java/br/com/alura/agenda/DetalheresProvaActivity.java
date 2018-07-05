package br.com.alura.agenda;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import br.com.alura.agenda.modelo.Prova;

public class DetalheresProvaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalheres_prova);

        //Pega a Intent "vaiParaDetalhes" que chegou de ProvasActivity linha 51
        Intent intent = getIntent();
        Prova prova = (Prova) intent.getSerializableExtra("prova");

        //pega os campos da view
        TextView materia = (TextView) findViewById(R.id.detalhes_prova_materia);
        TextView data = (TextView) findViewById(R.id.detalhes_prova_data);
        ListView listaTopicos = (ListView) findViewById(R.id.detalhes_prova_data_topicos);

        //joga os dados na view
        materia.setText(prova.getMateria());
        data.setText(prova.getData());
        //para pegar a lista tem que fazer um adapter e jogar em modelo de lista
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, prova.getTopicos());
        listaTopicos.setAdapter(adapter);



    }
}
