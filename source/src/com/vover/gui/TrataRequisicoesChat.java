package com.vover.gui;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;

public class TrataRequisicoesChat implements Runnable {

	private ObjectOutputStream enviaDados;
	private ObjectInputStream recebeDados;
	private Socket conexao;
	private JTable tabelaUsuario;
	private JTextArea areaTexto;
	private DefaultTableModel modeloTabela;

	public TrataRequisicoesChat(Socket clientSocket, JTable tabelaUsuario, JTextArea areaTexto ) {
		this.conexao = clientSocket;
		this.tabelaUsuario = tabelaUsuario;
		this.areaTexto = areaTexto;
	}

	public void run() {

		try {

			// obtendo os fluxos de entrada e saida.
			enviaDados = new ObjectOutputStream(conexao.getOutputStream());
			recebeDados = new ObjectInputStream(conexao.getInputStream());

			// obtendo a mensagem enviada pelo cliente.
			String[] mensagem = (String[])recebeDados.readObject();

			if ( mensagem[0].equalsIgnoreCase("ENTROU") ) {
				modeloTabela = (DefaultTableModel)tabelaUsuario.getModel();
				Object linha[] = new Object[1];
				linha[0] = mensagem[1].toUpperCase();
				modeloTabela.addRow(linha);
			}

			else if ( mensagem[0].equalsIgnoreCase("SAIU") ) {
				modeloTabela = (DefaultTableModel)tabelaUsuario.getModel();
				modeloTabela.removeRow(localizar(mensagem[1]));
			}

			else if ( mensagem[0].equalsIgnoreCase("MENSAGEM") ) 
				areaTexto.setText(mensagem[1].trim() + ": " + mensagem[2] + "\n" + areaTexto.getText());

			else {
				int qtde = mensagem.length;
				modeloTabela = (DefaultTableModel)tabelaUsuario.getModel();
				Object linha[] = new Object[qtde];

				for (int l = 0; l < qtde; l++)
					linha[l] = mensagem[l].toUpperCase();

				modeloTabela.addRow(linha);
			}
			fecharConexao();
		} catch(Exception e) {
			e.printStackTrace();
		}
	};
	

	public void fecharConexao() throws IOException {
		enviaDados.close();
		recebeDados.close();
		conexao.close();
	}

	public int localizar(String texto)  {  
		int tamanho = tabelaUsuario.getRowCount();  
		for (int linha = 0; linha < tamanho; linha++)  
		{    
			String tabela = (String)tabelaUsuario.getValueAt(linha, 0);    
			if (tabela.equalsIgnoreCase(texto))
				return linha; 
		}
		return 0;
	} 
}
