/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.exemplo.db;

import static java.lang.System.out;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author phpdi
 */
public class ExemploDb {

    public static void main(String[] args) throws SQLException {
        // Conecta ao servidor de Banco de Dados
        Connection conexao = DriverManager.getConnection("jdbc:mysql://200.128.142.85:3307/teste", "root", "root");
        
        // Executa comandos no Banco de Dados
        Statement stm = conexao.createStatement();
        stm.execute("insert into clientes(nome, telefone) values ('ph', '010101')");
        stm.close();
        
        // Executa comando SQL no Banco de Dados com Retorno
        PreparedStatement pstm = conexao.prepareStatement("select nome, telefone from clientes where id > ? and  id < ?");
        pstm.setInt(1, 0);
        pstm.setInt(2, 1000);
        ResultSet rs = pstm.executeQuery();
        while(rs.next()){
            out.print("\n\nNome: " + rs.getString("Nome") +
                      "\nTelefone: " + rs.getString("Telefone"));
        }
        rs.close();
        pstm.close();
        conexao.close();
    }
}
