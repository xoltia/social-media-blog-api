package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Model.Message;
import Util.ConnectionUtil;

public class MessageDAO {
    private static MessageDAO instance;
    
    public static MessageDAO getInstance() {
        if (instance == null)
            instance = new MessageDAO();
        return instance;
    }

    public Message create(int postedBy, String text, long timePosted) throws SQLException {
        Connection conn = ConnectionUtil.getConnection();

        PreparedStatement stmt = conn.prepareStatement(
            "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)",
            Statement.RETURN_GENERATED_KEYS
        );

        stmt.setInt(1, postedBy);
        stmt.setString(2, text);
        stmt.setLong(3, timePosted);
        stmt.executeUpdate();
        
        ResultSet result = stmt.getGeneratedKeys();
        result.next();

        Message message = new Message();
        message.setMessage_id(result.getInt(1));
        message.setMessage_text(text);
        message.setPosted_by(postedBy);
        message.setTime_posted_epoch(timePosted);
        return message;
    }

    public void update(int id, String text) throws SQLException {
        Connection conn = ConnectionUtil.getConnection();
        PreparedStatement stmt = conn.prepareStatement(
            "UPDATE message SET message_text = ? WHERE message_id = ?"
        );
        stmt.setString(1, text);
        stmt.setInt(2, id);
        stmt.executeUpdate();
    }

    public Message getById(int id) throws SQLException {
        Connection conn = ConnectionUtil.getConnection();
        PreparedStatement stmt = conn.prepareStatement(
            "SELECT message_id, posted_by, message_text, time_posted_epoch FROM message WHERE message_id = ?"
        );
        stmt.setInt(1, id);
        stmt.executeQuery();

        ResultSet result = stmt.getResultSet();
        if (!result.next()) {
            return null;
        }

        Message message = new Message();
        message.setMessage_id(result.getInt(1));
        message.setPosted_by(result.getInt(2));
        message.setMessage_text(result.getString(3));
        message.setTime_posted_epoch(result.getLong(4));
        return message;
    }
 
    public void deleteById(int id) throws SQLException {
        Connection conn = ConnectionUtil.getConnection();
        PreparedStatement stmt = conn.prepareStatement(
            "DELETE FROM message WHERE message_id = ?"
        );
        stmt.setInt(1, id);
        stmt.executeUpdate();
    }

    public List<Message> getAll() throws SQLException {
        Connection conn = ConnectionUtil.getConnection();
        PreparedStatement stmt = conn.prepareStatement(
            "SELECT message_id, posted_by, message_text, time_posted_epoch FROM message"
        );
        stmt.executeQuery();
        
        ResultSet result = stmt.getResultSet();
        ArrayList<Message> messages = new ArrayList<>();

        while (result.next()) {
            Message message = new Message();
            message.setMessage_id(result.getInt(1));
            message.setPosted_by(result.getInt(2));
            message.setMessage_text(result.getString(3));
            message.setTime_posted_epoch(result.getLong(4));
            messages.add(message);
        }

        return messages;
    }

    public List<Message> getAllByUser(int userId) throws SQLException {
        Connection conn = ConnectionUtil.getConnection();
        PreparedStatement stmt = conn.prepareStatement(
            "SELECT message_id, posted_by, message_text, time_posted_epoch FROM message WHERE posted_by = ?"
        );
        stmt.setInt(1, userId);
        stmt.executeQuery();
        
        ResultSet result = stmt.getResultSet();
        ArrayList<Message> messages = new ArrayList<>();

        while (result.next()) {
            Message message = new Message();
            message.setMessage_id(result.getInt(1));
            message.setPosted_by(result.getInt(2));
            message.setMessage_text(result.getString(3));
            message.setTime_posted_epoch(result.getLong(4));
            messages.add(message);
        }

        return messages;
    }
}
