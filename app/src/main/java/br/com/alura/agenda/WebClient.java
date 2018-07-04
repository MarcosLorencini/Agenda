package br.com.alura.agenda;

import java.io.IOException;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class WebClient {

    public String post(String json){
        try {
            URL url = new URL("https://www.caelum.com.br/mobile");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //tipo de dado que está enviando
            connection.setRequestProperty("Content-type","application/json");
            //tipo de dado que aceita como resposta
            connection.setRequestProperty("Accept","application/json");

            connection.setDoOutput(true);//quero fazer um post no corpo da requisicao

            //escrever no corpo da requisição o q enviar
            PrintStream output = new PrintStream(connection.getOutputStream());
            output.println(json);

            //enviar e pegar a resposta

            //estabelece a conexao
            connection.connect();
            //pegar a resposta
            Scanner scanner = new Scanner(connection.getInputStream());
            //ler a resposta do servidor
            String resposta = scanner.next();
            return resposta;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
