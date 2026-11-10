package pacote.Threads;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.FileWriter;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.JOptionPane;
import pacote.Util;

public class WebRecepcaoThreads implements Runnable {
    private Util utils;
    private volatile boolean statusServidor = false;
    private HttpServer receptor;
    private ExecutorService threads;
    
    public WebRecepcaoThreads(){
        this.utils = new Util();
    }

    @Override
    public void run() {
        try{
            this.threads = Executors.newVirtualThreadPerTaskExecutor();
            this.receptor = HttpServer.create(new InetSocketAddress(utils.getPortaRecepcaoWeb()), 0);
            this.receptor.setExecutor(this.threads);
            this.receptor.createContext("/mensagens", conexao -> {
                try(conexao){
                    if(!conexao.getRequestMethod().equals("POST")){
                        responderCliente(conexao, 405);
                        return;
                    }
                    String mensagem = new String(conexao.getRequestBody().readAllBytes(), "UTF-8");
                    String arquivo = utils.getPathDesktopTxt();
                    try{
                        synchronized(Util.mensagensDesktop){
                            try(FileWriter fwriter = new FileWriter(arquivo, true)){
                                fwriter.write(mensagem);
                                fwriter.write(System.lineSeparator());
                            }
                            Util.mensagensDesktop.add(mensagem);
                        }
                    } catch(Exception error){
                        responderCliente(conexao, 500);
                        throw error;
                    }
                    responderCliente(conexao, 200);
                } catch(Exception error){
                    if(!this.statusServidor){
                        System.out.println("WebRecepcaoThreads::run: Error " + error.getMessage());
                        Util.registrarErro("web", conexao.getRemoteAddress().getAddress(), "WebRecepcaoThreads:run", error);
                        JOptionPane.showMessageDialog(null, "WebRecepcaoThreads::run: Error " + error.getMessage());
                    }
                }
            });
            this.receptor.start();
        } catch(Exception error){
            if(!this.statusServidor){
                System.out.println("WebRecepcaoThreads::run: Error " + error.getMessage());
                Util.registrarErro("web", "WebRecepcaoThreads:run", error);
                JOptionPane.showMessageDialog(null, "WebRecepcaoThreads::run: Error " + error.getMessage());
            }
        }
    }
    
    private void responderCliente(HttpExchange conexao, int status){
        try{
            conexao.sendResponseHeaders(status, -1);
        } catch(Exception error){
            System.out.println("WebRecepcaoThreads::responderCliente: Error " + error.getMessage());
            Util.registrarErro("web", conexao.getRemoteAddress().getAddress(), "WebRecepcaoThreads:responderCliente", error);
        }
    }
    
    public void fecharServidor(){
        this.statusServidor = true;
        if(this.receptor != null) this.receptor.stop(0);
        if(this.threads != null) this.threads.shutdown();
    }
}