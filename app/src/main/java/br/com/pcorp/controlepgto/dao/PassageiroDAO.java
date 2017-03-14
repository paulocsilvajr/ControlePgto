package br.com.pcorp.controlepgto.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import br.com.pcorp.controlepgto.modelo.Passageiro;


/**
 * Created by root on 11/03/17.
 */

public class PassageiroDAO {
    private final SQLiteDatabase dbWriter;
    private final SQLiteDatabase dbReader;

    public PassageiroDAO(Context context) {
        ControlePgtoDAO dao = new ControlePgtoDAO(context);

        this.dbWriter = dao.getEscritaBanco();
        this.dbReader = dao.getLeituraBanco();
    }

    @NonNull
    public Passageiro getPassageiro(Cursor c) {
        Passageiro passageiro = new Passageiro(
                c.getString(c.getColumnIndex("nome")),
                c.getString(c.getColumnIndex("celular")),
                c.getString(c.getColumnIndex("cidade")),
                c.getString(c.getColumnIndex("instituicao_ensino")) );
        passageiro.setId(c.getLong(c.getColumnIndex("id")));
        passageiro.setCpf(c.getString(c.getColumnIndex("cpf")));
        passageiro.setRg(c.getString(c.getColumnIndex("rg")));
        passageiro.setTituloEleitor(c.getString(c.getColumnIndex("titulo_eleitor")));

        return passageiro;
    }

    public List<Passageiro> buscaPassageiros() {
        String sql = "SELECT * FROM passageiro";
        Cursor c = dbReader.rawQuery(sql, null);

        ArrayList<Passageiro> mensalidades = new ArrayList<>();

        while(c.moveToNext()){
            Passageiro passageiro = getPassageiro(c);

            mensalidades.add(passageiro);
        }

        c.close();

        dbReader.close();

        return mensalidades;
    }

    @NonNull
    private ContentValues pegaDadosPassageiro(Passageiro passageiro) {
        ContentValues dados = new ContentValues();
        dados.put("nome", passageiro.getNome());
        dados.put("celular", passageiro.getCelular());
        dados.put("cidade", passageiro.getCidade());
        dados.put("instituicao_ensino", passageiro.getInstituicaoEstudo());
        dados.put("cpf", passageiro.getCpf());
        dados.put("rg", passageiro.getRg());
        dados.put("titulo_eleitor", passageiro.getTituloEleitor());

        return dados;
    }

    public void insere(Passageiro passageiro){
        ContentValues dados = pegaDadosPassageiro(passageiro);

        dbWriter.insert("passageiro", null, dados);

        dbWriter.close();
    }

    public boolean deleta(Passageiro passageiro) {

        String[] params = {passageiro.getId().toString()};

        String sql = "SELECT * FROM pagamento WHERE id_passageiro = " + passageiro.getId();
        Cursor c = dbReader.rawQuery(sql, null);

        // se não foi encontrado pagamento efetuado pelo passageiro informado acima, apaga o passageiro
        if(c.getCount() == 0) {
            dbWriter.delete("passageiro", "id = ?", params);

            dbWriter.close();

            return true;
        }else{
            dbWriter.close();

            return false;
        }
    }

    public void altera(Passageiro passageiro) {

        ContentValues dados = pegaDadosPassageiro(passageiro);
        String[] params = {passageiro.getId().toString()};

        dbWriter.update("passageiro", dados, "id = ?", params);

        dbWriter.close();
    }


}
