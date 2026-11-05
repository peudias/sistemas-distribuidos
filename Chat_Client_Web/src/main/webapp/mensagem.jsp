<%-- 
    Document   : mensagem
    Created on : Oct 15, 2026, 7:48:11 AM
    Author     : phpdi
--%>

<%@page import="java.net.HttpURLConnection"%>
<%@page import="java.net.URI"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <% // Scriptlet
            if(request.getParameter("btnEnviar") != null){
                String msg = "";
                msg += "<img src=\"" + session.getAttribute("Avatar") + "\" width=\"30\" height=\"30\">";
                msg += "<font size=\"+1\" color=\"" + session.getAttribute("cor") + "\"><b>" + session.getAttribute("nickname") + "</font></b>";
                msg += " diz: " + request.getParameter("txtMsg") + "<img src=\"" + request.getParameter("cbEmoji") + "\" width=\"30\" height=\"30\">";
                msg += "<br>";
            
                try{
                    HttpURLConnection conexao = (HttpURLConnection) URI.create("http://127.0.0.1:6664/mensagens").toURL().openConnection();
                    conexao.setReadTimeout(3000);
                    conexao.setRequestMethod("POST");
                    conexao.setDoOutput(true);
                    conexao.getOutputStream().write(msg.getBytes("UTF-8"));
                    int status = conexao.getResponseCode();
                    conexao.disconnect();
                    if(status != 200){
                        out.print("<p style='color:red'>A mensagem não foi gravada no servidor. Tente enviar novamente.</p>");
                    }
                } catch(Exception error){
                    out.print("<p style='color:red'>Servidor em manutenção... aguarde.</p>");
                }
            }
        %>
        <form name="frmMsg" method="post" actions="mensagem.jsp">
            <label for="txtMsg">Mensagem:</label>
            <input type="text" name ="txtMsg" size="220" maxlength="200">
            <label for="cbEmoji">Emoji:</label>
            <select name="cbEmoji">
                <option value="">Selecione</option>
                <option value="images/coracao.png">Coração</option>
                <option value="images/beijo.png">Beijo</option>
                <option value="images/dinheiro.png">Dinheiro</option>
            </select>
            <input type="submit" name="btnEnviar" value="Enviar">
        </form>
    </body>
</html>
