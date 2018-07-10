package br.com.alura.agenda;

//Classe responsável pelo GPS

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class Localizador implements GoogleApiClient.ConnectionCallbacks {
    //quem usar a classe deve trazer o contexto pelo construtor
    public Localizador(Context context) {
        //acesso a api do Google
        // GoogleApiClient client = new GoogleApiClient.Builder(context) criação da API Client
        //addApi() define qual api so servico queremos usar
        //LocationService.API queremos consumir a API do LocationService
        // .build(); controi o client
        // .addConnectionCallbacks() é chamado quando conseguir se conectar com o LocalServices.API
        GoogleApiClient client = new GoogleApiClient.Builder(context)
        .addApi(LocationServices.API)
        .addConnectionCallbacks(this)
        .build();
        //realiza a conexao com o LocationServices.API e chamara o ConnectionCallbacks atraves do  .addConnectionCallbacks(this)
        //e chamará o metodo onConnected da interface ConnectionCallbacks ou o metodo onConnectionSuspended quando a conexao for
        //suspensa
        client.connect();


    }
    //vamos conversar com o gsp do nosso celular, para pedir para ele as atualizacoes de posicao
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //quais tipos de dados vamos receber do celular
        @SuppressLint("RestrictedApi") LocationRequest request = new LocationRequest();
        //espaco minimo para receber atualizacao do gps, pois quando estou parado não preciso receber atualizacao de posicao

        //recebe atualizacao a cada um segundo se sair do raio de 50 metros

        //50 receber atualizacao caso eu saia do raio de 50 metros
        request.setSmallestDisplacement(50);
        //intervalo de tempo de atualizacao
        //recebe atualizacao a cada um segundo
        request.setInterval(1000);
        //presicao de localizacao, pode usar wifi, rede 3g, GPS etc quanto mais presico mais gasta a bateria
        //PRIORITY_HIGH_ACCURACY o celular vai usar o melhor dispositivo para calcular a precisao
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        //passar os dados para o LocationServices
        //LocationServices.FusedLocationApi.re

    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}
