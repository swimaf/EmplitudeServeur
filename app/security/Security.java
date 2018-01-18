package security;

import controllers.HomeController;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import views.html.error;

/**
 * Created by martinet on 31/07/16.
 */
public class Security extends Action.Simple {

    public CompletionStage<Result> call(Http.Context ctx) {
        if(HomeController.connected()){
            return delegate.call(ctx);
        }else{
            Result result = unauthorized(error.render("401","Vous n'êtes pas administrateur du site."));
            return CompletableFuture.completedFuture(result);
        }
    }


}