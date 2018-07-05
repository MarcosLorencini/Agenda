package br.com.alura.agenda.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


import br.com.alura.agenda.R;
import br.com.alura.agenda.modelo.Aluno;

//

public class AlunosAdapter extends BaseAdapter {

    private final List<Aluno> alunos;
    private final Context context;

    public AlunosAdapter(Context context, List<Aluno> alunos) {
        //receber o contexto e os alunos
        this.alunos = alunos;
        this.context = context;
        
    }

    //a lista pergunta para o adapter quantos intens ele pode pedir
    //tamanho da lista
    @Override
    public int getCount() {
        return alunos.size();
    }

    //devolve um item na determinada posicao da lista
    @Override
    public Object getItem(int position) {
        return alunos.get(position);
    }

    //devolve o item por Id(pode ser o id que salvou no banco, rg, cpf...)
    @Override
    public long getItemId(int position) {
        return alunos.get(position).getId();
    }

    //envocado pela lista quando precisa mostrar algo
    //pega um aluno por vez e joga na tela
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Aluno aluno = alunos.get(position);//posicao do aluno na lista

        //vamos inflar o xml e transformar em uma View, construir a View inflando o xml list_item.xml

        //from pede um contexto
        LayoutInflater inflater = LayoutInflater.from(context);
        //convertView se existir 10000 de itens o android não instância estes 10000 e coloca na lista, vai instanciar a quantidade de itens para colocar na tela
        //caso role a tela o android tem q instanciar os itens seguintes, para não fazer isso o android instancia um pouco mais de itens acima e abaixo, quando acabar estes itens
        //o android reaproveita os itens decima e joga para baixo
        //se o android passar uma convertView significa que a view já esta pronta, só muda os valores para mostrar na tela, não precisando fazer o inflate sempre, subiu joga abaixo, subiu joga abaixo
        //melhora a performance
        View view = convertView;
        //so faz o inflate se o converViewr for nula
        if(view == null){
            //inflar o xml list_item.xml
            //parent = quando inflar passa um pai como referencia para o tamanho
            //false = se informar o pai "parent" o inflate já vai pegar o item e coloar na lista do parent informado e em seguida quando devolver a view "return view" vai colocar novamente na lista. Gera uma exception
            //ou seja se informa o pai vai colacar dentro da lista depois vai tentar coloar novamente, por isso deve-se coloar o parametro "false", não coloca o item ainda na lista, somente no final "return view"
            //o android que escolhe qual tipo de tela ele vai abrir se é normal ou landscape, pois existem dois arquivo com o mesmo nome
            view = inflater.inflate(R.layout.list_item, parent, false);
        }

        //pegar a ref dos valores no layout list_item representada pela "view" e preenche-los na tela
        TextView campoNome = (TextView) view.findViewById(R.id.item_nome);
        campoNome.setText(aluno.getNome());

        TextView campoTelefone = (TextView) view.findViewById(R.id.item_telefone);
        campoTelefone.setText(aluno.getTelefone());

        TextView campoEndereco = (TextView) view.findViewById(R.id.item_endereco);
        //campoEndereco está somente no modo retrato
        if(campoEndereco != null){
            campoEndereco.setText(aluno.getEndereco());
        }

        TextView campoSite = (TextView) view.findViewById(R.id.item_site);
        //campoSite está somente no modo retrato
        if(campoSite != null){
            campoSite.setText(aluno.getSite());
        }

        ImageView campoFoto = (ImageView) view.findViewById(R.id.item_foto);
        String caminhoFoto = aluno.getCaminhoFoto();

        if(caminhoFoto != null){
            //abrir a foto que foi tirada
            //pegou a ref. do ImageView
            //transf. o arquivo em bitmap a partir do caminho da foto
            Bitmap bitmap = BitmapFactory.decodeFile(caminhoFoto);
            //alterar o tamanho da foto
            // 300 é o tamanho, true é o filtro para melhorar a imagem
            Bitmap bitmapReduzido = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
            campoFoto.setImageBitmap(bitmapReduzido);
            //faz com que a imagem se encaixe no ImageView
            campoFoto.setScaleType(ImageView.ScaleType.FIT_XY);//para encaixar no quadradinho de 100x100
        }

        return view;
    }
}
