package server.module.data_access.dao.daos;

import server.module.data_access.dao.CheckDAO;
import shared.entity.CheckResult;
import shared.entity.UserAccount;
import org.hibernate.Session;

import javax.ejb.Stateless;
import javax.persistence.Query;
import java.util.List;

@Stateless(name = "CheckDAO")
public class CheckDAOImpl implements CheckDAO {

	private Session session;

	public CheckDAOImpl(Session session) {
		this.session = session;
	}

	public CheckDAOImpl() {
	}

	@SuppressWarnings("unchecked")
	public List<CheckResult> getChecks() {
//        Query query = entityManager.createQuery("select cr from CheckResult  as cr ");
//        return query.getResultList();
		return null;
	}

	public void addCheck(CheckResult checkResult) {
		session.save(checkResult);
	}

	public void updateChecks(List<CheckResult> checkBean) {
		checkBean.forEach(checkResult -> session.merge(checkResult));
	}

	public void deleteAll(UserAccount userAccount) {
		Query q = session.createQuery("delete from CheckResult where owner = " + userAccount.getId());
		q.executeUpdate();
	}
}