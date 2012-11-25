package com.vover.servidor.app;

import java.io.IOException;
import java.net.SocketException;

import com.vover.servidor.ServidorMultiCliente;
import com.vover.servidor.persistencia.BancoDados;

public class InicializaServidor {

	public static void main(String args[]) throws SocketException, IOException, Exception {

		// Inicializa o banco de dados e verifica de o banco existe.
		BancoDados setBanco = new BancoDados();
		setBanco.verificaBancoDados();
		ServidorMultiCliente servidor = new ServidorMultiCliente(5555);
		new Thread(servidor).start();
	}
}
