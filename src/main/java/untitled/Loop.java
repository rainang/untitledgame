package untitled;

import javax.annotation.Nonnull;

public class Loop
{
	private final Game game;
	
	private final int targetUps;
	private final int targetFps;
	
	private final long stateUpdateInterval;
	private final long frameUpdateInterval;
	
	private final int maxFrameSkips;
	
	boolean isRunning;
	
	public Loop(@Nonnull Game game, int targetUps, int targetFps, int maxFrameSkips)
	{
		this.game = game;
		this.targetUps = targetUps;
		this.targetFps = targetFps;
		this.stateUpdateInterval = 1_000_000_000 / targetUps;
		this.frameUpdateInterval = 1_000_000_000 / targetFps;
		this.maxFrameSkips = maxFrameSkips;
	}
	
	@Nonnull
	public Game getGame()
	{
		return game;
	}
	
	public boolean isRunning()
	{
		return isRunning;
	}
	
	public int getTargetUps()
	{
		return targetUps;
	}
	
	public int getTargetFps()
	{
		return targetFps;
	}
	
	public long getStateUpdateInterval()
	{
		return stateUpdateInterval;
	}
	
	public long getFrameUpdateInterval()
	{
		return frameUpdateInterval;
	}
	
	public int getMaxFrameSkips()
	{
		return maxFrameSkips;
	}
	
	public void runLoop()
	{
		if (isRunning)
			throw new IllegalStateException("Game is already running!");
		
		Game game = getGame();
		
		State.Handler stateHandler = game.getStateHandler();
		
		long nextStateUpdateTime = System.nanoTime();
		long nextFrameUpdateTime = System.nanoTime();
		long currentTime;
		
		while (isRunning() || stateHandler.getCurrentState() instanceof State.Starting)
		{
			int numSkippedFrames = 0;
			currentTime = System.nanoTime();
			
			while (currentTime - nextStateUpdateTime >= 0 && numSkippedFrames++ < getMaxFrameSkips())
			{
				stateHandler.flushEventQueue();
				
				game.getInput()
					.update();
				game.getDisplay()
					.update();
				stateHandler.getCurrentState()
							.update();
				
				nextStateUpdateTime += getStateUpdateInterval();
			}
			
			currentTime = System.nanoTime();
			
			while (currentTime - nextFrameUpdateTime >= 0)
			{
				float leftOverTime = nextStateUpdateTime - currentTime;
				float deltaTime = (getStateUpdateInterval() - leftOverTime) / getStateUpdateInterval();
				
				game.getDisplay()
					.render(deltaTime);
				
				nextFrameUpdateTime += getFrameUpdateInterval();
			}
		}
		
		assert stateHandler.getCurrentState() instanceof State.Stopping : "Stopped outside of a stopping state";
	}
}
