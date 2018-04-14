package server;

import server.module.beans.CheckBean;
import server.module.beans.UserBean;
import server.module.data_access.dao.DBService;
import shared.entity.CheckResult;
import shared.entity.UserAccount;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Stateless
@Path("rest")
public class ServiceServer {

	private ConcurrentMap<String,Integer> sessions = new ConcurrentHashMap<>();

	@EJB
	private UserBean userBean;

	@EJB
	private CheckBean checkBean;

	public ServiceServer() {
	}

	@POST
	@Produces("application/json")
	@Path("register/{login}/{password}")
	public Boolean registerUser(@PathParam("login") String login, @PathParam("password") String password) throws Exception {
		UserAccount userAccount = new UserAccount();

		MessageDigest md = MessageDigest.getInstance("SHA-1");
		byte[] passwordHash = md.digest(password.getBytes());
		password = bytesToHex(passwordHash);

		userAccount.setLogin(login);
		userAccount.setPassword(password);
		if (!userBean.checkLogin(userAccount.getLogin())) {
			userBean.addUser(userAccount);
			return true;
		}

		return false;
	}

	@POST
	@Produces("application/json")
	@Path("auth/{login}/{password}")
	public Integer logIn(@PathParam("login") String login, @PathParam("password") String password) throws Exception {
		Random random = new Random();
		UserAccount userAccount = new UserAccount();
		Integer sid = random.nextInt(Integer.MAX_VALUE);
		sessions.put(login,sid);

		MessageDigest md = MessageDigest.getInstance("SHA-1");
		byte[] passwordHash = md.digest(password.getBytes());
		password = bytesToHex(passwordHash);

		userAccount.setLogin(login);
		userAccount.setPassword(password);
		return userBean.authorize(userAccount) ? sid : -1;
	}

	@POST
	@Path("logOut/{login}/{sid}")
	@Produces("application/json")
	public Boolean logOut(@PathParam("login") String login, @PathParam("sid") Integer sid) {
		for (String s : sessions.keySet()) {
			if(s.equals(login) && sessions.get(s).equals(sid)) {
				sessions.remove(s);
				return true;
			}
		}
		return false;
	}


	@GET
	@Produces("application/json")
	@Path("checks")
	public List<CheckResult> getAllHits(@CookieParam("login") String login, @CookieParam("sid") Integer sid) {
		for (String s : sessions.keySet()) {
			if(s.equals(login) && sessions.get(s).equals(sid)){
				UserAccount account = userBean.getUser(login);
				List<CheckResult> checkResults =  userBean.getAllUserHits(account);
				for (CheckResult checkResult : checkResults) {
					checkResult.setOwner(null);
				}
				return checkResults;
			}
		}
		return null;
	}

	@DELETE
	@Path("checks/delete/")
	public void deleteAllUserHits(@CookieParam("login") String login, @CookieParam("sid") Integer sid) {
		for (String s : sessions.keySet()) {
			if(s.equals(login) && sessions.get(s).equals(sid)){
				UserAccount account = userBean.getUser(login);
				userBean.deleteAllUserHits(account);
			}
		}
	}

	@POST
	@Path("hits")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public CheckResult checkHit(@CookieParam("login") String login, @CookieParam("sid") Integer sid ,CheckResult checkResult) {
		for (String s : sessions.keySet()) {
			if(s.equals(login) && sessions.get(s).equals(sid)){
				UserAccount account = userBean.getUser(login);
				checkResult.setOwner(account);
				CheckResult res = checkBean.addCheck(checkResult);
				res.setOwner(null);
				return res;
			}
		}
		return null;
	}

	@POST
	@Path("auths")
	public void auth(@CookieParam("login") String login, @CookieParam("sid") Integer sid ) {
		for (String s : sessions.keySet()) {
			if(s.equals(login) && sessions.get(s).equals(sid)){
				return;
			}
		}
		throw new RuntimeException();
	}


	@POST
	@Path("/justhit")
	@Consumes(MediaType.APPLICATION_JSON)
	public void justHit(@CookieParam("login") String login, @CookieParam("sid") Integer sid , BigDecimal r) {
		for (String s : sessions.keySet()) {
			if(s.equals(login) && sessions.get(s).equals(sid)){
				UserAccount userAccount = userBean.getUser(login);
				checkBean.updateChecks(userAccount,r);
			}
		}
	}

	@GET
	@Path("/getpoints")
	@Produces(MediaType.APPLICATION_JSON)
	@Deprecated
	public List<CheckResult> getPoints() {
		return DBService.getInstance().getChecks();
	}

	private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
	private String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for ( int j = 0; j < bytes.length; j++ ) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}
}
