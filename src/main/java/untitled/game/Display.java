package untitled.game;

import untitled.Logger;
import untitled.display.Graphics;
import untitled.display.Scene;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public abstract class Display<T extends Graphics>
{
	protected final Game game;
	protected final Logger log;
	
	private State state;
	
	private final Map<Class<? extends State>, Scene> scenes = new HashMap<>();
	private Scene scene;
	
	public Display(Game game)
	{
		this.game = game;
		this.log = Logger.get("display");
		this.scene = getFallbackScene();
	}
	
	protected abstract T getGraphics();
	
	public abstract int getWidth();
	
	public abstract int getHeight();
	
	protected abstract void open();
	
	protected abstract void close();
	
	void update()
	{
		State.Handler stateHandler = game.getStateHandler();
		if (state != stateHandler.getCurrentState())
		{
			state = stateHandler.getCurrentState();
			scene.cleanUp();
			scene = getSceneFor(state);
			scene.setUp(state, game.getInput());
		}
		scene.update();
	}
	
	protected void render(float deltaTime)
	{
		T graphics = getGraphics();
		scene.render(state, graphics, deltaTime);
		graphics.dispose();
	}
	
	protected final void setSceneFor(@Nonnull Class<? extends State> clazz, @Nonnull Scene scene)
	{
		scenes.put(clazz, scene);
	}
	
	@SuppressWarnings("unchecked")
	private final Scene getSceneFor(State state)
	{
		Scene scene = scenes.get(state.getClass());
		if (scene == null)
		{
			log.warning(
					"No scene for state: name=%s, type=%s",
					state.name(),
					state.getClass()
						 .getName()
			);
			scene = getFallbackScene();
			setSceneFor(state.getClass(), scene);
		}
		return scene;
	}
	
	protected abstract Scene getFallbackScene();
}
