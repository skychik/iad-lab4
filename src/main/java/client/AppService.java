package client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("AppService")
public interface AppService extends RemoteService {
	// Sample interface method of remote interface
	String getMessage(String msg);

	/**
	* Utility/Convenience class.
	* Use AppService.View.getInstance() to access static instance of AppServiceAsync
	*/
	public static class App {
		private static AppServiceAsync ourInstance = GWT.create(AppService.class);

		public static synchronized AppServiceAsync getInstance() {
		return ourInstance;
	}
	}
}
