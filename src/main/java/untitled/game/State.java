package untitled.game;

import untitled.Logger;

import javax.annotation.Nonnull;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class State
{
	protected final Game game;
	
	public State(@Nonnull Game game)
	{
		this.game = game;
	}
	
	public String name()
	{
		return getClass().getSimpleName();
	}
	
	protected void setUp() {}
	
	protected void update() {}
	
	protected void suspend() {}
	
	protected void resume() {}
	
	protected void cleanUp() {}
	
	@Override
	public String toString() { return name(); }
	
	public static class Starting extends State
	{
		public Starting(@Nonnull Game game)
		{
			super(game);
		}
		
		@Override
		protected void update()
		{
			game.getGameLoop().isRunning = true;
			game.getDisplay()
				.open();
		}
	}
	
	public static class Stopping extends State
	{
		public Stopping(@Nonnull Game game)
		{
			super(game);
		}
		
		@Override
		protected void update()
		{
			game.getGameLoop().isRunning = false;
			game.getDisplay()
				.close();
		}
	}
	
	public static class Handler
	{
		private final Queue<State> stateStack = Collections.asLifoQueue(new ArrayDeque<>());
		private final Queue<Runnable> eventQueue = new ConcurrentLinkedQueue<>();
		private State currentState;
		private State previousState;
		private State anteriorState;
		
		private final Logger log;
		
		public Handler(@Nonnull Game game, State.Starting initialState)
		{
			log = game.getLogger();
			currentState = initialState;
		}
		
		void flushEventQueue()
		{
			for (Runnable event = eventQueue.poll(); event != null; event = eventQueue.poll())
				event.run();
		}
		
		private void pushState(State nextState)
		{
			log.fine("Pushing state: %s [%s] %s", stateStack, currentState, nextState);
			currentState.suspend();
			
			stateStack.add(currentState);
			previousState = anteriorState = currentState;
			currentState = nextState;
			
			currentState.setUp();
		}
		
		private void switchState(State nextState)
		{
			log.fine("Switching state: %s [%s] %s", stateStack, currentState, nextState);
			currentState.cleanUp();
			
			previousState = currentState;
			currentState = nextState;
			
			currentState.setUp();
		}
		
		private void popState()
		{
			log.fine("Popping state: %s [%s]", stateStack, currentState);
			currentState.cleanUp();
			
			previousState = currentState;
			currentState = stateStack.remove();
			anteriorState = stateStack.peek();
			
			currentState.resume();
		}
		
		public void queueStatePush(@Nonnull State state)
		{
			eventQueue.add(() -> pushState(state));
		}
		
		public void queueStateSwitch(@Nonnull State state)
		{
			eventQueue.add(() -> switchState(state));
		}
		
		public void queueStatePop()
		{
			eventQueue.add(this::popState);
		}
		
		public void queueEvent(@Nonnull Runnable event)
		{
			eventQueue.add(event);
		}
		
		public State getCurrentState()
		{
			return currentState;
		}
		
		public State getPreviousState()
		{
			return previousState;
		}
		
		public State getAnteriorState()
		{
			return anteriorState;
		}
	}
}
