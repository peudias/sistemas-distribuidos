package pacote.Threads;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.File;
import java.io.FileWriter;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.JOptionPane;
import pacote.Util;

public class LogRecepcaoThreads implements Runnable {
    private Util utils;
    private volatile boolean statusServidor = false;
    private HttpServer receptor;
    private ExecutorService threads;
    
    public LogRecepcaoThreads(){
        this.utils = new Util();
    }

    @Override
    public void run() {
        try{
            // Item 2: Platform Threads, com um pool fixo de 4.
            // O coletor recebe poucos eventos, e as gravações já acontecem uma por vez (synchronized),
            // então mais threads só ficariam esperando a vez de gravar. O pool fixo também limita o uso
            // de recursos se algum cliente entrar em loop enviando erros, sem afetar os outros serviços.
            this.threads = Executors.newFixedThreadPool(4);
            this.receptor = HttpServer.create(new InetSocketAddress(utils.getPortaRecepcaoLog()), 0);
            this.receptor.setExecutor(this.threads);
            this.receptor.createContext("/logs/", conexao -> {
                try(conexao){
                    if(!conexao.getRequestMethod().equals("POST")){
                        responderCliente(conexao, 405);
                        return;
                    }
                    // O tipo vem na URL (/logs/desktop) e vira parte do nome do arquivo,
                    // por isso só aceita letras e números: impede gravar fora da pasta de logs
                    String tipo = conexao.getRequestURI().getPath().substring("/logs/".length());
                    if(!tipo.matches("[a-zA-Z0-9]+")){
                        responderCliente(conexao, 400);
                        return;
                    }
                    String registro = new String(conexao.getRequestBody().readAllBytes(), "UTF-8").replaceAll("[\\r\\n]+", " ");
                    String dataHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
                    String arquivo = utils.getPathLogs() + "client_" + tipo + "_errors.log";
                    try{
                        // Uma gravação por vez, para os registros simultâneos não se misturarem
                        synchronized(LogRecepcaoThreads.class){
                            new File(utils.getPathLogs()).mkdirs();
                            // O true acrescenta no fim do arquivo, sem apagar os registros anteriores
                            try(FileWriter fwriter = new FileWriter(arquivo, true)){
                                fwriter.write(dataHora + " | " + registro);
                                fwriter.write(System.lineSeparator());
                                fwriter.write(System.lineSeparator());
                            }
                        }
                    } catch(Exception error){
                        responderCliente(conexao, 500);
                        throw error;
                    }
                    responderCliente(conexao, 200);
                } catch(Exception error){
                    System.out.println("LogRecepcaoThreads::run: Error " + error.getMessage());
                    Util.registrarErro("logs", conexao.getRemoteAddress().getAddress(), "LogRecepcaoThreads:run", error);
                }
            });
            this.receptor.start();
        } catch(Exception error){
            if(!this.statusServidor){
                System.out.println("LogRecepcaoThreads::run: Error " + error.getMessage());
                Util.registrarErro("logs", "LogRecepcaoThreads:run", error);
                JOptionPane.showMessageDialog(null, "LogRecepcaoThreads::run: Error " + error.getMessage());
            }
        }
    }
    
    private void responderCliente(HttpExchange conexao, int status){
        try{
            conexao.sendResponseHeaders(status, -1);
        } catch(Exception error){
            System.out.println("LogRecepcaoThreads::responderCliente: Error " + error.getMessage());
            Util.registrarErro("logs", conexao.getRemoteAddress().getAddress(), "LogRecepcaoThreads:responderCliente", error);
        }
    }
    
    public void fecharServidor(){
        this.statusServidor = true;
        if(this.receptor != null) this.receptor.stop(0);
        if(this.threads != null) this.threads.shutdown();
    }
}