package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.h2.engine.User;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import play.libs.XPath;
import scala.xml.XML;
import com.avaje.ebean.Model;
import com.google.inject.Inject;
import models.City;
import models.Groups;
import models.School;
import models.Users;
import play.data.DynamicForm;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import views.html.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.concurrent.CompletionStage;

/**
 * Created by martinet on 31/07/16.
 */
@With(security.Security.class)
public class AdministratorController extends Controller {

    @Inject
    WSClient ws;

    @Inject
    FormFactory formFactory;


    public CompletionStage<Result> nantes(){
        WSRequest request = ws.url("https://edt.univ-nantes.fr/sciences/g6868.ics")
                .setMethod("GET")
                .setAuth("E166178D","WrrdLLpg");
        return request.get()
                .thenApply(response -> ok(response.getBody()).as("html"));
    }


    public Result administrator() {
        return ok(administrator.render(Users.find.all()));
    }

    public Result city() {
        return ok(map.render(Json.toJson(City.find.all())));
    }

    public Result newCity() {
        return newModel(City.class, "La ville existe déjà.");
    }

    public Result newGroup() {
        return newModel(Groups.class, "Le groupe existe déjà.");
    }

    public Result newSchool() {
        return newModel(School.class, "L'etablissement existe déjà.");
    }

    public Result newUser() {
        return newModel(Users.class, "Le login existe déjà.");
    }

    public Result newModel(Class model, String defaultError){
        Form<Model> login ;
        try {
            login = formFactory.form(model).bindFromRequest();
        } catch (Exception e) {
            return badRequest(defaultError);
        }
        if (login.hasErrors()) {
            return badRequest(String.valueOf(login.error("message").message()));
        } else {
            login.get().save();
            return redirect(request().getHeader("referer"));
        }
    }

    public Result getCity(String id) {
        return ok(city_information.render(City.find.byId(id)));
    }

    public Result getSchool(Integer id) {
        return ok(group.render(School.find.byId(id)));
    }

    public Result groupDelete() {
        DynamicForm requestData = formFactory.form().bindFromRequest();
        Iterator iterator = requestData.value().get().getData().keySet().iterator();
        while (iterator.hasNext()) {
            Groups.find.byId(Integer.parseInt(iterator.next().toString())).delete();
        }
        return redirect(request().getHeader("referer"));
    }

    public Result deleteUser(String login) {
        Users users = Users.find.byId(login);
        users.delete();
        return redirect(request().getHeader("referer"));
    }

    public Result editPassword() {
        DynamicForm requestData = formFactory.form().bindFromRequest();
        Users users = Users.find.byId(session().get("login"));
        if(requestData.get("password").equals(requestData.get("password2"))) {
            users.setPassword(requestData.get("password"));
            users.update();
        }
        flash().put("error", "Les mots de passe ne sont pas identique");
        return redirect(request().getHeader("referer"));


    }

    public Result addUrl(Integer id) {
        DynamicForm requestData = formFactory.form().bindFromRequest();
        School school = School.find.byId(id);
        school.setUrl(requestData.get("url"));
        school.update();

        return redirect(request().getHeader("referer"));
    }

    public static Document fromInputStream(InputStream in, String encoding) {
        try {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();

            InputSource is = new InputSource(in);
            is.setEncoding(encoding);

            return builder.parse(is);

        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public Result validSchool(String city){
        InputStream inputStream = play.Play.application().resourceAsStream("public/xml/etablissement.xml");
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode array = mapper.createArrayNode();
        Document dom = fromInputStream(inputStream,"utf-8");
        if (dom == null) {
            return badRequest("Expecting Xml data");
        } else {
            NodeList name = XPath.selectNodes("//etablissement[commune='"+city+"']/nom", dom);
            for(int i=0; i<name.getLength(); i++){
                array.add(name.item(i).getTextContent());
            }
            return ok(array.toString());
        }
    }

}
