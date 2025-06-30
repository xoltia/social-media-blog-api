package Service;

import java.util.List;

import DAO.MessageDAO;
import Model.Message;
import Util.ValidationException;

public class MessageService {
    private static MessageService instance;

    public static MessageService getInstance() {
        if (instance == null)
            instance = new MessageService();
        return instance;
    }

    public Message get(int id) throws Exception {
        MessageDAO dao = MessageDAO.getInstance();
        Message message = dao.getById(id);
        return message;
    }

    public Message delete(int id) throws Exception {
        MessageDAO dao = MessageDAO.getInstance();
        Message message = dao.getById(id);
        if (message != null) {
            dao.deleteById(id);
        }
        return message;
    }

    public Message update(int id, String newText) throws Exception {
        if (newText.length() == 0 || newText.length() >= 255)
            throw new ValidationException("Message must be from 1 to 254 characters");
        MessageDAO dao = MessageDAO.getInstance();
        dao.update(id, newText);
        return dao.getById(id);
    }

    public Message create(int postedBy, String text, long timePosted) throws Exception {
        AccountService accountService = AccountService.getInstance();
        if (!accountService.exists(postedBy))
            throw new ValidationException("Invalid user");
        if (text.length() == 0 || text.length() >= 255)
            throw new ValidationException("Message must be from 1 to 254 characters");
        MessageDAO dao = MessageDAO.getInstance();
        return dao.create(postedBy, text, timePosted);
    }

    public List<Message> getAll() throws Exception {
        MessageDAO dao = MessageDAO.getInstance();
        return dao.getAll();
    }

    public List<Message> getAllByUser(int userID) throws Exception {
        MessageDAO dao = MessageDAO.getInstance();
        return dao.getAllByUser(userID);
    }
}
