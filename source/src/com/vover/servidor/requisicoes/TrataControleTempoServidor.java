package com.vover.servidor.requisicoes;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.DatagramPacket;
import java.net.InetAddress;

import com.vover.pacote.PacoteRTP;

public class TrataControleTempoServidor implements ActionListener {

	private TrataRequisicoesVideo trataVideo;
	private DatagramPacket pacoteUDP; // Pacote UDP que contem os quadros de video.
	public static int TAMANHO_QUADRO = 500; // Comprimento do video em quadros.
	byte[] bufferQuadro; // Buffer usado para armazenar as imagens para enviar para o cliente.
	private InetAddress IP_CLIENTE;
	private int PORTA_CLIENTE, TIPO_MJPEG, PERIODO_QUADRO, numeroImagem;

	public TrataControleTempoServidor(TrataRequisicoesVideo trataVideo, InetAddress ip_cliente, int porta_cliente) {
		super();
		this.trataVideo = trataVideo;
		numeroImagem = 0;
		IP_CLIENTE = ip_cliente;
		PORTA_CLIENTE = porta_cliente;
		TIPO_MJPEG = 26;
		PERIODO_QUADRO = 60;
		bufferQuadro = new byte[15000];
	}

	// Evento do temporizador(timer):
	public void actionPerformed(ActionEvent e) {

		// Se a corrente de imagem nb e menor que o comprimento do trataVideo.
		if (numeroImagem < TAMANHO_QUADRO) {

			numeroImagem++;
			try {

				// Obtendo o proximo quadro para enviar ao trataVideo, bem como o seu tamanho.
				int tamanhoImagem = trataVideo.video.obterProximoQuadro(bufferQuadro);
				
				// Constroi um objeto RTPpacket contendo o quadro.
				PacoteRTP pacoteRTP = new PacoteRTP(TIPO_MJPEG, numeroImagem, numeroImagem*PERIODO_QUADRO, bufferQuadro, tamanhoImagem);

				// Obtendo o total do tamanho completo do pacote RTP para enviar.
				int tamanhoPacote = pacoteRTP.getTamanhoPacote();

				// Recupera o fluxo de bits(bitstream) do pacote e armazena-o em um array de bytes.
				byte[] bitsPacote = new byte[tamanhoPacote];
				pacoteRTP.criaPacote(bitsPacote);

				// Cria um pacote UDP(DatagramPacket). Depois envia o pacote pelo socket UDP.
				pacoteUDP = new DatagramPacket(bitsPacote, tamanhoPacote, IP_CLIENTE, PORTA_CLIENTE);
				trataVideo.soqueteUDP.send(pacoteUDP);
			}
			catch(Exception ex) {
				ex.printStackTrace();
				System.exit(0);
			}
		}
		else  {
			trataVideo.timer.stop();
		}
	}

}
