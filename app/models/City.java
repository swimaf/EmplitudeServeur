package models;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import play.data.validation.Constraints;
import play.data.validation.ValidationError;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by martinet on 27/07/16.
 */
@Entity
public class City extends Model{

    @Id
    private String name;

    @Constraints.Required
    private double lng;

    @Constraints.Required
    private double lat;

    private String photo;

    @OneToMany(fetch = FetchType.LAZY) @JsonBackReference
    @OrderBy("name asc")
    private List<School> schools;

    public static Finder<String, City> find = new Finder<>(City.class);

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public List<School> getSchools() {
        return schools;
    }

    public void setSchools(List<School> schools) {
        this.schools = schools;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public List<ValidationError> validate() {
        List<ValidationError> errors = new ArrayList<ValidationError>();
        City city = City.find.where().icontains("name", "%"+name+"%").findUnique();
        if (city != null) {
            errors.add(new ValidationError("message", "La ville existe déjà."));
        }
        return errors.isEmpty() ? null : errors;
    }
}
