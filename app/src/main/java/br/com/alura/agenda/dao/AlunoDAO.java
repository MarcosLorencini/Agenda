package br.com.alura.agenda.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.alura.agenda.modelo.Aluno;

public class AlunoDAO extends SQLiteOpenHelper {
    public AlunoDAO(Context context) {
        super(context, "Agenda", null, 4);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE Alunos (id CHAR(36) PRIMARY KEY, nome TEXT NOT NULL, endereco TEXT, telefone TEXT, site TEXT, nota REAL, caminhoFoto TEXT);";
        db.execSQL(sql);
    }


    //chamado quando muda a versao do bd
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("oldVersion", String.valueOf(oldVersion));
        Log.i("newVersion", String.valueOf(newVersion));

        String sql = "";
        switch (oldVersion){

            case 1:
            sql = "ALTER TABLE  Alunos ADD COLUMN caminhoFoto TEXT";
            db.execSQL(sql);//indo para versao 2 / mudar para 2 no construtor

            //mudar a estrutura de id para UUID
            case 2:
            String criandoTabelaNova = "CREATE TABLE Alunos_novo " +
                    "(id CHAR(36) PRIMARY KEY," +
                    "nome TEXT NOT NULL," +
                    "endereco TEXT, " +
                    "telefone TEXT, " +
                    "site TEXT, " +
                    "nota REAL, " +
                    "caminhoFoto TEXT);";
            db.execSQL(criandoTabelaNova);//indo para versao 3 / mudar para 3 no construtor

            //incluir dados da tabela antiga "Alunos" pra tabela nova "Alunos_novo"
                String inserirAlunosNaNovaTabela = "INSERT INTO Alunos_novo (id, nome, endereco, telefone, site, nota, caminhoFoto) " +
                        "SELECT id, nome, endereco, telefone, site, nota, caminhoFoto " +
                        "FROM Alunos";
                db.execSQL(inserirAlunosNaNovaTabela);

            //Remover a tabela antiga Alunos
            String removendoTabelaAntiga = "DROP TABLE Alunos";
            db.execSQL(removendoTabelaAntiga);

            //Alterando o nome da tabela atual "Alunos_novo" para "Alunos"
                String alterandoNomeDaTabelaNova = "ALTER TABLE Alunos_novo " +
                        "RENAME TO Alunos";
                db.execSQL(alterandoNomeDaTabelaNova);
            case 3:
                //add o UUID para cada um
                String buscaAlunos = "SELECT * FROM Alunos";

                //executa a query e devolve o cursor da linhas;
                //null não estmaos enviando nemhum parametro
                Cursor cursor = db.rawQuery(buscaAlunos, null);

                //pega cada aluno da tabela
                List<Aluno> alunos = populaAlunos(cursor);

                //atualizar o id do aluno
                // id=? novo id
                // id=? id que já existe atualmente
                String alteraIdDoAluno = "UPDATE Alunos SET id=? WHERE id=?";
                for(Aluno  aluno : alunos){
                    //geraUUID() gera o UUID
                    //aluno.getId() para cada id
                    db.execSQL(alteraIdDoAluno, new String[]{geraUUID(), aluno.getId()});
                }
        }

    }
    //o java gera o UUID
    private String geraUUID() {
        return UUID.randomUUID().toString();
    }

    public void insere(Aluno aluno) {
        //para evitar instrucoes maliciosas o SQLite faz a insercao dos dados
        SQLiteDatabase db = getWritableDatabase();// ref do banco de dados
        insereIdSeNecessario(aluno);
        ContentValues dados = pegaDadosDoAluno(aluno);
        db.insert("Alunos",null, dados);//monta a instrucao sql. protege os dados

    }

    private void insereIdSeNecessario(Aluno aluno) {
        if(aluno.getId() == null){
            //gera o UUID do aluno antes de persistir no sqlite, pq o sqlite não saber gerar este tipode id
            aluno.setId(geraUUID());
        }
    }

    @NonNull
    private ContentValues pegaDadosDoAluno(Aluno aluno) {
        ContentValues dados = new ContentValues();//server para inserir os dados
        dados.put("id", aluno.getId());
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

        List<Aluno> alunos = populaAlunos(c);
        c.close();//libera o cursor(recurso)libera a memorira
        return alunos;
    }

    @NonNull
    private List<Aluno> populaAlunos(Cursor c) {
        List<Aluno> alunos = new ArrayList<Aluno>();

        while(c.moveToNext()){//vai para a proxima linha  e mostra se existe mais linhas
            Aluno aluno = new Aluno();
            aluno.setId( c.getString(c.getColumnIndex("id")));
            aluno.setNome( c.getString(c.getColumnIndex("nome")));//pega o nome de dentro do cursor na coluna nome
            aluno.setEndereco( c.getString(c.getColumnIndex("endereco")));
            aluno.setTelefone( c.getString(c.getColumnIndex("telefone")));
            aluno.setSite( c.getString(c.getColumnIndex("site")));
            aluno.setNota( c.getDouble(c.getColumnIndex("nota")));
            aluno.setCaminhoFoto(c.getString(c.getColumnIndex("caminhoFoto")));

            alunos.add(aluno);
        }
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

    //inserir os alunos vindo do servidor e inserir no banco sqlite
    public void sincroniza(List<Aluno> alunos) {
        for(Aluno aluno : alunos){
            if(existeJaAlunoNoBancoSqlite(aluno)){
                //pega as informacoes do servidor e atualiza no banco
                altera(aluno);
            }else{
                insere(aluno);
            }
        }

    }

    private boolean existeJaAlunoNoBancoSqlite(Aluno aluno) {
        SQLiteDatabase db = getReadableDatabase();
        String existe = "SELECT id FROM Alunos WHERE id=? LIMIT 1";
        Cursor cursor = db.rawQuery(existe, new String[]{aluno.getId()});
        int quantidade = cursor.getCount();
        return quantidade > 0;
        }

    }

