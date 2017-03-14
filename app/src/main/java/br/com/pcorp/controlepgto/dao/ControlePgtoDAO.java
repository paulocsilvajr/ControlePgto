package br.com.pcorp.controlepgto.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by root on 11/03/17.
 */

public class ControlePgtoDAO extends SQLiteOpenHelper {
    public ControlePgtoDAO(Context context) {
        super(context, "ControlePgto", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql =
                "CREATE TABLE mensalidade(" +
                        "id INTEGER PRIMARY KEY," +
                        "mes INTEGER NOT NULL," +
                        "ano INTEGER NOT NULL," +
                        "vencimento TEXT NOT NULL," +
                        "valor REAL NOT NULL);";
        db.execSQL(sql);

        sql =
                "CREATE TABLE passageiro(" +
                        "id INTEGER PRIMARY KEY," +
                        "nome TEXT NOT NULL," +
                        "celular TEXT NOT NULL," +
                        "cidade TEXT NOT NULL," +
                        "instituicao_ensino TEXT NOT NULL," +
                        "cpf TEXT," +
                        "rg TEXT," +
                        "titulo_eleitor TEXT);";
        db.execSQL(sql);

        sql =
                "CREATE TABLE pagamento(" +
                        "id INTEGER PRIMARY KEY," +
                        "pagamento TEXT NOT NULL," +
                        "valor REAL NOT NULL," +
                        "id_mensalidade INTEGER NOT NULL," +
                        "id_passageiro INTEGER NOT NULL);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    public SQLiteDatabase getEscritaBanco(){
        return getWritableDatabase();
    }

    public SQLiteDatabase getLeituraBanco(){
        return getReadableDatabase();
    }

}
