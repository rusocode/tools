package _LABORATORIO;

public abstract class Screen {

	protected Game game;

	public Screen(Game game) {
		this.game = game;
	}

	public void print() {
		System.out.println("Metodo del objeto Screen");
	}

}
