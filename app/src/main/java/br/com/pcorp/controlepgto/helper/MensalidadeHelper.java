package br.com.pcorp.controlepgto.helper;

import android.widget.EditText;

import br.com.pcorp.controlepgto.CadastroMensalidade;
import br.com.pcorp.controlepgto.R;
import br.com.pcorp.controlepgto.modelo.Mensalidade;

/**
 * Created by root on 11/03/17.
 */

public class MensalidadeHelper {
    private final EditText campoMes;
    private final EditText campoAno;
    private final EditText campoVencimento;
    private final EditText campoValor;
    private Mensalidade mensalidade;
    private CadastroMensalidade activity;

    public MensalidadeHelper(CadastroMensalidade activity) {
        campoMes = (EditText) activity.findViewById(R.id.cad_mensal_mes);
        campoAno = (EditText) activity.findViewById(R.id.cad_mensal_ano);
        campoVencimento = (EditText) activity.findViewById(R.id.cad_mensal_vencimento);
        campoValor = (EditText) activity.findViewById(R.id.cad_mensal_valor);

        this.activity = activity;

        mensalidade = new Mensalidade();
    }

    public EditText getCampoMes() {
        return campoMes;
    }

    public EditText getCampoAno() {
        return campoAno;
    }

    public EditText getCampoVencimento() {
        return campoVencimento;
    }

    public EditText getCampoValor() {
        return campoValor;
    }

    public Mensalidade getMensalidade(){
        mensalidade.setMes(Integer.valueOf(campoMes.getText().toString()));
        mensalidade.setAno(Integer.valueOf(campoAno.getText().toString()));
        mensalidade.setVencimentoFormatado(campoVencimento.getText().toString());
        mensalidade.setValor(Double.valueOf(campoValor.getText().toString()));

        return mensalidade;
    }

    public void preencheFormulario(Mensalidade mensalidade){
        campoMes.setText(String.valueOf(mensalidade.getMes()));
        campoAno.setText(String.valueOf(mensalidade.getAno()));
        campoVencimento.setText(mensalidade.getVencimentoFormatado());
        campoValor.setText(String.valueOf(mensalidade.getValor()));

        this.mensalidade = mensalidade;
    }
}
