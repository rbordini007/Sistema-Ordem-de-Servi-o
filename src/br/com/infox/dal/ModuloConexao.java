package br.com.infox.dal;

import java.sql.*;

public class ModuloConexao {
    // metodo responsavel por estabelecer a conexao com banco

    public static Connection conector() {
        java.sql.Connection conexao = null;

        String driver = "com.mysql.jdbc.Driver";
        //String url = "jdbc:mysql://192.168.1.20:3306/dbinfox";
        String url = "jdbc:mysql://localhost:3306/dbinfox";
        String user = "root";
        String password = "";

        //estabelecendo conexao
        try {
            Class.forName(driver);
            conexao = DriverManager.getConnection(url, user, password);
            return conexao;
        } catch (Exception e) {
            //System.out.println(e);
            return null;
        }
    }
}
