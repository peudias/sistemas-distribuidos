@echo off
REM Item 6: abre o Middleware sem o NetBeans, com dois cliques.
REM Entra primeiro na pasta deste arquivo (a pasta do projeto), porque o TXT
REM e a pasta logs usam caminhos relativos a ela.
cd /d "%~dp0"
java -jar target\Chat_Middeware_Service-1.0-SNAPSHOT.jar
REM Mantem a janela aberta se o Middleware fechar, para ler a mensagem de erro.
pause
