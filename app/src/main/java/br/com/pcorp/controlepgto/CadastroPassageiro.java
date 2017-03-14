package br.com.pcorp.controlepgto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import br.com.pcorp.controlepgto.dao.PassageiroDAO;
import br.com.pcorp.controlepgto.helper.PassageiroHelper;
import br.com.pcorp.controlepgto.modelo.Mensalidade;
import br.com.pcorp.controlepgto.modelo.Passageiro;

public class CadastroPassageiro extends AppCompatActivity {

    private PassageiroHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_passageiro);

        this.setTitle("Cadastro de passageiros");

        // Valores default
        EditText txtCidade = (EditText) findViewById(R.id.cad_passag_cidade);
        EditText txtInstituicao = (EditText) findViewById(R.id.cad_passag_instituicao);

        txtCidade.setText("Itaporanga");
        txtInstituicao.setText("FAFIT");
        //

        helper = new PassageiroHelper(this);

        Intent intent = getIntent();
        Passageiro passageiro = (Passageiro) intent.getSerializableExtra("passageiro");

        if(passageiro != null){
            helper.preencheFormulario(passageiro);
        }

        final Button btnSalvar = (Button) findViewById(R.id.cad_passag_salvar);
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvar();
            }
        });

    }

    private void salvar() {
        if(helper.getCampoNome().getText().toString().isEmpty()) {
            Toast.makeText(
                    CadastroPassageiro.this,
                    "Informe um nome",
                    Toast.LENGTH_SHORT).show();

            helper.getCampoNome().requestFocus();
        } else if(helper.getCampoCelular().getText().toString().isEmpty()) {
            Toast.makeText(
                    CadastroPassageiro.this,
                    "Informe um celular",
                    Toast.LENGTH_SHORT).show();

            helper.getCampoCelular().requestFocus();
        } else if(helper.getCampoCidade().getText().toString().isEmpty()){
            Toast.makeText(
                    CadastroPassageiro.this,
                    "Informe um cidade",
                    Toast.LENGTH_SHORT).show();

            helper.getCampoCidade().requestFocus();
        } else if (helper.getCampoInstituicao().getText().toString().isEmpty()) {
            Toast.makeText(
                    CadastroPassageiro.this,
                    "Informe um instituição de ensino",
                    Toast.LENGTH_SHORT).show();

            helper.getCampoInstituicao().requestFocus();
        } else {

            Passageiro passageiro = helper.getPassageiro();

            PassageiroDAO passageiroDAO = new PassageiroDAO(this);

//            if (!passageiro.ehAnoValido(String.valueOf(passageiro.getAno()))) {
//                Toast.makeText(
//                        CadastroMensalidade.this,
//                        "Informe um ano válido",
//                        Toast.LENGTH_SHORT).show();
//
//                helper.getCampoAno().requestFocus();
//            } else {

            String status;
            if (passageiro.getId() != null) {
                passageiroDAO.altera(passageiro);
                status = "alterado";
            } else {
                passageiroDAO.insere(passageiro);
                status = "salvo";
            }

            Toast.makeText(
                    CadastroPassageiro.this,
                    String.format("Passageiro %s %s!", passageiro.getNome(), status),
                    Toast.LENGTH_SHORT).show();

            finish();
        }
    }
}
