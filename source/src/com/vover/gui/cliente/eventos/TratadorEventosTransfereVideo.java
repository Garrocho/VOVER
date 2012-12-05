package com.vover.gui.cliente.eventos;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.vover.gui.cliente.DialogoTransfereVideo;
import com.vover.gui.recursos.DialogoErro;
import com.vover.gui.recursos.DialogoSucesso;
import com.vover.recursos.ConexaoCliente;
import static com.vover.recursos.Recursos.*;

public class TratadorEventosTransfereVideo implements ActionListener {

	private DialogoTransfereVideo dialogoTransfereVideo;
	private ConexaoCliente conexaoCliente;

	public TratadorEventosTransfereVideo(DialogoTransfereVideo dialogoTransfereVideo) {
		super();
		this.dialogoTransfereVideo = dialogoTransfereVideo;
		this.conexaoCliente = new ConexaoCliente();
	}

	public void actionPerformed(ActionEvent evento) {

		// Caso o evento tenha ocorrido no botao abrir. Chama o metodo dialogoAbrirArquivo.
		if (evento.getSource() == dialogoTransfereVideo.getBotaoAbrir()) {

			String endereco = dialogoAbrirArquivo(dialogoTransfereVideo, "Selecione um video");
			if (endereco != null) 
				dialogoTransfereVideo.getCampoEnderecoVideo().setText(endereco);
		}

		else {

			String ERROS = "Os Seguintes Erros Foram Encontrados:";
			String nome = dialogoTransfereVideo.getCampoNome().getText();
			String descricao = dialogoTransfereVideo.getCampoDescricao().getText();
			String endereco = dialogoTransfereVideo.getCampoEnderecoVideo().getText();

			if (nome.length() < 5) {
				ERROS += "\nO nome do video deve ter no minimo 5 caracteres.";
			}

			if (descricao.length() < 5) {
				ERROS += "\nA descricao do video deve ter no minimo 5 caracteres.";
			}
			
			if (endereco.length() < 2) {
				ERROS += "\nVoce deve selecionar um video.";
			}

			// Verifica se nao existe erros.
			if (ERROS.equalsIgnoreCase("Os Seguintes Erros Foram Encontrados:")) {
				// Caso o evento tenha ocorrido no botao enviar.
				if (evento.getSource() == dialogoTransfereVideo.getBotaoEnviar()) {
					enviarArquivo(nome, descricao, endereco);
				}

				// Caso o evento tenha ocorrido no campo de texto nome.
				else if (evento.getSource() == dialogoTransfereVideo.getCampoNome()) {
					enviarArquivo(nome, descricao, endereco);
				}

				// Caso o evento tenha ocorrido no campo de texto descricao.
				else if (evento.getSource() == dialogoTransfereVideo.getCampoDescricao()) {
					enviarArquivo(nome, descricao, endereco);
				}
			}
			else
				new DialogoErro(dialogoTransfereVideo, "Erros", ERROS);
		}
	}

	private void enviarArquivo(String nome, String descricao, String endereco) {
		try {
			transfereArquivo(nome, descricao, endereco);
		} catch (HeadlessException e) {
		} catch (IOException e) {
		} catch (ClassNotFoundException e) {
		}
	}

	// Transfere um arquivo para o servidor.
	private void transfereArquivo(String nome, String descricao, String endereco) throws IOException, ClassNotFoundException {
		boolean SAIR = false;

		conexaoCliente.conectaServidor();

		// Envia o nome do video que sera pesquisado.
		conexaoCliente.getEnviaDados().writeObject("TRANSFERIR");
		conexaoCliente.getEnviaDados().flush();

		// Cria uma referencia ao ao arquivo em disco.
		File arquivoDisco = new File(endereco);  

		// Envia o nome do arquivo.
		conexaoCliente.getEnviaDados().writeObject(nome + ".Mjpeg");
		conexaoCliente.getEnviaDados().flush();

		// Obtem a descricao do video.
		String confirmacao = (String)conexaoCliente.getRecebeDados().readObject();

		// Verifica a resposta do servidor. Caso nao exista outro video cadastrado com o mesmo nome ele envia o video.
		if (confirmacao.equalsIgnoreCase("OK_AUTORIZADO")) {

			String[] mensagem = new String[] {dialogoTransfereVideo.getNomeUsuario(), descricao}; 
			conexaoCliente.getEnviaDados().writeObject(mensagem);
			conexaoCliente.getEnviaDados().flush();

			FileInputStream arquivoVideo = new FileInputStream(arquivoDisco);  
			byte[] buf = new byte[4096];

			// Envia o arquivo, enquanto nao tenha chegado no final do arquivo.
			while (SAIR != true) {  
				int tamanho = arquivoVideo.read(buf);  
				if (tamanho == -1) {
					SAIR = true;
					break;  
				}
				conexaoCliente.getEnviaDados().write(buf, 0, tamanho);  
			}
			dialogoTransfereVideo.dispose();
			new DialogoSucesso(dialogoTransfereVideo, "Sucesso", "Arquivo Enviado.");
			conexaoCliente.desconectaServidor();
			dialogoTransfereVideo.getJanelaPai().setVisible(true);
		}
		else {
			new DialogoErro(dialogoTransfereVideo, "Erro", "Ja Existe Um Arquivo com Esse Nome.");
			dialogoTransfereVideo.getCampoEnderecoVideo().setText("");
			conexaoCliente.desconectaServidor();
		}
	}

}
