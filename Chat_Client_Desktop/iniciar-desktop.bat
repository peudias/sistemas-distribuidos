@echo off
REM Item 6: abre o cliente Desktop sem o NetBeans, com dois cliques.
REM Para usar em outro computador da rede, rode pelo terminal passando o IP do servidor:
REM   iniciar-desktop.bat 192.168.0.10
cd /d "%~dp0"
java -jar target\Chat_Client_Desktop-1.0-SNAPSHOT.jar %1
REM Mantem a janela aberta se o Desktop fechar com erro, para ler a mensagem.
pause
