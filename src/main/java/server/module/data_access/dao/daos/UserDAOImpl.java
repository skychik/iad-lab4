package server.module.data_access.dao.daos;

import server.module.data_access.dao.UserDAO;
import shared.entity.UserAccount;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.ejb.Stateless;


@Stateless(name = "UserDAO")
public class UserDAOImpl implements UserDAO {

	private Session session;

	public UserDAOImpl(Session session) {
		this.session = session;
	}

	public UserDAOImpl() {
	}

	public void addUser(UserAccount userAccount) {
		session.save(userAccount);
	}

	public boolean checkLogin(String login) {
		Query query = session.createQuery("FROM UserAccount where login = :login");
		query.setParameter("login",login);
		return query.list().size() == 1;
	}

	public UserAccount getUser(String login) {
		Query query = session.createQuery("FROM UserAccount where login = :login");
		query.setParameter("login", login);
		return (UserAccount) query.list().get(0);
	}

	public boolean checkAccountData(UserAccount userAccount) {
		Query query = session.createQuery("FROM UserAccount where login = :login and password = :password");
		query.setParameter("login", userAccount.getLogin());
		query.setParameter("password", userAccount.getPassword());
		return query.list().size() == 1;
	}
}
