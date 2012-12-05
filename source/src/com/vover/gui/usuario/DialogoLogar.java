package com.vover.gui.usuario;

import static com.vover.recursos.Recursos.customizarBotao;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import com.vover.gui.Janela;
import com.vover.gui.recursos.TamanhoMaximo;
import com.vover.gui.usuario.eventos.TratadorEventosLogar;

public class DialogoLogar extends Janela {

	private static final long serialVersionUID = 1L;
	private JTextField fieldLogar;
	private JPasswordField fieldSenha;
	private JButton botaoLogar, botaoCadastrar;

	public DialogoLogar(JFrame janelaPai) throws IOException {

		super("VIDEO OVER SERVER");
		TratadorEventosLogar tratadorEvento = new TratadorEventosLogar(this);

		JPanel painelNorte = new JPanel();

		ImageIcon imagem = new ImageIcon("Recursos//Icones//Imagens//logo_150.png");
		JLabel labelImagem = new JLabel(imagem);

		painelNorte.add(labelImagem);
		add(painelNorte, BorderLayout.NORTH);

		JPanel painelCentro = new JPanel(new GridLayout(2,1));

		JLabel labelLogar = new JLabel("LOGIN   ");
		labelLogar.setFont(new Font(Font.DIALOG, Font.BOLD, 17));
		fieldLogar = new JTextField(11);
		fieldLogar.setPreferredSize(new Dimension(110, 30));
		fieldLogar.setDocument(new TamanhoMaximo(15));
		fieldLogar.setToolTipText("Informe o nick do usuario.");

		JPanel painelLogar = new JPanel();
		JPanel painelSenha = new JPanel();

		painelLogar.add(labelLogar);
		painelLogar.add(fieldLogar);

		painelCentro.add(painelLogar);

		JLabel labelSenha = new JLabel("SENHA  ");
		labelSenha.setFont(new Font(Font.DIALOG, Font.BOLD, 17));

		fieldSenha = new JPasswordField(11);
		fieldSenha.setPreferredSize(new Dimension(110, 30));
		fieldSenha.setDocument(new TamanhoMaximo(15));
		fieldSenha.setToolTipText("Informe a senha do usuario.");

		painelSenha.add(labelSenha);
		painelSenha.add(fieldSenha);

		painelCentro.add(painelSenha);
		add(painelCentro, BorderLayout.CENTER);

		JPanel painelSul = new JPanel();

		botaoLogar = new JButton("LOGIN");
		botaoLogar.setPreferredSize(new Dimension(95,50));
		botaoLogar.addActionListener(tratadorEvento);
		customizarBotao(botaoLogar);
		
		painelSul.add(botaoLogar);
		
		botaoCadastrar = new JButton("CADASTRO");
		botaoCadastrar.addActionListener(tratadorEvento);
		botaoCadastrar.setPreferredSize(new Dimension(130,50));
		customizarBotao(botaoCadastrar);
		
		painelSul.add(botaoCadastrar);
		add(painelSul, BorderLayout.SOUTH);

		// Define as propriedades da janela.
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent arg0) {
				getThis().dispose();
			}
		});
		pack();
		setIconImage(Toolkit.getDefaultToolkit().getImage("Recursos//Icones//Imagens//logo.png"));
		setLocationRelativeTo(janelaPai);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setResizable(false);
		setVisible(true);
	}
	
	public DialogoLogar getThis() {
		return this;
	}

	public JTextField getFieldLogar() {
		return fieldLogar;
	}

	public JPasswordField getFieldSenha() {
		return fieldSenha;
	}

	public JButton getBotaoLogar() {
		return botaoLogar;
	}

	public JButton getbotaoCadastrar() {
		return botaoCadastrar;
	}
}