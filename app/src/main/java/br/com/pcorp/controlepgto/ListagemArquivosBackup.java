package br.com.pcorp.controlepgto;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import br.com.pcorp.controlepgto.helper.Backup;

public class ListagemArquivosBackup extends AppCompatActivity {

    private ListView listaArquivos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listagem_arquivos_backup);

        setTitle("Restauração");

        listaArquivos = (ListView) findViewById(R.id.lista_arquivos);

        registerForContextMenu(listaArquivos);
    }

    @Override
    protected void onResume() {
        super.onResume();

        carregarLista();
    }

    private void carregarLista(){
        // baseado em http://pt.stackoverflow.com/questions/48317/como-listar-diretorio-em-um-listview
        File file;
        List<String> lista = new ArrayList<>();

        Backup backup = new Backup(this);
        file = new File(backup.getBackupDB().getParent());

        File listaArquivos[] = file.listFiles();

        for (File arquivo : listaArquivos) {
            if(arquivo.getName().endsWith(".db.bk"))
                lista.add(arquivo.getName());
        }

        Collections.reverse(lista);

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lista);

        this.listaArquivos.setAdapter(adapter);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, final ContextMenu.ContextMenuInfo menuInfo) {
        // criando itens do menu de contexto e criando evento de click para cada um.
        MenuItem restaurar = menu.add("Restaurar");

        restaurar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                // capturando a mensalidade selecionado pela posição em listaMensalidades
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
                final String arquivo = (String) listaArquivos.getItemAtPosition(info.position);

                // caixa de dialogo de confirmação
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Ação quando o usuário clicar no botão sim
                                Backup backup = new Backup(ListagemArquivosBackup.this);
                                backup.setBackupDBName(arquivo);
                                backup.restore();

                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //Ação quando o usuário clicar no botão Não
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(ListagemArquivosBackup.this);
                builder.setMessage(
                        String.format(Locale.getDefault(),"Deseja realmente restaurar %s?", arquivo)).
                        setPositiveButton("Sim", dialogClickListener).
                        setNegativeButton("Não", dialogClickListener).show();
                // fim caixa dialogo

                return false;
            }
        });
    }
}
