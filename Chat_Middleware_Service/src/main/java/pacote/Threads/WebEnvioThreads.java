package pacote.Threads;

import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.JOptionPane;
import pacote.Util;

public class WebEnvioThreads implements Runnable {
    private HttpServer emissor;
    private ExecutorService threads;
    private Util utils;
    private volatile boolean statusServidor = false;
    
    public WebEnvioThreads(){
        this.utils = new Util();
    }
    
    @Override
    public void run() {
        try{
            threads = Executors.newVirtualThreadPerTaskExecutor();
            emissor = HttpServer.create(new InetSocketAddress(utils.getPortaEnvioWeb()), 0);
            emissor.setExecutor(threads);
            emissor.createContext("/mensagens", conexao -> {
                try(conexao){
                    byte[] msgs = Util.lerMensagensDesktop().getBytes("UTF-8");
                    conexao.sendResponseHeaders(200, msgs.length);
                    conexao.getResponseBody().write(msgs);
                } catch(Exception error){
                    System.out.println("WebEnvioThreads::run: Error " + error.getMessage());
                }
            });
            emissor.start();
        } catch(Exception error){
            if(!this.statusServidor){
                System.out.println("WebEnvioThreads::run: Error " + error.getMessage());
                JOptionPane.showMessageDialog(null, "WebEnvioThreads::run: Error " + error.getMessage());
            }
        }
    }
    
    public void fecharServidor(){
        this.statusServidor = true;
        if(this.emissor != null) this.emissor.stop(0);
        if(this.threads != null) this.threads.shutdown();
    }
}