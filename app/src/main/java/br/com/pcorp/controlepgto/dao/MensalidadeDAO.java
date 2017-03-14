package br.com.pcorp.controlepgto.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import br.com.pcorp.controlepgto.modelo.Mensalidade;

/**
 * Created by root on 11/03/17.
 */

public class MensalidadeDAO {
    private final SQLiteDatabase dbWriter;
    private final SQLiteDatabase dbReader;

    public MensalidadeDAO(Context context) {
        ControlePgtoDAO dao = new ControlePgtoDAO(context);

        this.dbWriter = dao.getEscritaBanco();
        this.dbReader = dao.getLeituraBanco();
    }

    @NonNull
    public Mensalidade getMensalidade(Cursor c) {
        Mensalidade mensalidade = new Mensalidade(
                c.getInt(c.getColumnIndex("mes")),
                c.getInt(c.getColumnIndex("ano")),
                c.getString(c.getColumnIndex("vencimento")),
                c.getDouble(c.getColumnIndex("valor")));
        mensalidade.setId(c.getLong(c.getColumnIndex("id")));
        return mensalidade;
    }

    public List<Mensalidade> buscaMensalidades() {
        String sql = "SELECT * FROM mensalidade";
        Cursor c = dbReader.rawQuery(sql, null);

        ArrayList<Mensalidade> mensalidades = new ArrayList<>();

        while(c.moveToNext()){
            Mensalidade mensalidade = getMensalidade(c);

            mensalidades.add(mensalidade);
        }

        c.close();

        dbReader.close();

        return mensalidades;
    }

    @NonNull
    private ContentValues pegaDadosMensalidade(Mensalidade mensalidade) {
        ContentValues dados = new ContentValues();
        dados.put("mes", mensalidade.getMes());
        dados.put("ano", mensalidade.getAno());
        dados.put("vencimento", mensalidade.getVencimentoFormatado());
        dados.put("valor", mensalidade.getValor());

        return dados;
    }

    public boolean insere(Mensalidade mensalidade){
        ContentValues dados = pegaDadosMensalidade(mensalidade);

        String sql = String.format(
                Locale.getDefault(),
                "SELECT * from mensalidade WHERE mes = %s AND ano = %s",
                dados.get("mes"), dados.get("ano") );
        Cursor c = dbReader.rawQuery(sql, null);
        c.moveToNext();

        boolean retorno;

        if(c.getCount() > 0) {
            retorno = false;
        } else {
            dbWriter.insert("mensalidade", null, dados);
            retorno = true;
        }

        dbWriter.close();
        c.close();

        return retorno;
    }

    public void deleta(Mensalidade mensalidade) {

        String[] params = {mensalidade.getId().toString()};
        dbWriter.delete("mensalidade", "id = ?", params);

        dbWriter.delete("pagamento", "id_mensalidade = ?", params);

        dbWriter.close();
    }

    public void altera(Mensalidade mensalidade) {

        ContentValues dados = pegaDadosMensalidade(mensalidade);
        String[] params = {mensalidade.getId().toString()};

        dbWriter.update("mensalidade", dados, "id = ?", params);

        dbWriter.close();
    }
}
