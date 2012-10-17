package com.vover.recursos;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Recursos {
	
	/** 
	 * Exibe uma caixa de dialogo <code>javax.swing.JFileChooser</code> para 
	 * o usuario indicar o nome do diretorio e arquivo que sera aberto. 
	 * 
	 * @param janelaPai objeto <code>java.awt.Component</code> que identifica a
	 *        janela pai sobre a qual a janela <code>JFileChooser</code> sera
	 *        exibida.  
	 * @param titulo <code>String</code> com o nome da barra de titulo da caixa 
	 *        de dialogo.
	 *        
	 * @return <code>String</code> com o nome do arquivo a ser aberto. 
	 *         Se o usuário cancelar a operacao (clicar no botao "Cancelar") sera
	 *         retornado <code>null</code>.
	 *         
	 * @see java.awt.Component
	 */
	public static String dialogoAbrirArquivo(Component janelaPai, String titulo) {

		JFileChooser dialogoAbrir = new JFileChooser();
		
		dialogoAbrir.setFileSelectionMode(JFileChooser.FILES_ONLY);
		dialogoAbrir.setDialogTitle(titulo);
		dialogoAbrir.setApproveButtonText("Abrir");
		dialogoAbrir.setApproveButtonToolTipText("Abre um arquivo em disco.");
		dialogoAbrir.setFileFilter(new FileNameExtensionFilter("Mjpeg", "Mjpeg"));
		dialogoAbrir.setAcceptAllFileFilterUsed(false);

		int opcao = dialogoAbrir.showOpenDialog(janelaPai);

		if (opcao == JFileChooser.CANCEL_OPTION)
			return null;

		File arquivo = dialogoAbrir.getSelectedFile();
		String nomeArquivo = arquivo.getPath();

		return nomeArquivo;
	}
	
	public static Image adicionaImagem(String endrecoImagem) {
		Image imagem = Toolkit.getDefaultToolkit().getImage(endrecoImagem);
		Image menor = imagem.getScaledInstance(32, 32, Image.SCALE_DEFAULT);
		return menor;
	}
	
	public static void customizarBotao(JButton botao) {
		botao.setBackground(new Color(0,185,242));
		botao.setFont(new Font(Font.DIALOG, Font.BOLD, 15));
		botao.setFocusable(false);
		botao.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	}

}
