package com.vover.gui.anonimo;

import static com.vover.recursos.Recursos.adicionaImagem;

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
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;

import com.vover.gui.Janela;
import com.vover.gui.anonimo.eventos.TratadorEventosAnonimo;
import com.vover.gui.eventos.TratadorControleTempoCliente;
import com.vover.recursos.TamanhoMaximo;

public class JanelaAnonimo extends Janela { 

	private static final long serialVersionUID = 1L;
	private JTextField campoPesquisa;
	private JTable tabelaVideo;
	private JButton botaoExecutar, botaoPausar, botaoParar, botaoForward, botaoRewind, botaConfig;
	private JPopupMenu popMenu;
	private JMenuItem menuCadastro, menuLogar;

	public JanelaAnonimo() {

		super("VIDEO OVER SERVER");
		final TratadorEventosAnonimo tratadorEventos = new TratadorEventosAnonimo(this);

		// Instancia novos paineis.
		JPanel painelNorte = new JPanel();
		JPanel painelCentro = new JPanel();
		JPanel painelVideo = new JPanel(new GridBagLayout());
		JPanel painelBotoes = new JPanel();
		JPanel painelVideoBotoes = new JPanel(new GridBagLayout());
		painelVideoBotoes.setBackground(Color.DARK_GRAY);
		painelBotoes.setBackground(Color.DARK_GRAY);

		JPanel painelTabela = new JPanel(new GridBagLayout());

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
		
		botaConfig = new JButton(new ImageIcon("Recursos//Icones//Botoes//configuracao.png"));
		botaConfig.setBorder(null);
		botaConfig.addMouseListener(tratadorEventos);
		botaConfig.setText("OPCOES                ");
		
		menuCadastro = new JMenuItem("CADASTRO");
		menuCadastro.addActionListener(tratadorEventos);
		
		menuLogar = new JMenuItem("LOGIN");
		menuLogar.addActionListener(tratadorEventos);
		
		popMenu.add(menuCadastro);  
		popMenu.add(menuLogar);
		
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
		painelBotoes.add(botaoRewind);
		painelBotoes.add(botaoForward);
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

		String colunasTabela[] = {"VIDEOS"},
		dadosTabela[][] = new String[0][0];
		
		tabelaVideo = new JTable(new DefaultTableModel(dadosTabela, colunasTabela)){
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
		
		add(painelTabela, BorderLayout.EAST);
		add(painelCentro, BorderLayout.WEST);

		// Define as propriedades da janela.
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent arg0) {
				if (getSoqueteRTP() != null)
					tratadorEventos.sairVideo();
				System.exit(0);
			}
		});
		pack();
		setIconImage(Toolkit.getDefaultToolkit().getImage("Recursos//Icones//Imagens//logo.png"));
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
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

	public JPopupMenu getPopMenu() {
		return popMenu;
	}

	public JMenuItem getMenuCadastro() {
		return menuCadastro;
	}

	public JMenuItem getMenuLogar() {
		return menuLogar;
	}

	public JButton getBotaConfig() {
		return botaConfig;
	}
}
