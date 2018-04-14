package server.module.data_access.dao;

import shared.entity.CheckResult;
import shared.entity.UserAccount;

import java.util.List;

public interface CheckDAO {

	List<CheckResult> getChecks();

	void addCheck(CheckResult checkResult);

	void updateChecks(List<CheckResult> checkBean);

	void deleteAll(UserAccount userAccount);
}