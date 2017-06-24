package untitled.display;

import untitled.Input;
import untitled.State;

public interface Scene
{
	void render(State state, Graphics graphics, float deltaTime);
	
	default void setUp(State state, Input input) {}
	
	default void cleanUp() {}
	
	default void update() {}
}
