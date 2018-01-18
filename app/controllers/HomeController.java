package controllers;

import com.google.inject.Inject;
import models.Users;
import play.data.DynamicForm;
import play.data.Form;
import play.data.FormFactory;
import play.data.validation.ValidationError;
import play.libs.mailer.Email;
import play.libs.mailer.MailerClient;
import play.mvc.*;
import views.html.*;

import java.util.Iterator;


/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {

    final private String DESTINATION = "e.martinet0@gmail.com";

    @Inject
    MailerClient mailerClient;

    @Inject
    FormFactory formFactory;



    public Result index() {
        if(connected()) {
            return redirect(routes.AdministratorController.administrator());
        }
        return ok(index.render("Empli'tude", null));
    }

    public Result email() {
        DynamicForm requestData = formFactory.form().bindFromRequest();

        Email email = new Email()
                .setSubject(requestData.get("object"))
                .addTo(DESTINATION)
                .setFrom("FROM <"+requestData.get("email")+">")
                .setBodyText(requestData.get("message"));

        mailerClient.send(email);
        return ok("Email envoyé avec succès.");
    }


    public Result init(){
        Users users = new Users();
        users.setLogin("ADMIN");
        users.setNom("LOGIN");
        users.setPrenom("PRENOM");
        users.setEmail("MAIL@gmail.com");
        users.setPassword("PASSWORD");
        users.save();
        return ok("Succès");
    }


    public Result connexion() {
        DynamicForm requestData = formFactory.form().bindFromRequest();
        String login = requestData.get("login");
        String password = requestData.get("password");
        Users users = Users.find.byId(login);
        if (users == null) {
            return badRequest("Le login n'existe pas.");
        } else if(!users.getPassword().equals(password)) {
            return badRequest("Le mot de passe est incorrect.");
        }
        session().clear();
        session("login", login);
        return redirect(routes.AdministratorController.administrator());
    }

    public Result logout() {
        session().clear();
        return redirect(routes.HomeController.index());
    }

    public static Boolean connected(){
        return session().containsKey("login");
    }

}
