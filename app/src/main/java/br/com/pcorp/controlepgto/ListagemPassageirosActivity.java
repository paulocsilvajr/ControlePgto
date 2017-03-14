package br.com.pcorp.controlepgto;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import br.com.pcorp.controlepgto.dao.PassageiroDAO;
import br.com.pcorp.controlepgto.modelo.Mensalidade;
import br.com.pcorp.controlepgto.modelo.Passageiro;

public class ListagemPassageirosActivity extends AppCompatActivity {

    private ListView listaPassageiros;
    private Mensalidade mensalidade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listagem_passageiros);

        this.setTitle("Passageiros");

        listaPassageiros = (ListView) findViewById(R.id.lista_passageiros);

        Intent intent = getIntent();
        mensalidade = (Mensalidade) intent.getSerializableExtra("mensalidade");

        listaPassageiros.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // chamar mensalidade específico do passageiro selecionado quando a chamada vier
                // depois de listagem de mensalidade.
                if(mensalidade != null){
                    Passageiro passageiro = (Passageiro) listaPassageiros.getItemAtPosition(position);

                    Intent intentCadPagamento = new Intent(
                            ListagemPassageirosActivity.this,
                            CadastroPagamento.class);

                    intentCadPagamento.putExtra("passageiro", passageiro);
                    intentCadPagamento.putExtra("mensalidade", mensalidade);

                    startActivity(intentCadPagamento);

                    finish();
                }
            }
        });

        // filtro de pesquisa na listagem de passageiros
        final EditText pesquisa = (EditText) findViewById(R.id.pesquisa_passageiros);
        pesquisa.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(!pesquisa.getText().toString().isEmpty()) {
                    ArrayAdapter<Passageiro> adapter = carregarLista();
                    adapter.getFilter().filter(s.toString());
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Button novoPassageiro = (Button) findViewById(R.id.novo_passageiro);
        novoPassageiro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCadPassageiro = new Intent(
                        ListagemPassageirosActivity.this,
                        CadastroPassageiro.class);

                startActivity(intentCadPassageiro);
            }
        });

        registerForContextMenu(listaPassageiros);

    }

    @Override
    protected void onResume() {
        super.onResume();

        carregarLista();
    }

    private ArrayAdapter<Passageiro> carregarLista(){
        PassageiroDAO passageiroDAO = new PassageiroDAO(this);

        List<Passageiro> passageiros = passageiroDAO.buscaPassageiros();

        // ordenando lista
        Collections.sort(passageiros);

        ArrayAdapter<Passageiro> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, passageiros);

        listaPassageiros.setAdapter(adapter);

        return adapter;
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

                Passageiro passageiro = (Passageiro) listaPassageiros.getItemAtPosition(info.position);

                Intent intentCadPassageiro = new Intent(ListagemPassageirosActivity.this,
                        CadastroPassageiro.class);

                intentCadPassageiro.putExtra("passageiro", passageiro);
                startActivity(intentCadPassageiro);

                return false;
            }
        });

        deletar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                // capturando a mensalidade selecionado pela posição em listaMensalidades
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
                final Passageiro passageiro = (Passageiro) listaPassageiros.getItemAtPosition(info.position);

                // caixa de dialogo de confirmação
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Ação quando o usuário clicar no botão sim
                                PassageiroDAO dao  = new PassageiroDAO(ListagemPassageirosActivity.this);
                                if(!dao.deleta(passageiro)){
                                    Toast.makeText(
                                            ListagemPassageirosActivity.this,
                                            String.format(
                                                    "Passageiro %s não pode ser excluído!\n" +
                                                    "Possui pagamentos efetuados.",
                                                    passageiro.getNome()),
                                            Toast.LENGTH_SHORT).show();
                                }

                                carregarLista();

                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //Ação quando o usuário clicar no botão Não
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(ListagemPassageirosActivity.this);
                builder.setMessage(
                        String.format(Locale.getDefault(),"Deseja realmente excluir %s", passageiro)).
                        setPositiveButton("Sim", dialogClickListener).
                        setNegativeButton("Não", dialogClickListener).show();
                // fim caixa dialogo

                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem total = menu.add("Calcular quantidade");

        total.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                int quantidade = listaPassageiros.getCount();
                String msgQuantidade = String.format(
                        Locale.getDefault(),
                        "%d passageiro%s cadastrado%s",
                        quantidade,
                        quantidade>1?"s":"",
                        quantidade>1?"s":"");

                AlertDialog.Builder builder = new AlertDialog.Builder(ListagemPassageirosActivity.this);
                builder.setMessage(
                                    msgQuantidade ).
                        setNeutralButton("OK", null).
                        show();

                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}
