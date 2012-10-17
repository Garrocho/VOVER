package com.vover.gui.usuario;

import static com.vover.recursos.Recursos.customizarBotao;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import com.vover.gui.usuario.eventos.TratadorEventosCadastro;
import com.vover.recursos.TamanhoMaximo;

public class DialogoCadastro extends JDialog {

	private static final long serialVersionUID = 1L;
	private Component janelaPai;
	private JTextField fieldNick;
	private JPasswordField fieldSenha;
	private JButton botaoCadastrar, botaoSair;

	public DialogoCadastro(Component janelaPai) throws IOException {

		super();
		setTitle("VIDEO OVER SERVER");
		this.janelaPai = janelaPai;
		TratadorEventosCadastro tratadorEvento = new TratadorEventosCadastro(this);

		JPanel painelNorte = new JPanel();

		ImageIcon imagem = new ImageIcon("Recursos//Icones//Imagens//logo_150.png");
		JLabel labelImagem = new JLabel(imagem);

		painelNorte.add(labelImagem);
		add(painelNorte, BorderLayout.NORTH);

		JPanel painelCentro = new JPanel(new GridLayout(2,1));
		JPanel painelNick = new JPanel();
		JPanel painelSenha = new JPanel();

		JLabel labelNick = new JLabel("NICK   ");
		labelNick.setFont(new Font(Font.DIALOG, Font.BOLD, 17));
		fieldNick = new JTextField(13);
		fieldNick.setPreferredSize(new Dimension(110, 30));
		fieldNick.setDocument(new TamanhoMaximo(15));
		fieldNick.setToolTipText("Descreva um nick que sera utilizado para efetuar login.");
		painelNick.add(labelNick);
		painelNick.add(fieldNick);

		painelCentro.add(painelNick);

		JLabel labelSenha = new JLabel("SENHA");
		labelSenha.setFont(new Font(Font.DIALOG, Font.BOLD, 17));

		fieldSenha = new JPasswordField(13);
		fieldSenha.setPreferredSize(new Dimension(110, 30));
		fieldSenha.setDocument(new TamanhoMaximo(15));
		fieldSenha.setToolTipText("Descreva uma senha de no minimo 6 caracteres e no maximo 15.");

		painelSenha.add(labelSenha);
		painelSenha.add(fieldSenha);

		painelCentro.add(painelSenha);
		add(painelCentro, BorderLayout.CENTER);

		JPanel painelSul = new JPanel();

		botaoCadastrar = new JButton("CADASTRAR");
		botaoCadastrar.setPreferredSize(new Dimension(140,50));
		botaoCadastrar.addActionListener(tratadorEvento);
		customizarBotao(botaoCadastrar);

		painelSul.add(botaoCadastrar);

		botaoSair = new JButton("SAIR");
		botaoSair.addActionListener(tratadorEvento);
		botaoSair.setPreferredSize(new Dimension(85,50));
		botaoSair.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		customizarBotao(botaoSair);

		painelSul.add(botaoSair);
		add(painelSul, BorderLayout.SOUTH);
		
		// Define as propriedades da janela.
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent arg0) {
				getThis().dispose();
				getJanelaPai().setVisible(true);
			}
		});
		pack();
		setIconImage(Toolkit.getDefaultToolkit().getImage("Recursos//Icones//Imagens//logo.png"));
		setLocationRelativeTo(janelaPai);
		janelaPai.setVisible(false);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setResizable(false);
		setVisible(true);
	}
	
	public DialogoCadastro getThis() {
		return this;
	}

	public JPasswordField getFieldSenha() {
		return fieldSenha;
	}

	public JButton getBotaoCadastrar() {
		return botaoCadastrar;
	}

	public JButton getbotaoSair() {
		return botaoSair;
	}

	public JTextField getFieldNick() {
		return fieldNick;
	}

	public Component getJanelaPai() {
		return janelaPai;
	}

}