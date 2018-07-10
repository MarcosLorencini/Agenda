package br.com.alura.agenda;

//Classe responsável pelo GPS

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

public class Localizador implements GoogleApiClient.ConnectionCallbacks, LocationListener {
    private final GoogleApiClient client;
    private final GoogleMap mapa;

    //quem usar a classe deve trazer o contexto pelo construtor
    public Localizador(Context context, GoogleMap mapa) {
        //acesso a api do Google
        // GoogleApiClient client = new GoogleApiClient.Builder(context) criação da API Client
        //addApi() define qual api so servico queremos usar
        //LocationService.API queremos consumir a API do LocationService
        // .build(); controi o client
        // .addConnectionCallbacks() é chamado quando conseguir se conectar com o LocalServices.API
        client = new GoogleApiClient.Builder(context)
        .addApi(LocationServices.API)
        .addConnectionCallbacks(this)
        .build();
        //realiza a conexao com o LocationServices.API e chamara o ConnectionCallbacks atraves do  .addConnectionCallbacks(this)
        //e chamará o metodo onConnected da interface ConnectionCallbacks ou o metodo onConnectionSuspended quando a conexao for
        //suspensa
        client.connect();
        //recebe mapa pelo construtor
        this.mapa = mapa;


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
        //client pede o Google API cliente que se conectou com o servico LocationServices.API
        //request inf para o LocationServices.API
        //this responsavel por tratar os dados quando estiverem pronto, nossa classe, porém tem que implementar LocationListener
        LocationServices.FusedLocationApi.requestLocationUpdates(client, request, this);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }
    //chamado quando a localizacao for alterada, vamos mudar a posicao do nosso mapa
    @Override
    public void onLocationChanged(Location location) {
        //referencia do mapa
        LatLng coordenada = new LatLng(location.getLatitude(), location.getLongitude());
        //cameraUpdate nova posicao do mapa
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(coordenada);
        //moveCamera vai atualizar o mapa e colocar posicao no mapa
        mapa.moveCamera(cameraUpdate);


    }
}
