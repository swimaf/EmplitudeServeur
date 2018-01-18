package controllers;

import com.google.inject.Inject;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import views.html.script;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.concurrent.CompletionStage;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by martinet on 10/09/16.
 */
@With(security.Security.class)
public class DataController extends Controller {

    @Inject
    WSClient ws;

    @Inject
    FormFactory formFactory;

    public Result index() {
        return ok(script.render());
    }

    public CompletionStage<Result> saveNantes() {
        DynamicForm requestData = formFactory.form().bindFromRequest();
        String url = requestData.get("url");

        WSRequest request = ws.url(url)
                .setMethod("GET")
                .setAuth("E166178D","WrrdLLpg");
        return request.get().thenApply(response -> saveData(response.getBody(), Integer.parseInt(requestData.get("school.id")), requestData.get("authentification") != null));
    }

    public Result saveData(String res, Integer school, Boolean auth) {
        Pattern p = Pattern.compile("<option value=\"(.*).html\">(.*)</option>", Pattern.MULTILINE);
        Matcher m = p.matcher(res);

        Connection c = null;
        PreparedStatement pstmt = null;

        try {
            String url = "jdbc:postgresql://emplitude.info/emplitude";
            Class.forName("org.postgresql.Driver").newInstance();
            c = DriverManager.getConnection(url, "postgres", "postgres");
            pstmt = c.prepareStatement("INSERT INTO groups (name, identifiant, authentification, school_id) VALUES(?, ?, ?, ?)");
            while(m.find()) {
                pstmt.setString(1, m.group(2));
                pstmt.setString(2, m.group(1));
                pstmt.setBoolean(3, auth);
                pstmt.setInt(4, school);
                int i = pstmt.executeUpdate();
            }

        }catch(Exception e){
            e.printStackTrace();
        } finally {
            try { if (pstmt != null) pstmt.close(); } catch (Exception e) {};
            try { if (c != null) c.close(); } catch (Exception e) {};
        }

        return ok("Succes");
    }
}
