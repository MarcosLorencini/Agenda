package br.com.alura.agenda.services;

import br.com.alura.agenda.modelo.Aluno;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AlunoService {

    //Call o retrofit usa como padrao para fazer as chamadas
    //http://192.168.1.2:8080/api/aluno
    //JacksonConverterFactory converte o aluno
    //@Body pega o aluno transf em json e deixa no corpo da requisicao
    @POST("aluno")
    Call<Void> insere(@Body Aluno aluno);
}
