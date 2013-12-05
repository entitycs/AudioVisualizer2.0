package audiovisualizer.gdx.visualizer.visualization.shapes;

import audiovisualizer.gdx.AudioVisualizer;
import audiovisualizer.gdx.visualizer.visualization.Visualization;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Shapes extends Visualization {

	Sprite star, spiral, background, ball, circle;
	protected int rotation = 0, rotation2 = 0, rotation3 = 0;
	protected int height = AudioVisualizer.HEIGHT;
	protected int width = AudioVisualizer.WIDTH;
	protected int range = 2048 ;
	protected float average = 0;
	
	public Shapes(SpriteBatch _batch, float[] _spectrum) {
		super(_batch, _spectrum);
		
		star = new Sprite(new Texture(Gdx.files.internal("res/star.png")));
		spiral = new Sprite(new Texture(Gdx.files.internal("res/spiral.png")));
		background = new Sprite(new Texture(Gdx.files.internal("res/background.png")));
		ball = new Sprite(new Texture(Gdx.files.internal("res/ball.png")));
		circle = new Sprite(new Texture(Gdx.files.internal("res/circle.png")));
	}

	@Override
	public void visualize() {
		
		star.setRotation(rotation);
		background.setRotation(rotation2);
		spiral.setRotation(rotation3);
		
		rotation+=2;
		rotation2--;
		rotation3++;
		
		if(average > avg(range))
			{rotation-=30;}
		average = avg(range);
		
		star.setPosition(width/2 - star.getWidth()/2, 
				height/2 - star.getHeight()/2);
		spiral.setPosition(width/2 - spiral.getWidth()/2, 
				height/2 - spiral.getHeight()/2);
		background.setPosition(width/2 - background.getWidth()/2, 
				height/2 - background.getHeight()/2);
		circle.setPosition(width/2 - circle.getWidth()/2, 
				height/2 - circle.getHeight()/2);
		ball.setPosition(width/2 - ball.getWidth()/2, 
				height/2 - ball.getHeight()/2);
		
		circle.draw(batch);
		background.draw(batch);
		ball.draw(batch);
		spiral.draw(batch);
		star.draw(batch);
		
		
	}

	
	protected int avg(int numSamples)
	{
		int sum = 0;
		
		for (int i = 0; i < numSamples; i++)
		{
			sum += spectrum[i];
		}
		return (sum / numSamples);
	
	}
}
