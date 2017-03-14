package br.com.pcorp.controlepgto.modelo;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by root on 11/03/17.
 */

public class Pagamento implements Serializable, Comparable<Pagamento> {
    private Long id;
    private Date pagamento;
    private Double valor;
    private Mensalidade mensalidade;
    private Passageiro passageiro;

    private final SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());;

    public Pagamento(Date pagamento, Double valor, Mensalidade mensalidade, Passageiro passageiro) {
        this.pagamento = pagamento;
        this.valor = valor;
        this.mensalidade = mensalidade;
        this.passageiro = passageiro;
    }

    public Pagamento(String pagamento, Double valor, Mensalidade mensalidade, Passageiro passageiro) {
        setPagamentoFormatado(pagamento);
        this.valor = valor;
        this.mensalidade = mensalidade;
        this.passageiro = passageiro;
    }

    public Pagamento(){ }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getPagamento() {
        return pagamento;
    }

    public String getPagamentoFormatado(){
        return formatador.format(pagamento);
    }

    public void setPagamento(Date pagamento) {
        this.pagamento = pagamento;
    }

    public void setPagamentoFormatado(String pagamento){
        try {
            this.pagamento = formatador.parse(pagamento);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Mensalidade getMensalidade() {
        return mensalidade;
    }

    public void setMensalidade(Mensalidade mensalidade) {
        this.mensalidade = mensalidade;
    }

    public Passageiro getPassageiro() {
        return passageiro;
    }

    public void setPassageiro(Passageiro passageiro) {
        this.passageiro = passageiro;
    }

    public boolean ehValorValido(Double valor){
        return valor > 0;
    }

    public boolean ehPagamentoValido(String pagamento){
        return pagamento.split("/").length == 3;
    }

    @Override
    public int compareTo(@NonNull Pagamento other) {
        return this.getPagamento().compareTo(other.getPagamento());
    }

    @Override
    public String toString() {
        String nome;
        if (passageiro != null)
            nome = passageiro.getNome();
        else
            nome = "";

        return String.format(
                Locale.getDefault(),
                "%s - %s: R$ %.2f",
                getPagamentoFormatado(), nome, valor );
    }
}
