package com.vover.gui.usuario.eventos;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import com.vover.gui.recursos.DialogoErro;
import com.vover.gui.recursos.DialogoSucesso;
import com.vover.gui.usuario.DialogoCadastro;
import com.vover.recursos.ConexaoCliente;

public class TratadorEventosCadastro implements ActionListener {

	private DialogoCadastro dialogoCadastro;
	private ConexaoCliente conexaoCliente;

	public TratadorEventosCadastro(DialogoCadastro dialogoCadastro) {
		super();
		this.dialogoCadastro = dialogoCadastro;
		this.conexaoCliente = new ConexaoCliente();
	}

	@SuppressWarnings("deprecation")
	public void actionPerformed(ActionEvent evento) {

		// Caso o evento tenha ocorrido no botao cancelar.
		if (evento.getSource() == dialogoCadastro.getbotaoSair()) {
			dialogoCadastro.dispose();
		}

		// Caso o evento tenha ocorrido no botao cadastrar.
		else if (evento.getSource() == dialogoCadastro.getBotaoCadastrar()) {

			String ERROS = "Os Seguintes Erros Foram Encontrados:";
			String nick = dialogoCadastro.getFieldNick().getText();
			String senha = dialogoCadastro.getFieldSenha().getText();

			if (nick.length() < 6)
				ERROS += "\nDescreva Melhor o Nick.";
			if (senha.length() < 6)
				ERROS += "\nDescreva Melhor a Senha.";

			// Verifica se nao existe erros.
			if (!ERROS.equalsIgnoreCase("Os Seguintes Erros Foram Encontrados:"))
				new DialogoErro(dialogoCadastro, "Erros", ERROS);

			else {

				try {
					String resposta = cadastrarCliente(nick, senha);
					if (resposta.equalsIgnoreCase("NAO_AUTORIZADO")) {
						new DialogoErro(dialogoCadastro, "Erro", "Ja Existe um Usuario com esse Nick Cadastrado.");
					}
					
					else if (resposta.equalsIgnoreCase("OFFLINE")) {
						new DialogoErro(dialogoCadastro, "Erro ao Conectar...", "O Servidor Esta Desligado.");
					}
					else {
						new DialogoSucesso(dialogoCadastro, "Sucesso", "Usuario Cadastrado.");
						dialogoCadastro.dispose();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private String cadastrarCliente(String nick, String senha) throws IOException, ClassNotFoundException {

		String mensagem[] = new String[] {nick, senha};
		String resposta = "OFFLINE";

		if (conexaoCliente.conectaServidor()) {

			conexaoCliente.getEnviaDados().writeObject("CADASTRAR");
			conexaoCliente.getEnviaDados().flush();

			conexaoCliente.getEnviaDados().writeObject(mensagem);
			conexaoCliente.getEnviaDados().flush();

			// Lendo a mensagem enviada pelo Cliente.
			resposta = (String) conexaoCliente.getRecebeDados().readObject();

			conexaoCliente.desconectaServidor();
		}

		return resposta;
	}

}
