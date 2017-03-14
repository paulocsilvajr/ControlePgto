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

import br.com.pcorp.controlepgto.dao.PagamentoDAO;
import br.com.pcorp.controlepgto.modelo.Mensalidade;
import br.com.pcorp.controlepgto.modelo.Pagamento;
import br.com.pcorp.controlepgto.modelo.Passageiro;

public class ListagemPagamentosActivity extends AppCompatActivity {

    private ListView listaPagamentos;
    private Mensalidade mensalidade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listagem_pagamentos);

        Intent intent = getIntent();
        mensalidade = (Mensalidade) intent.getSerializableExtra("mensalidade");

        this.setTitle(
                String.format(Locale.getDefault(),
                "Pagamentos da mensal. %s", mensalidade.getMesAno()) );

        listaPagamentos = (ListView) findViewById(R.id.lista_pagamentos);

        // clique sobre um item da lista de pagamentos
//        listaPagamentos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//        });

        Button novoPagamento = (Button) findViewById(R.id.novo_pagamento);
        novoPagamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentListaPassageiros = new Intent(
                        ListagemPagamentosActivity.this,
                        ListagemPassageirosActivity.class);

                intentListaPassageiros.putExtra("mensalidade", mensalidade);

                startActivity(intentListaPassageiros);
            }
        });

        registerForContextMenu(listaPagamentos);
    }

    @Override
    protected void onResume() {
        super.onResume();

        carregarLista();
    }

    private List<Pagamento> carregarLista(){
        PagamentoDAO pagamentoDAO = new PagamentoDAO(this);

        List<Pagamento> pagamentos = pagamentoDAO.buscaPagamentos(this, mensalidade.getId());

        // ordenando lista
        Collections.sort(pagamentos);

        ArrayAdapter<Pagamento> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, pagamentos);

        listaPagamentos.setAdapter(adapter);

        return pagamentos;
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

                Pagamento pagamento = (Pagamento) listaPagamentos.getItemAtPosition(info.position);

                Intent intentCadPagamento = new Intent(
                        ListagemPagamentosActivity.this,
                        CadastroPagamento.class);

                intentCadPagamento.putExtra("pagamento", pagamento);
                intentCadPagamento.putExtra("mensalidade", mensalidade);
                intentCadPagamento.putExtra("passageiro", pagamento.getPassageiro());

                startActivity(intentCadPagamento);

                return false;
            }
        });

        deletar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                // capturando a mensalidade selecionado pela posição em listaMensalidades
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
                final Pagamento pagamento = (Pagamento) listaPagamentos.getItemAtPosition(info.position);

                // caixa de dialogo de confirmação
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Ação quando o usuário clicar no botão sim
                                PagamentoDAO dao  = new PagamentoDAO(ListagemPagamentosActivity.this);
                                dao.deleta(pagamento);

                                carregarLista();

                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //Ação quando o usuário clicar no botão Não
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(ListagemPagamentosActivity.this);
                builder.setMessage(
                        String.format(Locale.getDefault(),"Deseja realmente excluir %s", pagamento)).
                        setPositiveButton("Sim", dialogClickListener).
                        setNegativeButton("Não", dialogClickListener).show();
                // fim caixa dialogo

                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem total = menu.add("Calcular total");

        total.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                int quantidade = listaPagamentos.getCount();
                double total = somarPagamentos();
                String msgQuantidade = String.format(
                                                Locale.getDefault(),
                                                "%d pagamento%s",
                                                quantidade,
                                                quantidade>1?"s":"");
                String msgTotal = String.format(
                                            Locale.getDefault(),
                                            "Total R$ %.2f",
                                            total);

                AlertDialog.Builder builder = new AlertDialog.Builder(ListagemPagamentosActivity.this);
                builder.setMessage(
                                    msgQuantidade + "\n" +
                                    msgTotal).
                        setNeutralButton("OK", null).
                        show();

                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private double somarPagamentos(){
        List<Pagamento> pagamentos = carregarLista();

        double soma = 0.0;
        for (Pagamento pagamento: pagamentos){
            soma += pagamento.getValor();
        }

        return soma;
    }
}
