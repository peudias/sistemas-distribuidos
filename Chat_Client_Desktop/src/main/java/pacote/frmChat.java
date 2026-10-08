package pacote;

import java.awt.event.KeyEvent;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

public class frmChat extends javax.swing.JFrame {

    public String msg = "";
    private static final Object[][] EMOJIS = {
        {":-)", 0x1F600}, {";-)", 0x1F609}, {"x-x", 0x1F635}, {":-(", 0x1F627},
        {";-;", 0x1F62D}, {"<3", 0x2764}, {"$$", 0x1F4B0}, {":*", 0x1F618},
        {":D", 0x1F601}, {":P", 0x1F61B}, {"B-)", 0x1F60E}, {":o", 0x1F62E}
    };
    private javax.swing.JPopupMenu popupEmojis = new javax.swing.JPopupMenu();
    private static final String[] FIGURINHAS = {"coracao.png", "dinheiro.png", "beijo.png"};
    private static final String[] GIFS = {"funny.gif", "sonic-thumbs-up.gif", "danca.gif"};
    private String figurinhaSelecionada = null;
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(frmChat.class.getName());

    /**
     * Creates new form frmChat
     */
    public frmChat() {
        initComponents();
        montarPopupEmojis();
        Thread.startVirtualThread(() -> {
            String ultimoConteudo = null;
            while (true) {
                try {
                    // Socket client = new Socket("200.128.141.103", 6661);
                    Socket client = new Socket("localhost", 6661);
                    ObjectInputStream input = new ObjectInputStream(client.getInputStream());
                    String msgs = input.readUTF();
                    input.close();
                    client.close();
                    // Só redesenha quando algo mudou, evitando que a tela pisque
                    if (!msgs.equals(ultimoConteudo)) {
                        ultimoConteudo = msgs;
                        javax.swing.SwingUtilities.invokeLater(() -> {
                            try {
                                edtConversa.setText("");
                                HTMLEditorKit kit = (HTMLEditorKit) edtConversa.getEditorKit();
                                HTMLDocument doc = (HTMLDocument) edtConversa.getDocument();
                                kit.insertHTML(doc, doc.getLength(), resolverImagens(msgs), 0, 0, null);
                            } catch (Exception ex) {
                                System.out.println("FrmChat::FrmChat: Error " + ex.getMessage());
                            }
                        });
                    }
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ex) {
                    }
                } catch (Exception error) {
                    System.out.println("FrmChat::FrmChat: Error " + error.getMessage());
                    ultimoConteudo = null;
                    javax.swing.SwingUtilities.invokeLater(()
                            -> edtConversa.setText("<h2 style='color:red'>Servidor em manutenção... aguarde.</h2>"));
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ex) {
                    }
                    //JOptionPane.showMessageDialog(null, "FrmChat::FrmChat: Error " + error.getMessage());
                }
            }
        });
    }

    private String resolverImagens(String html) {
        java.net.URL pasta = frmChat.class.getResource("/pacote/images/");
        if (pasta == null) {
            return html;
        }
        return html.replace("src='/pacote/images/", "src='" + pasta)
                .replace("src='images/", "src='" + pasta);
    }

    private void montarPopupEmojis() {
        javax.swing.JPanel grade = new javax.swing.JPanel(new java.awt.GridLayout(0, 4, 2, 2));
        javax.swing.JPanel gradeFigurinhas = new javax.swing.JPanel(new java.awt.GridLayout(0, 3, 2, 2));
        javax.swing.JPanel gradeGifs = new javax.swing.JPanel(new java.awt.GridLayout(0, 3, 2, 2));
        javax.swing.JTabbedPane abas = new javax.swing.JTabbedPane();

        for (Object[] e : EMOJIS) {
            String codigo = (String) e[0];
            int cp = (int) e[1];

            javax.swing.JButton b = new javax.swing.JButton(new String(Character.toChars(cp)));
            b.setFont(new java.awt.Font("Segoe UI Emoji", java.awt.Font.PLAIN, 18));
            b.setMargin(new java.awt.Insets(2, 2, 2, 2));
            b.setFocusable(false);
            b.setToolTipText(codigo);

            b.addActionListener(ev -> {
                inputTxtMensagem.replaceSelection(codigo);
                popupEmojis.setVisible(false);
                inputTxtMensagem.requestFocus();
            });
            grade.add(b);
        }
        // popupEmojis.add(grade);
        abas.addTab("Emojis", grade);

        for (String nome : FIGURINHAS) {
            java.net.URL url = frmChat.class.getResource("/pacote/images/" + nome);
            java.awt.Image img = new javax.swing.ImageIcon(url).getImage().getScaledInstance(48, 48, java.awt.Image.SCALE_SMOOTH);
            javax.swing.JButton b = new javax.swing.JButton(new javax.swing.ImageIcon(img));
            b.setFocusable(false);
            b.setToolTipText(nome);

            b.addActionListener(ev -> {
                figurinhaSelecionada = nome;
                popupEmojis.setVisible(false);
                inputTxtMensagem.requestFocus();
            });
            gradeFigurinhas.add(b);
        }
        abas.addTab("Figurinhas", gradeFigurinhas);

        for (String nome : GIFS) {
            java.net.URL url = frmChat.class.getResource("/pacote/images/" + nome);
            java.awt.Image img = new javax.swing.ImageIcon(url).getImage().getScaledInstance(64, 64, java.awt.Image.SCALE_DEFAULT);
            javax.swing.JButton b = new javax.swing.JButton(new javax.swing.ImageIcon(img));
            b.setFocusable(false);
            b.setToolTipText(nome);

            b.addActionListener(ev -> {
                figurinhaSelecionada = nome;
                popupEmojis.setVisible(false);
                inputTxtMensagem.requestFocus();
            });
            gradeGifs.add(b);
        }
        abas.addTab("GIFs", gradeGifs);

        popupEmojis.add(abas);
    }

    public void gerarAndEnviarMsg() {
        this.msg = "";
        this.msg += "<img src='/pacote/images/" + Util.avatar + "' width='24px' height='24px'> ";
        this.msg += "<font color='" + Util.cor + "'><b>" + Util.nickname + " </b></font>";

        if (selectModo.getSelectedItem().toString().equals("Fala")) {
            this.msg += "fala: " + this.inputTxtMensagem.getText();
        } else if (selectModo.getSelectedItem().toString().equals("Grita")) {
            this.msg += "<b>grita: </b> <font size='+2'>" + this.inputTxtMensagem.getText().toUpperCase() + "</font>";
        } else if (selectModo.getSelectedItem().toString().equals("Sussurra")) {
            this.msg += "<font size='-1'><i>sussura: </i>" + this.inputTxtMensagem.getText().toLowerCase() + "</font>";
        } else if (selectModo.getSelectedItem().toString().equals("Xinga")) {
            this.msg += "<font size='+1' color='red'><b><u>xinga: " + this.inputTxtMensagem.getText().toUpperCase() + "</u></b></font>";
        }

        // if(!selectEmoji.getSelectedItem().toString().equals("Nenhum")){
        //     this.msg += "<img src='images/" + selectEmoji.getSelectedItem().toString().toLowerCase() + ".png' width='15' height='15'>";
        // }
        if (figurinhaSelecionada != null) {
            this.msg += "<br><img src='images/" + figurinhaSelecionada + "' width='64' height='64'>";
        }

        this.msg += "<br>";

        ArrayList<String> codigos = new ArrayList<>();
        ArrayList<String> simbolos = new ArrayList<>();

        codigos.add(":-)");
        simbolos.add("&#128512;");
        codigos.add(";-)");
        simbolos.add("&#128521;");
        codigos.add("x-x");
        simbolos.add("&#128565;");
        codigos.add(":-(");
        simbolos.add("&#128551;");
        codigos.add(";-;");
        simbolos.add("&#128557;");

        // for(int i=0; i < codigos.size(); i++){
        //     this.msg = this.msg.replace(codigos.get(i), simbolos.get(i));
        // }
        for (Object[] e : EMOJIS) {
            this.msg = this.msg.replace((String) e[0], "&#" + e[1] + ";");
        }

        inputTxtMensagem.setText("");
        selectModo.setSelectedIndex(0);
        // selectEmoji.setSelectedIndex(0);
        figurinhaSelecionada = null;

        try {
            // Socket client = new Socket("200.128.141.103", 6662);
            Socket client = new Socket("localhost", 6662);
            ObjectOutputStream output = new ObjectOutputStream(client.getOutputStream());
            output.writeUTF(this.msg);
            output.close();
            client.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "FrmChat::gerarAndEnviarMsg(): Erro " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scrConversa = new javax.swing.JScrollPane();
        edtConversa = new javax.swing.JEditorPane();
        lblMensagem = new javax.swing.JLabel();
        inputTxtMensagem = new javax.swing.JTextField();
        lblModo = new javax.swing.JLabel();
        selectModo = new javax.swing.JComboBox<>();
        lblEmoji = new javax.swing.JLabel();
        selectEmoji = new javax.swing.JComboBox<>();
        btnEnviar = new javax.swing.JButton();
        btnEmoji = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Chat");
        setName("fmrChat"); // NOI18N

        scrConversa.setName("scrConversa"); // NOI18N

        edtConversa.setContentType("text/html"); // NOI18N
        scrConversa.setViewportView(edtConversa);

        lblMensagem.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblMensagem.setText("Mensagem:");
        lblMensagem.setName("lblMensagem"); // NOI18N

        inputTxtMensagem.setName("inputTxtMensagem"); // NOI18N
        inputTxtMensagem.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                inputTxtMensagemKeyPressed(evt);
            }
        });

        lblModo.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblModo.setText("Modo:");
        lblModo.setName("lblModo"); // NOI18N

        selectModo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Fala", "Grita", "Sussurra", "Xinga" }));
        selectModo.setName("selectModo"); // NOI18N

        lblEmoji.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblEmoji.setText("Emoji:");
        lblEmoji.setName("lblEmoji"); // NOI18N

        selectEmoji.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nenhum", "Coracao", "Dinheiro", "Beijo", " " }));
        selectEmoji.setName("selectEmoji"); // NOI18N

        btnEnviar.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnEnviar.setText("Enviar");
        btnEnviar.setName("btnEnviar"); // NOI18N
        btnEnviar.addActionListener(this::btnEnviarActionPerformed);

        btnEmoji.setText(":-)");
        btnEmoji.addActionListener(this::btnEmojiActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(scrConversa))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(lblMensagem, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(inputTxtMensagem, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblModo)
                        .addGap(12, 12, 12)
                        .addComponent(selectModo, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblEmoji)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(selectEmoji, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnEmoji)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 47, Short.MAX_VALUE)
                                .addComponent(btnEnviar, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrConversa, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMensagem, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                    .addComponent(inputTxtMensagem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblModo)
                    .addComponent(selectModo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblEmoji)
                    .addComponent(btnEnviar)
                    .addComponent(btnEmoji))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(selectEmoji, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnEnviarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEnviarActionPerformed
        this.gerarAndEnviarMsg();
    }//GEN-LAST:event_btnEnviarActionPerformed

    private void inputTxtMensagemKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_inputTxtMensagemKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            this.gerarAndEnviarMsg();
        }
    }//GEN-LAST:event_inputTxtMensagemKeyPressed

    private void btnEmojiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEmojiActionPerformed
        // TODO add your handling code here:
        popupEmojis.show(btnEmoji, 0, -popupEmojis.getPreferredSize().height);
    }//GEN-LAST:event_btnEmojiActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new frmChat().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEmoji;
    private javax.swing.JButton btnEnviar;
    private javax.swing.JEditorPane edtConversa;
    private javax.swing.JTextField inputTxtMensagem;
    private javax.swing.JLabel lblEmoji;
    private javax.swing.JLabel lblMensagem;
    private javax.swing.JLabel lblModo;
    private javax.swing.JScrollPane scrConversa;
    private javax.swing.JComboBox<String> selectEmoji;
    private javax.swing.JComboBox<String> selectModo;
    // End of variables declaration//GEN-END:variables
}
