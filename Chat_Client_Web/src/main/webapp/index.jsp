<%-- 
    Document   : index
    Created on : Oct 14, 2026, 9:37:59 AM
    Author     : phpdi
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Chat Distribuído WEB</title>
    </head>
    <body>
        <form action="chat.jsp" method="post">
            <br><br><br>
            <center>
                <table border="2">
                    <tr>
                        <td 
                            colspan="2" 
                            bgcolor="crimson" 
                            align="center"> 
                            <font size="+2" color="white"> Chat Web 1.0 </td>
                    </tr>
                    <tr>
                        <td align="right"><b>Nickname:</b></td>
                        <td>
                            <input 
                                type="text" 
                                name="txtNickname" 
                                maxlength="20"
                                size="100"
                                >
                        </td>
                    </tr>
                    <tr>
                        <td align="right"><b>Cor:</b></td>
                        <td>
                            <input type="radio" name="radCor" value="navy" checked>
                            <font color="navy"><b>Azul</b></font>

                            <input type="radio" name="radCor" value="orange">
                            <font color="orange"><b>Laranja</b></font>

                            <input type="radio" name="radCor" value="purple">
                            <font color="purple"><b>Roxo</b></font>
                        </td>
                    </tr>
                    <tr>
                        <td align="right"><b>Avatar:</b></td>
                        <td>
                            <input type="radio" name="radAvatar" value="AvatarHomem.png" checked>
                            <img src="/figurinhas/AvatarHomem.png" width="30" height="30">

                            <input type="radio" name="radAvatar" value="AvatarIndeterminado.png">
                            <img src="/figurinhas/AvatarIndeterminado.png" width="30" height="30">

                            <input type="radio" name="radAvatar" value="AvatarMulher.png">
                            <img src="/figurinhas/AvatarMulher.png" width="30" height="30">
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2" align="center">
                            <input type="submit" name="btnEntrar" value="Entrar">
                        </td>
                    </tr>
                </table>
            </center>
        </form>
    </body>
</html>
