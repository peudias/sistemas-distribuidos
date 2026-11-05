package pacote.Threads;

import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JOptionPane;
import pacote.Util;

public class DesktopRecepcaoThreads implements Runnable {
    private Util utils;
    private volatile boolean statusServidor = false;
    private ServerSocket receptor;
    private Socket client;
    
    public DesktopRecepcaoThreads(){
        this.utils = new Util();
    }

    @Override
    public void run() {
        try{
            this.receptor = new ServerSocket();
            this.receptor.setReuseAddress(true);
            this.receptor.bind(new java.net.InetSocketAddress(utils.getPortaRecepcaoDesktop()));
            while(!this.statusServidor){
                this.client = receptor.accept();
                Socket conexao = this.client;
                Thread.ofVirtual().start(() -> {
                    try(conexao){
                        ObjectInputStream reader = new ObjectInputStream(conexao.getInputStream());
                        String mensagem = reader.readUTF();
                        String arquivo = utils.getPathDesktopTxt();
                        try {
                            synchronized(Util.mensagensDesktop){
                                try(FileWriter fwriter = new FileWriter(arquivo, true)){
                                    fwriter.write(mensagem);
                                    fwriter.write(System.lineSeparator());
                                }
                                Util.mensagensDesktop.add(mensagem);
                            }
                        } catch(Exception error){
                            responderCliente(conexao, "ERRO");
                            throw error;
                        }
                        responderCliente(conexao, "OK");
                        reader.close();
                    } catch(Exception error){
                        if(!this.statusServidor){
                            System.out.println("DesktopRecepcaoThreads::run: Error " + error.getMessage());
                            JOptionPane.showMessageDialog(null, "DesktopRecepcaoThreads::run: Error " + error.getMessage());
                        }
                    }
                });
            }
        } catch(Exception error){
            if(!this.statusServidor){
                System.out.println("DesktopRecepcaoThreads::run: Error " + error.getMessage());
                JOptionPane.showMessageDialog(null, "DesktopRecepcaoThreads::run: Error " + error.getMessage());
            }
        }
    }
    
    private void responderCliente(Socket conexao, String resposta){
        try{
            ObjectOutputStream output = new ObjectOutputStream(conexao.getOutputStream());
            output.writeUTF(resposta);
            output.flush();
        } catch(Exception error){
            System.out.println("DesktopRecepcaoThreads::responderCliente: Error " + error.getMessage());
        }
    }
    
    public void fecharServidor(){
        this.statusServidor = true;
        try{
            if(this.client != null && !this.client.isClosed()){
                this.client.close();
            }
            if(this.receptor != null && !this.receptor.isClosed()){
                this.receptor.close();
            }
        } catch(Exception error){
            if(this.statusServidor){
                System.out.println("DesktopRecepcaoThreads::fecharServidor: Error " + error.getMessage());
                JOptionPane.showMessageDialog(null, "DesktopRecepcaoThreads::fecharServidor: Error " + error.getMessage());
            }
        }
    }
    
}
