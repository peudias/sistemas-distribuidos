
package pacote;

public class Util {
    public static String nickname = "";    
    public static String cor = "";    
    public static String avatar = "";
    // Item 6: endereço do Middleware; trocado pelo argumento do java -jar para rodar em rede
    public static String servidor = "localhost";
    public static String urlFigurinhas = "http://localhost:8080/figurinhas/";
    public static String urlLogs = "http://localhost:6665/logs/desktop";

    // Envia a exceção ao coletor em outra thread, para não travar a tela.
    // Uma falha aqui só é mostrada no console: o erro do log não pode derrubar o chat.
    public static void enviarErro(String classeMetodo, Exception erro) {
        Thread.startVirtualThread(() -> {
            try {
                java.net.InetAddress maquina = java.net.InetAddress.getLocalHost();
                String registro = maquina.getHostName() + " | " + maquina.getHostAddress() + " | " + classeMetodo + " | " + erro;
                java.net.HttpURLConnection conexao = (java.net.HttpURLConnection) java.net.URI.create(urlLogs).toURL().openConnection();
                conexao.setConnectTimeout(2000);
                conexao.setReadTimeout(2000);
                conexao.setRequestMethod("POST");
                conexao.setDoOutput(true);
                conexao.getOutputStream().write(registro.getBytes("UTF-8"));
                conexao.getResponseCode();
                conexao.disconnect();
            } catch (Exception error) {
                System.out.println("Util::enviarErro: Error " + error.getMessage());
            }
        });
    }
}
