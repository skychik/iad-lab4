package server.module.interfaces;


import shared.entity.CheckResult;

import javax.ejb.Local;
import java.util.List;

@Local
public interface CheckBeanLocal {

	CheckResult addCheck(CheckResult checkResult);

	void updateChecks(List<CheckResult> checkResults);

}