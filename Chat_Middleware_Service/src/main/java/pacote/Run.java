package pacote;

import javax.swing.JOptionPane;

public class Run {
    static MenuService menuServidor;
    
    public static void main(String[] args){
        try{
            Util.carregarMensagensDesktop();
        } catch(Exception error){
            System.out.println("Run::main: Error " + error.getMessage());
            Util.registrarErro("internal", "Run:main", error);
            JOptionPane.showMessageDialog(null, "Run::main: Error " + error.getMessage());
            System.exit(1);
        }
        menuServidor = new MenuService();
    }
}
