package br.com.pcorp.controlepgto.helper;

import android.widget.EditText;
import android.widget.TextView;

import br.com.pcorp.controlepgto.CadastroPagamento;
import br.com.pcorp.controlepgto.R;
import br.com.pcorp.controlepgto.modelo.Mensalidade;
import br.com.pcorp.controlepgto.modelo.Pagamento;
import br.com.pcorp.controlepgto.modelo.Passageiro;

/**
 * Created by root on 12/03/17.
 */

public class PagamentoHelper {
    private final EditText campoValor;
    private final EditText campoPagamento;
    private final Mensalidade mensalidade;
    private final Passageiro passageiro;
    private final TextView campoMensalidade;
    private final TextView campoPassageiro;
    private Pagamento pagamento;
    private CadastroPagamento activity;

    public PagamentoHelper(CadastroPagamento activity) {
        campoValor = (EditText) activity.findViewById(R.id.cad_pag_valor);
        campoPagamento = (EditText) activity.findViewById(R.id.cad_pag_pagamento);
        mensalidade = (Mensalidade) activity.getIntent().getSerializableExtra("mensalidade");
        passageiro = (Passageiro) activity.getIntent().getSerializableExtra("passageiro");
        campoMensalidade = (TextView) activity.findViewById(R.id.cad_pag_mensalidade);
        campoPassageiro = (TextView) activity.findViewById(R.id.cad_pag_passageiro);

        this.activity = activity;

        pagamento = new Pagamento();
    }

    public EditText getCampoValor() {
        return campoValor;
    }

    public EditText getCampoPagamento() {
        return campoPagamento;
    }

    public Mensalidade getMensalidade() {
        return mensalidade;
    }

    public Passageiro getPassageiro() {
        return passageiro;
    }

    public TextView getCampoMensalidade() {
        return campoMensalidade;
    }

    public TextView getCampoPassageiro() {
        return campoPassageiro;
    }

    public Pagamento getPagamento(){
        pagamento.setPagamentoFormatado(campoPagamento.getText().toString());
        pagamento.setValor(Double.valueOf(campoValor.getText().toString()));
        pagamento.setMensalidade(mensalidade);
        pagamento.setPassageiro(passageiro);

        return pagamento;
    }

    public void preencheFormulario(Pagamento pagamento){
        campoPagamento.setText(pagamento.getPagamentoFormatado());
        campoValor.setText(String.valueOf(pagamento.getValor()));
        campoMensalidade.setText(pagamento.getMensalidade().toString());
        campoPassageiro.setText(pagamento.getPassageiro().toString());

        this.pagamento = pagamento;
    }
}
