package pacote;

public class Util {
   private static int portaEnvioDesktop = 6661;
   private static int portaRecepcaoDesktop = 6662;
   // Relativo à pasta do projeto, que é o diretório de trabalho ao rodar pelo NetBeans
   private static String pathDesktopTxt = "src/main/java/pacote/txt/RepositorioDesktop.txt";
   public static DesktopRecepcaoThreads threadDesktopRecepcaoThreads;   
   public static DesktopEnvioThreads threadDesktopEnvioThreads;
   
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
}
