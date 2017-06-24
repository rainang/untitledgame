package untitled.display;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class FXGraphicsWrapper implements Graphics<GraphicsContext>
{
	private final Canvas canvas;
	
	public FXGraphicsWrapper(Canvas canvas)
	{
		this.canvas = canvas;
	}
	
	@Override
	public GraphicsContext getGraphicsContext()
	{
		return canvas.getGraphicsContext2D();
	}
	
	@Override
	public void translate(double x, double y)
	{
		getGraphicsContext().translate(x, y);
	}
	
	@Override
	public void setColor(double r, double g, double b, double a)
	{
		Color c = new Color(r, g, b, a);
		getGraphicsContext().setFill(c);
		getGraphicsContext().setStroke(c);
	}
	
	@Override
	public void setFont(String family, int style, int size)
	{
		// TODO: 2017-06-24 add style
		getGraphicsContext().setFont(Font.font(family, size));
	}
	
	// TODO: 2017-06-24 move text rendering out of fx canvas
	@Override
	public void drawText(String text, double x, double y)
	{
		getGraphicsContext().strokeText(text, x, y);
	}
	
	@Override
	public void fillRect(double x, double y, double w, double h)
	{
		getGraphicsContext().fillRect(x, y, w, h);
	}
	
	@Override
	public void strokeRect(double x, double y, double w, double h)
	{
		getGraphicsContext().strokeRect(x, y, w, h);
	}
	
	@Override
	public void fillOval(double x, double y, double w, double h)
	{
		getGraphicsContext().fillOval(x, y, w, h);
	}
	
	@Override
	public void strokeOval(double x, double y, double w, double h)
	{
		getGraphicsContext().strokeOval(x, y, w, h);
	}
	
	@Override
	public void strokeLine(double x1, double y1, double x2, double y2)
	{
		getGraphicsContext().strokeLine(x1, y1, x2, y2);
	}
	
	@Override
	public void dispose() {}
}
