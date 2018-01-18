package models;

import com.avaje.ebean.Model;
import play.data.validation.Constraints;
import play.data.validation.ValidationError;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by martinet on 31/07/16.
 */

@Entity
public class Users extends Model {

    @Id
    private String login;

    private String nom;

    private String prenom;

    @Constraints.Required
    private String password;

    @Constraints.Email
    private String email;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<ValidationError> validate() {
        List<ValidationError> errors = new ArrayList<ValidationError>();
        Users users = Users.find.byId(login);
        if (users != null) {
            errors.add(new ValidationError("message", "Le login existe déjà."));
        }
        return errors.isEmpty() ? null : errors;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public static Finder<String, Users> find = new Finder<>(Users.class);

}
