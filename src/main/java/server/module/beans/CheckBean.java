package server.module.beans;

import server.module.data_access.dao.CheckDAO;
import server.module.data_access.dao.DBService;
import shared.entity.CheckResult;
import shared.entity.UserAccount;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.math.BigDecimal;

@Stateless
public class CheckBean  {

	@EJB
	private CheckDAO checkDAO;



	public CheckBean() {

	}

	//    @Override
	public CheckResult addCheck(CheckResult checkResult) {
		checkResult.setHits(checkHits(checkResult));
		DBService.getInstance().addCheck(checkResult);
		return checkResult;
	}

	//    @Override
	public void updateChecks(UserAccount userAccount, BigDecimal r) {
		userAccount.getAllUserHits().forEach(checkResult -> {
			checkResult.setR(r);
			checkResult.setHits(checkHits(checkResult));
		});
		DBService.getInstance().updateChecks(userAccount.getAllUserHits());
	}

	public boolean checkHits(CheckResult checkResult) {

		boolean hit = false;

		if (checkResult.getX().compareTo(BigDecimal.ZERO) > 0 && checkResult.getY().compareTo(BigDecimal.ZERO) > 0) {
			if (checkResult.getX().compareTo(checkResult.getR()) < 1 && checkResult.getY().compareTo(checkResult.getR().divide(new BigDecimal(2))) < 1)
				hit = true;
		}

		if (!hit && checkResult.getX().compareTo(BigDecimal.ZERO) < 1 && checkResult.getY().compareTo(BigDecimal.ZERO) < 1) {
			if (checkResult.getX().divide(new BigDecimal("2")).negate().add(checkResult.getR().divide(new BigDecimal("2")).negate()).compareTo(checkResult.getY()) < 0)
				hit = true;
		}

		if (!hit && checkResult.getX().compareTo(BigDecimal.ZERO) == 1 && checkResult.getY().compareTo(BigDecimal.ZERO) == -1) {
			if (checkResult.getX().pow(2).add(checkResult.getY().pow(2)).compareTo(checkResult.getR().divide(new BigDecimal("2")).pow(2)) < 1)
				hit = true;
		}

		return hit;
	}
}