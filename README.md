# Introdução
O _VOVER_ é um open-source desenvolvido em Java.

É um servidor de streaming vídeo e cliente que se comunica usando o protocolo de fluxo contínuo em tempo real RTSP e envia o o fluxo de quadros utilizando o protocolo RTP. Ele permite você fazer upload de videos, editar videos, e compartilhar com várias outras pessoas.


# Características
#### Protocolos utilizados:
1. RTSP: ”Real Time Stream Protocol” é utilizado para enviar (cliente) e receber (servidor) as requisições e trata-las.
2. RTP: ”Real Time Protocol” é utilizado para o envio fluxo dos videos.

#### Formatos do video: 
1. MJPEG

Mantém estado de conexão, mas não mantém conexão.

#### Acesso ao Servidor:
  1. Anônimo ( Ler )
     * Visualizar os videos existentes na base de dados do servidor.
  2. Cliente ( Ler e Escrever )
     * Visualizar, editar, enviar os videos na base de dados do servidor.
     * Acesso a um chat de apenas clientes cadastrados.

## Desenvolvedor:
1. Charles Tim Garrocho


## Screenshots
Tela Anônimo:

![alt text](http://i.imm.io/FYV9.png "Tela Inicia")

Tela Cliente:

![alt text](http://i.imm.io/FYVV.png "Tela Cliente")

Tela Edição de Videos:

![alt text](http://i.imm.io/FYSS.png "Tela Edição de Videos")
