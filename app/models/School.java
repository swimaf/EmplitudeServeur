package models;


import com.avaje.ebean.Expr;
import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import play.data.validation.Constraints;
import play.data.validation.ValidationError;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class School extends Model {

    @Id
    private Integer id;

    private String name;

    @OneToMany(fetch = FetchType.LAZY) @JsonBackReference
    @OrderBy("name asc")
    private List<Groups> groups;

    @ManyToOne(fetch = FetchType.LAZY) @JsonBackReference
    private City city;

    private String url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Groups> getGroups() {
        return groups;
    }

    public void setGroups(List<Groups> groups) {
        this.groups = groups;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public List<ValidationError> validate() {
        List<ValidationError> errors = new ArrayList<ValidationError>();
        School school = School.find.where().and(Expr.eq("city.name", getCity().getName()) , Expr.eq("name", name)).findUnique();
        if (school != null) {
            errors.add(new ValidationError("message", "L'établissement existe déjà."));
        }
        return errors.isEmpty() ? null : errors;
    }

    public static Finder<Integer, School> find = new Finder<>(School.class);

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
