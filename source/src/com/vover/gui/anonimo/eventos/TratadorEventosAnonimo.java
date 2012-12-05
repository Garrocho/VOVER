package com.vover.gui.anonimo.eventos;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;

import javax.swing.table.DefaultTableModel;

import com.vover.gui.anonimo.JanelaAnonimo;
import com.vover.gui.recursos.DialogoErro;
import com.vover.gui.usuario.DialogoCadastro;
import com.vover.gui.usuario.DialogoLogar;
import com.vover.recursos.ConexaoCliente;
import com.vover.recursos.Requisicao;
import com.vover.recursos.Resposta;
import com.vover.recursos.Video;

public class TratadorEventosAnonimo extends KeyAdapter implements ActionListener, MouseListener {

	private JanelaAnonimo janelaAnonimo;
	private DefaultTableModel modeloTabela;
	private ConexaoCliente conexaoCliente;

	// Estados do Protocolo RTSP:
	private final int PRONTO = 1;
	private final int EXECUTANDO = 2;

	private int ESTADO;
	private String nomeVideo;
	private int RTSPSeqNb = 0; // Numero de sequencia de mensagens RTSP dentro da sessao.
	private int RTSPid = 0; // ID da sessao RTSP (dada pelo servidor RTSP).

	public TratadorEventosAnonimo(JanelaAnonimo janelaAnonimo) {

		super();
		this.janelaAnonimo = janelaAnonimo;
		this.conexaoCliente = new ConexaoCliente();
	}

	public void actionPerformed(ActionEvent evento) {

		if (evento.getSource() == janelaAnonimo.getMenuCadastro()) {
			if (janelaAnonimo.getSoqueteRTP() != null)
				sairVideo();
			try {
				new DialogoCadastro(janelaAnonimo);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		else if (evento.getSource() == janelaAnonimo.getMenuLogar()) {
			if (janelaAnonimo.getSoqueteRTP() != null)
				sairVideo();
			try {
				new DialogoLogar(janelaAnonimo);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// Caso o evento tenha ocorrido no botao Executar.
		else if (evento.getSource() == janelaAnonimo.getBotaoExecutar() && janelaAnonimo.getSoqueteRTP() != null) {

			if (ESTADO == PRONTO)
				executarVideo();
		}
		// Caso o evento tenha ocorrido no botao stop.
		else if (evento.getSource() == janelaAnonimo.getBotaoPausar() && janelaAnonimo.getSoqueteRTP() != null) {

			if (ESTADO == EXECUTANDO)
				pausarVideo();
		}

		// Caso o evento tenha ocorrido no botao stop.
		else if (evento.getSource() == janelaAnonimo.getBotaoParar() && janelaAnonimo.getSoqueteRTP() != null) {

			if (ESTADO == EXECUTANDO || ESTADO == PRONTO) {
				sairVideo();
			}
		}

		// Caso o evento tenha ocorrido no botao forward.
		else if (evento.getSource() == janelaAnonimo.getBotaoForward() && janelaAnonimo.getSoqueteRTP() != null) {

			if (ESTADO == EXECUTANDO) {
				RTSPSeqNb++;
				conexaoCliente.enviaRequisicao("FORWARD","RTSP/1.0", nomeVideo, RTSPSeqNb, RTSPid);

				// Aguardando a resposta.
				if (analisaResposta() == 200)
					ESTADO = EXECUTANDO;
				else {
					ESTADO = PRONTO;
					janelaAnonimo.getControleTempo().stop();
				}
			}
		}

		// Caso o evento tenha ocorrido no botao rewind.
		else if (evento.getSource() == janelaAnonimo.getBotaoRewind() && janelaAnonimo.getSoqueteRTP() != null) {

			if (ESTADO == EXECUTANDO) {
				RTSPSeqNb++;
				conexaoCliente.enviaRequisicao("REWIND","RTSP/1.0", nomeVideo, RTSPSeqNb, RTSPid);

				// Aguardando a resposta.
				if (analisaResposta() == 200)
					ESTADO = EXECUTANDO;
				else {
					ESTADO = PRONTO;
					janelaAnonimo.getControleTempo().stop();
				}
			}
		}
	}

	public void keyReleased(KeyEvent event) {
		pesquisaPorNome();
	}

	private void pesquisaPorNome() {

		if (janelaAnonimo.getSoqueteRTP() != null)
			sairVideo();

		nomeVideo = janelaAnonimo.getCampoPesquisa().getText();
		Video videos[] = null;

		// Obtem a referencia da tabela de videos. Seta a numero de linhas data tabela de videos com 0.
		modeloTabela = ((DefaultTableModel)(janelaAnonimo.getTabelaVideo().getModel()));
		modeloTabela.setNumRows(0);

		// Adiciona a variavel "%" para pesquisar todos os nomes a partir das letras ja inseridas.
		if (nomeVideo.length() > 0) {

			nomeVideo += "%";
			if (conexaoCliente.conectaServidor()) {
				try {

					// Envia ao servidor o comando de PESQUISAR.
					conexaoCliente.getEnviaDados().writeObject("PESQUISAR");
					conexaoCliente.getEnviaDados().flush();

					String[] mensagem = new String[] {"ANONIMO", nomeVideo};

					// Envia o nome do video que sera pesquisado.
					conexaoCliente.getEnviaDados().writeObject(mensagem);
					conexaoCliente.getEnviaDados().flush();

					// Recebe uma composicao de videos do servidor.
					videos = (Video[])conexaoCliente.getRecebeDados().readObject();

					conexaoCliente.desconectaServidor();

					// Verifica se a composicao de videos e diferente de null. Se sim joga os videos na tabela.
					if (videos != null ) {
						Object[] linha = new Object[2];
						for (Video video : videos) {
							linha[0] = video.getNome().substring(0,video.getNome().indexOf("."));
							linha[1] = video.getDescricao();
							modeloTabela.addRow(linha);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else
				new DialogoErro(janelaAnonimo, "Erro ao Conectar...", "O Servidor Esta Desligado.");
		}
	}

	public void mouseClicked(MouseEvent evento) {

		if (evento.getSource() == janelaAnonimo.getTabelaVideo() && evento.getClickCount() == 2 ){
			if (janelaAnonimo.getSoqueteRTP() != null)
				sairVideo();

			// Obtem a posicao da linha onde a tabela foi clicada.
			int posicao = janelaAnonimo.getTabelaVideo().getSelectedRow();

			// Obtem os dados dessa linha.
			nomeVideo = janelaAnonimo.getTabelaVideo().getValueAt(posicao, 0).toString();
			try {
				conexaoCliente.conectaServidor();
				conexaoCliente.getEnviaDados().writeObject("VIZUALIZAR");
				executaVideo();
			} catch (NumberFormatException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}

		else if (evento.getSource() == janelaAnonimo.getLabelIcone() && janelaAnonimo.getSoqueteRTP() != null) {

			if (ESTADO == PRONTO)
				executarVideo();
			else if (ESTADO == EXECUTANDO)
				pausarVideo();
		}
	}

	public void executarVideo() {
		RTSPSeqNb++;
		conexaoCliente.enviaRequisicao("EXECUTAR","RTSP/1.0", nomeVideo, RTSPSeqNb, RTSPid);
		ESTADO = EXECUTANDO;
		janelaAnonimo.getControleTempo().start();
	}

	public void pausarVideo() {
		RTSPSeqNb++;
		conexaoCliente.enviaRequisicao("PAUSAR","RTSP/1.0", nomeVideo, RTSPSeqNb, RTSPid);

		if (analisaResposta() != 200)
			ESTADO = EXECUTANDO;
		else {
			ESTADO = PRONTO;
			janelaAnonimo.getControleTempo().stop();
		}
	}

	public void sairVideo() {
		RTSPSeqNb++;
		conexaoCliente.enviaRequisicao("SAIR","RTSP/1.0", nomeVideo, RTSPSeqNb, RTSPid);
		analisaResposta();
		janelaAnonimo.getSoqueteRTP().close();
		janelaAnonimo.setSoqueteRTP(null);
		janelaAnonimo.getControleTempo().stop();
		janelaAnonimo.limparPlayer();
	}

	public void executaVideo() throws IOException, ClassNotFoundException {

		conexaoCliente.getEnviaDados().writeObject(nomeVideo + ".Mjpeg");
		String descricao = (String)conexaoCliente.getRecebeDados().readObject();
		janelaAnonimo.getNomeVideo().setText(nomeVideo);

		janelaAnonimo.getDescricao().setText("Descricao do video:");
		janelaAnonimo.getDescricaoVideo().setText(descricao);

		try {
			// Construindo um novo soquete UDP para receber pacotes RTP do servidor.
			janelaAnonimo.setSoqueteRTP(new DatagramSocket(conexaoCliente.getPORTA()));
			janelaAnonimo.getSoqueteRTP().setSoTimeout(5);
		}catch (SocketException se) {
			se.printStackTrace();
			System.exit(0);
		}

		// Numero de sequencia de inicializacao do Protocolo RTSP:
		RTSPSeqNb = 1;

		// REQUISICAO / NOME VIDEO/ RSTP / CSEQ / TRANSPORTE
		Requisicao MSN_REQUISICAO = new Requisicao("CONFIGURAR", nomeVideo + ".Mjpeg", "RTSP/1.0", RTSPSeqNb, "TRANSPORTE: RTP/UDP; PORTA_CLIENTE= " + conexaoCliente.getPORTA());
		System.out.println(MSN_REQUISICAO.toString());

		conexaoCliente.getEnviaDados().writeObject(MSN_REQUISICAO);
		conexaoCliente.getEnviaDados().flush();

		if (analisaResposta() != 200)
			System.out.println("Resposta Invalida do Servidor");
		else {
			// Mudando o estado do RTSP.
			ESTADO = PRONTO;
		}
	}

	// Analisando a resposta do servidor:
	private int analisaResposta() {

		int VERIFICA = 0; 
		try {
			Resposta MSN_RESPOSTA = (Resposta)conexaoCliente.getRecebeDados().readObject();
			VERIFICA = MSN_RESPOSTA.getConfirmacao();

			if (VERIFICA == 200)
				RTSPid = MSN_RESPOSTA.getSECAO(); 
		}
		catch(Exception ex) {
			ex.printStackTrace();
			System.exit(0);
		}
		return VERIFICA;
	}

	// Os metodos abaixo nao sao implementados.
	public void mouseEntered(MouseEvent evento) {
	}

	public void mouseExited(MouseEvent evento) {
	}
	public void mousePressed(MouseEvent evento) {
	}
	public void mouseReleased(MouseEvent arg0) {
	}
}
