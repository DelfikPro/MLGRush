package pro.delfik.stickfight.game;

public abstract class Game {
	
	private final SFTeam[] teams = new SFTeam[2];
	private volatile int remaining = 300;
	
	protected Game() {
	
	}
	
	protected abstract void respawnPlayers();
	
	public void update() {
	
	}
	
}
