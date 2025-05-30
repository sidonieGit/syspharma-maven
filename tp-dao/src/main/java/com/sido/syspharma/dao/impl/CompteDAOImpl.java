package com.sido.syspharma.dao.impl;

import com.sido.syspharma.dao.interfaces.AbstractDAO;
import com.sido.syspharma.dao.interfaces.ICompteDAO;
import com.sido.syspharma.domaine.enums.Role;
import com.sido.syspharma.domaine.enums.StatutCompte;
import com.sido.syspharma.domaine.model.Compte;
import com.sido.syspharma.exceptions.DatabaseException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DAO pour la gestion des comptes utilisateur.
 */
public class CompteDAOImpl extends AbstractDAO implements ICompteDAO {

    @Override
    public Compte findByEmail(String email) throws DatabaseException {
        String sql = "SELECT * FROM compte WHERE email = ?";

        try (PreparedStatement stmt = prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Compte compte = new Compte(
                        rs.getString("email"),
                        rs.getString("password"),
                        Role.valueOf(rs.getString("role"))
                );
                compte.setStatut(StatutCompte.valueOf(rs.getString("statut")));
                return compte;
            }

        } catch (SQLException e) {
            throw new DatabaseException("Erreur lors de la récupération du compte", e);
        } finally {
            closeConnection();
        }

        return null;
    }

    @Override
    public boolean update(Compte compte) throws DatabaseException {
        String sql = "UPDATE compte SET password = ?, role = ?, statut = ? WHERE email = ?";

        try (PreparedStatement stmt = prepareStatement(sql)) {
            stmt.setString(1, compte.getMotDePasse());
            stmt.setString(2, compte.getRole().name());
            stmt.setString(3, compte.getStatut().name());
            stmt.setString(4, compte.getIdentifiant());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DatabaseException("Erreur lors de la mise à jour du compte", e);
        } finally {
            closeConnection();
        }
    }
}
