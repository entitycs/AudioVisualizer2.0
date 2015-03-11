package audiovisualizer.gdx.visualizer.visualization.shapes;

import java.util.ArrayList;

import audiovisualizer.gdx.AudioVisualizer;
import audiovisualizer.gdx.visualizer.visualization.Visualization;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class BassSquare extends Visualization
{
	float rotation = 0;
	
	Sprite square;
	Sprite squareYellow;
	Sprite squareOrange;
	Sprite squareRed;
	
	ArrayList<Sprite> squares = new ArrayList<Sprite>();
	

	public BassSquare(SpriteBatch batch, float[] spectrum)
	{
		super(batch, spectrum);
		
		square = null;
		squareYellow = new Sprite(new Texture(Gdx.files.internal("res/squareYellow.png")));
		squareOrange = new Sprite(new Texture(Gdx.files.internal("res/squareOrange.png")));
		squareRed = new Sprite(new Texture(Gdx.files.internal("res/squareRed.png")));
	}

	
	@Override
	public void visualize()
	{
		rotation += .5f;
		
		float sum = 0;
		for (int i = 0; i < 8; i++)
		{
			sum += spectrum[i];
		}
		float avg = sum / 8;
		
		if (avg < 66)
		{
			square = new Sprite(squareYellow);
			square.setScale(avg / 75f);
			square.setRotation(rotation);
		}
		else if (avg >= 66 && avg < 140)
		{
			square = new Sprite(squareOrange);
			square.setScale(avg / 85f);
			square.setRotation(rotation);
		}
		else
		{
			square = new Sprite(squareRed);
			square.setScale(avg / 64f);
			square.setRotation(rotation);
			square.rotate(45);
		}
		
		if (square != null)
		{
			square.setX(AudioVisualizer.WIDTH / 2 - square.getWidth() / 2);
			square.setY(AudioVisualizer.HEIGHT / 2 - square.getHeight() / 2);
			
			squares.add(square);
		}
		
		draw();
	}
	
	
	public void draw()
	{
		for (int i = 0; i < squares.size(); i++)
		{
			Sprite curSquare = squares.get(i);
			Color color = curSquare.getColor();
			
			if (color.a > 0)
			{
				curSquare.draw(batch);
				curSquare.setColor(color.r, color.g, color.b, color.a - .01f);
			}
			else
			{
				squares.remove(curSquare);
			}
		}
	}
}
