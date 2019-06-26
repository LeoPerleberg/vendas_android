package br.edu.ifsul.vendas.model;

import java.io.Serializable;

public class Cliente implements Serializable {
    private Long codigoDeBarras;
    private String cpf;
    private String nome;
    private String sobrenome;
    private String url_foto = "";
    private boolean situacao;
    private String key; //atributo apenas local

    public Cliente() {
    }

    public Long getCodigoDeBarras() {
        return codigoDeBarras;
    }

    public void setCodigoDeBarras(Long codigoDeBarras) {
        this.codigoDeBarras = codigoDeBarras;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getUrl_foto() {
        return url_foto;
    }

    public void setUrl_foto(String url_foto) {
        this.url_foto = url_foto;
    }

    public boolean isSituacao() {
        return situacao;
    }

    public void setSituacao(boolean situacao) {
        this.situacao = situacao;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public String toString() {
        return "Cliente{" +
                "codigoDeBarras=" + codigoDeBarras +
                ", cpf='" + cpf + '\'' +
                ", nome='" + nome + '\'' +
                ", sobrenome='" + sobrenome + '\'' +
                ", url_foto='" + url_foto + '\'' +
                ", situacao=" + situacao +
                ", key='" + key + '\'' +
                '}';
    }
}
