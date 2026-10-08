
package pacote;

import java.awt.CheckboxMenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.MenuItem;
import java.awt.Menu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class MenuService {
    private SystemTray tray;
    private TrayIcon icon;
    private PopupMenu menuPopup;
    private MenuItem menuPainelControle;
    private Menu menuAcoes;
    private CheckboxMenuItem menuItemDesktop, menuItemWeb, menuItemTerceiro, menuItemPublicidade;
    private PainelDeControle painelDeControle;
    
    private void criaMenuPopup(){
        this.menuPopup.add(this.menuPainelControle);
        this.menuPopup.addSeparator();
        this.menuPopup.add(this.menuItemDesktop);
        this.menuPopup.add(this.menuItemWeb);
        this.menuPopup.add(this.menuItemTerceiro);
        this.menuPopup.addSeparator();
        this.menuPopup.add(this.menuItemPublicidade);
        this.menuPopup.add(this.menuAcoes);
    }
    
    public MenuService() {
        try{
            if(!SystemTray.isSupported()){
              System.out.println("Sem suporte a System Tray");
            } else{
                this.painelDeControle = new PainelDeControle();
                this.tray = SystemTray.getSystemTray();
                ImageIcon iconServidor = new ImageIcon("C:\\Users\\LuanLuL\\Downloads\\SD\\Chat_Middeware_Service\\src\\main\\java\\pacote\\images\\iconServidor.png", "Gerenciador do servidor de Chhat");
                this.icon = new TrayIcon(iconServidor.getImage());
                this.icon.setImageAutoSize(true);
                this.menuPopup = new PopupMenu();
                this.menuPainelControle = new MenuItem("Abrir painel de controle");
                this.menuAcoes = new Menu("Ações");
                this.menuItemDesktop = new CheckboxMenuItem("Servidor Desktop");
                this.menuItemWeb = new CheckboxMenuItem("Servidor Web");
                this.menuItemTerceiro = new CheckboxMenuItem("Servidor Terceiro");
                this.menuItemPublicidade = new CheckboxMenuItem("Servidor de Publicidade");
                this.criaMenuPopup();
                this.icon.setPopupMenu(this.menuPopup);
                this.tray.add(this.icon);
                this.menuPainelControle.addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        painelDeControle.setVisible(true);
                    }
                });
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "MenuService::MenuService Error " + e.getMessage());
            System.out.println("MenuService::MenuService Error " + e.getMessage());
        }
    }
}
