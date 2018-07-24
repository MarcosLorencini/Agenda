package br.com.alura.agenda.retrofit;


//para usar o retrofit tem que baixar na dependencias
//Retrofit efetua as requisicoes http

import com.google.android.gms.ads.internal.gmsg.HttpClient;

import br.com.alura.agenda.services.AlunoService;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RetrofitInicializador  {

    private final Retrofit retrofit;

    public RetrofitInicializador(){

        //cria o interceptador antes de chamar o retrofit
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        //nivel de log que queremos saber das requisicoes
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //cliente que enviaremos para o retrofit conseguir pegar os logs
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.addInterceptor(interceptor);

        //inicializa o retrofit
        //baseUrl url base
        //addConverterFactory add a fabrica de conversor
        //tem que add o com.squareup.retrofit2:converter-jackson na dependencias para usar JacksonConverterFactory.create()
        retrofit = new Retrofit.Builder().baseUrl("http://192.168.137.1:8080/api/")
                .addConverterFactory(JacksonConverterFactory.create())
                .client(client.build())//recebe o client e o retrofit constroe o client e recebe este client do interceptor
                .build();
    }

    public AlunoService getAlunoService() {
        //retrofit.create() cria o servico
        return retrofit.create(AlunoService.class);
    }
}
