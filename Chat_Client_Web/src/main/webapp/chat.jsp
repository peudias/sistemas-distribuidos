<%-- 
    Document   : chat
    Created on : Oct 15, 2026, 7:41:06 AM
    Author     : phpdi
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sala de Bate-Papo</title>
    </head>
    <body>
        <% // Scriptlet
           /* out.print("Nickname: " + request.getParameter("txtNickname") 
                        + "<br>RadCor: " + request.getParameter("radCor")
                        + "<br>RadAvatar: " + request.getParameter("radAvatar")); */
           session.setAttribute("nickname", request.getParameter("txtNickname"));           
           session.setAttribute("cor", request.getParameter("radCor"));
           session.setAttribute("avatar", request.getParameter("radAvatar"));
           
           /* out.print("nickname: " + session.getAttribute("nickname")
                        + "<br>cor: " + session.getAttribute("cor")
                        + "<br>avatar: " + session.getAttribute("avatar"));*/   
        %>
        <iframe src="topo.jsp" name="topo" width="100%" height="20%"></iframe>
        <iframe src="conversa.jsp" name="conversa" width="100%" height="600px"></iframe>
        <iframe src="mensagem.jsp" name="mensagem" width="100%" height="20%"></iframe>
    </body>
</html>
