package server.module.interfaces;

import shared.entity.CheckResult;
import shared.entity.UserAccount;

import javax.ejb.Local;
import java.util.List;

@Local
public interface UserBeanLocal {

	boolean checkLogin(String login);

	boolean authorize(UserAccount userAccount);

	void addUser(UserAccount userAccount);

	List<CheckResult> getAllUserHits(UserAccount userAccount);

	void deleteAllUserHits(UserAccount userAccount);

}
