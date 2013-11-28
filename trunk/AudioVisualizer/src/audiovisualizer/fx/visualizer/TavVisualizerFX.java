package audiovisualizer.fx.visualizer;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import application.setting.TavSettingsFileLoader;
import application.visualizer.event.TavSceneHeightChangedEventHandler;
import application.visualizer.event.TavSceneWidthChangedEventHandler;
import application.visualizer.interfacing.TavVisualization;
import application.visualizer.interfacing.TavVisualizer;
import audiovisualizer.fx.visualizer.visualization.deprecated.TestVis2;
import audiovisualizer.fx.visualizer.visualization.testvis.TestVis;

public class TavVisualizerFX implements TavVisualizer
{
	TavVisualization visualization;
	protected Number initHeight = 400;
	protected Number initWidth = 900;
	private Canvas canvas;
	private Scene scene;
	private AnchorPane visualizationPane;
	private AnchorPane controlPane;
	private Stage visualizerStage;
	private GraphicsContext gc;

	private int numVisualizations = 2;

	private TavVisualization[] visualizations = new TavVisualization[numVisualizations];

	public TavVisualizerFX()
	{

		TavSettingsFileLoader propertiesLoader;

		propertiesLoader = new TavSettingsFileLoader (
				TavVisualizerFX.class.getResourceAsStream ("choices.tav"));

		visualizationPane = new AnchorPane();
		controlPane = new AnchorPane();
		visualizerStage = new Stage();
		scene = new Scene (visualizationPane, this.initWidth.doubleValue(),
				this.initHeight.doubleValue());
		visualizerStage.setScene (scene);
		this.canvas = new Canvas (initWidth.doubleValue(),
				initHeight.doubleValue());

		// Create the graphics content
		gc = canvas.getGraphicsContext2D();

		// add the new pane (needed?) and the canvas to the visualization pane
		// visualizationPane.getChildren().add(new Pane());
		visualizationPane.getChildren().add (canvas);

		// create the visualizations given the created canvas, context, stage
		this.visualizations[0] = new TestVis (canvas, gc, visualizerStage,
				controlPane);
		this.visualizations[1] = new TestVis2 (canvas, gc, visualizerStage,
				controlPane);

		this.visualization = this.visualizations[0];

		// Tie the canvas width to the scene width
		scene.widthProperty().addListener (
				new TavSceneWidthChangedEventHandler (canvas.widthProperty(),
						visualization.widthProperty()));
		scene.heightProperty().addListener (
				new TavSceneHeightChangedEventHandler (
						canvas.heightProperty(), visualization
								.heightProperty()));
	}

	/*
	 * private Canvas canvas; private Stage stage; private Scene scene; private
	 * Pane root; private final StackPane visualizerPane = new StackPane();
	 */
	float canvasCenterY;
	float canvasCenterX;

	@Override
	public TavVisualization getVisualization()
	{
		return visualization;
	}

	@Override
	public void setWidth(double width)
	{
		this.getVisualization().setWidth (width);
		this.initWidth = width;
	}

	@Override
	public void setHeight(double height)
	{
		this.getVisualization().setHeight (height);
		this.initHeight = height;
	}

	@Override
	public boolean isShowing()
	{
		if (this.visualizerStage != null)

			return this.visualizerStage.isShowing();

		return false;
	}

	@Override
	public void show()
	{
		if (this.visualizerStage != null)
			this.visualizerStage.show();
	}

	@Override
	public void build()
	{
		visualization.build (visualizerStage.getScene(), 0);
	}

	@Override
	public void setVisualizationIndex(int visualizationIndex)
	{
		if (visualizationIndex > 0
				&& visualizationIndex < this.visualizations.length)
			this.visualization = visualizations[visualizationIndex];
	}
}
