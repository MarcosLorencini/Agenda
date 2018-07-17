package br.com.alura.agenda;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

import br.com.alura.agenda.dao.AlunoDAO;
import br.com.alura.agenda.modelo.Aluno;
import br.com.alura.agenda.retrofit.RetrofitInicializador;
import br.com.alura.agenda.tasks.InsereAlunoTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormularioActivity extends AppCompatActivity {

    public static final int CODIGO_CAMERA = 567;
    private FormularioHelper helper;
    private String caminhoFoto;

    @Override//cria o formulario
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);

        helper = new FormularioHelper(this);

        //recuperar os dados que vieram da listaAlunosActivity
        Intent intent = getIntent();
        Aluno aluno = (Aluno) intent.getSerializableExtra("aluno");//etiqueta "aluno" da listaAluno
        //verifica se existe o aluno para ser preenchido no form
        if(aluno != null){
            helper.preecheFormulario(aluno);
        }

       //refer para o botao da foto da tela
        Button botaoFoto = (Button) findViewById(R.id.formulario_botao_foto);
        botaoFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //intent implicita para tirar a foto
                Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //define onde quer salvar a foto
                //getExternalFilesDir() pasta onde esta localizada a nossa app
                //System.currentTimeMillis() data e hora para diferenciar uma foto da outra
                caminhoFoto = getExternalFilesDir(null) + "/" +System.currentTimeMillis() + ".jpg";
                //cria o obj do tipo arquivo
                File arquivoFoto = new File(caminhoFoto);
                //MediaStore.EXTRA_OUTPUT constante que especifica o caminho da foto, pois toda a aplicacao de camera vai conseguir entender
                // Uri.fromFile(arquivoFoto) cria o caminho fisico para salvar a foto
                intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(arquivoFoto));
                //o S.O. avisa para nos que a foto foi tirada e podemos utilizá-la para mostrar na tela apos ter tirado
                startActivityForResult(intentCamera, CODIGO_CAMERA);

            }
        });
    }


    //vai chamar quando o método startActivityForResult(intentCamera, CODIGO_CAMERA); retornar o seu resultado, terminar a sua acão
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
         //verifica se foi completada a ação de tirar a foto, não saiu no momento de tirar a foto
        if(resultCode == Activity.RESULT_OK){
            //verifica se foi a ação da camera
            if(requestCode == CODIGO_CAMERA){
               helper.carregaImagem(caminhoFoto);
            }
         }
    }

    //criação do menu
    //passa um Menu vazio para ser criado
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_formulario, menu);//transforma o xml em um menu
        return super.onCreateOptionsMenu(menu);
    }

    //metodo chamado sempre que clica em algum item do menu
    //botao cheque no campo sup direito
    //passa o item que foi clicado
    //usado quando usa o xml
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //faz o switch para ver o que for clicado pelo id
        switch (item.getItemId()){
            case R.id.menu_formulario_ok:
                Aluno aluno = helper.pegarAluno();//aluno novo o id estará vazio
                AlunoDAO dao = new AlunoDAO(this); //contexto da nossa activit

                if(aluno.getId() != null){
                    dao.altera(aluno);
                }else{
                    dao.insere(aluno);
                }

                dao.close();// ganhamos do SQLiteHelper

                //thread separado da thred main e q não trave o app
                //execute() faz o processo que a AsyncTask precisa para entrar na thread secundária e não travar a thread principal
               // new InsereAlunoTask(aluno).execute();

                Call call = new RetrofitInicializador().getAlunoService().insere(aluno);
                ////thread separado da thred main e q não trave o app
                call.enqueue(new Callback() {
                    //metodos de retorno
                    //sucesso
                    @Override
                    public void onResponse(Call call, Response response) {
                        Log.i("onResponse", "Requisição com sucesso ");
                    }
                    //falha
                    @Override
                    public void onFailure(Call call, Throwable t) {
                        Log.e("onFailure", "requisicao falhou: ");
                    }
                });

                Toast.makeText(FormularioActivity.this,"Aluno "+aluno.getNome()+" salvo com sucesso!", Toast.LENGTH_SHORT).show();

                finish();
            break;
        }
        return super.onOptionsItemSelected(item);
    }
}
