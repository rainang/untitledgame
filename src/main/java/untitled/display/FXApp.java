package untitled.display;

import javafx.application.Application;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;

public abstract class FXApp extends Application
{
	private Stage primaryStage;
	private Canvas canvas;
	
	@Override
	public void start(Stage primaryStage) throws Exception
	{
		this.primaryStage = primaryStage;
		this.canvas = new Canvas();
		
		Thread t = new Thread(() -> createGame(this));
		t.setName("main");
		t.setDaemon(true);
		t.start();
	}
	
	public abstract void createGame(FXApp app);
	
	public Stage getPrimaryStage()
	{
		return primaryStage;
	}
	
	public Canvas getCanvas()
	{
		return canvas;
	}
}
