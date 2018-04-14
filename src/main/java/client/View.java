package client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import org.fusesource.restygwt.client.Defaults;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Entry point classes define <code>onModuleLoad()</code>
 */
public class View implements EntryPoint {

	private static final FlowPanel mainPanel = new FlowPanel();

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		Defaults.setServiceRoot(GWT.getHostPageBaseURL());
		mainPanel.add(new StartPage().getMainPanel());
		RootPanel.get("center").add(mainPanel);

//		final Button button=new Button("Click me");
//		final Label label=new Label();
//
//		button.addClickHandler(event -> {
//				if (label.getText().equals("")) {
//					AppService.View.getInstance().getMessage("Hello, World!", new MyAsyncCallback(label));
//				} else {
//					label.setText("");
//				}
//			}
//		);
//
//		// Assume that the host HTML has elements defined whose
//		// IDs are "slot1", "slot2".  In a real app, you probably would not want
//		// to hard-code IDs.  Instead, you could, for example, search for all
//		// elements with a particular CSS class and replace them with widgets.
//		//
//		RootPanel.get("slot1").add(button);
//		RootPanel.get("slot2").add(label);
	}

	public static void setContent(Widget widget) {
		mainPanel.clear();
		mainPanel.add(widget);
		RootPanel.get("center").clear();
		RootPanel.get("center").add(mainPanel);
	}

//	private static class MyAsyncCallback implements AsyncCallback<String> {
//		private Label label;
//
//		public MyAsyncCallback(Label label) {
//			this.label = label;
//		}
//
//		public void onSuccess(String result) {
//			label.getElement().setInnerHTML(result);
//		}
//
//		public void onFailure(Throwable throwable) {
//			label.setText("Failed to receive answer from server!");
//		}
//	}
}
