package com.vover.gui.cliente.eventos;

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
import com.vover.gui.cliente.DialogoMeusVideos;
import com.vover.gui.cliente.DialogoTransfereVideo;
import com.vover.gui.cliente.JanelaCliente;
import com.vover.gui.recursos.DialogoErro;
import com.vover.recursos.ConexaoCliente;
import com.vover.recursos.Requisicao;
import com.vover.recursos.Resposta;
import com.vover.recursos.Video;

public class TratadorEventosCliente extends KeyAdapter implements ActionListener, MouseListener {

	private JanelaCliente janelaCliente;
	private DefaultTableModel modeloTabela;
	private ConexaoCliente conexaoCliente;
	private ConexaoCliente conexaoClienteChat;

	// Estados do Protocolo RTSP:
	private final int PRONTO = 1;
	private final int EXECUTANDO = 2;

	private int ESTADO;
	private String nomeVideo;
	private int RTSPSeqNb = 0; // Numero de sequencia de mensagens RTSP dentro da sessao.
	private int RTSPid = 0; // ID da sessao RTSP (dada pelo servidor RTSP).

	public TratadorEventosCliente(JanelaCliente janelaCliente) {

		super();
		this.janelaCliente = janelaCliente;
		this.conexaoCliente = new ConexaoCliente();
		this.conexaoClienteChat = new ConexaoCliente();
	}

	public void actionPerformed(ActionEvent evento) {

		if (evento.getSource() == janelaCliente.getBotaoEnviar()) {
			enviarMensagem();
		}

		else if (evento.getSource() == janelaCliente.getCampoTexto()) {
			enviarMensagem();
		}

		else if (evento.getSource() == janelaCliente.getMenuSair()) {
			if (janelaCliente.getSoqueteRTP() != null)
				sairVideo();
			try {
				janelaCliente.getClienteServidor().fecharConexao();
				conexaoCliente.conectaServidor();

				conexaoCliente.getEnviaDados().writeObject("DESLOGAR");
				conexaoCliente.getEnviaDados().flush();

				conexaoCliente.getEnviaDados().writeObject(janelaCliente.getUsuario());
				conexaoCliente.getEnviaDados().flush();

				conexaoCliente.desconectaServidor();
				janelaCliente.dispose();
				new JanelaAnonimo();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		else if (evento.getSource() == janelaCliente.getMenuTransferencia()) {
			if (janelaCliente.getSoqueteRTP() != null)
				sairVideo();
			try {
				new DialogoTransfereVideo(janelaCliente,janelaCliente.getUsuario());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		else if (evento.getSource() == janelaCliente.getMenuMeusVideos()) {
			if (janelaCliente.getSoqueteRTP() != null)
				sairVideo();
			new DialogoMeusVideos(janelaCliente, janelaCliente.getUsuario());
		}

		// Caso o evento tenha ocorrido no botao Executar.
		else if (evento.getSource() == janelaCliente.getBotaoExecutar() && janelaCliente.getSoqueteRTP() != null) {

			if (ESTADO == PRONTO)
				executarVideo();
		}
		// Caso o evento tenha ocorrido no botao stop.
		else if (evento.getSource() == janelaCliente.getBotaoPausar() && janelaCliente.getSoqueteRTP() != null) {

			if (ESTADO == EXECUTANDO)
				pausarVideo();
		}

		// Caso o evento tenha ocorrido no botao stop.
		else if (evento.getSource() == janelaCliente.getBotaoParar() && janelaCliente.getSoqueteRTP() != null) {

			if (ESTADO == EXECUTANDO || ESTADO == PRONTO) {
				sairVideo();
			}
		}

		// Caso o evento tenha ocorrido no botao forward.
		else if (evento.getSource() == janelaCliente.getBotaoForward() && janelaCliente.getSoqueteRTP() != null) {

			if (ESTADO == EXECUTANDO) {
				RTSPSeqNb++;
				enviaRequisicao("FORWARD");

				// Aguardando a resposta.
				if (analisaResposta() == 200)
					ESTADO = EXECUTANDO;
				else {
					ESTADO = PRONTO;
					janelaCliente.getControleTempo().stop();
				}
			}
		}

		// Caso o evento tenha ocorrido no botao rewind.
		else if (evento.getSource() == janelaCliente.getBotaoRewind() && janelaCliente.getSoqueteRTP() != null) {

			if (ESTADO == EXECUTANDO) {
				RTSPSeqNb++;
				enviaRequisicao("REWIND");

				// Aguardando a resposta.
				if (analisaResposta() == 200)
					ESTADO = EXECUTANDO;
				else {
					ESTADO = PRONTO;
					janelaCliente.getControleTempo().stop();
				}
			}
		}
	}

	public void keyReleased(KeyEvent event) {
		pesquisaPorNome();
	}

	private void pesquisaPorNome() {

		if (janelaCliente.getSoqueteRTP() != null)
			sairVideo();

		String nome = janelaCliente.getCampoPesquisa().getText();
		Video videos[] = null;

		// Obtem a referencia da tabela de videos. Seta a numero de linhas data tabela de videos com 0.
		modeloTabela = ((DefaultTableModel)(janelaCliente.getTabelaVideo().getModel()));
		modeloTabela.setNumRows(0);

		// Adiciona a variavel "%" para pesquisar todos os nomes a partir das letras ja inseridas.
		if (nome.length() != 0) {

			nome += "%";

			if (conexaoCliente.conectaServidor()) {
				try {

					// Envia ao servidor o comando de PESQUISAR.
					conexaoCliente.getEnviaDados().writeObject("PESQUISAR");
					conexaoCliente.getEnviaDados().flush();

					String[] mensagem = new String[] {"ANONIMO", nome};

					// Envia o nome do video que sera pesquisado.
					conexaoCliente.getEnviaDados().writeObject(mensagem);
					conexaoCliente.getEnviaDados().flush();

					// Recebe uma composicao de videos do servidor.
					videos = (Video[])conexaoCliente.getRecebeDados().readObject();

					conexaoCliente.desconectaServidor();

					// Verifica se a composicao de videos e diferente de null. Se sim joga os videos na tabela.
					if (videos != null ) {
						Object[] linha = new Object[1];
						for (Video video : videos) {
							linha[0] = video.getNome().substring(0,video.getNome().indexOf("."));
							modeloTabela.addRow(linha);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else
				new DialogoErro(janelaCliente, "Erro ao Conectar...", "O Servidor Esta Desligado.");
		}
	}

	public void mouseClicked(MouseEvent evento) {

		if (evento.getSource() == janelaCliente.getTabelaVideo() && evento.getClickCount() == 2 ){
			if (janelaCliente.getSoqueteRTP() != null)
				sairVideo();

			// Obtem a posicao da linha onde a tabela foi clicada.
			int posicao = janelaCliente.getTabelaVideo().getSelectedRow();

			// Obtem os dados dessa linha.
			nomeVideo = janelaCliente.getTabelaVideo().getValueAt(posicao, 0).toString();
			try {
				if (conexaoCliente.conectaServidor()) {
					conexaoCliente.getEnviaDados().writeObject("VIZUALIZAR");
					executaVideo();
				}
				else
					new DialogoErro(janelaCliente, "Erro ao Conectar...", "O Servidor Esta Desligado.");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}

		else if (evento.getSource() == janelaCliente.getLabelIcone() && janelaCliente.getSoqueteRTP() != null) {

			if (ESTADO == PRONTO)
				executarVideo();
			else if (ESTADO == EXECUTANDO)
				pausarVideo();
		}
	}

	public void executarVideo() {
		RTSPSeqNb++;
		enviaRequisicao("EXECUTAR");
		ESTADO = EXECUTANDO;
		janelaCliente.getControleTempo().start();
	}

	public void pausarVideo() {
		RTSPSeqNb++;
		enviaRequisicao("PAUSAR");

		if (analisaResposta() != 200)
			ESTADO = EXECUTANDO;
		else {
			ESTADO = PRONTO;
			janelaCliente.getControleTempo().stop();
		}
	}

	public void sairVideo() {
		RTSPSeqNb++;
		enviaRequisicao("SAIR");
		analisaResposta();
		janelaCliente.getSoqueteRTP().close();
		janelaCliente.setSoqueteRTP(null);
		janelaCliente.getControleTempo().stop();
		janelaCliente.limparPlayer();
	}

	public void executaVideo() throws IOException, ClassNotFoundException {

		conexaoCliente.getEnviaDados().writeObject(nomeVideo + ".Mjpeg");
		String descricao = (String)conexaoCliente.getRecebeDados().readObject();
		janelaCliente.getNomeVideo().setText(nomeVideo);

		janelaCliente.getDescricao().setText("Descricao do video:");
		janelaCliente.getDescricaoVideo().setText(descricao);

		try {
			// Construindo um novo "DatagramSocket" para receber pacotes RTP do servidor.
			janelaCliente.setSoqueteRTP(new DatagramSocket(conexaoCliente.getPORTA()));
			janelaCliente.getSoqueteRTP().setSoTimeout(5);
		}catch (SocketException se) {
			se.printStackTrace();
			System.exit(0);
		}

		// Numero de sequencia de inicializacao do Protocolo RTSP:
		RTSPSeqNb = 1;

		// REQUISICAO / NOME VIDEO/ RSTP / CSEQ / TRANSPORTE
		Requisicao MSN_REQUISICAO = new Requisicao("CONFIGURAR", nomeVideo + ".Mjpeg", "RTSP/1.0", RTSPSeqNb, "TRANSPORTE: RTP/UDP; PORTA_CLIENTE= " + conexaoCliente.getPORTA());

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

	// Enviando uma REQUISICAO RTSP ao servidor.
	private void enviaRequisicao(String requisicao) {

		try {

			// REQUISICAO / NOME VIDEO/ RSTP / CSEQ / TRANSPORTE
			Requisicao MSN_REQUISICAO = new Requisicao(requisicao, nomeVideo, "RTSP/1.0", RTSPSeqNb, RTSPid);

			System.out.println(MSN_REQUISICAO.toStringSECAO());

			// ENVIA A MENSAGEM DE REQUISICAO AO SERVIDOR.
			conexaoCliente.getEnviaDados().writeObject(MSN_REQUISICAO);
			conexaoCliente.getEnviaDados().flush();
		}
		catch(Exception ex) {
			ex.printStackTrace();
			System.exit(0);
		}
	}

	public void deslogarSistema() {
		try {
			conexaoClienteChat.conectaServidor();
			conexaoClienteChat.getEnviaDados().writeObject("DESLOGAR");
			conexaoClienteChat.getEnviaDados().writeObject(janelaCliente.getUsuario());
			conexaoClienteChat.getEnviaDados().flush();
			conexaoClienteChat.desconectaServidor();

			if (ESTADO == EXECUTANDO || ESTADO == PRONTO && janelaCliente.getSoqueteRTP() != null) {
				sairVideo();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void enviarMensagem() {
		String[] mensagem = new String[] {janelaCliente.getUsuario(), janelaCliente.getCampoTexto().getText()};

		janelaCliente.getCampoTexto().setText("");
		try {
			conexaoClienteChat.conectaServidor();
			conexaoClienteChat.getEnviaDados().writeObject("MENSAGEM");
			conexaoClienteChat.getEnviaDados().writeObject(mensagem);
			conexaoClienteChat.getEnviaDados().flush();
			conexaoClienteChat.desconectaServidor();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Os metodos abaixo nao sao implementados.
	public void mouseEntered(MouseEvent evento) {
	}

	public void mouseExited(MouseEvent evento) {
	}
	public void mousePressed(MouseEvent evento) {
	}
	public void mouseReleased(MouseEvent e) {
	}
}
