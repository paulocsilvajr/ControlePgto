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
import br.com.pcorp.controlepgto.modelo.Pagamento;
import br.com.pcorp.controlepgto.modelo.Pagamento;
import br.com.pcorp.controlepgto.modelo.Passageiro;

/**
 * Created by root on 11/03/17.
 */

public class PagamentoDAO {
    private final SQLiteDatabase dbWriter;
    private final SQLiteDatabase dbReader;

    public PagamentoDAO(Context context) {
        ControlePgtoDAO dao = new ControlePgtoDAO(context);

        this.dbWriter = dao.getEscritaBanco();
        this.dbReader = dao.getLeituraBanco();
    }

    public List<Pagamento> buscaPagamentos(Context context, Long id) {
        String sql = "SELECT * FROM pagamento WHERE id_mensalidade = " + id;
        Cursor c = dbReader.rawQuery(sql, null);

        ArrayList<Pagamento> pagamentos = new ArrayList<>();

        PassageiroDAO passageiroDAO = new PassageiroDAO(context);
        MensalidadeDAO mensalidadeDAO = new MensalidadeDAO(context);

        while(c.moveToNext()){
            Pagamento pagamento;
            pagamento = new Pagamento(
                    c.getString(c.getColumnIndex("pagamento")),
                    c.getDouble(c.getColumnIndex("valor")),
                    getMensalidade(context, c.getLong(c.getColumnIndex("id_mensalidade"))),
                    getPassageiro(context, c.getLong(c.getColumnIndex("id_passageiro"))) );

            pagamento.setId(c.getLong(c.getColumnIndex("id")));

            pagamentos.add(pagamento);
        }

        c.close();

        dbReader.close();

        return pagamentos;
    }

    private Passageiro getPassageiro(Context context, Long id) {
        String sql = "SELECT * from passageiro WHERE id = " + id;
        Cursor c = dbReader.rawQuery(sql, null);
        c.moveToNext();

        PassageiroDAO passageiroDAO = new PassageiroDAO(context);
        Passageiro passageiro = passageiroDAO.getPassageiro(c);

        c.close();

        return passageiro;
    }

    private Mensalidade getMensalidade(Context context, Long id) {
        String sql = "SELECT * from mensalidade WHERE id = " + id;
        Cursor c = dbReader.rawQuery(sql, null);
        c.moveToNext();

        MensalidadeDAO mensalidadeDAO = new MensalidadeDAO(context);
        Mensalidade mensalidade = mensalidadeDAO.getMensalidade(c);

        c.close();

        return mensalidade;
    }

    @NonNull
    private ContentValues pegaDadosMensalidade(Pagamento pagamento) {
        ContentValues dados = new ContentValues();
        dados.put("pagamento", pagamento.getPagamentoFormatado());
        dados.put("valor", pagamento.getValor());
        dados.put("id_mensalidade", pagamento.getMensalidade().getId());
        dados.put("id_passageiro", pagamento.getPassageiro().getId());

        return dados;
    }

    public boolean insere(Pagamento pagamento){
        ContentValues dados = pegaDadosMensalidade(pagamento);

        String sql = String.format(
                Locale.getDefault(),
                "SELECT * from pagamento WHERE id_passageiro = %s AND id_mensalidade = %s",
                dados.get("id_passageiro"), dados.get("id_mensalidade") );
        Cursor c = dbReader.rawQuery(sql, null);
        c.moveToNext();

        boolean retorno;

        if(c.getCount() > 0) {
            retorno = false;
        } else {
            dbWriter.insert("pagamento", null, dados);
            retorno = true;
        }

        dbWriter.close();
        c.close();

        return retorno;
    }

    public void deleta(Pagamento pagamento) {

        String[] params = {pagamento.getId().toString()};
        dbWriter.delete("pagamento", "id = ?", params);

        dbWriter.close();
    }

    public void altera(Pagamento pagamento) {

        ContentValues dados = pegaDadosMensalidade(pagamento);
        String[] params = {pagamento.getId().toString()};

        dbWriter.update("pagamento", dados, "id = ?", params);

        dbWriter.close();
    }
}
