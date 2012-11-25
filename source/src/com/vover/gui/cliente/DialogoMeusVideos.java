package com.vover.gui.cliente;

import static com.vover.recursos.Recursos.customizarBotao;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.vover.gui.cliente.eventos.TratadorEventosMeusVideos;
import com.vover.recursos.TamanhoMaximo;

public class DialogoMeusVideos extends JDialog {

	private static final long serialVersionUID = 1L;
	private JButton botaoGravar, botaoLimpar, botaoSair;
	private JTextField campoNome, campoDescricao, campoPesquisa;
	private JTable tabelaVideo;
	private JPopupMenu popMenu;
	private JMenuItem menuAlterar, menuExcluir;
	private String nomeUsuario;
	
	public DialogoMeusVideos(JFrame janelaPai, String nomeUsuario) {
		super(janelaPai, "MEUS VIDEOS");
		this.nomeUsuario = nomeUsuario;
		TratadorEventosMeusVideos tratadorEventos = new TratadorEventosMeusVideos(this);
		
		// Instancia novos paineis.
		JPanel painelNorte = new JPanel();
		JPanel painelCentro = new JPanel();
		JPanel painelSul = new JPanel(new GridLayout(3, 1));
		JPanel painelBotoes = new JPanel();
		JPanel painelNome = new JPanel();
		JPanel painelDescricao = new JPanel();

		// Adiciona o labelPesquisa, o campo de pesquisa e o botao pesquisar no painelNorte.
		JLabel VOVER = new JLabel(new ImageIcon("Recursos//Icones//Imagens//vover.png"));
		painelNorte.add(VOVER);

		campoPesquisa = new JTextField(20);
		campoPesquisa.setToolTipText("Descreva o nome do video.");
		campoPesquisa.addKeyListener(tratadorEventos);
		campoPesquisa.setPreferredSize(new Dimension(500, 32));
		campoPesquisa.setDocument(new TamanhoMaximo(25));
		painelNorte.add(campoPesquisa);
		
		add(painelNorte, BorderLayout.NORTH);
		
		popMenu = new JPopupMenu();
		menuAlterar = new JMenuItem("ALTERAR");
		menuAlterar.addActionListener(tratadorEventos);
		menuExcluir = new JMenuItem("EXCLUIR");
		menuExcluir.addActionListener(tratadorEventos);
		popMenu.add(menuAlterar);  
		popMenu.add(menuExcluir);
		
		String colunasTabela[] = {"VIDEO","DESCRICAO"},
		dadosTabela[][] = new String[0][0];
		
		tabelaVideo = new JTable(new DefaultTableModel(dadosTabela, colunasTabela)){
            private static final long serialVersionUID = 5727320816550514929L;
            public boolean isCellEditable(int rowIndex, int colIndex) {
                if (colIndex == getColumn("VIDEO").getModelIndex() || colIndex == getColumn("DESCRICAO").getModelIndex()){
                  return false; // evita a edicao das celulas
                }
                else
                    return true;
            }
        };
		tabelaVideo.addMouseListener(tratadorEventos);
		tabelaVideo.setPreferredScrollableViewportSize(new Dimension(380, 150));
		tabelaVideo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		tabelaVideo.setForeground(new Color(0,185,242));
		tabelaVideo.getColumn("DESCRICAO").setPreferredWidth(200);
		
		// Cria uma barra de rolagem para a tabela de videos.
		JScrollPane barraRolagem = new JScrollPane(tabelaVideo);
		painelCentro.add(barraRolagem);
		
		add(painelCentro, BorderLayout.CENTER);
		
		JLabel labelNome = new JLabel("NOME         ");
		labelNome.setFont(new Font(Font.DIALOG, Font.BOLD, 17));
		painelNome.add(labelNome);

		campoNome = new JTextField(24);
		campoNome.addActionListener(tratadorEventos);
		campoNome.setPreferredSize(new Dimension(500, 32));
		campoNome.setToolTipText("Descreva um nome para o video que sera carregado.");
		campoNome.setDocument(new TamanhoMaximo(25));
		campoNome.setEditable(false);
		painelNome.add(campoNome);
		
		painelSul.add(painelNome);

		JLabel labelDescricao = new JLabel("DESCRICAO");
		labelDescricao.setFont(new Font(Font.DIALOG, Font.BOLD, 17));
		painelDescricao.add(labelDescricao);

		campoDescricao = new JTextField(24);
		campoDescricao.setPreferredSize(new Dimension(500, 32));
		campoDescricao.setToolTipText("Descreva uma descricao para o video que sera carregado.");
		campoDescricao.setDocument(new TamanhoMaximo(50));
		campoDescricao.setEditable(false);
		painelDescricao.add(campoDescricao);
		
		painelSul.add(painelDescricao);
		add(painelSul, BorderLayout.SOUTH);
		
		botaoGravar = new JButton("GRAVAR");
		botaoGravar.addActionListener(tratadorEventos);
		customizarBotao(botaoGravar);
		botaoGravar.setPreferredSize(new Dimension(110, 35));
		painelBotoes.add(botaoGravar);
		
		botaoLimpar = new JButton("LIMPAR");
		botaoLimpar.addActionListener(tratadorEventos);
		customizarBotao(botaoLimpar);
		botaoLimpar.setPreferredSize(new Dimension(115, 35));
		painelBotoes.add(botaoLimpar);
		
		botaoSair = new JButton("SAIR");
		botaoSair.addActionListener(tratadorEventos);
		customizarBotao(botaoSair);
		botaoSair.setPreferredSize(new Dimension(100, 35));
		painelBotoes.add(botaoSair);
		
		painelSul.add(painelBotoes);
		add(painelSul, BorderLayout.SOUTH);
		
		// Definindo as propriedades da janela.
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent arg0) {
				getThis().dispose();
			}
		});
		pack();
		setModal(true);
		setIconImage(Toolkit.getDefaultToolkit().getImage("Recursos//Icones//icone.png"));
		setLocationRelativeTo(janelaPai);
		setResizable(false);
		setVisible(true);
	}
	
	public DialogoMeusVideos getThis(){
		return this;
	}

	public JButton getBotaoGravar() {
		return botaoGravar;
	}

	public JButton getBotaoLimpar() {
		return botaoLimpar;
	}

	public JButton getBotaoSair() {
		return botaoSair;
	}

	public JTextField getCampoNome() {
		return campoNome;
	}

	public JTextField getCampoDescricao() {
		return campoDescricao;
	}

	public JTextField getCampoPesquisa() {
		return campoPesquisa;
	}

	public JTable getTabelaVideo() {
		return tabelaVideo;
	}

	public JPopupMenu getPopMenu() {
		return popMenu;
	}

	public JMenuItem getMenuAlterar() {
		return menuAlterar;
	}

	public JMenuItem getMenuExcluir() {
		return menuExcluir;
	}

	public String getNomeUsuario() {
		return nomeUsuario;
	}
	
}
