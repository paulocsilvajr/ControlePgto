package br.com.pcorp.controlepgto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import br.com.pcorp.controlepgto.dao.PagamentoDAO;
import br.com.pcorp.controlepgto.helper.Backup;
import br.com.pcorp.controlepgto.helper.PagamentoHelper;
import br.com.pcorp.controlepgto.modelo.Mensalidade;
import br.com.pcorp.controlepgto.modelo.Pagamento;
import br.com.pcorp.controlepgto.modelo.Passageiro;

public class CadastroPagamento extends AppCompatActivity {

    private PagamentoHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_pagamento);

        this.setTitle("Cadastro de pagamentos");

        helper = new PagamentoHelper(this);

        Intent intent = getIntent();
        Mensalidade mensalidade = (Mensalidade) intent.getSerializableExtra("mensalidade");
        Passageiro passageiro = (Passageiro) intent.getSerializableExtra("passageiro");
        Pagamento pagamento = (Pagamento) intent.getSerializableExtra("pagamento");

        // Valores default
        Calendar agora = new GregorianCalendar();

        SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String pag = formatador.format(agora.getTime());

        Double valor = mensalidade.getValor();

        TextView lblMensalidade = (TextView) findViewById(R.id.cad_pag_mensalidade);
        TextView lblPassageiro = (TextView) findViewById(R.id.cad_pag_passageiro);
        EditText txtValor = (EditText) findViewById(R.id.cad_pag_valor);
        EditText txtPagamento = (EditText) findViewById(R.id.cad_pag_pagamento);

        lblMensalidade.setText(mensalidade.toString());
        lblPassageiro.setText(passageiro.getNome());
        txtValor.setText(String.valueOf(valor));
        txtPagamento.setText(pag);
        //

        if(pagamento != null){
            helper.preencheFormulario(pagamento);
        }

        final Button btnSalvar = (Button) findViewById(R.id.cad_pag_salvar);
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvar();
            }
        });
    }

    private void salvar() {
        if(helper.getCampoValor().getText().toString().isEmpty()) {
            Toast.makeText(
                    CadastroPagamento.this,
                    "Informe um valor",
                    Toast.LENGTH_SHORT).show();

            helper.getCampoValor().requestFocus();
        } else if(helper.getCampoPagamento().getText().toString().isEmpty()) {
            Toast.makeText(
                    CadastroPagamento.this,
                    "Informe uma data de pagamento",
                    Toast.LENGTH_SHORT).show();

            helper.getCampoPagamento().requestFocus();
        } else {

            Pagamento pagamento = helper.getPagamento();

            PagamentoDAO pagamentoDAO = new PagamentoDAO(this);

            if (!pagamento.ehValorValido(pagamento.getValor())) {
                Toast.makeText(
                        CadastroPagamento.this,
                        "Informe um valor válido",
                        Toast.LENGTH_SHORT).show();

                helper.getCampoValor().requestFocus();
            } else {

                String mensagem;
                if (pagamento.getId() != null) {
                    pagamentoDAO.altera(pagamento);
                    mensagem = String.format(
                                    "Pagamento de %s da %s alterado!",
                                    pagamento.getPassageiro().getNome(),
                                    pagamento.getMensalidade() );
                } else {
                    if(pagamentoDAO.insere(pagamento)) {
                        mensagem = String.format(
                                        "Pagamento de %s da %s salvo!",
                                        pagamento.getPassageiro().getNome(),
                                        pagamento.getMensalidade() );
                    } else {
                        mensagem = String.format(
                                        "Passageiro %s já efetuou pagamento da %s!",
                                        pagamento.getPassageiro().getNome(),
                                        pagamento.getMensalidade() );
                    }

                    // testar e melhorar, não está salvando, classe: Backup.
                    Backup backup = new Backup(this);
                    backup.backUp();
                }

                Toast.makeText(
                        CadastroPagamento.this,
                        mensagem,
                        Toast.LENGTH_SHORT).show();

                finish();
            }
        }
    }
}
