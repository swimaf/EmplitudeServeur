package models;

import com.avaje.ebean.Expr;
import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import play.data.validation.Constraints;
import play.data.validation.ValidationError;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by martinet on 27/07/16.
 */
@Entity
public class Groups extends Model {

    @Id
    private Integer id;

    private String name;

    @Constraints.Required
    private String identifiant;

    private Boolean authentification;

    @ManyToOne(fetch = FetchType.LAZY) @JsonBackReference
    private School school;


    public String getIdentifiant() {
        return identifiant;
    }

    public void setIdentifiant(String identifiant) {
        this.identifiant = identifiant;
    }

    public Boolean getAuthentification() {
        return authentification == null ? false : authentification;
    }

    public void setAuthentification(Boolean authentification) {
        this.authentification = authentification;
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ValidationError> validate() {
        List<ValidationError> errors = new ArrayList<ValidationError>();
        Groups groups = Groups.find.where().and(Expr.eq("school.id", school.getId()), Expr.or(Expr.eq("name", name), Expr.eq("identifiant", identifiant))).findUnique();
        if (groups != null) {
            errors.add(new ValidationError("message", "Le groupe existe déjà."));
        }
        return errors.isEmpty() ? null : errors;
    }
    public static Finder<Integer, Groups> find = new Finder<>(Groups.class);

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
