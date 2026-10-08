package pacote;

public class Util {
   private static int portaEnvioDesktop = 6661;
   private static int portaRecepcaoDesktop = 6662;
   private static String pathDesktopTxt = "C:\\Users\\phpdi\\Desktop\\SD_CEFET-main\\Chat_Middeware_Service\\src\\main\\java\\pacote\\txt\\RepositorioDesktop.txt";
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
