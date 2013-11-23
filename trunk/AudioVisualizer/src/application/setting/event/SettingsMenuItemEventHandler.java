package application.setting.event;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import application.setting.listener.VisualizerDimensionSettingListener;

public class SettingsMenuItemEventHandler implements EventHandler<ActionEvent> {

	private VisualizerDimensionSettingListener listener;

	public SettingsMenuItemEventHandler(VisualizerDimensionSettingListener listener){
		this.listener = listener;
	}
	
	@Override
	public void handle(ActionEvent arg0) {

		Stage settings = new Stage();
		AnchorPane root = new AnchorPane();
		// Creating a GridPane container
		GridPane grid = new GridPane();
		grid.setPadding(new Insets(10, 10, 10, 10));
		grid.setVgap(5);
		grid.setHgap(5);
		// Defining the Name text field
		final TextField width = new TextField();
		width.setPromptText("Visualization width");
		width.setPrefColumnCount(10);
		width.getText();
		GridPane.setConstraints(width, 0, 0);
		grid.getChildren().add(width);
		// Defining the Last Name text field
		final TextField height = new TextField();
		height.setPromptText("Visualization height");
		GridPane.setConstraints(height, 0, 1);
		grid.getChildren().add(height);

		// Defining the Submit button
		Button submit = new Button("Submit");
		GridPane.setConstraints(submit, 1, 0);
		grid.getChildren().add(submit);

		// Adding a Label
		final Label label = new Label();
		GridPane.setConstraints(label, 0, 3);
		GridPane.setColumnSpan(label, 2);
		grid.getChildren().add(label);

		// Add the grid (and its elements) to the root
		root.getChildren().add(grid);

		// Setting an action for the Submit button
		submit.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				if ((width.getText() != null && !width.getText().isEmpty())
						&& (height.getText() != null && !height.getText()
								.isEmpty())) {

					listener.widthSetting(
							Double.parseDouble(width.getText()));
					listener.heightSetting(
							Double.parseDouble(height.getText()));

					label.setText(width.getText() + " " + height.getText()
							+ ", " + "Visualizer size was set successfully");
				} else {
					label.setText("Unable to parse visualizer size.");
				}

			}
		});

		settings.setScene(new Scene(root, 300, 250));
		settings.show();
	}

}
