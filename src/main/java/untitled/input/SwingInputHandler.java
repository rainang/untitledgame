package untitled.input;

import untitled.game.Input;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class SwingInputHandler extends Input.Handler implements KeyListener, MouseListener, MouseWheelListener,
		MouseMotionListener
{
	public SwingInputHandler(Component component)
	{
		component.addKeyListener(this);
		component.addMouseMotionListener(this);
		component.addMouseListener(this);
	}
	
	@Override
	public void keyTyped(KeyEvent event) {}
	
	@Override
	public void keyPressed(KeyEvent event)
	{
		pressButton(event.getKeyCode());
	}
	
	@Override
	public void keyReleased(KeyEvent event)
	{
		releaseButton(event.getKeyCode());
	}
	
	@Override
	public void mouseClicked(MouseEvent event) {}
	
	@Override
	public void mousePressed(MouseEvent event)
	{
		pressButton(~event.getButton());
	}
	
	@Override
	public void mouseReleased(MouseEvent event)
	{
		releaseButton(~event.getButton());
	}
	
	@Override
	public void mouseMoved(MouseEvent event)
	{
		moveMouse(event.getX(), event.getY());
	}
	
	@Override
	public void mouseDragged(MouseEvent event)
	{
		moveMouse(event.getX(), event.getY());
	}
	
	@Override
	public void mouseEntered(MouseEvent event)
	{
		setMouseExited(false);
	}
	
	@Override
	public void mouseExited(MouseEvent event)
	{
		setMouseExited(true);
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent event) {}
}