package pro.delfik.lmao.mlg.game.side;

import org.bukkit.Location;
import pro.delfik.lmao.core.Person;
import pro.delfik.lmao.mlg.game.Sector;

public class RedSide extends Side {
	public static Location defaultLoc;
	public static Location[] defaultBed;

	public RedSide(Sector s, Person p) {
		super(s, p);
	}

	@Override
	public Location[] getDefaultBed() {
		return defaultBed;
	}

	@Override
	public Location getDefaultLocation() {
		return defaultLoc;
	}
}
