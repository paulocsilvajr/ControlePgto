package br.com.pcorp.controlepgto.helper;

import android.widget.EditText;

import br.com.pcorp.controlepgto.CadastroPassageiro;
import br.com.pcorp.controlepgto.R;
import br.com.pcorp.controlepgto.modelo.Passageiro;

/**
 * Created by root on 12/03/17.
 */

public class PassageiroHelper {
    private final EditText campoNome;
    private final EditText campoCelular;
    private final EditText campoCidade;
    private final EditText campoInstituicao;
    private final EditText campoCpf;
    private final EditText campoRg;
    private final EditText campoTitulo;
    private CadastroPassageiro activity;
    private Passageiro passageiro;

    public PassageiroHelper(CadastroPassageiro activity) {
        campoNome = (EditText) activity.findViewById(R.id.cad_passag_nome);
        campoCelular = (EditText) activity.findViewById(R.id.cad_passag_celular);
        campoCidade = (EditText) activity.findViewById(R.id.cad_passag_cidade);
        campoInstituicao = (EditText) activity.findViewById(R.id.cad_passag_instituicao);
        campoCpf = (EditText) activity.findViewById(R.id.cad_passag_cpf);
        campoRg = (EditText) activity.findViewById(R.id.cad_passag_rg);
        campoTitulo = (EditText) activity.findViewById(R.id.cad_passag_titulo);

        this.activity = activity;

        passageiro = new Passageiro();
    }

    public EditText getCampoNome() {
        return campoNome;
    }

    public EditText getCampoCelular() {
        return campoCelular;
    }

    public EditText getCampoCidade() {
        return campoCidade;
    }

    public EditText getCampoInstituicao() {
        return campoInstituicao;
    }

    public EditText getCampoCpf() {
        return campoCpf;
    }

    public EditText getCampoRg() {
        return campoRg;
    }

    public EditText getCampoTitulo() {
        return campoTitulo;
    }

    public Passageiro getPassageiro(){
        passageiro.setNome(campoNome.getText().toString());
        passageiro.setCelular(campoCelular.getText().toString());
        passageiro.setCidade(campoCidade.getText().toString());
        passageiro.setInstituicaoEstudo(campoInstituicao.getText().toString());
        passageiro.setCpf(campoCpf.getText().toString());
        passageiro.setRg(campoRg.getText().toString());
        passageiro.setTituloEleitor(campoTitulo.getText().toString());

        return passageiro;
    }

    public void preencheFormulario(Passageiro passageiro){
        campoNome.setText(passageiro.getNome());
        campoCelular.setText(passageiro.getCelular());
        campoCidade.setText(passageiro.getCidade());
        campoInstituicao.setText(passageiro.getInstituicaoEstudo());
        campoCpf.setText(passageiro.getCpf());
        campoRg.setText(passageiro.getRg());
        campoTitulo.setText(passageiro.getTituloEleitor());

        this.passageiro = passageiro;
    }
}
