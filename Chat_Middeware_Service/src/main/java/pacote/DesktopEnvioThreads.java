package pacote;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JOptionPane;

public class DesktopEnvioThreads implements Runnable {
    private ServerSocket emissor;
    private Socket client;
    private Util utils;
    private volatile boolean statusServidor = false;

      
    public DesktopEnvioThreads(){
        this.utils = new Util();
    }
    
    @Override
    public void run() {
        try{
            // while(true){
            emissor = new ServerSocket();
            emissor.setReuseAddress(true);
            emissor.bind(new java.net.InetSocketAddress(utils.getPortaEnvioDesktop()));
            while(!this.statusServidor){
                client = emissor.accept();
                FileReader fReader = new FileReader(utils.getPathDesktopTxt());
                BufferedReader buffer = new BufferedReader(fReader);
                String msgs = "";
                while(buffer.ready()){
                    msgs += buffer.readLine();
                }
                buffer.close();
                fReader.close();
                ObjectOutputStream output = new ObjectOutputStream(client.getOutputStream());
                output.writeUTF(msgs);
                output.close();
                client.close();
            }
            emissor.close();
        } catch(Exception error){
            if(!this.statusServidor){
                System.out.println("DesktopEnvioThreads::run: Error " + error.getMessage());
                JOptionPane.showMessageDialog(null, "DesktopEnvioThreads::run: Error " + error.getMessage());
            }
        }
    }
    
    public void fecharServidor(){
        this.statusServidor = true;
        try{
            if(this.client != null && !this.client.isClosed()) this.client.close();
            if(this.emissor != null && !this.emissor.isClosed()) this.emissor.close();
        } catch(Exception error){}
        }
}
