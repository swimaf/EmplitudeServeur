package controllers;

import models.City;
import models.Groups;
import models.School;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * Created by martinet on 07/08/16.
 */
public class ApiController extends Controller {

    public Result getCities() {
        return ok(Json.toJson(City.find.orderBy("name asc").findList()));
    }

    public Result getSchools(String city) {
        return ok(Json.toJson(School.find.where().eq("city.name", city).orderBy("name asc").findList()));
    }

    public Result getGroups(Integer school) {
        return ok(Json.toJson(Groups.find.where().eq("school.id", school).orderBy("name asc").findList()));
    }


}
