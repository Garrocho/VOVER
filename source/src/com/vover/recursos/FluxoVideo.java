package com.vover.recursos;

import java.io.FileInputStream;

public class FluxoVideo {

	FileInputStream arquivoVideo;
	int quadroAtualVideo;

	public FluxoVideo(String nomeVideo) throws Exception {

		arquivoVideo = new FileInputStream(nomeVideo);
		quadroAtualVideo = 0;
	}

	public int obterProximoQuadro(byte[] quadro) throws Exception {

		int tamanho = 0;
		byte[] tamanhoQuadro = new byte[5];

		// Le o comprimento do quadro atual.
		arquivoVideo.read(tamanhoQuadro, 0, 5);

		// Transforma o tamanhoQuadro para inteiro.
		tamanho = Integer.parseInt(new String(tamanhoQuadro));

		return arquivoVideo.read(quadro, 0, tamanho);
	}

}