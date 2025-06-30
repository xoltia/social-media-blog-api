package Service;

import DAO.AccountDAO;
import Model.Account;
import Util.ValidationException;

public class AccountService {
    // Singletone instance.
    private static AccountService instance;

    public static AccountService getInstance() {
        if (instance == null)
            instance = new AccountService();
        return instance;
    }

    public boolean exists(int id) throws Exception {
        AccountDAO dao = AccountDAO.getInstance();
        return dao.getById(id) != null;
    }

    public Account login(String username, String password) throws Exception {
        AccountDAO dao = AccountDAO.getInstance();
        Account account = dao.getByUsername(username);
        if (account == null) return null;
        if (!account.getPassword().equals(password)) return null;
        return account;
    }

    public Account register(String username, String password) throws Exception {
        if (username.length() == 0)
            throw new ValidationException("Username must not be blank");
        if (password.length() < 4)
            throw new ValidationException("Password must be at least 4 characters long");
        AccountDAO dao = AccountDAO.getInstance();
        Account account = dao.create(username, password);
        if (account == null) throw new ValidationException("Username taken");
        return account;
    }
}
