package client;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.ScriptInjector;
import com.google.gwt.dom.client.Document;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.view.client.ListDataProvider;
import shared.FieldVerifier;
import shared.entity.CheckResult;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import java.math.BigDecimal;
import java.util.List;

public class MainPage extends Composite {
	private CheckService checkService = GWT.create(CheckService.class);
	private AccountService accountService = GWT.create(AccountService.class);
	private FlowPanel mainPane;

	public MainPage() {
		/* Error box */
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

		mainPane = new FlowPanel();

		FlowPanel selectorsPanel = new FlowPanel();
		selectorsPanel.getElement().setClassName("selectors-panel");

		HorizontalPanel xSelectorPanel = new HorizontalPanel();
		final Label xLabel = new Label("Select x:");
		final ListBox xListBox = new ListBox();
		final Anchor returnLabel = new Anchor("Log out");
		xListBox.addItem("-5");
		xListBox.addItem("-4");
		xListBox.addItem("-3");
		xListBox.addItem("-2");
		xListBox.addItem("-1");
		xListBox.addItem("0");
		xListBox.addItem("1");
		xListBox.addItem("2");
		xListBox.setSelectedIndex(0);
		xSelectorPanel.setSpacing(5);
		xSelectorPanel.add(xLabel);
		xSelectorPanel.add(xListBox);
		selectorsPanel.add(xSelectorPanel);

		HorizontalPanel rSelectorPanel = new HorizontalPanel();
		final Label rLabel = new Label("Select radius:");
		final ListBox rListBox = new ListBox();
		rListBox.addItem("1");
		rListBox.addItem("2");
		rListBox.addItem("3");
		rListBox.addItem("4");
		rListBox.addItem("5");
		rListBox.setSelectedIndex(0);

		rSelectorPanel.setSpacing(5);
		rSelectorPanel.add(rLabel);
		rSelectorPanel.add(rListBox);
		selectorsPanel.add(rSelectorPanel);

		HorizontalPanel ySelectorPanel = new HorizontalPanel();
		ySelectorPanel.setSpacing(5);
		ySelectorPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		final Label yLabel = new Label("Select y:");
		final TextBox yText = new TextBox();
		ySelectorPanel.add(yLabel);
		ySelectorPanel.add(yText);
		final Button sendButton = new Button("Check");
		selectorsPanel.add(ySelectorPanel);
		selectorsPanel.add(sendButton);
		selectorsPanel.add(returnLabel);
		mainPane.add(selectorsPanel);

		FlowPanel imagePanel = new FlowPanel();
		imagePanel.getElement().setClassName("image-panel");


		FlowPanel ww = new FlowPanel();
		ww.getElement().setClassName("ww");
		FlowPanel imgWrapper = new FlowPanel();
		imgWrapper.getElement().setClassName("img_wrapper");

		final Image plot = new Image("resources/img/areas.png");

		imgWrapper.add(plot);
		ww.add(imgWrapper);

		final Button clearButton = new Button("Clear checks history");
		//imagePanel.add(imageLabel);
		imagePanel.add(ww);
		imagePanel.add(clearButton);
		mainPane.add(imagePanel);


		FlowPanel tablePanel = new FlowPanel();
		tablePanel.getElement().setClassName("table-panel");
		//Label tableLabel = new Label("Checks history:");
		// Create a CellTable.
		CellTable<CheckResult> table = new CellTable<>();
		table.setVisibleRange(0, Integer.MAX_VALUE);

		// Create x column.
		TextColumn<CheckResult> xColumn = new TextColumn<CheckResult>() {
			@Override
			public String getValue(CheckResult contact) {
				return contact.getX().toString();
			}
		};

		// Create address column.
		TextColumn<CheckResult> yColumn = new TextColumn<CheckResult>() {
			@Override
			public String getValue(CheckResult contact) {
				return contact.getY().toString();
			}
		};

		TextColumn<CheckResult> rColumn = new TextColumn<CheckResult>() {
			@Override
			public String getValue(CheckResult contact) {
				return contact.getR().toString();
			}
		};

		TextColumn<CheckResult> hitsColumn = new TextColumn<CheckResult>() {
			@Override
			public String getValue(CheckResult contact) {
				return contact.isHits() ? "true" : "false";
			}
		};

		// Add the columns.
		table.addColumn(xColumn, "x");
		table.addColumn(yColumn, "y");
		table.addColumn(rColumn, "r");
		table.addColumn(hitsColumn, "hits");

		// Create a data provider.
		ListDataProvider<CheckResult> dataProvider = new ListDataProvider<>();

		// Connect the table to the data provider.
		dataProvider.addDataDisplay(table);


		// Add the data to the data provider, which automatically pushes it to the
		// widget.
		List<CheckResult> list = dataProvider.getList();

		rListBox.addChangeHandler(changeEvent -> {
			clearPlot();
			if(!list.isEmpty()) {
				checkService.justHit(new BigDecimal(rListBox.getSelectedValue()), new MethodCallback<BigDecimal>() {
					@Override
					public void onFailure(Method method, Throwable throwable) {
						errorLabelPopup.setText(throwable.getMessage());
						errorBox.center();
					}

					@Override
					public void onSuccess(Method method, BigDecimal r) {
						accountService.getAllHits(new MethodCallback<List<CheckResult>>() {
							@Override
							public void onFailure(Method method, Throwable throwable) {

							}

							@Override
							public void onSuccess(Method method, List<CheckResult> checkResults) {
								list.clear();
								list.addAll(checkResults);
								for (CheckResult checkResult : list) {
									String x = checkResult.getX().toString();
									String y = checkResult.getY().toString();
									String r = checkResult.getR().toString();
									boolean hits = checkResult.isHits();
									addPoint(x, y, r, hits);
								}
							}
						});

						dataProvider.refresh();
					}
				});
			}
		});

		tablePanel.add(table);
		mainPane.add(tablePanel);
		mainPane.setStyleName("style");

		okButton.addClickHandler(clickEvent -> errorBox.hide());

		clearButton.addClickHandler(event -> accountService.deleteAllUserHits(new MethodCallback() {
			@Override
			public void onFailure(Method method, Throwable throwable) {
				errorLabelPopup.setText(throwable.getMessage());
				errorBox.center();

			}

			@Override
			public void onSuccess(Method method, Object o) {
				list.clear();
				clearPlot();
			}
		}));

		sendButton.addClickHandler(event -> {
			switch (FieldVerifier.isValidY(yText.getText())) {
				case INVALID_SYMBOLS: {
					errorLabelPopup.setText("Invalid y value");
					errorBox.center();
				} break;
				case WRONG_RANGE: {
					errorLabelPopup.setText("Invalid y range (it should be between -3 and 5)");
					errorBox.center();
				}
				case EMPTY_STRING: {
					errorLabelPopup.setText("Y should be specified");
					errorBox.center();
				}
				case OK: {
					CheckResult cr = new CheckResult();
					cr.setX(new BigDecimal(xListBox.getSelectedValue()));
					cr.setY(new BigDecimal(yText.getText()));
					cr.setY(cr.getY().setScale(10,BigDecimal.ROUND_CEILING));
					cr.setR(new BigDecimal(rListBox.getSelectedValue()));
					checkService.checkHit(cr, new MethodCallback<CheckResult>() {
						@Override
						public void onFailure(Method method, Throwable throwable) {
							errorLabelPopup.setText(throwable.getMessage());
							errorBox.center();
						}

						@Override
						public void onSuccess(Method method, CheckResult checkResult) {
							list.add(checkResult);
							addPoint(checkResult.getX().toString(), checkResult.getY().toString(), checkResult.getR().toString(), checkResult.isHits());
						}
					});
				}
			}
		});

		plot.addClickHandler(event -> {
			Double y = (double)event.getNativeEvent().getClientY() - plot.getAbsoluteTop() + Document.get().getScrollTop();
			Double x = (double)event.getNativeEvent().getClientX() - plot.getAbsoluteLeft() + Document.get().getScrollLeft();

			y-=110. * getZoomFactor();
			y/=80. * getZoomFactor();
			y = -y;
			x-=110. * getZoomFactor();
			x/=80. * getZoomFactor();

			CheckResult cr = new CheckResult();

			cr.setX(new BigDecimal(x));
			cr.setX(cr.getX().multiply(new BigDecimal(rListBox.getSelectedValue())));
			cr.setX(cr.getX().setScale(10,BigDecimal.ROUND_CEILING));

			cr.setY(new BigDecimal(y));
			cr.setY(cr.getY().multiply(new BigDecimal(rListBox.getSelectedValue())));
			cr.setY(cr.getY().setScale(10,BigDecimal.ROUND_CEILING));

			cr.setR(new BigDecimal(rListBox.getSelectedValue()));

			checkService.checkHit(cr, new MethodCallback<CheckResult>() {
				@Override
				public void onFailure(Method method, Throwable throwable) {
					errorLabelPopup.setText(throwable.getMessage());
					errorBox.center();
				}

				@Override
				public void onSuccess(Method method, CheckResult checkResult) {
					list.add(checkResult);
					addPoint(checkResult.getX().toString(), checkResult.getY().toString(), checkResult.getR().toString(), checkResult.isHits());
				}
			});
		});

		returnLabel.addClickHandler(event -> accountService.logOut(Cookies.getCookie("login"), Integer.parseInt(Cookies.getCookie("sid")), new MethodCallback<Boolean>() {
			@Override
			public void onFailure(Method method, Throwable throwable) {
				errorLabelPopup.setText(throwable.getMessage());
				errorBox.center();
			}

			@Override
			public void onSuccess(Method method, Boolean aBoolean) {
				if(aBoolean) {
					Cookies.removeCookie("sid");
					Cookies.removeCookie("login");
					View.setContent(new StartPage());
				}
			}
		}));

		ScriptInjector.fromUrl("resources/js/jquery.min.js")
				.setRemoveTag(false).setWindow(ScriptInjector.TOP_WINDOW).setCallback(new Callback<Void, Exception>() {
			@Override
			public void onFailure(Exception e) {
				errorLabelPopup.setText(e.getMessage());
				errorBox.center();
			}

			@Override
			public void onSuccess(Void aVoid) {
				ScriptInjector.fromUrl("resources/js/jquery.mousewheel.min.js")
						.setRemoveTag(false).setWindow(ScriptInjector.TOP_WINDOW).inject();
				ScriptInjector.fromUrl("resources/js/checker.js")
						.setRemoveTag(false).setWindow(ScriptInjector.TOP_WINDOW).setCallback(new Callback<Void, Exception>() {
					@Override
					public void onFailure(Exception reason) {
						errorLabelPopup.setText(reason.getMessage());
						errorBox.center();
					}

					@Override
					public void onSuccess(Void result) {
						accountService.getAllHits(new MethodCallback<List<CheckResult>>() {
							@Override
							public void onFailure(Method method, Throwable throwable) {
								errorLabelPopup.setText(throwable.getMessage());
								errorBox.center();
							}

							@Override
							public void onSuccess(Method method, List<CheckResult> checkResults) {
								list.addAll(checkResults);
								for (CheckResult checkResult : list) {
									addPoint(checkResult.getX().toString(), checkResult.getY().toString(), checkResult.getR().toString(), checkResult.isHits());
								}
							}

						});
					}
				}).inject();
			}
		}).inject();


	}

	public static native void addPoint(String x, String y, String r, boolean hits)/*-{
        $wnd.addPoint(x, y, r, hits);
    }-*/;

	public static native void clearPlot() /*-{
        $wnd.clearPlot();
    }-*/;

	public static native double getZoomFactor() /*-{
        return $wnd.getZF();
    }-*/;

	public FlowPanel getMainPane() {
		return mainPane;
	}
}
