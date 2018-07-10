package br.com.alura.agenda;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

import br.com.alura.agenda.dao.AlunoDAO;
import br.com.alura.agenda.modelo.Aluno;

public class MapaFragment extends SupportMapFragment implements OnMapReadyCallback {

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);


        //getMapAsync vai preparar uma instacia do GoogleMap, para podermaos manipular o mapa, colocar pinho, desenhar linha, circulo
        //this faça algo quando o mapa estiver pronto para uso.
        // Quem vai fazer isso é o nosso frag.
        // Deve-se implementar o OnMapReadyCallback q implementa o metodo onMapReady
        getMapAsync(this);

    }

    //recebera o mapa quando o mesmo  estiver pronto ,
    @Override
    public void onMapReady(GoogleMap googleMap) {
     //centralizar o mapa no endereço da caelum
     LatLng posicaoDaEscola = pegaCoordenadaDoEndereco("Rua Vergueiro 3185, Vila Mariana, Sao Paulo");
     //ser não for nulla atualizar o mapa
        if(posicaoDaEscola != null){
            //17 é um nivel de zoom
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(posicaoDaEscola, 17);
            googleMap.moveCamera(update);
        }

        //mostrar os alunos no mapa, colocar os pininho no mapa
        AlunoDAO alunoDAO = new AlunoDAO(getContext());
        for(Aluno aluno : alunoDAO.buscaAlunos()){
            LatLng coordenada = pegaCoordenadaDoEndereco(aluno.getEndereco());
            if(coordenada != null){
                //marcador de pininhos
                MarkerOptions marcador = new MarkerOptions();
                //posicao do pininho no mapa
                marcador.position(coordenada);
                //texto acima do pininho
                marcador.title(aluno.getNome());
                //subtexto abaixo do nome do aluno
                marcador.snippet(String.valueOf(aluno.getNota()));//converte para String, pois a nota é um Double
                //colocar o pino criado
                googleMap.addMarker(marcador);

            }
        }
        alunoDAO.close();

    }

    private LatLng pegaCoordenadaDoEndereco(String endereco) {
        //pega o endereço e transforma em coordenadas lat e long
        //getContext() contexto do nosso fragment
        try{
            Geocoder geocoder = new Geocoder(getContext());
            //1 é a quant de resultado que queremos do google
            List<Address> resultados = geocoder.getFromLocationName(endereco, 1);
            //pegar a coordenada do endereco
            if(!resultados.isEmpty()){
                LatLng posicao = new LatLng(resultados.get(0).getLatitude(),resultados.get(0).getLongitude());
                return posicao;
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
