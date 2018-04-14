package server.module.data_access.dao;

import shared.entity.UserAccount;

public interface UserDAO {

	void addUser(UserAccount userAccount);

	boolean checkLogin(String login);

	public UserAccount getUser(String login);

	boolean checkAccountData(UserAccount userAccount);
}