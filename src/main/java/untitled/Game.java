package untitled;

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
	Logger getLogger();
}
