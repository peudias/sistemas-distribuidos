package pacote;

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
            emissor = new ServerSocket();
            emissor.setReuseAddress(true);
            emissor.bind(new java.net.InetSocketAddress(utils.getPortaEnvioDesktop()));
            while(!this.statusServidor){ // melhorar performance - quando chega uma msg, servidor grava no disco;
                client = emissor.accept(); //acabou de gravar no disco? precisa disponibilizar as msg, mas nao quero acessar o disco de armazenamento a cada iteração. apenas quando chegar uma msg nova. estrutura de dados na memoria ram do middleware. gravou no disco, atualizou, a memoria ram atualiza e toda hora que alguem for buscar mensagem, pega da memoria ram.
                Socket conexao = client;
                Thread.ofVirtual().start(() -> {
                    try(conexao){
                        String msgs = Util.lerMensagensDesktop();
                        ObjectOutputStream output = new ObjectOutputStream(conexao.getOutputStream());
                        output.writeUTF(msgs);
                        output.close();
                    } catch (Exception error){
                        System.out.println("DesktopEnvioThreads::Run: Error " + error.getMessage());
                    }
                });
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
