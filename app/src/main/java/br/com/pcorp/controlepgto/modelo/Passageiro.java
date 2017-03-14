package br.com.pcorp.controlepgto.modelo;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Locale;

/**
 * Created by root on 11/03/17.
 */

public class Passageiro implements Serializable, Comparable<Passageiro> {
    private Long id;
    private String nome;
    private String celular;
    private String cidade;
    private String instituicaoEstudo;
    private String cpf;
    private String rg;
    private String tituloEleitor;

    public Passageiro(String nome, String celular, String cidade, String instituicaoEstudo) {
        this.nome = nome;
        this.celular = celular;
        this.cidade = cidade;
        this.instituicaoEstudo = instituicaoEstudo;

        cpf = "";
        rg = "";
        tituloEleitor = "";
    }

    public Passageiro(){ }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getInstituicaoEstudo() {
        return instituicaoEstudo;
    }

    public void setInstituicaoEstudo(String instituicaoEstudo) {
        this.instituicaoEstudo = instituicaoEstudo;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getTituloEleitor() {
        return tituloEleitor;
    }

    public void setTituloEleitor(String tituloEleitor) {
        this.tituloEleitor = tituloEleitor;
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(),
                "%s(%d): %s", nome, id, cidade);
    }

    @Override
    public int compareTo(@NonNull Passageiro other) {
        return this.getNome().compareToIgnoreCase(other.getNome());
    }
}
