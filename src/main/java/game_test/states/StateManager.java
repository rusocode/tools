package game_test.states;

/**
 * Administrador de estados.
 *
 * <p>TODO Aplico LazySingleton solo para el lado del cliente?
 * <p>TODO Aplico BillPughSingleton (seguro y eficaz para subprocesos) solo para el lado del servidor?
 */

public class StateManager {

	private State state;
	private static StateManager instance;

	private StateManager() {
	}

	public static StateManager getInstance() {
		if (instance == null) instance = new StateManager();
		return instance;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

}
