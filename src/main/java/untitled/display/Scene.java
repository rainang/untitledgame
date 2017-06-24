package untitled.display;

import untitled.game.Input;
import untitled.game.State;

public interface Scene
{
	void render(State state, Graphics graphics, float deltaTime);
	
	default void setUp(State state, Input input) {}
	
	default void cleanUp() {}
	
	default void update() {}
}
