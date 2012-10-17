package com.vover.gui;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JTable;
import javax.swing.JTextArea;


public class ClienteServidor implements Runnable {

	protected int          portaServidor    = 6666;
	protected ServerSocket soqueteServidor  = null;
	protected Thread       processoServidor = null;
	protected boolean      SAIR             = false;
	protected JTable       tabelaUsuario;
	protected JTextArea    areaTexto;

	public ClienteServidor(int porta, JTable tabelaUsuario, JTextArea areaTexto) {
		this.portaServidor = porta;
		this.tabelaUsuario = tabelaUsuario;
		this.areaTexto = areaTexto;
	}

	public void run() {
		abrirSoqueteServidor();
		while(!SAIR) {
			Socket soqueteCliente = null;
			try {
				soqueteCliente = this.soqueteServidor.accept();
			} catch (IOException e) {
				System.out.println("Servidor Parou.");
			}
			new Thread( new TrataRequisicoesChat(soqueteCliente, tabelaUsuario, areaTexto)).start();
		}
		System.out.println("SAIU DO SERVIDOR DO CLIENTE");
	}

	private void abrirSoqueteServidor() {
		try {
			this.soqueteServidor = new ServerSocket(this.portaServidor);
		} catch (IOException e) {
			throw new RuntimeException("Nao Pode Abrir Porta " + portaServidor);
		}
	}
	
	public void fecharConexao() throws IOException {
		SAIR = true;
		soqueteServidor.close();
	}
	
}