package untitled;

import untitled.display.Display;

import javax.annotation.Nonnull;

public interface Game
{
	@Nonnull
	String getTitle();
	
	@Nonnull
	String getVersion();
	
	@Nonnull
	Loop getGameLoop();
	
	@Nonnull
	State.Handler getStateHandler();
	
	@Nonnull
	Display getDisplay();
	
	@Nonnull
	Input getInput();
	
	@Nonnull
	Logger getLogger();
}
