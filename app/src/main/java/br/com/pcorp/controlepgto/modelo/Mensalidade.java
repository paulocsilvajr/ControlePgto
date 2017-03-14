package br.com.pcorp.controlepgto.modelo;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.lang.Comparable;
import java.util.Date;
import java.util.Locale;

/**
 * Created by root on 11/03/17.
 */

public class Mensalidade implements Serializable, Comparable<Mensalidade> {
    private Long id;
    private Integer mes;
    private Integer ano;
    private Date vencimento;
    private Double valor;

    private final SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    public Mensalidade(Integer mes, Integer ano, Date vencimento, Double valor) {
        this.mes = mes;
        this.ano = ano;
        this.vencimento = vencimento;
        this.valor = valor;
    }

    public Mensalidade(Integer mes, Integer ano, String vencimento, Double valor) {
        this.mes = mes;
        this.ano = ano;
        setVencimentoFormatado(vencimento);
        this.valor = valor;
    }

    public Mensalidade(){ }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getMes() {
        return mes;
    }

    public void setMes(Integer mes) {
        this.mes = mes;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public String getMesAno(){
        return String.format(Locale.getDefault(), "%d/%d", mes, ano);
    }

    public void setMesAno(Integer mes, Integer ano){
        this.mes = mes;
        this.ano = ano;
    }

    public void setMesAno(String mesAno){
        String parteMesAno[] = mesAno.split("/");
        mes = Integer.valueOf(parteMesAno[0]);
        ano = Integer.valueOf(parteMesAno[1]);
    }

    public Date getVencimento() {
        return vencimento;
    }

    public String getVencimentoFormatado(){
        return formatador.format(vencimento);
    }

    public void setVencimento(Date vencimento) {
        this.vencimento = vencimento;
    }

    public void setVencimentoFormatado(String vencimento){
        try {
            this.vencimento = formatador.parse(vencimento);
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

    public boolean ehMesValido(String mes){
        return mes.matches("(0*[1-9]|1[0-2])");
    }

    public boolean ehVencimentoValido(String vencimento){
        return vencimento.split("/").length == 3;
    }

    public boolean ehAnoValido(String ano){
        return ano.matches("(199[0-9]|20[0-9][0-9])");
    }

    public boolean ehValorValido(Double valor){
        return valor > 0;
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(),
                "Mensalidade de %s", getMesAno(), valor);
    }

    @Override
    public int compareTo(@NonNull Mensalidade other) {
        if (this.getAno() < other.getAno() || this.getMes() < other.getMes()){
            return 1;
        } else if(this.getAno() > other.getAno() || this.getMes() > other.getMes()){
            return -1;
        }
        return 0;
    }
}
