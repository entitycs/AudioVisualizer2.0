package audiovisualizer.fx.visualizer.visualization.testvis;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import application.mediaPlayer.TavMetaData;
import application.visualizer.interfacing.TavVisualizationCustomizable;
import application.visualizer.interfacing.TavVisualizationHeight;
import application.visualizer.interfacing.TavVisualizationWidth;

//import javafx.scene.effect.Reflection;
public class TestVis implements TavVisualizationCustomizable
{

	// remove one of the 2s if not using octave

	private final StackPane visualizerPane = new StackPane();

	private AnchorPane root;

	boolean running = false;
	private boolean built = false;

	Stage stage;
	Scene scene;

	private AnchorPane root2;

	private Slider thresholdDBSlider;
	private Slider thresholdTopHatSlider;
	private Slider barWidthSlider;
	private Slider topHatHeightSlider;

	private double interval = .025;

	private RenderTestVisFrame child;

	public TestVis(Canvas canvas, GraphicsContext gc, Stage visualizerStage,
			AnchorPane controlPane)
	{
		this.root = ((AnchorPane) visualizerStage.getScene().getRoot());
		this.root2 = controlPane;
		this.stage = visualizerStage;
		child = new RenderTestVisFrame (canvas, gc);
	}

	public AnchorPane getPane()
	{
		return this.root2;
	}

	// @Override
	public void build2(Number videoWidth)
	{
		if (built)
			return;

		videoWidth = 0;
		root = (AnchorPane) scene.getRoot();
		root.getChildren().remove (visualizerPane);
		root.getChildren().add (visualizerPane);

		child.heightProperty().setValue ((float) root.getHeight());
		child.widthProperty().setValue ((float) (root.getWidth()));
		if (this.barWidthSlider != null)
		{  
			this.barWidthSlider.valueProperty().addListener (
					new ChangeListener<Number>()
					{
						@Override
						public void changed(
								ObservableValue<? extends Number> ov,
								Number old_val, Number new_val)
						{
							// threshold = new_val.intValue();
							child.setBarWidth (new_val.intValue());
						}
					});
		}

		if (this.thresholdDBSlider != null)
		{
			this.thresholdDBSlider.valueProperty().addListener (
					new ChangeListener<Number>()
					{
						@Override
						public void changed(
								ObservableValue<? extends Number> ov,
								Number old_val, Number new_val)
						{
							child.setThreshold (new_val.intValue());
						}
					});
			// to move the slider onto the visualization, do the following
			// root.getChildren().add (this.thresholdDBSlider);
		}

		if (this.thresholdTopHatSlider != null)
		{
			this.thresholdTopHatSlider.valueProperty().addListener (
					new ChangeListener<Number>()
					{
						@Override
						public void changed(
								ObservableValue<? extends Number> ov,
								Number old_val, Number new_val)
						{

							child.setTestVar1 (new_val.intValue());
						}
					});
		}

		if (this.topHatHeightSlider != null)
		{
			this.topHatHeightSlider.valueProperty().addListener (
					new ChangeListener<Number>()
					{
						@Override
						public void changed(
								ObservableValue<? extends Number> ov,
								Number old_val, Number new_val)
						{
							child.setTestVar2 (new_val.intValue());
						}
					});
		}

		visualizerPane.getChildren().add (new Pane());

		built = true;
		child.setBuilt (true);

	}

	public boolean built()
	{
		return built;
	}

	private double diffPlayerTime;
	private double diffTimeStamp;

	public void spectrumDataUpdate(double timestamp, double duration,
			float[] magnitudes, float[] phases, double offset)
	{

		child.updateMagnitudes (magnitudes);

		//
		// TavApplicationManager.getInstance().getMediaPlayerManager()
		// .getMediaPlayer().setAudioSpectrumInterval(interval);
		// TavApplicationManager.getInstance().getMediaPlayerManager()
		// .getMediaPlayer().setAudioSpectrumNumBands(numBands * 2);
		// TavApplicationManager.getInstance().getMediaPlayerManager()
		// .getMediaPlayer().setAudioSpectrumThreshold(threshold);
		child.run();
		// Platform.runLater(child);
	}

	// @Override
	// public void cleanup()
	// {
	// }

	public void build(Scene scene2, int i)
	{
		// TODO Auto-generated method stub
		this.scene = scene2;
		build2 (0);
		child.widthProperty().setValue ((float) this.scene.getWidth());
		child.heightProperty().setValue ((float) this.scene.getHeight());

	}

	// @Override
	// public double getRuntime()
	// {
	// return 0;
	// }

	@Override
	public double getInterval()
	{
		return this.interval;
	}

	@Override
	public int getNumBands()
	{
		return this.child.getNumBands();
	}

	@Override
	public int getThreshold()
	{
		return child.getThreshold();
	}

	@Override
	public void setWidth(double width)
	{
		if (stage != null)
			stage.setWidth (width);
	}

	@Override
	public void setHeight(double height)
	{
		if (stage != null)
			stage.setHeight (height);
	}

	@Override
	public void setMetaData(TavMetaData metaData)
	{

	}

	@Override
	public TavVisualizationWidth widthProperty()
	{
		return child.widthProperty();
	}

	@Override
	public TavVisualizationHeight heightProperty()
	{
		return child.heightProperty();
	}

	@Override
	public boolean isCustomizable()
	{
		return true;
	}

	@Override
	public void changeColor(int index, Color color)
	{
		child.changeColor (index, color);
	}

	@Override
	public Color getCustomColor(int index)
	{
		return child.getCustomColor (index);
	}

	@Override
	public void setCustomizeLevels(GridPane levelControls)
	{
		this.thresholdDBSlider = (Slider) levelControls
				.lookup ("#MajorThreshold");
		this.thresholdTopHatSlider = (Slider) levelControls
				.lookup ("#MinorThreshold");
		this.barWidthSlider = (Slider) levelControls.lookup ("#MajorOffset");
		this.topHatHeightSlider = (Slider) levelControls
				.lookup ("#MinorOffset");
	}
}
