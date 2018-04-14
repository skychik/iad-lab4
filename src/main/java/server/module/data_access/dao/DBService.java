package server.module.data_access.dao;

import server.module.data_access.dao.daos.CheckDAOImpl;
import server.module.data_access.dao.daos.UserDAOImpl;
import shared.entity.CheckResult;
import shared.entity.UserAccount;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.util.List;

public class DBService {
	private static DBService instance;
	private final SessionFactory sessionFactory;

	public static DBService getInstance() {
		if (instance == null) {
			instance = new DBService();
		}

		return instance;
	}

	public DBService() {
		sessionFactory = createSessionFactory(getConfiguration());
	}

	private Configuration getConfiguration() {
		Configuration configuration = new Configuration();
		configuration.addAnnotatedClass(CheckResult.class);
		configuration.addAnnotatedClass(UserAccount.class);
//        configuration.configure("hibernate.cfg.xml");
		configuration.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
		configuration.setProperty("hibernate.connection.url", "jdbc:postgresql://localhost:5432/postgres");
		configuration.setProperty("hibernate.connection.username", "ArtyomMatantsev");
		configuration.setProperty("hibernate.connection.password", "");
		configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
//        configuration.setProperty("hbm2ddl.auto", "create");
		configuration.setProperty("hibernate.hbm2ddl.auto", "update");
		return configuration;
	}


	public UserAccount getUser(String login) {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		UserDAO userDAO = new UserDAOImpl(session);
		UserAccount userAccount = userDAO.getUser(login);
		transaction.commit();
		session.close();
		return userAccount;
	}

	public List<CheckResult> getChecks() {
		Session session = sessionFactory.openSession();
		CheckDAO dao = new CheckDAOImpl(session);
		List<CheckResult> dataSet = dao.getChecks();
		session.close();
		return dataSet;

	}

	public void addCheck(CheckResult CheckBean) {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		CheckDAO dao = new CheckDAOImpl(session);
		dao.addCheck(CheckBean);
		transaction.commit();
		session.close();
	}

	public void updateChecks(List<CheckResult> checkBean) {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		CheckDAO dao = new CheckDAOImpl(session);
		dao.updateChecks(checkBean);
		transaction.commit();
		session.close();
	}

	public void deleteAll(UserAccount userAccount) {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		CheckDAO dao = new CheckDAOImpl(session);
		dao.deleteAll(userAccount);
		transaction.commit();
		session.close();
	}

	public void addUser(UserAccount userAccount) {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		UserDAO dao = new UserDAOImpl(session);
		dao.addUser(userAccount);
		transaction.commit();
		session.close();

	}


	public boolean authorize(UserAccount userAccount) {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		UserDAO dao = new UserDAOImpl(session);
		boolean ok = dao.checkAccountData(userAccount);
		transaction.commit();
		session.close();
		return ok;
	}

	public boolean checkLogin(String login){
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		UserDAO dao = new UserDAOImpl(session);
		boolean ok = dao.checkLogin(login);
		transaction.commit();
		session.close();
		return ok;
	}


	private static SessionFactory createSessionFactory(Configuration configuration) {
		StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
		builder.applySettings(configuration.getProperties());
		ServiceRegistry serviceRegistry = builder.build();
		return configuration.buildSessionFactory(serviceRegistry);
	}
}