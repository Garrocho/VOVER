package com.vover.gui.eventos;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.DatagramPacket;

import javax.swing.ImageIcon;

import com.vover.gui.Janela;
import com.vover.pacote.PacoteRTP;

public class TratadorControleTempoCliente implements ActionListener {

	private Janela janela;

	public TratadorControleTempoCliente(Janela janela) {
		super();
		this.janela = janela;

		// Alocando memoria suficiente para o buffer usado para receber dados do servidor.
		janela.setBufferEntrada(new byte[15000]);
	}

	public void actionPerformed(ActionEvent evento) {

		// Construindo um pacote UDP(DatagramPacket) para receber os dados do soquete UDP.
		janela.setPacoteUDP(new DatagramPacket(janela.getBufferEntrada(), janela.getBufferEntrada().length));

		try {

			// Recebendo o DP do pacote UDP:
			janela.getSoqueteRTP().receive(janela.getPacoteUDP());

			// Criando um objeto RTPpacket da DP:
			PacoteRTP pacoteRTP = new PacoteRTP(janela.getPacoteUDP().getData(), janela.getPacoteUDP().getLength());

			// Obtendo A carga util(payload) do fluxo de bits(bitstream) do objeto RTPpacket:
			int tamanhoCarga = pacoteRTP.getComprimetoCarga();
			byte[] carga = new byte[tamanhoCarga];
			pacoteRTP.getCarga(carga);

			// Obtendo um objeto Image a partir da carga util(payload) do fluxo de bits(bitstream):
			Toolkit toolkit = Toolkit.getDefaultToolkit();
			Image image = toolkit.createImage(carga, 0, tamanhoCarga);

			// Exibindo a imagem como um objeto ImageIcon:
			janela.setIcone(new ImageIcon(image));
			janela.getLabelIcone().setIcon(janela.getIcone());
		}
		catch (InterruptedIOException iioe) {
		}
		catch (IOException ioe) {
		}
	}
}
