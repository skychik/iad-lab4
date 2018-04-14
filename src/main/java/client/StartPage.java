package client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.*;
import shared.FieldVerifier;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;
import static client.View.setContent;

public class StartPage extends Composite {

	private AccountService service = GWT.create(AccountService.class);
	private final VerticalPanel mainPanel = new VerticalPanel();

	public VerticalPanel getMainPanel() {
		return mainPanel;
	}

	public StartPage() {
		if(Cookies.getCookie("login") != null && Cookies.getCookie("sid") != null) {
			service.auth(new MethodCallback() {
				@Override
				public void onFailure(Method method, Throwable throwable) {

				}

				@Override
				public void onSuccess(Method method, Object o) {
					setContent(new MainPage().getMainPane());
				}
			});
		}

		final Button sendButton = new Button("Log in");
		final TextBox loginField = new TextBox();
		final Label loginLabel = new Label("Login:");
		final PasswordTextBox passwordField = new PasswordTextBox();
		final Label passwordLabel = new Label("Password:");
		loginField.setText("uzver");
		final Anchor registerLabel = new Anchor("Not registered yet?");
		final Grid tab = new Grid(2,2);
		tab.setWidget(0,0,loginLabel);
		tab.setWidget(0,1,loginField);
		tab.setWidget(1,0,passwordLabel);
		tab.setWidget(1,1,passwordField);
		mainPanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
		mainPanel.add(tab);
		mainPanel.add(sendButton);
		mainPanel.add(registerLabel);
		mainPanel.setSpacing(5);
		RootPanel.get("center").add(mainPanel);
		mainPanel.setStyleName("style");

		// We can add style names to widgets
		sendButton.addStyleName("sendButton");

		// Focus the cursor on the name field when the app loads
		loginField.setFocus(true);
		loginField.selectAll();

		final DialogBox errorBox = new DialogBox();
		errorBox.setText("Error");
		final VerticalPanel errorPanel = new VerticalPanel();
		final Label errorLabelPopup = new Label();
		final Button okButton = new Button("Ok");
		errorPanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
		errorPanel.add(errorLabelPopup);
		errorPanel.add(okButton);
		errorPanel.setSpacing(5);
		errorBox.add(errorPanel);

		// Create the popup dialog box
		Grid popupTab = new Grid(2,2);
		final DialogBox dialogBox = new DialogBox();
		dialogBox.setText("Registration");
		dialogBox.setAnimationEnabled(true);
		final Button closeButton = new Button("Done");
		final Button cancel = new Button("Cancel");
		// We can set the id of a widget by accessing its Element
		closeButton.getElement().setId("closeButton");
		final TextBox registrationLoginField = new TextBox();
		final Label registrationLoginLabel = new Label("Login:");
		final PasswordTextBox registrationPasswordField = new PasswordTextBox();
		final Label registrationPasswordLabel = new Label("Password:");
		popupTab.setWidget(0,0,registrationLoginLabel);
		popupTab.setWidget(0,1,registrationLoginField);
		popupTab.setWidget(1,0,registrationPasswordLabel);
		popupTab.setWidget(1,1,registrationPasswordField);
		final VerticalPanel dialogVPanel = new VerticalPanel();
		dialogVPanel.addStyleName("dialogVPanel");
		dialogVPanel.add(new HTML("<b>Input your data for registration:</b>"));
		dialogVPanel.add(popupTab);
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);

		final HorizontalPanel dialogHButtons = new HorizontalPanel();
		dialogHButtons.add(closeButton);
		dialogHButtons.add(cancel);

		dialogVPanel.add(dialogHButtons);
		dialogVPanel.setSpacing(5);
		dialogBox.setWidget(dialogVPanel);

		// Add a handler to close the DialogBox
		closeButton.addClickHandler(event -> {
			boolean login = false;
			switch (FieldVerifier.isValidName(registrationLoginField.getText())) {
				case CHECK_OK: {
					login = true;
				} break;
				case CHECK_INVALID_LENGTH: {
					errorLabelPopup.setText("Login should have at least 4 symbols!");
					errorBox.showRelativeTo(registrationLoginLabel);
				} break;
				case CHECK_INVALID_SYMBOLS: {
					errorLabelPopup.setText("Login should contain only latin characters and numbers!");
					errorBox.showRelativeTo(registrationLoginLabel);
				} break;
			}
			if(login) {
				switch (FieldVerifier.isValidPassword(registrationPasswordField.getText())) {
					case CHECK_INVALID_LENGTH: {
						errorLabelPopup.setText("Password should have at least 6 symbols!");
						errorBox.showRelativeTo(registrationLoginLabel);
					}
					break;
					case CHECK_INVALID_SYMBOLS: {
						errorLabelPopup.setText("Password should contain only latin characters and numbers!");
						errorBox.showRelativeTo(registrationLoginLabel);
					}
					break;
					case CHECK_OK: {
						service.registerUser(registrationLoginField.getText(), registrationPasswordField.getText(), new MethodCallback<Boolean>() {
							@Override
							public void onFailure(Method method, Throwable throwable) {
								errorLabelPopup.setText(throwable.getMessage());
								errorBox.showRelativeTo(registrationLoginLabel);
							}

							@Override
							public void onSuccess(Method method, Boolean o) {
								if(o) {
									errorBox.setText("Success");
									errorLabelPopup.setText("You have successfully registered");
									errorBox.showRelativeTo(registrationLoginLabel);
									errorBox.setText("Error");
									dialogBox.hide();
								} else {
									errorBox.setText("Success");
									errorLabelPopup.setText("Login " + registrationLoginField.getText() + " is already in use." );
									errorBox.showRelativeTo(registrationLoginLabel);
								}
							}
						});
					}
				}
			}
		});

		sendButton.addClickHandler(event -> {
			boolean login = false;
			switch (FieldVerifier.isValidName(loginField.getText())) {
				case CHECK_OK: {
					login = true;
				} break;
				case CHECK_INVALID_LENGTH: {
					errorLabelPopup.setText("Login should have at least 4 symbols!");
					errorBox.center();
				} break;
				case CHECK_INVALID_SYMBOLS: {
					errorLabelPopup.setText("Login should contain only latin characters and numbers!");
					errorBox.center();
				} break;
			}
			if(login) {
				switch (FieldVerifier.isValidPassword(passwordField.getText())) {
					case CHECK_INVALID_LENGTH: {
						errorLabelPopup.setText("Password should have at least 6 symbols!");
						errorBox.center();
					}
					break;
					case CHECK_INVALID_SYMBOLS: {
						errorLabelPopup.setText("Password should contain only latin characters and numbers!");
						errorBox.center();
					}
					break;
					case CHECK_OK: {
						service.logIn(loginField.getText(), passwordField.getText(), new MethodCallback<Integer>() {
							@Override
							public void onFailure(Method method, Throwable throwable) {
								errorLabelPopup.setText(throwable.getCause().getMessage());
								errorBox.center();
							}

							@Override
							public void onSuccess(Method method, Integer o) {
								if(o < 0) {
									errorLabelPopup.setText("Invalid login or password!");
									errorBox.center();
								} else {
									Cookies.setCookie("sid", o.toString());
									Cookies.setCookie("login", loginField.getText());
									setContent(new MainPage().getMainPane());
								}
							}
						});
					}
				}
			}
		});



		cancel.addClickHandler(event -> dialogBox.hide());

		registerLabel.addClickHandler(event -> dialogBox.center());

		okButton.addClickHandler(event -> errorBox.hide());
	}
}
