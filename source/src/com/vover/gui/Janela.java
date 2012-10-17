package com.vover.gui;

import java.awt.HeadlessException;
import java.awt.Image;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.Timer;

public class Janela extends JFrame {

	private static final long serialVersionUID = 1L;
	private DatagramPacket pacoteUDP;
	private byte[] bufferEntrada;
	private JLabel labelIcone;
	private ImageIcon icone;
	private DatagramSocket soqueteRTP;
	private Timer controleTempo;
	private JLabel nomeVideo, descricaoVideo, descricao;
	
	public Janela(String titulo) throws HeadlessException {
		super(titulo);
	}
	
	public void limparPlayer() {
		ImageIcon imagem = new ImageIcon("Recursos//Icones//Player//tela.png");
		imagem.setImage(imagem.getImage().getScaledInstance(384, 288, Image.SCALE_DEFAULT));
		labelIcone.setIcon(imagem);
		descricaoVideo.setText(" ");
		nomeVideo.setText(" ");
		descricao.setText(" ");
	}

	public JLabel getNomeVideo() {
		return nomeVideo;
	}

	public JLabel getDescricaoVideo() {
		return descricaoVideo;
	}

	public JLabel getDescricao() {
		return descricao;
	}

	public DatagramSocket getSoqueteRTP() {
		return soqueteRTP;
	}

	public Timer getControleTempo() {
		return controleTempo;
	}

	public JLabel getLabelIcone() {
		return labelIcone;
	}

	public void setSoqueteRTP(DatagramSocket soqueteRTP) {
		this.soqueteRTP = soqueteRTP;
	}

	public void setPacoteUDP(DatagramPacket pacoteUDP) {
		this.pacoteUDP = pacoteUDP;
	}

	public void setBufferEntrada(byte[] bufferEntrada) {
		this.bufferEntrada = bufferEntrada;
	}

	public void setLabelIcone(JLabel labelIcone) {
		this.labelIcone = labelIcone;
	}

	public void setIcone(ImageIcon icone) {
		this.icone = icone;
	}

	public void setControleTempo(Timer controleTempo) {
		this.controleTempo = controleTempo;
	}

	public void setNomeVideo(JLabel nomeVideo) {
		this.nomeVideo = nomeVideo;
	}

	public void setDescricaoVideo(JLabel descricaoVideo) {
		this.descricaoVideo = descricaoVideo;
	}

	public void setDescricao(JLabel descricao) {
		this.descricao = descricao;
	}

	public DatagramPacket getPacoteUDP() {
		return pacoteUDP;
	}

	public byte[] getBufferEntrada() {
		return bufferEntrada;
	}

	public ImageIcon getIcone() {
		return icone;
	}
}