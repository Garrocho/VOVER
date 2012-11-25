package com.vover.recursos;

import java.io.Serializable;

public class Resposta implements Serializable {

	private static final long serialVersionUID = 1L;
	private String transporte;
	private int confirmacao, CSEQ, SECAO;

	public Resposta() {
		super();
	}
	
	public Resposta(String transporte, int confirmacao, int cSEQ, int sECAO) {
		super();
		this.transporte = transporte;
		this.confirmacao = confirmacao;
		CSEQ = cSEQ;
		SECAO = sECAO;
	}

	public String getTransporte() {
		return transporte;
	}

	public void setTransporte(String transporte) {
		this.transporte = transporte;
	}

	public int getConfirmacao() {
		return confirmacao;
	}

	public void setConfirmacao(int confirmacao) {
		this.confirmacao = confirmacao;
	}

	public int getCSEQ() {
		return CSEQ;
	}

	public void setCSEQ(int cSEQ) {
		CSEQ = cSEQ;
	}

	public int getSECAO() {
		return SECAO;
	}

	public void setSECAO(int sECAO) {
		SECAO = sECAO;
	}

	public String toString() {
	return  transporte + " " + confirmacao +  " OK" + "\nCSEQ: " + CSEQ + "\n" + "SECAO: " + SECAO + "\n";
	}
}
