package com.vover.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.vover.gui.Janela.ShadowBorder;

public abstract class Dialogo extends JDialog {

	private static final long serialVersionUID = 1L;

	public Dialogo() {
		super();
	}
	
	protected abstract void criarElementos();
	
	protected abstract void adicionarElementos();
	
	protected abstract void customizarElementos();
	
	protected abstract void configurarEventos();
	
	public void definirPropriedades(JFrame janelaPai, String titulo, Dimension dimensao) {
		setTitle(titulo);
		this.getRootPane().setBorder(new ShadowBorder());
		if (dimensao == null)
			pack();
		else
			setSize(dimensao);
		setModal(true);
		setBackground(Color.DARK_GRAY);
		setIconImage(Toolkit.getDefaultToolkit().getImage("Recursos//Icones//Imagens//logo.png"));
		setLocationRelativeTo(janelaPai);
		setResizable(false);
		setVisible(true);
	}
	
	/** Encontra o recurso no diretorio de recursos do jar
	 * @param enderecoArquivo <code>String</code> caminho do recurso
	 * @return <code>URL</code> com o endereco do recurso
	 */
	public static URL getResource(String enderecoArquivo){
		return Dialogo.class.getResource("/icones/" + enderecoArquivo);
	}
	
	public class Painel extends JPanel {
		private static final long serialVersionUID = 1L;
		private Image bg;
		
		public Painel(ImageIcon imagem) {
			this.bg = imagem.getImage();
		}
	    
	    @Override
	    public void paintComponent(Graphics g) {
	        g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
	    }
	}
	
}