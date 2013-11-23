package visualizer.visualizerGDX;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.farng.mp3.AbstractMP3Tag;
import org.farng.mp3.MP3File;

import visualizer.visualizerGDX.visualizations.Visualization;
import visualizer.visualizerGDX.visualizations.grid.Grid_SrcMid_BassMid;
import application.event.TavEndOfMediaEventHandler;
import application.listener.TavAudioSpectrumListener;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.AudioDevice;
import com.badlogic.gdx.audio.analysis.KissFFT;
import com.badlogic.gdx.audio.io.Mpg123Decoder;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sun.glass.ui.Application;


/**
 * AudioVisualizer is the main class for the whole system. It facilitates the
 * complete process of the Visualizer. This process is as follows:
 * <ul>
 * <li>Load the mp3 file to visualize and play
 * <li>Go through the song, decoding each frame to raw data samples
 * <li>Write samples to AudioDevice
 * <li>Transform samples data into spectrum data
 * <li>Visualize spectrum data and draw visualization to screen
 * </ul>
 * 
 * @author Eric
 */
public class AudioVisualizer extends Game implements Runnable
{
	private static AudioVisualizer AV;
	static LwjglApplication visApp;
	
	public static final int WIDTH = 1024;
	public static final int HEIGHT = 800;

	public static final int SAMPLE_SIZE = 2048;

	private OrthographicCamera camera;
	private SpriteBatch batch;

	private short[] samples;
	private float[] spectrum;

	private KissFFT fft;
	private Visualization visualization;
	private Mpg123Decoder decoder;
	private AudioDevice device;

	private Thread playbackThread;

	private boolean paused = true;
	private boolean playing = false;
	private boolean stopped = false;
	private boolean opening = false;

	private boolean closing = false;

	JFileChooser fileChooser;

	BitmapFont font;

	String songName = "";
	String songArtist = "";

	// if called from the application manager, this value will not be null
	private String mediaLocation = null;
	private TavAudioSpectrumListener audioSpectrumListener;
	private TavEndOfMediaEventHandler endOfMediaEventHandler;

	public AudioVisualizer(String arg)
	{
		this.mediaLocation = arg;

	}

	public AudioVisualizer()
	{

	}

	@Override
	public void create()
	{
		// libGDX graphics
		camera = new OrthographicCamera();
		camera.setToOrtho(false, WIDTH, HEIGHT);
		batch = new SpriteBatch();

		samples = new short[SAMPLE_SIZE];
		spectrum = new float[SAMPLE_SIZE];

		fft = new KissFFT(SAMPLE_SIZE);

		// create visualization
		visualization = new Grid_SrcMid_BassMid(batch, spectrum);

		// if file chosen by media player manager
		if (mediaLocation != null)
		{
			// no extension filter yet (JavaFX supports video)
			// openFile(mediaLocation);
			// instead of the above, (still no filter)
			// let the media player manager decide when to load and play
			font = new BitmapFont();
			// media player manager will call openFile( String s )
			openFile(mediaLocation);
		} else
		{
			fileChooser = new JFileChooser();
			fileChooser.setFileFilter(new FileNameExtensionFilter("MP3 file",
					"mp3"));
			font = new BitmapFont();
			openFile();
		}

	}

	/**
	 * Opens a dialog to open a file from the file system.
	 */
	public void openFile()
	{
		opening = true;

		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				int returnVal = fileChooser.showOpenDialog(null);

				if (returnVal == JFileChooser.APPROVE_OPTION)
				{
					String path = fileChooser.getSelectedFile()
							.getAbsolutePath();

					try
					{
						MP3File song = new MP3File(new File(path));

						AbstractMP3Tag tag = song.getID3v2Tag();

						/*
						 * if (song.hasFilenameTag()) { tag =
						 * FilenameTagBuilder.
						 * createFilenameTagFromMP3File(song); } else if
						 * (song.hasID3v1Tag()) { tag = song.getID3v1Tag(); }
						 * else if (song.hasID3v2Tag()) { tag =
						 * song.getID3v2Tag(); }
						 */

						songName = tag.getSongTitle();
						songArtist = tag.getLeadArtist();

					} catch (Exception e)
					{
						e.printStackTrace();
					}

					if (songName.equals(""))
					{
						songName = "Unknown title";
					}

					if (songArtist.equals(""))
					{
						songArtist = "Unknown artist";
					}

					songName = "Title: " + songName;
					songArtist = "Artist: " + songArtist;
					System.err.println(path);
					play(path);
				}

				opening = false;
			}
		}).start();
	}

	/**
	 * Opens a dialog to open a file from the file system.
	 * 
	 * Used by the media player manager
	 */
	public void openFile(final String path2)
	{
		opening = true;

		// "temporary" fix
		// check if deprecated
		final String path = path2.replace("file:", "");

		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					MP3File song = new MP3File(new File(path));

					AbstractMP3Tag tag = song.getID3v2Tag();

					/*
					 * if (song.hasFilenameTag()) { tag =
					 * FilenameTagBuilder.createFilenameTagFromMP3File(song); }
					 * else if (song.hasID3v1Tag()) { tag = song.getID3v1Tag();
					 * } else if (song.hasID3v2Tag()) { tag =
					 * song.getID3v2Tag(); }
					 */

					songName = tag.getSongTitle();
					songArtist = tag.getLeadArtist();

				} catch (Exception e)
				{
					e.printStackTrace();
				}

				if (songName.equals(""))
				{
					songName = "Unknown title";
				}

				if (songArtist.equals(""))
				{
					songArtist = "Unknown artist";
				}

				songName = "Title: " + songName;
				songArtist = "Artist: " + songArtist;

				play(path);

				opening = false;
			}
		}).start();
	}

	/**
	 * Creates and starts a thread for playback for the song specified by path.
	 * The playback thread manages decoding, writing to the audio device, and
	 * transforming with Fast Fourier Transform (FFT).
	 * 
	 * @param path
	 */
	public void play(String path)
	{
		paused = true;
		playing = false;
		stopped = false;
		songName = "";
		if (playbackThread != null)
			while (playbackThread.isAlive())
			{
			}

		FileHandle file = new FileHandle(path.replace("%20", " "));

		// declaring objects
		decoder = new Mpg123Decoder(file);

		System.out.println(Gdx.audio);
		device = Gdx.audio.newAudioDevice(decoder.getRate(),
				decoder.getChannels() == 1 ? true : false);

		// start a thread for playback
		playbackThread = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				int readSamples = 0;

				// read until we reach the end of the file
				while (playing)
				{
					if (paused)
					{
						continue;
					}

					if ((readSamples = decoder.readSamples(samples, 0,
							samples.length)) > 0)
					{
						// get audio spectrum
						fft.spectrum(samples, spectrum);

						// samples should really be converted to phases in parent
						if (audioSpectrumListener != null)
							audioSpectrumListener.spectrumDataUpdate(0, 0, spectrum, samples);
						else System.out.println("...");

						device.writeSamples(samples, 0, readSamples);
					} else
					{
						playing = false;
					}

					Thread.yield();
				}
				System.err.println("end of media");
				visualization.clear();
				playing = false;
				if (! stopped )
					visApp.postRunnable(endOfMediaEventHandler);
			}
		});

		paused = false;
		playing = true;
		playbackThread.setDaemon(true);

		playbackThread.start();
	}

	@Override
	public void render()
	{
		int songNameX = WIDTH - 20 - (int) font.getBounds(songName).width;
		int songNameY = HEIGHT - 20 - (int) font.getBounds(songName).height;
		int songArtistX = WIDTH - 20 - (int) font.getBounds(songArtist).width;
		int songArtistY = songNameY - 20
				- (int) font.getBounds(songArtist).height;

		// clear screen each frame
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		// update camera
		camera.update();
		batch.setProjectionMatrix(camera.combined);

		batch.begin(); // begin draw
		visualization.visualize(); // VISUALIZE!!!
		font.draw(batch, songName, songNameX, songNameY);
		font.draw(batch, songArtist, songArtistX, songArtistY);
		batch.end(); // end draw

		pollInput();
	}

	/**
	 * Detects hotkey presses and performs the appropriate actions.
	 */
	public void pollInput()
	{
		if (Gdx.input.isKeyPressed(Input.Keys.O) && !opening)
		{
			openFile();
		} else if (Gdx.input.isKeyPressed(Input.Keys.P) && !paused)
		{
			paused = true;
		} else if (Gdx.input.isKeyPressed(Input.Keys.R) && paused)
		{
			paused = false;
		} else if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE) && !closing)
		{
			closing = true;

			dispose();
		}
	}

	@Override
	public void dispose()
	{
		// synchronize with the thread
		playing = false;

		if (playbackThread != null)
			while (playbackThread.isAlive())
			{
			}

	
		// exit program
		Gdx.app.exit();

	}

	/**
	 * Let the media player manager set the location
	 * 
	 * @param location
	 */
	private void setMediaLocation(String location)
	{
		this.mediaLocation = location;
	}

	// avoid using GDX pause()
	public void pause1()
	{
		paused = true;
	}

	// avoid using GDX resume()
	public void resume1()
	{
		paused = false;
	}

	public static void main(String[] args)
	{
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "AudioVisualizer";
		cfg.useGL20 = false;
		cfg.width = WIDTH;
		cfg.height = HEIGHT;
		
		// for media player manager
		AV = new AudioVisualizer();

		// for media player manager
		if (args != null && args.length > 0)
		{
			// set the given media location
			AV.setMediaLocation(args[0]);
			
			// do not close the application stage on exit
			cfg.forceExit = false;

		}

		// calling as usual
		visApp = new LwjglApplication(AV, cfg);
		
	}

	@Override
	public void run()
	{

	}

	public AudioVisualizer getAV()
	{
		return this;
	}
	
	public LwjglApplication getVisApp(){
		return visApp;
	}

	public void newTrack(String mediaLocation)
	{
		playing = false;

		if (playbackThread != null)
			while (playbackThread.isAlive())
			{
			}

		this.mediaLocation = mediaLocation;
		openFile(this.mediaLocation);
	}

	public void stop()
	{
		stopped = true;
		playing = false;
		
		if (playbackThread != null)
			while (playbackThread.isAlive())
			{
			}
	}

	public void setAudioSpectrumListener(
			TavAudioSpectrumListener audioSpectrumListener)
	{
		this.audioSpectrumListener = audioSpectrumListener;
	}

	public void setonEndOfMedia(
			TavEndOfMediaEventHandler tavEndOfMediaEventHandler)
	{
		this.endOfMediaEventHandler = tavEndOfMediaEventHandler;
		
	}

	public void setApp(LwjglApplication visApp2)
	{
		this.visApp = visApp2;
		
	}
}