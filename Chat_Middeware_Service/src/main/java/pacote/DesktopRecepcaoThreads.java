
package pacote;

import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JOptionPane;

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
                ObjectInputStream reader = new ObjectInputStream(client.getInputStream());
                String mensagem = reader.readUTF();
                reader.close();
                this.client.close();
                String arquivo = utils.getPathDesktopTxt();
                FileWriter fwriter = new FileWriter(arquivo, true);
                fwriter.write(mensagem);
                fwriter.write(System.lineSeparator());
                fwriter.close();
            }
        } catch(Exception error){
            if(!this.statusServidor){
                System.out.println("DesktopRecepcaoThreads::run: Error " + error.getMessage());
                JOptionPane.showMessageDialog(null, "DesktopRecepcaoThreads::run: Error " + error.getMessage());
            }
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
