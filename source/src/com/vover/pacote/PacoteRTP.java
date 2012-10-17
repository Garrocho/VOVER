package com.vover.pacote;

public class PacoteRTP {
	
	static int TAMANHO_CABECALHO = 12;
	
	// Os campos que compoem o cabecalho RTP:
	public int Version;
	public int Padding;
	public int Extension;
	public int CC;
	public int Marker;
	public int PayloadType;
	public int SequenceNumber;
	public int TimeStamp;
	public int Ssrc;

	// Fluxo de bits(Bitstream) do cabeçalho RTP.
	public byte[] cabecalho;
	
	// Tamanho da carga util(payload) RTP.
	public int TAMANHO_CARGA;
	
	// Fluxo de bits(Bitstream) da carga util(payload) RTP:
	public byte[] carga;

	// Construtor de um objeto RTPpacket de campos de cabecalho e carga util(payload) e bitstream definidos:
	public PacoteRTP(int tipoCargaUtil, int numeroImagem, int Time, byte[] dados, int tamanhoDados) {
		
	    // Preenchendo os campos do cabecalho padrao.
	    Version = 2;
	    Padding = 0;
	    Extension = 0;
	    CC = 0;
	    Marker = 0;
	    Ssrc = 0;

	    // Preenchendo os campos de cabecalho que mundam conforme a execucao do processo:
	    SequenceNumber = numeroImagem;
	    TimeStamp = Time;
	    PayloadType = tipoCargaUtil;
	    
	    // Instancia o cabecalho do tamanho definido.
	    cabecalho = new byte[TAMANHO_CABECALHO];

	    // Preenchendo a matriz de byte do cabecalho com campos de cabecalho RTP:
	    cabecalho[0] = getByte(2); // Valor so e definido por 2.
	    cabecalho[1] = getByte(52); // 26 e o campo PT, mas existe um 0 no campo de M tambem.
	    cabecalho[2] = getByte(numeroImagem);
	    cabecalho[3] = getByte(numeroImagem, 1);
	    for (int i = 0; i < 4; i++) {
	    	cabecalho[4+i] = getByte(Time, i);
	    }
	    // Abandona o campo SSRC como 0, sem bytes sao definidos

	    // Preenchendo a carga de fluxo de bits(bitstream):
	    TAMANHO_CARGA = tamanhoDados;
	    carga = new byte[tamanhoDados];
	
	    // Preenchendo a matriz carga de bytes de dados (segundo o parametro do construtor).
	    for (int i = 0; i < tamanhoDados; i++) {
	    	carga[i] = dados[i];
	    }
	    
	}

	public int setBit(int value, int bit){
		value = value | 1 << (7 - bit);
		return value;
	}
	
	public byte getByte(int value){       
		return (byte) (value & 0xFF);
	}
	
	public byte getByte(int value, int byten){
		if(byten == 0) 
			return getByte(value);
		return (byte) (value >> (8*byten));
	}

	public PacoteRTP(byte[] packet, int packet_size) {
		
		// Preenchendo os campos do cabecalho padrao:
		Version = 2;
	    Padding = 0;
	    Extension = 0;
	    CC = 0;
	    Marker = 0;
	    Ssrc = 0;

	    // Verificando se o tamanho do pacote total e menor do que o tamanho do cabecalho
	    if (packet_size >= TAMANHO_CABECALHO)
	      {
	        // Obtendo o fluxo de bits(bitsream) do cabecalho:
	    	cabecalho = new byte[TAMANHO_CABECALHO];
	        for (int i=0; i < TAMANHO_CABECALHO; i++)
	        	cabecalho[i] = packet[i];

	        // Obtemm o fluxo de bits(payload) da carga util(bitstream):
	        TAMANHO_CARGA = packet_size - TAMANHO_CABECALHO;
	        carga = new byte[TAMANHO_CARGA];
	        for (int i=TAMANHO_CABECALHO; i < packet_size; i++)
	        	carga[i-TAMANHO_CABECALHO] = packet[i];
	      
	       // interpreta os campos mutaveis ​​do cabecalho:
	        PayloadType = cabecalho[1] & 127;
	        SequenceNumber = unsigned_int(cabecalho[3]) + 256*unsigned_int(cabecalho[2]);
	        TimeStamp = unsigned_int(cabecalho[7]) + 256*unsigned_int(cabecalho[6]) + 65536*unsigned_int(cabecalho[5]) + 16777216*unsigned_int(cabecalho[4]);
	    
	      }
	 }

	// Retorna o fluxo de bits(bistream) e carga util(payload) do RTPpacket e o seu tamanho.
	public int getCarga(byte[] dados) {
		
		for (int i=0; i < TAMANHO_CARGA; i++)
			dados[i] = carga[i];
	
		return(TAMANHO_CARGA);
	}
	
	// Retornar o comprimento da carga util(payload).
	public int getComprimetoCarga() {
		return(TAMANHO_CARGA);
	}
	
	// Retornar o comprimento total do pacote RTP.
	public int getTamanhoPacote() {
		return(TAMANHO_CARGA + TAMANHO_CABECALHO);
	}
	
	// Retorna o fluxo de bits(bitstream) do pacote e do seu comprimento.
	public void criaPacote(byte[] pacote) {
		  
		// Construindo o pacote. Ele vai receber o cabecalho(header) + carga util(payload).
		for (int i=0; i < TAMANHO_CABECALHO; i++)
			pacote[i] = cabecalho[i];
		for (int i=0; i < TAMANHO_CARGA; i++)
			pacote[i+TAMANHO_CABECALHO] = carga[i];
	}
	
	public int getTamanhoTotalPacote() {
		
		// Retorna o tamanho total do pacote.
		return(TAMANHO_CARGA + TAMANHO_CABECALHO);
	}
	
	// Obter o selo de tempo(timestamp).
	public int getTimestamp() {
		return(TimeStamp);
	}

	// Obter o numero de sequencia(sequencenumer).
	public int getSequenceNumber() {
		return(SequenceNumber);
	}

	// Obter o tipo da carga util(payload).
	public int getTipoCarga() {
		return(PayloadType);
	}
	
	// imprime o cabecalho sem o SSRC.
	public void imprimeCabecalho() {

		for (int i=0; i < (TAMANHO_CABECALHO-4); i++)	{
			
			for (int j = 7; j>=0 ; j--)
				if (((1<<j) & cabecalho[i] ) != 0)
					System.out.print("1");
				else
					System.out.print("0");
			System.out.print(" ");
		}
	    System.out.println();
	  }

	// Retorna o valor sem sinal de 8-bit inteiro nb.
	static int unsigned_int(int nb) {
		if (nb >= 0)
			return(nb);
		else
			return(256+nb);
	}
}