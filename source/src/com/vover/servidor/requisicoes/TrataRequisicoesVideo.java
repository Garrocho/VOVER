package com.vover.servidor.requisicoes;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.Timer;

import com.vover.recursos.FluxoVideo;
import com.vover.recursos.Requisicao;
import com.vover.recursos.Resposta;

public class TrataRequisicoesVideo {

	DatagramSocket soqueteUDP;
	FluxoVideo video;

	Timer timer; // Temporizador(timer) usado para enviar as imagens a uma taxa de quadro de video.

	// Estado do RTSP:
	final static int PRONTO = 1;
	final static int EXECUTANDO = 2;
	String ESTADO;

	static int RTSP_ID = 123456; // ID da sessao RTSP.
	int RTSPSeqNb; // Numero de sequencia(SequenceNumber) de mensagens RTSP dentro da sessao.

	private ObjectOutputStream enviaDados;
	private ObjectInputStream recebeDados;

	public TrataRequisicoesVideo(Socket soqueteRTSP, InetAddress ip, int porta, ObjectOutputStream enviaDados, ObjectInputStream recebeDados) throws IOException {

		this.enviaDados = enviaDados;
		this.recebeDados = recebeDados;
		
		RTSPSeqNb = 0;
		timer = new Timer(60, new TrataControleTempoServidor(getThis(), ip, porta));
		timer.setInitialDelay(0);
		timer.setCoalesce(true);
	}

	public boolean criaProcessoVideo() {
		processoVideo ts = new processoVideo();
		ts.start();

		if (ts.isInterrupted())
			return true;
		else 
			return false;
	}

	public TrataRequisicoesVideo getThis() {
		return this;
	}

	public class processoVideo extends Thread {

		public void run() {

			try {

				// Inicializando o estado RTSP.
				ESTADO = "INICIALIZAR";

				// Aguardando a mensagem de configuracao do cliente.
				Requisicao MSN_REQUISICAO = (Requisicao)recebeDados.readObject();
				String nomeVideo = MSN_REQUISICAO.getNomevideo();
				RTSPSeqNb = MSN_REQUISICAO.getCSEQ();

				// Atualizando o estado do RTSP.
				ESTADO = "PRONTO";

				// Enviando uma Resposta.
				enviaRespostaRTSP();

				// Inicializando o objeto VideoStream.
				nomeVideo = "Recursos/Videos/" + nomeVideo;
				video = new FluxoVideo(nomeVideo);

				// Instanciando um novo soquete UDP.
				soqueteUDP = new DatagramSocket();

				boolean feito = false;
				while(!feito) {

					// Aguardando a requisicao do cliente.
					String requisicao = analisaRequisicaoRTSP();

					if ((requisicao.equalsIgnoreCase("EXECUTAR")) && (ESTADO.equalsIgnoreCase("PRONTO")) ) {

						enviaRespostaRTSP();
						timer.start();
						ESTADO = "EXECUTAR";
					}
					else if ((requisicao.equalsIgnoreCase("PAUSAR")) && (ESTADO.equalsIgnoreCase("EXECUTAR")) ) {

						enviaRespostaRTSP();
						timer.stop();
						ESTADO = "PRONTO";
					}
					else if ((requisicao.equalsIgnoreCase("FORWARD")) && (ESTADO.equalsIgnoreCase("EXECUTAR")) ) {

						enviaRespostaRTSP();
						if (timer.getDelay() >= 10)
							timer.setDelay(timer.getDelay()-10);
					}
					else if ((requisicao.equalsIgnoreCase("REWIND")) && (ESTADO.equalsIgnoreCase("EXECUTAR")) ) {

						enviaRespostaRTSP();

						if (timer.getDelay() <= 150)
							timer.setDelay(timer.getDelay()+10);
					}
					else if (requisicao.equalsIgnoreCase("SAIR")) {

						enviaRespostaRTSP();
						timer.stop();
						soqueteUDP.close();
						feito = true;
					}
				}

			} catch(Exception e) {
				e.printStackTrace();
			}
		};
	}

	// Analisa uma requisicao RTSP do cliente.
	private String analisaRequisicaoRTSP() {

		String requisicao = "";
		try {
			Requisicao MSN_REQUISICAO = (Requisicao)recebeDados.readObject();
			requisicao = MSN_REQUISICAO.getRequisicao();
			RTSPSeqNb = MSN_REQUISICAO.getCSEQ();
		}
		catch(Exception ex) {
			ex.printStackTrace();
			System.exit(0);
		}
		return requisicao;
	}

	// Envia uma resposta de confirmacao RTSP.
	private void enviaRespostaRTSP() {

		try {
			Resposta MSN_RESPOSTA = new Resposta("RTSP/1.0", 200, RTSPSeqNb, RTSP_ID);
			System.out.println(MSN_RESPOSTA.toString());
			enviaDados.writeObject(MSN_RESPOSTA);
			enviaDados.flush();
		}
		catch(Exception ex) {
			ex.printStackTrace();
			System.exit(0);
		}
	}
}
