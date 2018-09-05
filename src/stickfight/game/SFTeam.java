package pro.delfik.lmao.stickfight.game;

import org.bukkit.Location;
import pro.delfik.lmao.core.Person;

public class SFTeam {
	
	private final Person[] players;
	private final int capacity;
	private final Location center;
	
	public SFTeam(int capacity, Location center) {
		this.capacity = capacity;
		this.players = new Person[capacity];
		this.center = center;
	}
	
	
	public Person[] getPlayers() {
		return players;
	}
	
	public int getCapacity() {
		return capacity;
	}
	
	public Location getCenter() {
		return center;
	}
	
	public boolean contains(String player) {
		for (Person person : players) if (person != null && person.getName().equalsIgnoreCase(player)) return true;
		return false;
	}
	
	public void addPlayer(Person p) {
		if (this.contains(p.getName())) return;
		for (int i = 0; i < players.length; i++) {
			if (players[i] == null) {
				players[i] = p;
				return;
			}
		}
	}
	
	public void removePlayer(Person p) {
		for (int i = 0; i < players.length; i++) if (players[i].equals(p)) players[i] = null;
	}
}
