<%-- 
    Document   : topo
    Created on : Oct 15, 2026, 7:47:46 AM
    Author     : phpdi
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <% // Scriptlet
            /*out.print("Nickname: " + request.getParameter("txtNickname") 
                        + "<br>RadCor: " + request.getParameter("radCor")
                        + "<br>RadAvatar: " + request.getParameter("radAvatar"));*/
            
            /*out.print("<br>nickname: " + session.getAttribute("nickname")
                        + "<br>cor: " + session.getAttribute("cor")
                        + "<br>avatar: " + session.getAttribute("avatar"));*/
            
            /*out.print("<img src=\"images/" + session.getAttribute("avatar") + "\" width=\"50\" height=\"50\">");*/
        %>
        <br>
        <img src="images/<%=session.getAttribute("avatar")%>" width="50" height="50">
        <font size="+2" color="<%=session.getAttribute("cor")%>">
            <%=session.getAttribute("nickname")%>
        </font>
    </body>
</html>
