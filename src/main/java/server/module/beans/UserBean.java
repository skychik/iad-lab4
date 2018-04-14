package server.module.beans;

import server.module.data_access.dao.DBService;
import shared.entity.CheckResult;
import shared.entity.UserAccount;

import javax.ejb.Stateless;
import java.util.List;


@Stateless
public class UserBean  {


	public UserBean() {
	}
	public UserAccount getUser(String login) {
		return DBService.getInstance().getUser(login);
	}

	public boolean checkLogin(String login) {
		return DBService.getInstance().checkLogin(login);
	}

	public boolean authorize(UserAccount userAccount) {
		return DBService.getInstance().authorize(userAccount);
	}

	public void addUser(UserAccount userAccount) {
		DBService.getInstance().addUser(userAccount);
	}

	public List<CheckResult> getAllUserHits(UserAccount userAccount) {
		return userAccount.getAllUserHits();
	}

	public void deleteAllUserHits(UserAccount userAccount) {
		DBService.getInstance().deleteAll(userAccount);
	}
}
