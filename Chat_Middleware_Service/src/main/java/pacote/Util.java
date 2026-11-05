package pacote;

import pacote.Threads.DesktopRecepcaoThreads;
import pacote.Threads.DesktopEnvioThreads;
import pacote.Threads.WebRecepcaoThreads;
import pacote.Threads.WebEnvioThreads;
import pacote.Threads.LogRecepcaoThreads;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;
import java.net.InetAddress;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Util {
   private static int portaEnvioDesktop = 6661;
   private static int portaRecepcaoDesktop = 6662;
   private static int portaEnvioWeb = 6663;
   private static int portaRecepcaoWeb = 6664;
   private static int portaRecepcaoLog = 6665;
   
   private static String pathDesktopTxt = "src/main/java/pacote/txt/RepositorioDesktop.txt";
   private static String pathLogs = "logs/";
   
   public static DesktopRecepcaoThreads threadDesktopRecepcaoThreads;   
   public static DesktopEnvioThreads threadDesktopEnvioThreads;
   public static WebRecepcaoThreads threadWebRecepcaoThreads;   
   public static WebEnvioThreads threadWebEnvioThreads;
   public static LogRecepcaoThreads threadLogRecepcaoThreads;
   
   public static final ArrayList<String> mensagensDesktop = new ArrayList<String>();
   
   public Util(){}
           
   public int getPortaEnvioDesktop(){
       return this.portaEnvioDesktop;
   }
   
   public int getPortaRecepcaoDesktop(){
       return this.portaRecepcaoDesktop;
   }
   
   public int getPortaEnvioWeb() {
       return this.portaEnvioWeb;
   }
   
   public int getPortaRecepcaoWeb() {
       return this.portaRecepcaoWeb;
   }
   
   public int getPortaRecepcaoLog(){
       return this.portaRecepcaoLog;
   }

   public String getPathLogs(){
       return this.pathLogs;
   }
   
   public String getPathDesktopTxt(){
       return this.pathDesktopTxt;
   }
   
   public static void carregarMensagensDesktop() throws Exception {
       FileReader fReader = new FileReader(pathDesktopTxt);
       BufferedReader buffer = new BufferedReader(fReader);
       synchronized(mensagensDesktop){
           mensagensDesktop.clear();
           while(buffer.ready()){
               mensagensDesktop.add(buffer.readLine());
           }
       }
       buffer.close();
       fReader.close();
   }
   
   public static String lerMensagensDesktop(){
       StringBuilder msgs = new StringBuilder();
       synchronized(mensagensDesktop){
           for(String mensagem : mensagensDesktop){
               msgs.append(mensagem);
           }
       }
       return msgs.toString();
   }
   
    // Registra um erro do próprio Middleware no arquivo do serviço onde ele aconteceu.
    // Grava direto no arquivo, sem passar pelo coletor, para funcionar mesmo com o coletor desligado.
    // O synchronized impede que erros simultâneos se misturem no arquivo.
    public static synchronized void registrarErro(String servico, String classeMetodo, Exception erro){
        try{
            InetAddress maquina = InetAddress.getLocalHost();
            String dataHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
            String registro = dataHora + " | " + maquina.getHostName() + " | " + maquina.getHostAddress() + " | " + classeMetodo + " | " + erro;
            new File(pathLogs).mkdirs();
            // O true acrescenta no fim do arquivo, sem apagar os registros anteriores
            try(FileWriter fwriter = new FileWriter(pathLogs + "server_middleware_" + servico + ".log", true)){
                fwriter.write(registro.replaceAll("[\\r\\n]+", " "));
                fwriter.write(System.lineSeparator());
                fwriter.write(System.lineSeparator());
            }
        } catch(Exception error){
            System.out.println("Util::registrarErro: Error " + error.getMessage());
        }
    }
}
