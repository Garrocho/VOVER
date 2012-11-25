package com.vover.recursos;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.FormatterClosedException;

public class ConexaoCliente {

	private ObjectOutputStream enviaDados;
	private ObjectInputStream recebeDados;

	private Socket conexao;
	private int PORTA = 5555;
	private String ENDERECO = "10.3.3.14";
	
	public void configurar() throws FormatterClosedException, IllegalStateException, IOException {
		ArquivoTexto arquivo = new ArquivoTexto();
		arquivo.abrir("Recursos/configuracoes.txt");
		String texto = arquivo.ler();
		texto = texto.substring(texto.indexOf(":") + 2, texto.length()-1);
		arquivo.fechar();
		ENDERECO = texto;
	}

	public ConexaoCliente() {
		super();
		try {
			this.configurar();
		} catch (Exception e) {
		}
	}

	public ConexaoCliente(String endereco, int porta) {
		super();
		this.ENDERECO = endereco.substring(endereco.indexOf("/") + 1 );
		this.PORTA = porta;
	}

	public Boolean conectaServidor() {

		try {
			InetAddress endereco = InetAddress.getByName(ENDERECO);
			conexao = new Socket(endereco, PORTA);
			enviaDados = new ObjectOutputStream(conexao.getOutputStream());
			recebeDados = new ObjectInputStream(conexao.getInputStream());
			return true;
		}catch (Exception e) {
			return false;
		}
	}

	public void desconectaServidor() throws IOException {

		recebeDados.close();
		enviaDados.close();
		conexao.close();
	}

	// Enviando uma REQUISICAO RTSP ao servidor.
	public void enviaRequisicao(String requisicao, String protocolo, String nomeVideo, int seqNb, int idNb) {

		try {

			// REQUISICAO / NOME VIDEO/ RSTP / CSEQ / TRANSPORTE
			Requisicao MSN_REQUISICAO = new Requisicao(requisicao, nomeVideo, protocolo, seqNb, idNb);

			System.out.println(MSN_REQUISICAO.toStringSECAO());

			// ENVIA A MENSAGEM DE REQUISICAO AO SERVIDOR.
			enviaDados.writeObject(MSN_REQUISICAO);
			enviaDados.flush();
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	public ObjectOutputStream getEnviaDados() {
		return enviaDados;
	}

	public ObjectInputStream getRecebeDados() {
		return recebeDados;
	}

	public Socket getConexao() {
		return conexao;
	}

	public int getPORTA() {
		return PORTA;
	}

	public String getENDERECO() {
		return ENDERECO;
	}

}
