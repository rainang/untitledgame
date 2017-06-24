package untitled.display;

import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.text.FontSmoothingType;
import javafx.stage.Stage;
import untitled.game.Display;
import untitled.game.Game;

public abstract class FXDisplay extends Display<FXGraphicsWrapper>
{
	private volatile javafx.scene.canvas.Canvas canvas;
	private volatile FXGraphicsWrapper graphics;
	
	private Stage primaryStage;
	
	public FXDisplay(Game game, FXApp app)
	{
		super(game);
		
		this.primaryStage = app.getPrimaryStage();
		
		Platform.runLater(() ->
		{
			canvas = app.getCanvas();
			graphics = new FXGraphicsWrapper(canvas);
			canvas.setWidth(getWidth());
			canvas.setHeight(getHeight());
			canvas.getGraphicsContext2D()
				  .setFontSmoothingType(FontSmoothingType.GRAY);
			Pane root = new Pane(canvas);
			root.setPrefSize(getWidth(), getHeight());
			
			javafx.scene.Scene scene = new javafx.scene.Scene(root, getWidth(), getHeight());
			primaryStage.setScene(scene);
			primaryStage.setOnCloseRequest(event -> close());
		});
	}
	
	@Override
	protected FXGraphicsWrapper getGraphics()
	{
		return graphics;
	}
	
	@Override
	protected void open()
	{
		log.info("Opening display");
		Platform.runLater(primaryStage::show);
	}
	
	@Override
	protected void close()
	{
		log.info("Closing display");
		Platform.runLater(primaryStage::close);
	}
	
	protected void render(float deltaTime)
	{
		getGraphics().getGraphicsContext()
					 .clearRect(0, 0, getWidth(), getHeight());
		super.render(deltaTime);
	}
}
