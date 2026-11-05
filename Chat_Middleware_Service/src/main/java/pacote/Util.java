package pacote;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class Util {
   private static int portaEnvioDesktop = 6661;
   private static int portaRecepcaoDesktop = 6662;
   private static String pathDesktopTxt = "src/main/java/pacote/txt/RepositorioDesktop.txt";
   public static DesktopRecepcaoThreads threadDesktopRecepcaoThreads;   
   public static DesktopEnvioThreads threadDesktopEnvioThreads;
   public static final ArrayList<String> mensagensDesktop = new ArrayList<String>();
   
   public Util(){}
           
   public int getPortaEnvioDesktop(){
       return this.portaEnvioDesktop;
   }
   
   public int getPortaRecepcaoDesktop(){
       return this.portaRecepcaoDesktop;
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
