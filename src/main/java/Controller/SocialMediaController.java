package Controller;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import Util.ValidationException;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::registerHandler);
        app.post("/login", this::loginHandler);
        app.post("/messages", this::messagePostHandler);
        app.delete("/messages/{id}", this::messageDeleteHandler);
        app.get("/messages", this::messageGetAllHandler);
        app.get("/messages/{id}", this::messageGetHandler);
        app.patch("/messages/{id}", this::messagePatchHandler);
        app.get("/accounts/{user_id}/messages", this::messageGetAllUserHandler);

        return app;
    }

    private void messagePatchHandler(Context context) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            int id = Integer.parseInt(context.pathParam("id"));
            MessageService service = MessageService.getInstance();
            Message messageBody = mapper.readValue(context.body(), Message.class);
            Message message = service.update(id, messageBody.getMessage_text());
            if (message != null)
                context.json(message);
            else
                context.status(400);
        } catch (NumberFormatException|ValidationException e) {
            context.status(400);
        } catch (Exception e) {
            context.status(500);
        }
    }

    private void messageGetHandler(Context context) {
        try {
            int id = Integer.parseInt(context.pathParam("id"));
            MessageService service = MessageService.getInstance();
            Message message = service.get(id);
            if (message != null)
                context.json(message);
            context.status(200);
        } catch (NumberFormatException e) {
            context.status(400);
        } catch (Exception e) {
            context.status(500);
        }
    }

    private void messageGetAllHandler(Context context) {
        try {
            MessageService service = MessageService.getInstance();
            List<Message> messages = service.getAll();
            context.json(messages);
        } catch (Exception e) {
            context.status(500);
        }
    }

    private void messageGetAllUserHandler(Context context) {
        try {
            int userID = Integer.parseInt(context.pathParam("user_id"));
            MessageService service = MessageService.getInstance();
            List<Message> messages = service.getAllByUser(userID);
            context.json(messages);
        } catch (NumberFormatException e) {
            context.status(400);
        } catch (Exception e) {
            context.status(500);
        }
    }

    private void messageDeleteHandler(Context context) {
        try {
            int id = Integer.parseInt(context.pathParam("id"));
            MessageService service = MessageService.getInstance();
            Message message = service.delete(id);
            if (message != null)
                context.json(message);
            context.status(200);
        } catch (NumberFormatException e) {
            context.status(400);
        } catch (Exception e) {
            context.status(500);
        }
    }

    private void messagePostHandler(Context context) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Message newMessage = mapper.readValue(context.body(), Message.class);
            MessageService service = MessageService.getInstance();
            Message message = service.create(
                newMessage.getPosted_by(),
                newMessage.getMessage_text(),
                newMessage.getTime_posted_epoch()
            );
            context.json(message);
        } catch (ValidationException e) {
            context.status(400);
        } catch (Exception e) {
            context.status(500);
        }
    }

    private void loginHandler(Context context) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Account credentials = mapper.readValue(context.body(), Account.class);

            AccountService service = AccountService.getInstance();
            Account account = service.login(credentials.getUsername(), credentials.getPassword());
            if (account == null) {
                context.status(401);
                return;
            }
            context.json(account);
        } catch (Exception e) {
            context.status(500);
        }
    }

    private void registerHandler(Context context) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Account newAccount = mapper.readValue(context.body(), Account.class);
            AccountService service = AccountService.getInstance();
            Account account = service.register(newAccount.getUsername(), newAccount.getPassword());
            context.json(account);
        } catch (ValidationException e) {
            context.status(400);
        } catch (Exception e) {
            context.status(500);
        }
    }
}