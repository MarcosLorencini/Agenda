package br.com.alura.agenda.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import br.com.alura.agenda.modelo.Aluno;

public class AlunoDAO extends SQLiteOpenHelper {
    public AlunoDAO(Context context) {
        super(context, "Agenda", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE Alunos (id INTEGER PRIMARY KEY, nome TEXT NOT NULL, endereco TEXT, telefone TEXT, site TEXT, nota REAL, caminhoFoto TEXT);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "";
        switch (oldVersion){
            case 1:
            sql = "ALTER TABLE  Alunos ADD COLUMN caminhoFoto TEXT";
            db.execSQL(sql);//indo para versao 2 / mudar para 2 no construtor
        }

    }

    public void insere(Aluno aluno) {
        //para evitar instrucoes maliciosas o SQLite faz a insercao dos dados
        SQLiteDatabase db = getWritableDatabase();// ref do banco de dados
        ContentValues dados = pegaDadosDoAluno(aluno);

        long id = db.insert("Alunos",null, dados);//monta a instrucao sql. protege os dados
        //pega o id antes de add no servidor
        aluno.setId(id);
    }

    @NonNull
    private ContentValues pegaDadosDoAluno(Aluno aluno) {
        ContentValues dados = new ContentValues();//server para inserir os dados
        dados.put("nome", aluno.getNome());
        dados.put("endereco", aluno.getEndereco());
        dados.put("telefone", aluno.getTelefone());
        dados.put("site", aluno.getSite());
        dados.put("nota", aluno.getNota());
        dados.put("caminhoFoto", aluno.getCaminhoFoto());
        return dados;
    }

    public List<Aluno> buscaAlunos() {
        String sql = "SELECT * FROM Alunos";
        SQLiteDatabase db = getReadableDatabase();//ler o banco
        Cursor c = db.rawQuery(sql, null); //null são parametros // retorno um Cursor(ponteiro) aponta primeiro para a 1 linha(vazia)

        List<Aluno> alunos = new ArrayList<Aluno>();

        while(c.moveToNext()){//vai para a proxima linha  e mostra se existe mais linhas
            Aluno aluno = new Aluno();
            aluno.setId( c.getLong(c.getColumnIndex("id")));
            aluno.setNome( c.getString(c.getColumnIndex("nome")));//pega o nome de dentro do cursor na coluna nome
            aluno.setEndereco( c.getString(c.getColumnIndex("endereco")));
            aluno.setTelefone( c.getString(c.getColumnIndex("telefone")));
            aluno.setSite( c.getString(c.getColumnIndex("site")));
            aluno.setNota( c.getDouble(c.getColumnIndex("nota")));
            aluno.setCaminhoFoto(c.getString(c.getColumnIndex("caminhoFoto")));

            alunos.add(aluno);
        }
        c.close();//libera o cursor(recurso)libera a memorira
        return alunos;
    }

    public void deleta(Aluno aluno) {
        SQLiteDatabase db = getWritableDatabase();

        String[] params = {aluno.getId().toString()};
        db.delete("Alunos", "id = ?", params);

    }

    public void altera(Aluno aluno) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues dados = pegaDadosDoAluno(aluno);

        String[] params = {aluno.getId().toString()};//converte do
        db.update("Alunos",dados, "id = ?", params);
    }

    public boolean ehAluno(String telefone){
        //pegou o bd
        SQLiteDatabase db = getReadableDatabase();
        Cursor c =  db.rawQuery("SELECT * FROM Alunos WHERE telefone = ?", new String[]{telefone});
        int resultado = c.getCount();
        c.close();;
        return resultado > 0;
    }
}
