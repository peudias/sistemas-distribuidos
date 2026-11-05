<%-- 
    Document   : conversa
    Created on : Oct 15, 2026, 7:48:05 AM
    Author     : phpdi
--%>

<%@page import="java.net.HttpURLConnection"%>
<%@page import="java.net.URI"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/enviarErro.jspf"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta http-equiv="refresh" content="5;conversa.jsp">
        <title>JSP Page</title>
    </head>
    <body>
        <%
            try{
                HttpURLConnection conexao = (HttpURLConnection) URI.create("http://127.0.0.1:6663/mensagens").toURL().openConnection();
                conexao.setReadTimeout(3000);
                String conversa = new String(conexao.getInputStream().readAllBytes(), "UTF-8");
                conexao.disconnect();
                out.print(conversa);
                session.removeAttribute("erroConversa");
            } catch(Exception error){
                out.print("<h2 style='color:red'>Servidor em manutenção... aguarde.</h2>");
                // Logs: registra só a primeira falha, porque a página recarrega a cada 5 segundos
                if(session.getAttribute("erroConversa") == null){
                    session.setAttribute("erroConversa", "sim");
                    enviarErro(request, "conversa.jsp:buscarMensagens", error);
                }
            }
        %>
    </body>
</html>
