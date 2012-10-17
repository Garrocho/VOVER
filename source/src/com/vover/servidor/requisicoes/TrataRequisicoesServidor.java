package com.vover.servidor.requisicoes;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.sql.SQLException;

import com.vover.recursos.ConexaoCliente;
import com.vover.recursos.Video;
import com.vover.servidor.persistencia.BancoDadosUsuario;
import com.vover.servidor.persistencia.BancoDadosVideo;

public class TrataRequisicoesServidor {

	public static void logar(ObjectOutputStream enviaDados, ObjectInputStream recebeDados, InetAddress ip) throws IOException, ClassNotFoundException, SQLException, InterruptedException {

		BancoDadosUsuario bancoDadosUsuario = new BancoDadosUsuario();
		String mensagem[];

		//obtendo a mensagem enviada pelo cliente
		mensagem = (String[])recebeDados.readObject();

		if (bancoDadosUsuario.verificaNickUsuario(mensagem[0]) != 0) {

			if (bancoDadosUsuario.verificaUsuario(mensagem[0], mensagem[1]) != 0) {

				if (bancoDadosUsuario.verificaUsuarioOnline(mensagem[0]) == 0) {

					enviaDados.writeObject("OK_AUTORIZADO");

					int qtde = bancoDadosUsuario.qtdeUsuarioOnline();
					String[] ipUsuarios = bancoDadosUsuario.obterIpUsuarios(qtde);
					String[] nickUsuarios = bancoDadosUsuario.obterUsuariosOnline(qtde);
					String[] novaMensagem = new String[] {"ENTROU", mensagem[0]};

					if (qtde > 0) {
						Thread.sleep(1000);
						enviarMensagem(ip.toString(), nickUsuarios);
					}

					qtde--;
					while(qtde >= 0) {
						enviarMensagem(ipUsuarios[qtde], novaMensagem);
						qtde--;
					}
					bancoDadosUsuario.atualizaStatus(mensagem[0],ip.toString());
				}
				else
					enviaDados.writeObject("NAO_USUARIO_ONLINE");
			}
			else
				enviaDados.writeObject("OK_USUARIO_NAO_SENHA");
		}
		else
			enviaDados.writeObject("NAO_AUTORIZADO");
	}

	public static void deslogar(ObjectOutputStream enviaDados, ObjectInputStream recebeDados) throws IOException, ClassNotFoundException, SQLException {

		BancoDadosUsuario bancoDadosUsuario = new BancoDadosUsuario();
		String mensagem;

		//obtendo a mensagem enviada pelo cliente
		mensagem = (String)recebeDados.readObject();
		System.out.println(mensagem + "teste");
		bancoDadosUsuario.atualizaStatus(mensagem,"0");

		int qtde = bancoDadosUsuario.qtdeUsuarioOnline();
		String[] ipUsuarios = bancoDadosUsuario.obterIpUsuarios(qtde);
		String[] novaMensagem = new String[] {"SAIU", mensagem};

		qtde--;
		while(qtde >= 0) {
			enviarMensagem(ipUsuarios[qtde], novaMensagem);
			qtde--;
		}
	}

	// Caso seja um cadastro de usuario.
	public static void cadastro(ObjectOutputStream enviaDados, ObjectInputStream recebeDados) throws IOException, ClassNotFoundException, SQLException {

		BancoDadosUsuario bancoDadosUsuario = new BancoDadosUsuario();
		String mensagem[];

		//obtendo a mensagem enviada pelo cliente
		mensagem = (String[])recebeDados.readObject();

		// Verifica se ja nao existe um usuario cadastro com o mesmo nome.
		if (bancoDadosUsuario.verificaNickUsuario(mensagem[0]) == 0) {
			bancoDadosUsuario.adicionarUsuario(mensagem[0], mensagem[1]);
			enviaDados.writeObject("OK_AUTORIZADO");
		}
		else 
			enviaDados.writeObject("NAO_AUTORIZADO");
	}

	// Caso seja um cadastro de usuario.
	public static void Mensagem(ObjectOutputStream enviaDados, ObjectInputStream recebeDados) throws IOException, ClassNotFoundException, SQLException {

		BancoDadosUsuario bancoDadosUsuario = new BancoDadosUsuario();
		String mensagem[];

		//obtendo a mensagem enviada pelo cliente
		mensagem = (String[])recebeDados.readObject();
		String[] novaMensagem = new String[] {"MENSAGEM", mensagem[0], mensagem[1]};
		int qtde = bancoDadosUsuario.qtdeUsuarioOnline();

		String[] ipUsuarios = bancoDadosUsuario.obterIpUsuarios(qtde);
		qtde--;

		while(qtde >= 0) {
			enviarMensagem(ipUsuarios[qtde], novaMensagem);
			qtde--;
		}
	}

	public static void enviarMensagem(String endereco, String[] mensagem) throws IOException {
		ConexaoCliente conexao = new ConexaoCliente(endereco, 6666);
		conexao.conectaServidor();
		conexao.getEnviaDados().writeObject(mensagem);
		conexao.desconectaServidor();
	}

	public static void pesquisaVideo(ObjectOutputStream enviaDados, ObjectInputStream recebeDados) throws IOException, ClassNotFoundException, SQLException {

		Video[] videos = null;
		int qtdeVideo = 0;
		String[] mensagem = (String[])recebeDados.readObject();
		BancoDadosVideo bancoDadosVideo = new BancoDadosVideo();

		if (mensagem[0].equalsIgnoreCase("ANONIMO")) {

			// Faz uma busca no banco de dados verificando se nao ja existe um video com o mesmo nome.
			qtdeVideo = bancoDadosVideo.verificaNomeVideo(mensagem[1]);

			// verifica se o nome do video tem pelo menos um caracter.
			if ( mensagem[1].length() > 0) {

				// Obtem os dados do video e o armazena em uma composicao de videos.
				videos = bancoDadosVideo.obterDadosVideo(mensagem[1], qtdeVideo);

				// Envia a composicao de videos.
				enviaDados.writeObject((Object)videos);
			}
			else
				// Envia a composicao de videos.
				enviaDados.writeObject((Object)videos);
		}
		else {
			// Faz uma busca no banco de dados verificando se nao ja existe um video com o mesmo nome.
			qtdeVideo = bancoDadosVideo.verificaNomeVideoUsuario(mensagem[0], mensagem[1]);

			// verifica se o nome do video tem pelo menos um caracter.
			if ( mensagem[1].length() > 0) {

				// Obtem os dados do video e o armazena em uma composicao de videos.
				videos = bancoDadosVideo.obterDadosVideoUsuario(mensagem[0], mensagem[1], qtdeVideo);

				// Envia a composicao de videos.
				enviaDados.writeObject((Object)videos);
			}
			else
				// Envia a composicao de videos.
				enviaDados.writeObject((Object)videos);
		}
	}

	public static void alterarVideo(ObjectOutputStream enviaDados, ObjectInputStream recebeDados) throws IOException, ClassNotFoundException, SQLException {

		String[] video = (String[])recebeDados.readObject();

		BancoDadosVideo bancoDadosVideo = new BancoDadosVideo();
		String descricao = bancoDadosVideo.descricaoVideo(video[1]);
		String descricaoAnterior = bancoDadosVideo.descricaoVideo(video[0]);

		if (bancoDadosVideo.verificaNomeVideo(video[1]) != 0  &&  !descricao.equalsIgnoreCase(descricaoAnterior))
			enviaDados.writeObject("NAO_AUTORIZADO");

		else if (video[0].equalsIgnoreCase(video[1])) {
			bancoDadosVideo.atualizarVideo(video[1], video[2], video[1]);
			enviaDados.writeObject("OK_AUTORIZADO");
		}

		else {
			renomeiaArquivo(video[0], video[1]);
			bancoDadosVideo.atualizarVideo( video[0], video[1], video[2], video[1]);
			enviaDados.writeObject("OK_AUTORIZADO");
		}
	}

	public static void excluirVideo(ObjectOutputStream enviaDados, ObjectInputStream recebeDados) throws IOException, ClassNotFoundException, SQLException {

		String video = (String)recebeDados.readObject();
		BancoDadosVideo bancoDadosVideo = new BancoDadosVideo();

		File seraApagado = new File("Recursos/Videos/" + video + ".Mjpeg");  
		if(seraApagado.exists())  
			seraApagado.delete();
		bancoDadosVideo.excluirVideo(video + ".Mjpeg");
	}

	public static void recebeArquivo(ObjectOutputStream enviaDados, ObjectInputStream recebeDados) throws IOException, SQLException, ClassNotFoundException {  
		boolean SAIR = false;
		BancoDadosVideo bancoDadosVideo = new BancoDadosVideo();

		// Caminho que o arquivo sera gravado.
		String endereco = "Recursos/Videos/";

		// Obtendo o nome do arquivo.
		String nomeArquivo = (String)recebeDados.readObject();

		// Verifica se ja nao existe um video com o mesmo nome.
		if ( bancoDadosVideo.verificaNomeVideo(nomeArquivo) == 0 ) {

			// Envia uma mensagem que tudo esta correto.
			enviaDados.writeObject("OK_AUTORIZADO");

			// Recebe o nome do usuario e a descricao do video.
			String[] mensagem = (String[])recebeDados.readObject();

			endereco += nomeArquivo;

			// Cria um novo arquivo em disco com o endereco e o nome do arquivo.
			FileOutputStream novoArquivo = new FileOutputStream(endereco);  
			byte[] buf = new byte[4096];

			// Grava o arquivo, enquanto nao tenha chegado no final.
			while (SAIR != true) {
				int tamanho = recebeDados.read(buf);  
				if (tamanho == -1)  {
					SAIR = true;
					break;
				}
				// Escreve no arquivo em disco.
				novoArquivo.write(buf, 0, tamanho);  
			}             
			novoArquivo.flush();  
			novoArquivo.close();

			// Adiciona o novo video no banco de dados.
			bancoDadosVideo.adicionarVideo(mensagem[0], nomeArquivo, mensagem[1], endereco);
		}
		else
			enviaDados.writeObject("NAO_AUTORIZADO");
	}

	public static void renomeiaArquivo(String antigoNome, String novoNome) throws IOException {

		// Declara um objeto que pode ser diretorio ou arquivo
		File diretorio = new File("Recursos/Videos/");

		// Declara um objeto que pode ser diretorio ou arquivo
		File arquivo = new File(diretorio, antigoNome);

		// Renomeia o arquivo acima criado.
		arquivo.renameTo(new File(diretorio, novoNome));
	}
}
