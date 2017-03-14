package br.com.pcorp.controlepgto;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import br.com.pcorp.controlepgto.dao.ControlePgtoDAO;
import br.com.pcorp.controlepgto.dao.MensalidadeDAO;
import br.com.pcorp.controlepgto.helper.MensalidadeHelper;
import br.com.pcorp.controlepgto.modelo.Mensalidade;

public class CadastroMensalidade extends AppCompatActivity {
    private String ano, mes, venc;
    private MensalidadeHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_mensalidade);

        this.setTitle("Cadastro de mensalidades");

        // Valores default
        Calendar agora = new GregorianCalendar();
        SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String data = formatador.format(agora.getTime());
        String[] partes = data.split("/");

        ano = partes[2];
        mes = partes[1];

        venc = String.format(Locale.getDefault(), "15/%s/%s", mes, ano);

        EditText txtAno = (EditText) findViewById(R.id.cad_mensal_ano);
        EditText txtMes = (EditText) findViewById(R.id.cad_mensal_mes);
        EditText txtVencimento = (EditText) findViewById(R.id.cad_mensal_vencimento);

        txtAno.setText(ano);
        txtMes.setText(mes);
        txtVencimento.setText(venc);
        //

        helper = new MensalidadeHelper(this);

        Intent intent = getIntent();
        Mensalidade mensalidade = (Mensalidade) intent.getSerializableExtra("mensalidade");

        if(mensalidade != null){
            helper.preencheFormulario(mensalidade);
        }

        final Button btnSalvar = (Button) findViewById(R.id.cad_mensal_salvar);
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvar();
            }
        });
    }

    private void salvar() {
        if(helper.getCampoMes().getText().toString().isEmpty()) {
            Toast.makeText(
                    CadastroMensalidade.this,
                    "Informe um mês",
                    Toast.LENGTH_SHORT).show();

            helper.getCampoMes().requestFocus();
        } else if(helper.getCampoAno().getText().toString().isEmpty()) {
            Toast.makeText(
                    CadastroMensalidade.this,
                    "Informe um ano",
                    Toast.LENGTH_SHORT).show();

            helper.getCampoAno().requestFocus();
        } else if(helper.getCampoVencimento().getText().toString().isEmpty()){
            Toast.makeText(
                    CadastroMensalidade.this,
                    "Informe um vencimento",
                    Toast.LENGTH_SHORT).show();

            helper.getCampoVencimento().requestFocus();
        } else if (helper.getCampoValor().getText().toString().isEmpty()) {
            Toast.makeText(
                    CadastroMensalidade.this,
                    "Informe um valor",
                    Toast.LENGTH_SHORT).show();

            helper.getCampoValor().requestFocus();
        } else {

            Mensalidade mensalidade = helper.getMensalidade();

            MensalidadeDAO mensalidadeDAO = new MensalidadeDAO(this);

            if (!mensalidade.ehAnoValido(String.valueOf(mensalidade.getAno()))) {
                Toast.makeText(
                        CadastroMensalidade.this,
                        "Informe um ano válido",
                        Toast.LENGTH_SHORT).show();

                helper.getCampoAno().requestFocus();
            } else if (!mensalidade.ehMesValido(String.valueOf(mensalidade.getMes()))) {
                Toast.makeText(
                        CadastroMensalidade.this,
                        "Informe um mês válido",
                        Toast.LENGTH_SHORT).show();

                helper.getCampoMes().requestFocus();
            } else if (!mensalidade.ehValorValido(mensalidade.getValor())) {
                Toast.makeText(
                        CadastroMensalidade.this,
                        "Informe um valor válido",
                        Toast.LENGTH_SHORT).show();

                helper.getCampoValor().requestFocus();
            } else {

                String mensagem;

                if (mensalidade.getId() != null) {
                    mensalidadeDAO.altera(mensalidade);
                    mensagem = String.format(
                                        "%s alterada!",
                                        mensalidade);

                } else {
                    if(mensalidadeDAO.insere(mensalidade)) {
                        mensagem = String.format(
                                        "%s salva!",
                                        mensalidade);
                    } else {
                        mensagem = String.format(
                                        "%s já existe!",
                                        mensalidade);
                    }
                }

                Toast.makeText(
                        CadastroMensalidade.this,
                        mensagem,
                        Toast.LENGTH_SHORT).show();

                finish();
            }
        }
    }
}
