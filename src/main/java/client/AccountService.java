package client;

import shared.entity.CheckResult;
import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;

import javax.ws.rs.*;
import java.util.List;

@Path("/api/rest")
public interface AccountService extends RestService {

	@POST
	@Path("register/{login}/{password}")
	void registerUser(@PathParam("login") String login, @PathParam("password") String password, MethodCallback<Boolean> callback);

	@POST
	@Path("auth/{login}/{password}")
	void logIn(@PathParam("login") String login, @PathParam("password") String password, MethodCallback<Integer> callback);

	@POST
	@Path("logOut/{login}/{sid}")
	void logOut(@PathParam("login") String login, @PathParam("sid") Integer sid, MethodCallback<Boolean> callback);

	@GET
	@Path("checks/")
	void getAllHits(MethodCallback<List<CheckResult>> callback);

	@POST
	@Path("auths/")
	void auth(MethodCallback methodCallback);

	@DELETE
	@Path("checks/delete")
	void deleteAllUserHits(MethodCallback callback);

}
