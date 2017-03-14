package br.com.pcorp.controlepgto;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import br.com.pcorp.controlepgto.dao.MensalidadeDAO;
import br.com.pcorp.controlepgto.helper.BackupHelper;
import br.com.pcorp.controlepgto.modelo.Mensalidade;

public class ListagemMensalidadesActivity extends AppCompatActivity {
    private ListView listaMensalidades;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listagem_mensalidades);

        this.setTitle("Mensalidades");

        listaMensalidades = (ListView) findViewById(R.id.lista_mensalidades);

        // clique sobre um item da lista de mensalidades
        listaMensalidades.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                chamar pagamentos ref. a mensalidade selecionada
                Mensalidade mensalidade = (Mensalidade) listaMensalidades.getItemAtPosition(position);

                Intent intentListaPagamentos = new Intent(
                        ListagemMensalidadesActivity.this,
                        ListagemPagamentosActivity.class);

                intentListaPagamentos.putExtra("mensalidade", mensalidade);
                startActivity(intentListaPagamentos);
            }
        });

        Button novaMensalidade = (Button) findViewById(R.id.nova_mensalidade);
        novaMensalidade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCadMensalidade = new Intent(
                        ListagemMensalidadesActivity.this,
                        CadastroMensalidade.class);

                startActivity(intentCadMensalidade);
            }
        });

        registerForContextMenu(listaMensalidades);
    }

    @Override
    protected void onResume() {
        super.onResume();

        carregarLista();
    }

    private void carregarLista(){
        MensalidadeDAO mensalidadeDAO = new MensalidadeDAO(this);

        List<Mensalidade> mensalidades = mensalidadeDAO.buscaMensalidades();

        // ordenando lista
        Collections.sort(mensalidades);

        ArrayAdapter<Mensalidade> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mensalidades);

        listaMensalidades.setAdapter(adapter);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, final ContextMenu.ContextMenuInfo menuInfo) {
        // criando itens do menu de contexto e criando evento de click para cada um.
        MenuItem alterar = menu.add("Alterar");
        MenuItem deletar = menu.add("Deletar");

        alterar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;

                Mensalidade mensalidade = (Mensalidade) listaMensalidades.getItemAtPosition(info.position);

                Intent intentCadMensalidade = new Intent(ListagemMensalidadesActivity.this,
                        CadastroMensalidade.class);

                intentCadMensalidade.putExtra("mensalidade", mensalidade);
                startActivity(intentCadMensalidade);

                return false;
            }
        });

        deletar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                // capturando a mensalidade selecionado pela posição em listaMensalidades
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
                final Mensalidade mensalidade = (Mensalidade) listaMensalidades.getItemAtPosition(info.position);

                // caixa de dialogo de confirmação
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Ação quando o usuário clicar no botão sim
                                MensalidadeDAO dao  = new MensalidadeDAO(ListagemMensalidadesActivity.this);
                                dao.deleta(mensalidade);

                                carregarLista();

                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //Ação quando o usuário clicar no botão Não
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(ListagemMensalidadesActivity.this);
                builder.setMessage(
                        String.format(Locale.getDefault(),"Deseja realmente excluir %s", mensalidade)).
                        setPositiveButton("Sim", dialogClickListener).
                        setNegativeButton("Não", dialogClickListener).show();
                // fim caixa dialogo

                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem passageiros = menu.add("Cadastro de passageiros");
        MenuItem backup = menu.add("Cópia de segurança da base de dados");
        MenuItem restauracao = menu.add("Restauração da base de dados");

        passageiros.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                Intent intentListaPassageiros = new Intent(
                        ListagemMensalidadesActivity.this,
                        ListagemPassageirosActivity.class);

                startActivity(intentListaPassageiros);

                return false;
            }
        });

        backup.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                // caixa de dialogo de confirmação
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Ação quando o usuário clicar no botão sim
                                BackupHelper backupHelper = new BackupHelper(ListagemMensalidadesActivity.this);
                                backupHelper.backUp();

                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //Ação quando o usuário clicar no botão Não
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(ListagemMensalidadesActivity.this);
                builder.setMessage("Deseja realmente realizar uma cópia de segurança?").
                        setPositiveButton("Sim", dialogClickListener).
                        setNegativeButton("Não", dialogClickListener).show();
                // fim caixa dialogo


                return false;
            }
        });

        restauracao.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                Intent intentListaArquivos = new Intent(
                        ListagemMensalidadesActivity.this,
                        ListagemArquivosBackup.class);

                startActivity(intentListaArquivos);

                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}
