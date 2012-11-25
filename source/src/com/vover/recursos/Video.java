package com.vover.recursos;

import java.io.Serializable;

public class Video implements Serializable {

	private static final long serialVersionUID = 1L;
	private String nome, descricao, endereco;

	public Video() {
		super();
	}

	public Video(String nome, String descricao, String endereco) {
		super();
		this.nome = nome;
		this.descricao = descricao;
		this.endereco = endereco;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

}
