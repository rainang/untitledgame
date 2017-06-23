package untitled;

import java.awt.event.KeyEvent;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class Input
{
	private final Set<Button> buttons = Collections.newSetFromMap(new ConcurrentHashMap<>());
	private final MouseLocation mouseLocation = new MouseLocation();
	
	Input(Handler handler)
	{
		handler.setInput(this);
	}
	
	public Button getButton(int id)
	{
		return getButton(id, true);
	}
	
	private Button getButton(int id, boolean create)
	{
		for (Button button : buttons)
			if (button.getId() == id)
				return button;
		if (!create)
			return null;
		Button button = new Button(id);
		buttons.add(button);
		return button;
	}
	
	public Button getMouseButton(int id)
	{
		return getButton(~id);
	}
	
	public MouseLocation getMouseLocation()
	{
		return mouseLocation;
	}
	
	void update()
	{
		mouseLocation.update();
		for (Button button : buttons)
			button.update();
	}
	
	public static class Handler
	{
		private Input input;
		
		private void setInput(Input input)
		{
			this.input = input;
		}
		
		public Input getInput()
		{
			return input;
		}
		
		protected void pressButton(int id)
		{
			Input.Button button = getInput().getButton(id, false);
			if (button != null && button.isReleased())
				button.press();
		}
		
		protected void releaseButton(int id)
		{
			Input.Button button = getInput().getButton(id, false);
			if (button != null && button.isPressed())
				button.release();
		}
		
		protected void moveMouse(int x, int y)
		{
			getInput().getMouseLocation()
					  .move(x, y);
		}
		
		protected void setMouseExited(boolean exited)
		{
			getInput().getMouseLocation()
					  .setExited(exited);
		}
	}
	
	public static final class Button
	{
		private static final int PRESSED = 0;
		private static final int RELEASED = ~0;
		
		private final int id;
		private final String name;
		
		private volatile int durationPressed = RELEASED;
		
		private Button(int id)
		{
			this.id = id;
			name = id >= 0 ? KeyEvent.getKeyText(id) : String.format("Mouse%d", ~id);
		}
		
		private synchronized void press()
		{
			durationPressed = PRESSED;
		}
		
		private synchronized void release()
		{
			durationPressed = RELEASED;
		}
		
		private synchronized void update()
		{
			if (isPressed())
				durationPressed++;
			else
				durationPressed--;
		}
		
		public boolean isPressed()
		{
			return durationPressed >= PRESSED;
		}
		
		public boolean isJustPressed()
		{
			return getDurationPressed() == 1;
		}
		
		public boolean isReleased()
		{
			return durationPressed <= RELEASED;
		}
		
		public boolean isJustReleased()
		{
			return getDurationReleased() == 1;
		}
		
		public int getDurationPressed()
		{
			return isReleased() ? 0 : durationPressed;
		}
		
		public int getDurationReleased()
		{
			return isPressed() ? 0 : ~durationPressed;
		}
		
		public int getId()
		{
			return id;
		}
		
		public String getName()
		{
			return name;
		}
		
		@Override
		public int hashCode()
		{
			return id;
		}
		
		@Override
		public boolean equals(Object other)
		{
			return other == this || (other instanceof Button && ((Button) other).id == id);
		}
		
		@Override
		public String toString()
		{
			return String.format("Button[id=%d,name=%s,durationPressed=%d]", id, name, durationPressed);
		}
	}
	
	public static final class MouseLocation
	{
		private volatile boolean exited;
		private int x;
		private int y;
		private int nextX;
		private int nextY;
		private boolean moved;
		
		private MouseLocation() {}
		
		private synchronized void update()
		{
			if (moved = (x != nextX || y != nextY))
			{
				x = nextX;
				y = nextY;
			}
		}
		
		private synchronized void move(int x, int y)
		{
			nextX = x;
			nextY = y;
		}
		
		private void setExited(boolean exited)
		{
			this.exited = exited;
		}
		
		public int getX() { return x; }
		
		public int getY() { return y; }
		
		public boolean hasMoved() { return moved; }
		
		public boolean hasExited() { return exited; }
		
		@Override
		public String toString()
		{
			return String.format("MouseLocation[x=%d,y=%d]", x, y);
		}
	}
}
