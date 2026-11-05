package pacote;

import pacote.Threads.DesktopRecepcaoThreads;
import pacote.Threads.DesktopEnvioThreads;
import pacote.Threads.WebRecepcaoThreads;
import pacote.Threads.WebEnvioThreads;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class Util {
   private static int portaEnvioDesktop = 6661;
   private static int portaRecepcaoDesktop = 6662;
   private static int portaEnvioWeb = 6663;
   private static int portaRecepcaoWeb = 6664;
   
   private static String pathDesktopTxt = "src/main/java/pacote/txt/RepositorioDesktop.txt";
   
   public static DesktopRecepcaoThreads threadDesktopRecepcaoThreads;   
   public static DesktopEnvioThreads threadDesktopEnvioThreads;
   public static WebRecepcaoThreads threadWebRecepcaoThreads;   
   public static WebEnvioThreads threadWebEnvioThreads;
   
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
}
