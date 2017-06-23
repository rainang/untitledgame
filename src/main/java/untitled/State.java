package untitled;

import javax.annotation.Nonnull;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class State
{
	@Nonnull
	protected abstract Game getGame();
	
	public abstract String name();
	
	protected void setUp() {}
	
	protected void update() {}
	
	protected void suspend() {}
	
	protected void resume() {}
	
	protected void cleanUp() {}
	
	@Override
	public String toString() { return name(); }
	
	public static abstract class Starting extends State
	{
		@Override
		protected void update()
		{
			getGame().getGameLoop().isRunning = true;
		}
		
	}
	
	public static abstract class Stopping extends State
	{
		@Override
		protected void update()
		{
			getGame().getGameLoop().isRunning = false;
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
		
		public Handler(@Nonnull Game game)
		{
			log = game.getLogger();
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
