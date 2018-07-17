package br.com.alura.agenda.retrofit;


//para usar o retrofit tem que baixar na dependencias
//Retrofit efetua as requisicoes http

import br.com.alura.agenda.services.AlunoService;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RetrofitInicializador  {

    private final Retrofit retrofit;

    public RetrofitInicializador(){
        //inicializa o retrofit
        //baseUrl url base
        //addConverterFactory add a fabrica de conversor
        //tem que add o com.squareup.retrofit2:converter-jackson na dependencias para usar JacksonConverterFactory.create()
        retrofit = new Retrofit.Builder().baseUrl("http://192.168.1.2:8080/api/")
                .addConverterFactory(JacksonConverterFactory.create()).build();
    }

    public AlunoService getAlunoService() {
        //retrofit.create() cria o servico
        return retrofit.create(AlunoService.class);
    }
}
