package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;

import Model.Account;
import Util.ConnectionUtil;

public class AccountDAO {
    // Singletone instance.
    private static AccountDAO instance;

    public static AccountDAO getInstance() {
        if (instance == null)
            instance = new AccountDAO();
        return instance;
    }

    public Account getById(int id) throws SQLException {
        Connection conn = ConnectionUtil.getConnection();
        PreparedStatement stmt = conn.prepareStatement(
            "SELECT account_id, username, password FROM account WHERE account_id = ?"
        );
        stmt.setInt(1, id);
        stmt.executeQuery();

        ResultSet result = stmt.getResultSet();
        if (!result.next()) {
            return null;
        }

        Account account = new Account();
        account.setAccount_id(result.getInt(1));
        account.setUsername(result.getString(2));
        account.setPassword(result.getString(3));
        return account;
    }


    public Account getByUsername(String username) throws SQLException {
        Connection conn = ConnectionUtil.getConnection();
        PreparedStatement stmt = conn.prepareStatement(
            "SELECT account_id, username, password FROM account WHERE username = ?"
        );
        stmt.setString(1, username);
        stmt.executeQuery();

        ResultSet result = stmt.getResultSet();
        if (!result.next()) {
            return null;
        }

        Account account = new Account();
        account.setAccount_id(result.getInt(1));
        account.setUsername(result.getString(2));
        account.setPassword(result.getString(3));
        return account;
    }

    public Account create(String username, String password) throws SQLException {
        Connection conn = ConnectionUtil.getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO account (username, password) VALUES (?, ?)",
                Statement.RETURN_GENERATED_KEYS
            );

            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.executeUpdate();

            
            ResultSet result = stmt.getGeneratedKeys();
            result.next();

            Account account = new Account();
            account.setAccount_id(result.getInt(1));
            account.setUsername(username);
            account.setPassword(password);

            return account;
        } catch (SQLIntegrityConstraintViolationException e) {
            return null;
        }
    }
}
