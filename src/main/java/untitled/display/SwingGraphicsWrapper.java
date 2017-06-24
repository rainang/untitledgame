package untitled.display;

import java.awt.*;
import java.awt.image.BufferStrategy;

public class SwingGraphicsWrapper implements Graphics<java.awt.Graphics>
{
	private final BufferStrategy buffer;
	private java.awt.Graphics graphics;
	
	public SwingGraphicsWrapper(BufferStrategy buffer)
	{
		this.buffer = buffer;
	}
	
	@Override
	public java.awt.Graphics getGraphicsContext()
	{
		if (graphics == null)
		{
			graphics = buffer.getDrawGraphics();
		}
		return graphics;
	}
	
	@Override
	public void dispose()
	{
		if (graphics != null)
		{
			graphics.dispose();
			graphics = null;
			if (!buffer.contentsLost())
				buffer.show();
		}
	}
	
	@Override
	public void translate(double x, double y)
	{
		getGraphicsContext().translate((int) x, (int) y);
	}
	
	@Override
	public void setColor(double r, double g, double b, double a)
	{
		Color c = new Color((float) r, (float) g, (float) b, (float) a);
		getGraphicsContext().setColor(c);
	}
	
	@Override
	public void setFont(String family, int style, int size)
	{
		getGraphicsContext().setFont(new Font(family, style, size));
	}
	
	@Override
	public void drawText(String text, double x, double y)
	{
		getGraphicsContext().drawString(text, (int) x, (int) y);
	}
	
	@Override
	public void fillRect(double x, double y, double w, double h)
	{
		getGraphicsContext().fillRect((int) x, (int) y, (int) w, (int) h);
	}
	
	@Override
	public void strokeRect(double x, double y, double w, double h)
	{
		getGraphicsContext().drawRect((int) x, (int) y, (int) w, (int) h);
	}
	
	@Override
	public void fillOval(double x, double y, double w, double h)
	{
		getGraphicsContext().fillOval((int) x, (int) y, (int) w, (int) h);
	}
	
	@Override
	public void strokeOval(double x, double y, double w, double h)
	{
		getGraphicsContext().drawOval((int) x, (int) y, (int) w, (int) h);
	}
	
	@Override
	public void strokeLine(double x1, double y1, double x2, double y2)
	{
		getGraphicsContext().drawLine((int) x1, (int) y1, (int) x2, (int) y2);
	}
}
