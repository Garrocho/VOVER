package com.vover.gui.cliente;

import static com.vover.recursos.Recursos.adicionaImagem;
import static com.vover.recursos.Recursos.customizarBotao;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;

import com.vover.gui.ClienteServidor;
import com.vover.gui.Janela;
import com.vover.gui.cliente.eventos.TratadorEventosCliente;
import com.vover.gui.eventos.TratadorControleTempoCliente;
import com.vover.gui.usuario.DialogoLogar;
import com.vover.recursos.TamanhoMaximo;

public class JanelaCliente extends Janela { 

	private static final long serialVersionUID = 1L;
	private JTextField campoPesquisa;
	private JTable tabelaVideo;
	private JButton botaoExecutar, botaoPausar, botaoParar, botaoForward, botaoRewind, botaConfig;
	private JPopupMenu popMenu;
	private JMenuItem menuSair, menuTransferencia, menuMeusVideos;
	private String usuario;
	private JTextArea areaTexto;
	private JTable tabelaUsuario;
	private JTextField campoTexto;
	private JButton botaoEnviar;
	private ClienteServidor clienteServidor;

	public JanelaCliente(DialogoLogar dialogoLogar, String usuario) {

		super("VIDEO OVER SERVER");
		final TratadorEventosCliente tratadorEventos = new TratadorEventosCliente(this);
		this.usuario = usuario.toUpperCase();
		
		JPanel painelTotalChat = new JPanel(new GridBagLayout());
		JPanel painelChat = new JPanel();
		JPanel painelChatBotoes = new JPanel();
		
		JPanel painelNorte = new JPanel();
		JPanel painelCentro = new JPanel();
		JPanel painelVideo = new JPanel(new GridBagLayout());
		JPanel painelBotoes = new JPanel();
		JPanel painelVideoBotoes = new JPanel(new GridBagLayout());
		painelVideoBotoes.setBackground(Color.DARK_GRAY);
		painelBotoes.setBackground(Color.DARK_GRAY);
		JPanel painelTabela = new JPanel(new GridBagLayout());

		areaTexto = new JTextArea("",8, 34);
		areaTexto.setEditable(false);
		
		// Cria uma barra de rolagem para a tabela de videos.
		JScrollPane barraRolagemTexto = new JScrollPane(areaTexto);
		painelChat.add(barraRolagemTexto);

		String colunasTabela[] = {"USUARIOS ONLINE"},
		dadosTabela[][] = new String[0][0];

		tabelaUsuario = new JTable(new DefaultTableModel(dadosTabela, colunasTabela)){
			private static final long serialVersionUID = 5727320816550514929L;
			public boolean isCellEditable(int rowIndex, int colIndex) {
				if (colIndex == getColumn("USUARIOS ONLINE").getModelIndex() ){
					return false; // evita a edicao das celulas
				}
				else
					return true;
			}
		};
		tabelaUsuario.setPreferredScrollableViewportSize(new Dimension(155, 93));
		tabelaUsuario.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		tabelaUsuario.setForeground(Color.WHITE);
		
		DefaultTableModel modeloTabela = (DefaultTableModel)tabelaUsuario.getModel();
		Object linha[] = new Object[1];
		linha[0] = this.usuario.toUpperCase();
		modeloTabela.addRow(linha);

		// Cria uma barra de rolagem para a tabela de videos.
		JScrollPane barraRolagemChat = new JScrollPane(tabelaUsuario);

		painelChat.add(barraRolagemChat);

		GridBagConstraints constantes = new GridBagConstraints();
		constantes.anchor = GridBagConstraints.FIRST_LINE_START;
		constantes.gridx = 0;
		constantes.gridy = 0;
		painelTotalChat.add(painelChat,constantes);

		campoTexto = new JTextField(34);
		campoTexto.setDocument(new TamanhoMaximo(43));
		campoTexto.setPreferredSize(new Dimension(110,35));
		campoTexto.addActionListener(tratadorEventos);
		painelChatBotoes.add(campoTexto);

		botaoEnviar = new JButton("ENVIAR");
		customizarBotao(botaoEnviar);
		botaoEnviar.setPreferredSize(new Dimension(165,40));
		botaoEnviar.addActionListener(tratadorEventos);
		painelChatBotoes.add(botaoEnviar);

		add(painelChatBotoes, BorderLayout.SOUTH);
		
		constantes.anchor = GridBagConstraints.FIRST_LINE_START;
		constantes.gridx = 0;
		constantes.gridy = 1;
		painelTotalChat.add(painelChatBotoes,constantes);
		
		add(painelTotalChat, BorderLayout.SOUTH);
		
		// Adiciona o labelPesquisa, o campo de pesquisa e o botao pesquisar no painelNorte.
		JLabel VOVER = new JLabel(new ImageIcon("Recursos//Icones//Imagens//vover.png"));
		painelNorte.add(VOVER);

		campoPesquisa = new JTextField(20);
		campoPesquisa.setToolTipText("Descreva o nome do video.");
		campoPesquisa.addKeyListener(tratadorEventos);
		campoPesquisa.setPreferredSize(new Dimension(500, 32));
		campoPesquisa.setDocument(new TamanhoMaximo(25));
		painelNorte.add(campoPesquisa);
		
		popMenu = new JPopupMenu();
		
		botaConfig = new JButton(new ImageIcon("Recursos//Icones//Botoes//usuario.png"));
		botaConfig.setBorder(null);
		botaConfig.addMouseListener(tratadorEventos);
		
		while(usuario.length() < 23)
			usuario += " ";
		botaConfig.setText(usuario.toUpperCase());
		
		menuMeusVideos = new JMenuItem("MEUS VIDEOS");
		menuMeusVideos.addActionListener(tratadorEventos);
		
		menuTransferencia = new JMenuItem("UPLOAD");
		menuTransferencia.addActionListener(tratadorEventos);
		
		menuSair = new JMenuItem("SAIR");
		menuSair.addActionListener(tratadorEventos);
		
		popMenu.add(menuMeusVideos);  
		popMenu.add(menuTransferencia);
		popMenu.add(menuSair);
		
		painelNorte.add(botaConfig);
		
		// Adiciona o painelNorte na regiao norte do Dialogo Consultar Candidato.
		add(painelNorte, BorderLayout.NORTH);
		
		GridBagConstraints c = new GridBagConstraints();
		GridBagConstraints c1 = new GridBagConstraints();
		GridBagConstraints c2 = new GridBagConstraints();

		setNomeVideo(new JLabel(" "));
		getNomeVideo().setFont(new Font(Font.DIALOG, Font.BOLD, 20));

		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.gridx = 0;
		c.gridy = 0;
		painelVideo.add(getNomeVideo(),c);
		
		setControleTempo(new Timer(60, new TratadorControleTempoCliente(this)));
		getControleTempo().setInitialDelay(0);
		getControleTempo().setCoalesce(true);

		// Rotulo de exibicao de imagem:
		ImageIcon imagem = new ImageIcon("Recursos//Icones//Player//tela.png");
		imagem.setImage(imagem.getImage().getScaledInstance(384, 288, Image.SCALE_DEFAULT));
		setLabelIcone(new JLabel(imagem));
		getLabelIcone().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		getLabelIcone().addMouseListener(tratadorEventos);

		c1.gridx = 0;
		c1.gridy = 0;
		painelVideoBotoes.add(getLabelIcone(),c1);
		
		botaoExecutar = new JButton(new ImageIcon(adicionaImagem("Recursos//Icones//Player//play.png")));
		botaoExecutar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		botaoExecutar.setBackground(Color.BLACK);
		botaoPausar = new JButton(new ImageIcon(adicionaImagem("Recursos//Icones//Player//pause.png")));
		botaoPausar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		botaoPausar.setBackground(Color.BLACK);
		botaoParar = new JButton(new ImageIcon(adicionaImagem("Recursos//Icones//Player//stop.png")));
		botaoParar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		botaoParar.setBackground(Color.BLACK);
		botaoRewind = new JButton(new ImageIcon(adicionaImagem("Recursos//Icones//Player//rewind.png")));
		botaoRewind.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		botaoRewind.setBackground(Color.BLACK);
		botaoForward = new JButton(new ImageIcon(adicionaImagem("Recursos//Icones//Player//forward.png")));
		botaoForward.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		botaoForward.setBackground(Color.BLACK);

		painelBotoes.add(botaoExecutar);
		painelBotoes.add(botaoPausar);
		painelBotoes.add(botaoForward);
		painelBotoes.add(botaoRewind);
		painelBotoes.add(botaoParar);

		botaoExecutar.addActionListener(tratadorEventos);
		botaoPausar.addActionListener(tratadorEventos);
		botaoForward.addActionListener(tratadorEventos);
		botaoRewind.addActionListener(tratadorEventos);
		botaoParar.addActionListener(tratadorEventos);

		c1.gridx = 0;
		c1.gridy = 1;
		painelVideoBotoes.add(painelBotoes,c1);

		c.gridx = 0;      
		c.gridy = 1;      
		painelVideo.add(painelVideoBotoes,c);
		
		setDescricao(new JLabel(" "));
		getDescricao().setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		getDescricao().setBackground(Color.WHITE);
		c.gridx = 0;      
		c.gridy = 2; 
		painelVideo.add(getDescricao(),c);

		setDescricaoVideo(new JLabel(" "));
		c.gridx = 0;      
		c.gridy = 3; 
		painelVideo.add(getDescricaoVideo(),c);

		painelCentro.add(painelVideo);

		String colunasTabelaVideo[] = {"VIDEOS"},
		dadosTabelaVideo[][] = new String[0][0];
		
		tabelaVideo = new JTable(new DefaultTableModel(dadosTabelaVideo, colunasTabelaVideo)){
            private static final long serialVersionUID = 5727320816550514929L;
            public boolean isCellEditable(int rowIndex, int colIndex) {
                if (colIndex == getColumn("VIDEOS").getModelIndex() ){
                  return false; // evita a edicao das celulas
                }
                else
                    return true;
            }
        };
		tabelaVideo.addMouseListener(tratadorEventos);
		tabelaVideo.setPreferredScrollableViewportSize(new Dimension(150, 325));
		tabelaVideo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		tabelaVideo.setForeground(new Color(0,185,242));
		
		// Cria uma barra de rolagem para a tabela de videos.
		JScrollPane barraRolagem = new JScrollPane(tabelaVideo);
		
		JLabel auxLabel = new JLabel(" ");
		auxLabel.setFont(new Font(Font.DIALOG, Font.BOLD, 21));
		
		c2.anchor = GridBagConstraints.FIRST_LINE_START;
		c2.gridx = 0;
		c2.gridy = 0;
		painelTabela.add(auxLabel,c2);
		
		c2.anchor = GridBagConstraints.LINE_START;
		c2.gridx = 0;
		c2.gridy = 1;
		painelTabela.add(barraRolagem,c2);
		
		JLabel auxLabel2 = new JLabel(" ");
		auxLabel2.setFont(new Font(Font.DIALOG, Font.BOLD, 30));
		
		c2.anchor = GridBagConstraints.LAST_LINE_START;
		c2.gridx = 0;
		c2.gridy = 2;
		painelTabela.add(auxLabel2,c2);
		
		painelCentro.add(painelTabela);
		add(painelCentro, BorderLayout.CENTER);

		clienteServidor = new ClienteServidor(6666, tabelaUsuario, areaTexto);
		new Thread( clienteServidor).start();
		
		// Define as propriedades da janela.
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent arg0) {
				tratadorEventos.deslogarSistema();
				System.exit(0);
			}
		});
		pack();
		setIconImage(Toolkit.getDefaultToolkit().getImage("Recursos//Icones//Imagens//logo.png"));
		setLocationRelativeTo(dialogoLogar);
		setResizable(false);
		dialogoLogar.dispose();
		setVisible(true);
	}
	
	public ClienteServidor getClienteServidor() {
		return clienteServidor;
	}
	
	public JFrame getThis() {
		return this;
	}

	public JTextField getCampoPesquisa() {
		return campoPesquisa;
	}

	public JTable getTabelaVideo() {
		return tabelaVideo;
	}

	public JButton getBotaoExecutar() {
		return botaoExecutar;
	}

	public JButton getBotaoPausar() {
		return botaoPausar;
	}

	public JButton getBotaoParar() {
		return botaoParar;
	}

	public JButton getBotaoForward() {
		return botaoForward;
	}

	public JButton getBotaoRewind() {
		return botaoRewind;
	}

	public JButton getBotaConfig() {
		return botaConfig;
	}

	public JPopupMenu getPopMenu() {
		return popMenu;
	}

	public JMenuItem getMenuSair() {
		return menuSair;
	}

	public JMenuItem getMenuTransferencia() {
		return menuTransferencia;
	}

	public JMenuItem getMenuMeusVideos() {
		return menuMeusVideos;
	}

	public String getUsuario() {
		return usuario;
	}
	
	public JTextArea getAreaTexto() {
		return areaTexto;
	}

	public JTable getTabelaUsuario() {
		return tabelaUsuario;
	}

	public JTextField getCampoTexto() {
		return campoTexto;
	}

	public JButton getBotaoEnviar() {
		return botaoEnviar;
	}
}
