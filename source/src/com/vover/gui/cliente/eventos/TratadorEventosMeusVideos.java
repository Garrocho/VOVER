package com.vover.gui.cliente.eventos;

import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.showMessageDialog;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.table.DefaultTableModel;

import com.vover.gui.cliente.DialogoMeusVideos;
import com.vover.recursos.ConexaoCliente;
import com.vover.recursos.Video;

public class TratadorEventosMeusVideos extends KeyAdapter implements ActionListener, MouseListener {

	private DialogoMeusVideos dialogoMeusVideos;
	private DefaultTableModel modeloTabela;
	private ConexaoCliente conexaoCliente;
	private Point click;
	private String nomeAnterior, nome, descricao;

	public TratadorEventosMeusVideos(DialogoMeusVideos dialogoMeusVideos) {
		super();
		this.dialogoMeusVideos = dialogoMeusVideos;
		this.conexaoCliente = new ConexaoCliente();
	}

	public void actionPerformed(ActionEvent evento) {

		nome = dialogoMeusVideos.getCampoNome().getText();
		descricao = dialogoMeusVideos.getCampoDescricao().getText();

		if ( evento.getSource() == dialogoMeusVideos.getBotaoSair() ) {
			dialogoMeusVideos.dispose();
		}

		else if ( evento.getSource() == dialogoMeusVideos.getBotaoLimpar() ) {
			limparDados();
		}

		else if ( evento.getSource() == dialogoMeusVideos.getBotaoGravar() ) {

			if ( !temErro() ) {
				try {
					if (conexaoCliente.conectaServidor()) {
						conexaoCliente.getEnviaDados().writeObject("ALTERAR");
						conexaoCliente.getEnviaDados().flush();

						String[] video = new String[] {nomeAnterior + ".Mjpeg", nome + ".Mjpeg", descricao};

						conexaoCliente.getEnviaDados().writeObject(video);
						conexaoCliente.getEnviaDados().flush();

						String resposta = (String)conexaoCliente.getRecebeDados().readObject();
						conexaoCliente.desconectaServidor();
						if (resposta.equalsIgnoreCase("OK_AUTORIZADO")) {
							limparDados();
							dialogoMeusVideos.getCampoPesquisa().setText("%");
							pesquisaPorNome();
						}
						else {
							showMessageDialog(dialogoMeusVideos, "Ja existe um video cadastrado com esse nome.", "Erros", ERROR_MESSAGE);
						}
					}
					else
						showMessageDialog(dialogoMeusVideos, "O Servidor Está Desligado.", "Erro ao Conectar...", ERROR_MESSAGE);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}

		else if ( evento.getSource() == dialogoMeusVideos.getMenuAlterar() ) {
			dialogoMeusVideos.getCampoNome().setEditable(true);
			dialogoMeusVideos.getCampoDescricao().setEditable(true);
			int coluna = dialogoMeusVideos.getTabelaVideo().rowAtPoint(click);
			nomeAnterior = dialogoMeusVideos.getTabelaVideo().getValueAt(coluna, 0).toString();
			dialogoMeusVideos.getCampoNome().setText(nomeAnterior);
			dialogoMeusVideos.getCampoDescricao().setText(dialogoMeusVideos.getTabelaVideo().getValueAt(coluna, 1).toString());
		}

		else if ( evento.getSource() == dialogoMeusVideos.getMenuExcluir() ) {

			int coluna = dialogoMeusVideos.getTabelaVideo().rowAtPoint(click);
			nomeAnterior = dialogoMeusVideos.getTabelaVideo().getValueAt(coluna, 0).toString();

			if (conexaoCliente.conectaServidor()) {
				try {
					conexaoCliente.getEnviaDados().writeObject("EXCLUIR");
					conexaoCliente.getEnviaDados().flush();

					conexaoCliente.getEnviaDados().writeObject(nomeAnterior);
					conexaoCliente.getEnviaDados().flush();

					conexaoCliente.desconectaServidor();
					limparDados();
					dialogoMeusVideos.getCampoPesquisa().setText("%");
					pesquisaPorNome();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else 
				showMessageDialog(dialogoMeusVideos, "O Servidor Está Desligado.", "Erro ao Conectar...", ERROR_MESSAGE);
		}
	}

	public void keyReleased(KeyEvent event) {
		pesquisaPorNome();
	}

	public boolean temErro() {

		String ERROS = "Os Seguintes Erros Foram Encontrados:";

		if (nome.length() < 5) {
			ERROS += "\nO nome do video deve ter no minimo 5 caracteres.";
		}

		if (descricao.length() < 5) {
			ERROS += "\nA descricao do video deve ter no minimo 5 caracteres.";
		}

		// Verifica se nao existe erros.
		if (ERROS.equalsIgnoreCase("Os Seguintes Erros Foram Encontrados:")) {
			return false;
		}
		else {
			showMessageDialog(dialogoMeusVideos, ERROS, "Erros", ERROR_MESSAGE);
			return true;
		}
	}

	private void limparDados() {
		dialogoMeusVideos.getCampoNome().setText("");
		dialogoMeusVideos.getCampoNome().setEditable(false);
		dialogoMeusVideos.getCampoDescricao().setText("");
		dialogoMeusVideos.getCampoDescricao().setEditable(false);
		dialogoMeusVideos.getCampoPesquisa().setText("");
		pesquisaPorNome();
	}

	private void pesquisaPorNome() {

		String nome = dialogoMeusVideos.getCampoPesquisa().getText();
		Video videos[] = null;

		// Obtem a referencia da tabela de videos. Seta a numero de linhas data tabela de videos com 0.
		modeloTabela = ((DefaultTableModel)(dialogoMeusVideos.getTabelaVideo().getModel()));
		modeloTabela.setNumRows(0);

		// Adiciona a variavel "%" para pesquisar todos os nomes a partir das letras ja inseridas.
		if (nome.length() != 0) {

			nome += "%";
			if (conexaoCliente.conectaServidor()) {
				try {

					// Envia ao servidor o comando de PESQUISAR.
					conexaoCliente.getEnviaDados().writeObject("PESQUISAR");
					conexaoCliente.getEnviaDados().flush();

					String[] mensagem = new String[] {dialogoMeusVideos.getNomeUsuario(), nome};

					// Envia o nome do video que sera pesquisado.
					conexaoCliente.getEnviaDados().writeObject(mensagem);
					conexaoCliente.getEnviaDados().flush();

					// Recebe uma composicao de videos do servidor.
					videos = (Video[])conexaoCliente.getRecebeDados().readObject();

					conexaoCliente.desconectaServidor();

					// Verifica se a composicao de videos e diferente de null. Se sim joga os videos na tabela.
					if (videos != null ) {
						Object[] linha = new Object[2];
						for (Video video : videos) {
							linha[0] = video.getNome().substring(0,video.getNome().indexOf("."));
							linha[1] = video.getDescricao();
							modeloTabela.addRow(linha);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else 
				showMessageDialog(dialogoMeusVideos, "O Servidor Está Desligado.", "Erro ao Conectar...", ERROR_MESSAGE);
		}
	}

	public void mouseClicked(MouseEvent evento) {
		if ( evento.getButton() == MouseEvent.BUTTON3) {
			if (evento.getSource() == dialogoMeusVideos.getTabelaVideo()) {
				dialogoMeusVideos.getPopMenu().show( evento.getComponent(), evento.getX(), evento.getY() );
				click = new Point(evento.getX(), evento.getY());
			}
		}
	}

	public void mousePressed(MouseEvent evento) {
	}
	public void mouseEntered(MouseEvent e) {
	}
	public void mouseExited(MouseEvent e) {
	}
	public void mouseReleased(MouseEvent e) {
	}
}
