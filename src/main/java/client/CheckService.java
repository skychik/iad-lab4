package client;

import shared.entity.CheckResult;
import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.util.List;

@Path("/api/rest")
public interface CheckService extends RestService {

	@POST
	@Path("/hits")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	void checkHit(CheckResult checkResult, MethodCallback<CheckResult> methodCallback);

	@POST
	@Path("/justhit")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	void justHit(BigDecimal r, MethodCallback<BigDecimal> methodCallback);

	@GET
	@Path("/getpoints")
	@Produces(MediaType.APPLICATION_JSON)
	void getPoints(MethodCallback<List<CheckResult>> methodCallback);
}