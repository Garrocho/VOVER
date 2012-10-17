package com.vover.recursos;

import java.io.Serializable;

public class Requisicao implements Serializable {

	private static final long serialVersionUID = 1L;
	private String requisicao, nomevideo, protocolo, transporte;
	private int CSEQ, SECAO;

	public Requisicao() {
		super();
	}
	
	public Requisicao(String requisicao, String nomevideo, String protocolo, int cSEQ, String transporte) {
		super();
		this.requisicao = requisicao;
		this.nomevideo = nomevideo;
		this.protocolo = protocolo;
		CSEQ = cSEQ;
		this.transporte = transporte;
	}
	
	public Requisicao(String requisicao, String nomevideo, String protocolo, int cSEQ, int sECAO) {
		super();
		this.requisicao = requisicao;
		this.nomevideo = nomevideo;
		this.protocolo = protocolo;
		CSEQ = cSEQ;
		SECAO = sECAO;
	}
	
	public String getRequisicao() {
		return requisicao;
	}

	public String getNomevideo() {
		return nomevideo;
	}

	public String getProtocolo() {
		return protocolo;
	}

	public String getTransporte() {
		return transporte;
	}

	public int getCSEQ() {
		return CSEQ;
	}

	public int getSECAO() {
		return SECAO;
	}

	public String toString() {
		return requisicao + " " + nomevideo + " " + protocolo  + "\nCSEQ: " + CSEQ + "\n" + transporte + "\n";
	}
	
	public String toStringSECAO() {
		return requisicao + " " + nomevideo + " " + protocolo + "\nCSEQ: " + CSEQ + "\nSECAO: " + SECAO + "\n";
	}
}
