package br.com.alura.agenda;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;

import br.com.alura.agenda.modelo.Aluno;

public class FormularioHelper {

    private final EditText campoNome;
    private final EditText campoEndereco;
    private final EditText campoTelefone;
    private final EditText campoSite;
    private final RatingBar campoNota;
    private final ImageView campoFoto;

    private Aluno aluno;

    //inserindo um novo aluno
    //mapeia os id´s dos campos da tela para uma variavel
    public FormularioHelper(FormularioActivity activity){
         campoNome = activity.findViewById(R.id.formulario_nome);
         campoEndereco = activity.findViewById(R.id.formulario_endereco);
         campoTelefone = activity.findViewById(R.id.formulario_telefone);
         campoSite = activity.findViewById(R.id.formulario_site);
         campoNota = activity.findViewById(R.id.formulario_nota);
         campoFoto = activity.findViewById(R.id.formulario_foto);
         aluno = new Aluno();//aluno novo nova instancia ainda não tem Id
    }
    //pega o aluno da tela
    public Aluno pegarAluno() {
        aluno.setNome(campoNome.getText().toString());
        aluno.setEndereco(campoEndereco.getText().toString());
        aluno.setTelefone(campoTelefone.getText().toString());
        aluno.setSite(campoSite.getText().toString());
        aluno.setNota(Double.valueOf(campoNota.getProgress()));
        aluno.setCaminhoFoto((String) campoFoto.getTag());//associado no FormularioActivity
        return aluno;
    }
    //pega o aluno e joga na tela
    public void preecheFormulario(Aluno aluno) {
        campoNome.setText(aluno.getNome());
        campoEndereco.setText(aluno.getEndereco());
        campoTelefone.setText(aluno.getTelefone());
        campoSite.setText(aluno.getSite());
        campoNota.setProgress(aluno.getNota().intValue());//double converte para int
        carregaImagem(aluno.getCaminhoFoto());
        this.aluno = aluno;//atualiza o aluno, o id está aqui, para depois conseguir pegar o aluno pelo metodos pegarAluno()


    }

    public void carregaImagem(String caminhoFoto) {
        if(caminhoFoto != null){
            //abrir a foto que foi tirada
            //pegou a ref. do ImageView
            //transf. o arquivo em bitmap a partir do caminho da foto
            Bitmap bitmap = BitmapFactory.decodeFile(caminhoFoto);
            //alterar o tamanho da foto
            // 300 é o tamanho, true é o filtro para melhorar a imagem
            Bitmap bitmapReduzido = Bitmap.createScaledBitmap(bitmap, 300, 300, true);
            campoFoto.setImageBitmap(bitmapReduzido);
            //faz com que a imagem se encaixe no ImageView
            campoFoto.setScaleType(ImageView.ScaleType.FIT_XY);
            campoFoto.setTag(caminhoFoto);//obj que consegue associar com qualquer View do android (no caso ImageView)
        }
    }
}
