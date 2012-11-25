package com.vover.servidor;

import static com.vover.servidor.requisicoes.TrataRequisicoesServidor.Mensagem;
import static com.vover.servidor.requisicoes.TrataRequisicoesServidor.alterarVideo;
import static com.vover.servidor.requisicoes.TrataRequisicoesServidor.cadastro;
import static com.vover.servidor.requisicoes.TrataRequisicoesServidor.deslogar;
import static com.vover.servidor.requisicoes.TrataRequisicoesServidor.excluirVideo;
import static com.vover.servidor.requisicoes.TrataRequisicoesServidor.logar;
import static com.vover.servidor.requisicoes.TrataRequisicoesServidor.pesquisaVideo;
import static com.vover.servidor.requisicoes.TrataRequisicoesServidor.recebeArquivo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.SQLException;

import com.vover.servidor.persistencia.BancoDadosVideo;
import com.vover.servidor.requisicoes.TrataRequisicoesVideo;

public class Servidor implements Runnable {

	private ObjectOutputStream enviaDados;
	private ObjectInputStream recebeDados;
	private Socket conexao;
	private boolean SAIR = false;
	private InetAddress ip_cliente;
	private int porta_cliente;

	public Servidor(Socket clientSocket, InetAddress ip, int porta) {
		this.conexao = clientSocket;
		this.ip_cliente = ip;
		this.porta_cliente = porta;
	}

	public void fecharConexao() throws IOException {
		enviaDados.close();
		recebeDados.close();
		conexao.close();
	}

	public void run() {

		try {
			//obtendo os fluxos de entrada e desaida.
			enviaDados = new ObjectOutputStream(conexao.getOutputStream());
			recebeDados = new ObjectInputStream(conexao.getInputStream());

			String requisicao;

			//obtendo a mensagem enviada pelo cliente
			requisicao = (String)recebeDados.readObject();
			
			// Caso o cliente tenha requisitado logar.
			if ( requisicao.equalsIgnoreCase("LOGAR"))
				logar( enviaDados, recebeDados, conexao.getInetAddress());

			// Caso o cliente tenha requisitado deslogar.
			else if ( requisicao.equalsIgnoreCase("DESLOGAR"))
				deslogar( enviaDados, recebeDados);

			// Caso o usuario tenha requisitado cadastrar.
			else if (requisicao.equalsIgnoreCase("CADASTRAR"))
				cadastro(enviaDados, recebeDados);
			
			// Caso o usuario tenha requisitado cadastrar.
			else if (requisicao.equalsIgnoreCase("MENSAGEM"))
				Mensagem(enviaDados, recebeDados);

			// Caso o usuario tenha requisitado transferir video.
			else if (requisicao.equalsIgnoreCase("TRANSFERIR"))
				recebeArquivo(enviaDados, recebeDados);

			// Caso o usuario tenha requisitado pesquisar video.
			else if (requisicao.equalsIgnoreCase("PESQUISAR"))
				pesquisaVideo(enviaDados, recebeDados);

			// Caso o usuario tenha requisitado alterar video.
			else if (requisicao.equalsIgnoreCase("ALTERAR"))
				alterarVideo(enviaDados, recebeDados);

			// Caso o usuario tenha requisitado excluir video.
			else if (requisicao.equalsIgnoreCase("EXCLUIR"))
				excluirVideo(enviaDados, recebeDados);

			// Caso o usuario tenha requisitado vizualizar.
			else if (requisicao.equalsIgnoreCase("VIZUALIZAR")) {
				SAIR = executaVideo();
			}

			if ( SAIR == true ) {
				fecharConexao();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	};

	public boolean executaVideo() throws IOException, SQLException, ClassNotFoundException {

		BancoDadosVideo bancoDadosVideo = new BancoDadosVideo();

		//obtendo o nome do video enviado pelo cliente
		String nome = (String)recebeDados.readObject();
		enviaDados.writeObject(bancoDadosVideo.descricaoVideo(nome));
		enviaDados.flush();

		TrataRequisicoesVideo trataVideo = new TrataRequisicoesVideo(conexao, ip_cliente, porta_cliente,  enviaDados, recebeDados);
		return trataVideo.criaProcessoVideo();
	}
}