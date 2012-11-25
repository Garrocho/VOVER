package com.vover.servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorMultiCliente implements Runnable {

	protected int          portaServidor    = 5555;
	protected ServerSocket soqueteServidor  = null;
	protected boolean      isStopped        = false;
	protected Thread       processoServidor = null;

	public ServidorMultiCliente(int porta){
		this.portaServidor = porta;
	}

	public void run() { 
		synchronized(this) { this.processoServidor = Thread.currentThread(); }
		abrirSoqueteServidor();
		System.out.println("Servidor Iniciou.");
		while(! isStopped()){
			Socket soqueteCliente = null;
			try {
				soqueteCliente = this.soqueteServidor.accept();
			} catch (IOException e) {
				System.out.println("Servidor Parou.");
			}
			new Thread( new Servidor(soqueteCliente, soqueteCliente.getInetAddress(), soqueteCliente.getLocalPort()) ).start();
		}
		System.out.println("Servidor Parou.") ;
	}

	private synchronized boolean isStopped() {
		return this.isStopped;
	}

	private void abrirSoqueteServidor() {
		try {
			this.soqueteServidor = new ServerSocket(this.portaServidor);
		} catch (IOException e) {
			throw new RuntimeException("Nao Pode Abrir Porta " + portaServidor);
		}
	}

}