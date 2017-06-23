package untitled.input;

import javafx.scene.Node;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class FXInputHandler extends Input.Handler
{
	public FXInputHandler(Node node)
	{
		node.setOnMouseReleased(this::mouseReleased);
		node.setOnMousePressed(this::mousePressed);
		node.setOnKeyReleased(this::keyReleased);
		node.setOnKeyPressed(this::keyPressed);
		node.setOnMouseMoved(this::mouseMoved);
		node.setOnMouseDragged(this::mouseMoved);
		node.setOnMouseEntered(this::mouseEntered);
		node.setOnMouseDragEntered(this::mouseEntered);
		node.setOnMouseExited(this::mouseExited);
		node.setOnMouseDragExited(this::mouseExited);
	}
	
	public void keyPressed(KeyEvent event)
	{
		pressButton(event.getCode()
						 .ordinal());
	}
	
	public void keyReleased(KeyEvent event)
	{
		releaseButton(event.getCode()
						   .ordinal());
	}
	
	public void mousePressed(MouseEvent event)
	{
		pressButton(~event.getButton()
						  .ordinal());
	}
	
	public void mouseReleased(MouseEvent event)
	{
		releaseButton(~event.getButton()
							.ordinal());
	}
	
	public void mouseMoved(MouseEvent event)
	{
		moveMouse((int) event.getX(), (int) event.getY());
	}
	
	public void mouseEntered(MouseEvent event)
	{
		setMouseExited(false);
	}
	
	public void mouseExited(MouseEvent event)
	{
		setMouseExited(true);
	}
}
