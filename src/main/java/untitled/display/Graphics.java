package untitled.display;

public interface Graphics<T>
{
	T getGraphicsContext();
	
	void translate(double x, double y);
	
	void setColor(double r, double g, double b, double a);
	
	default void setColor(double r, double g, double b)
	{
		setColor(r, g, b, 1.0f);
	}
	
	void setFont(String family, int style, int size);
	
	void drawText(String text, double x, double y);
	
	void strokeLine(double x1, double y1, double x2, double y2);
	
	void strokeRect(double x, double y, double w, double h);
	
	void strokeOval(double x, double y, double w, double h);
	
	void fillRect(double x, double y, double w, double h);
	
	void fillOval(double x, double y, double w, double h);
	
	void dispose();
}
