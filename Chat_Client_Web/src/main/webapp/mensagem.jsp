<%-- 
    Document   : mensagem
    Created on : Oct 15, 2026, 7:48:11 AM
    Author     : phpdi
--%>

<%@page import="java.io.ObjectOutputStream"%>
<%@page import="java.net.Socket"%>
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
            
                Socket socket = new Socket("127.0.0.1", 6662);
                ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                output.flush();
                output.writeUTF(msg);
                output.close();
                socket.close();
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
