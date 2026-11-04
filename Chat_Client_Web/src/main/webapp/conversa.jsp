<%-- 
    Document   : conversa
    Created on : Oct 15, 2026, 7:48:05 AM
    Author     : phpdi
--%>

<%@page import="java.io.ObjectInputStream"%>
<%@page import="java.net.Socket"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta http-equiv="refresh" content="5;conversa.jsp">
        <title>JSP Page</title>
    </head>
    <body>
        <%
            Socket socket = new Socket("127.0.0.1", 6661);
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            String conversa = input.readUTF();
            input.close();
            socket.close();
            out.print(conversa);
        %>
    </body>
</html>
