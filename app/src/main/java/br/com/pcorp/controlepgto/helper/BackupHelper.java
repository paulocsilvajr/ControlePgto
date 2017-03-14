package br.com.pcorp.controlepgto.helper;

import android.content.Context;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.R.attr.data;

/**
 * Created by root on 12/03/17.
 */

public class BackupHelper {
    private File bk;
    private File backupDB;
    private File currentDB;
    private String currentDBPath;
    private String backupDBPath;
    private Context context;

    public BackupHelper(Context context) {
        this.context = context;

        bk = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File data = Environment.getDataDirectory();

        Date currenty_date = new Date(System.currentTimeMillis());
        SimpleDateFormat formatador = new SimpleDateFormat("yyyyMMdd_", Locale.getDefault());
        String formated_date = formatador.format(currenty_date);

        currentDBPath = "/data/br.com.pcorp.controlepgto/databases/ControlePgto";
        backupDBPath = "/" + formated_date + "ControlePgto.db.bk";

        currentDB = new File(data, currentDBPath);
        backupDB = new File(bk, backupDBPath);
    }

    public void setBackupDBName(String backupDBPath) {
        this.backupDBPath = "/" + backupDBPath;
    }

    public File getBackupDB() {
        return backupDB;
    }

    public File getCurrentDB() {
        return currentDB;
    }

    // baseado em: http://stackoverflow.com/questions/39770990/backup-and-restore-database-android-studio
    public void backUp() {
        try {

            if (bk.canWrite()) {

                Log.d("backupDB path", "" + backupDB.getAbsolutePath());

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                    Toast.makeText(context, "Realizado cópia de segurança", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void restore() {
        try {

            if (bk.canWrite()) {

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(backupDB).getChannel();
                    FileChannel dst = new FileOutputStream(currentDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                    Toast.makeText(context, "Restorado banco de dados com successo", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
