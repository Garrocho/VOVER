package com.vover.gui.cliente;

import static com.vover.recursos.Recursos.customizarBotao;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.vover.gui.cliente.eventos.TratadorEventosTransfereVideo;
import com.vover.recursos.TamanhoMaximo;

public class DialogoTransfereVideo extends JDialog {

	private static final long serialVersionUID = 1L;
	private JFrame janelaPai;
	private JTextField campoNome;
	private JTextField campoDescricao;
	private JTextField campoEnderecoVideo;
	private JButton botaoEnviar;
	private JButton botaoAbrir;
	private String nomeUsuario;
	
	public DialogoTransfereVideo(JFrame janelaPai, String nomeUsuario) throws IOException {

		super(janelaPai, "Transferencia de Video");
		this.janelaPai = janelaPai;
		this.nomeUsuario = nomeUsuario;
		TratadorEventosTransfereVideo tratadorEventos = new TratadorEventosTransfereVideo(this);

		JPanel painelNorte = new JPanel();
		JPanel painelCentro = new JPanel();
		JPanel painelSul = new JPanel();

		JLabel labelNome = new JLabel(" NOME        ");
		labelNome.setFont(new Font(Font.DIALOG, Font.BOLD, 17));
		painelNorte.add(labelNome);

		campoNome = new JTextField(21);
		campoNome.addActionListener(tratadorEventos);
		campoNome.setPreferredSize(new Dimension(500, 32));
		campoNome.setToolTipText("Descreva um nome para o video que sera carregado.");
		campoNome.setDocument(new TamanhoMaximo(25));
		painelNorte.add(campoNome);

		botaoAbrir = new JButton(new ImageIcon("Recursos//Icones//Botoes//pasta.png"));
		botaoAbrir.addActionListener(tratadorEventos);
		botaoAbrir.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		botaoAbrir.setBorder(null);
		painelNorte.add(botaoAbrir);

		add(painelNorte, BorderLayout.NORTH);

		JLabel labelDescricao = new JLabel("DESCRICAO");
		labelDescricao.setFont(new Font(Font.DIALOG, Font.BOLD, 17));
		painelCentro.add(labelDescricao);

		campoDescricao = new JTextField(24);
		campoDescricao.setPreferredSize(new Dimension(500, 32));
		campoDescricao.setToolTipText("Descreva uma descricao para o video que sera carregado.");
		campoDescricao.setDocument(new TamanhoMaximo(50));
		painelCentro.add(campoDescricao);

		add(painelCentro, BorderLayout.CENTER);

		botaoEnviar = new JButton("ENVIAR");
		customizarBotao(botaoEnviar);
		botaoEnviar.setPreferredSize(new Dimension(107,32));
		botaoEnviar.addActionListener(tratadorEventos);
		botaoEnviar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		painelSul.add(botaoEnviar);

		campoEnderecoVideo = new JTextField(24);
		campoEnderecoVideo.setToolTipText("Endereco do video que sera carregado.");
		campoEnderecoVideo.setEditable(false);
		campoEnderecoVideo.setPreferredSize(new Dimension(500, 32));
		painelSul.add(campoEnderecoVideo);

		add(painelSul, BorderLayout.SOUTH);

		// Definindo as propriedades da janela.
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent arg0) {
				getThis().dispose();
				getJanelaPai().setVisible(true);
			}
		});
		pack();
		setModal(true);
		setIconImage(Toolkit.getDefaultToolkit().getImage("Recursos//Icones//icone.png"));
		setLocationRelativeTo(janelaPai);
		setResizable(false);
		janelaPai.setVisible(false);
		setVisible(true);
	}
	
	public DialogoTransfereVideo getThis(){
		return this;
	}
	
	public JFrame getJanelaPai() {
		return janelaPai;
	}

	public JTextField getCampoEnderecoVideo() {
		return campoEnderecoVideo;
	}

	public JButton getBotaoAbrir() {
		return botaoAbrir;
	}

	public JTextField getCampoNome() {
		return campoNome;
	}

	public JTextField getCampoDescricao() {
		return campoDescricao;
	}

	public JButton getBotaoEnviar() {
		return botaoEnviar;
	}

	public String getNomeUsuario() {
		return nomeUsuario;
	}
}