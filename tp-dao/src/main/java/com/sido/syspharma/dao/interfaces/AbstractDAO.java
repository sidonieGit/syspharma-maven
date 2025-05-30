// src/com/sido/syspharma/dao/AbstractDAO.java
package com.sido.syspharma.dao.interfaces;

import com.sido.syspharma.dao.database.ConnexionDB;
import com.sido.syspharma.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class AbstractDAO {
    protected Connection connection;

    /**
     * Prépare une requête SQL avec une connexion récupérée via ConnexionDB.
     * @param sql la requête SQL
     * @return PreparedStatement prêt à l’emploi
     * @throws SQLException
     * @throws DatabaseException
     */
    protected PreparedStatement prepareStatement(String sql) throws SQLException, DatabaseException {
         connection = ConnexionDB.getConnection();
        return connection.prepareStatement(sql);
    }

    /**
     * Ferme la connexion active si elle existe
     */
    protected void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la fermeture de la connexion : " + e.getMessage());
        }
    }
}
