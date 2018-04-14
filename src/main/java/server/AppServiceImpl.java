package server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import client.AppService;

public class AppServiceImpl extends RemoteServiceServlet implements AppService {
	// Implementation of sample interface method
	public String getMessage(String msg) {
		return "Client said: \"" + msg + "\"<br>Server answered: \"Hi!\"";
	}
}