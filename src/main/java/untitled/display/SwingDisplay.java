package untitled.display;

import untitled.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.lang.reflect.InvocationTargetException;

public abstract class SwingDisplay extends untitled.Display<SwingGraphicsWrapper>
{
	private static final int NUM_BUFFERS = 3;
	
	private JFrame frame;
	private Canvas canvas;
	private SwingGraphicsWrapper graphicsWrapper;
	
	public SwingDisplay(Game game, Canvas canvas)
	{
		super(game);
		
		this.canvas = canvas;
	}
	
	@Override
	public SwingGraphicsWrapper getGraphics()
	{
		return graphicsWrapper;
	}
	
	@Override
	public void open()
	{
		log.info("Opening display");
		
		try
		{
			SwingUtilities.invokeAndWait(() ->
			{
				frame = new JFrame(game.getTitle() + " v" + game.getVersion());
				frame.setIgnoreRepaint(true);
				frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				frame.addWindowListener(new WindowAdapter()
				{
					@Override
					public void windowClosing(WindowEvent event) { game.terminate(); }
				});
				
				canvas.setPreferredSize(new Dimension(getWidth(), getHeight()));
				canvas.setBounds(0, 0, getWidth(), getHeight());
				
				frame.getContentPane()
					 .add(canvas);
				frame.setResizable(false);
				frame.pack();
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
				
				canvas.createBufferStrategy(NUM_BUFFERS);
				canvas.requestFocus();
				
				BufferStrategy buffer = canvas.getBufferStrategy();
				graphicsWrapper = new SwingGraphicsWrapper(buffer);
			});
		} catch (InterruptedException e)
		{
			log.warning(e, "Interrupted while opening display");
		} catch (InvocationTargetException e)
		{
			throw new RuntimeException("Failed to open display", e.getTargetException());
		}
	}
	
	@Override
	public void close()
	{
		log.info("Closing display");
		SwingUtilities.invokeLater(frame::dispose);
	}
}
