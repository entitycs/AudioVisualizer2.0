package visualizer.visualizerFX;

import visualizer.visualizerFX.visualizations.TestVis;
import application.visualizer.interfacing.TavVisualization;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class TavVisualizerFX implements TavVisualizer
{
	TavVisualization visualization;
	protected Number initHeight = 400;
	protected Number initWidth = 760;
	public TavVisualizerFX()
	{ // setting a default visualization
		AnchorPane visualizationPane = new AnchorPane();
		AnchorPane controlPane = new AnchorPane();
		Stage visualizerStage = new Stage();
		Scene scene = new Scene(visualizationPane, this.initWidth.doubleValue(),
				this.initHeight.doubleValue());
		visualizerStage.setScene(scene);
		this.visualization = new TestVis(visualizerStage, controlPane);
		this.visualization.build(scene, 0);
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
		// TODO Auto-generated method stub
		return visualization;
	}

	@Override
	public void setVisualization(TavVisualization visualization)
	{
		// TODO Auto-generated method stub
		this.visualization = visualization;
	}

	@Override
	public void setWidth(double width)
	{
		this.getVisualization().setWidth(width);
	}

	@Override
	public void setHeight(double height)
	{
		this.getVisualization().setHeight(height);
		
	}

}
