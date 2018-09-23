package pro.delfik.mlg.side;

import org.bukkit.Location;
import pro.delfik.lmao.user.Person;
import pro.delfik.mlg.Sector;

public class BlueSide extends Side {
	public static Location defaultLoc;
	public static Location[] defaultBed;

	public BlueSide(Sector s, Person p) {
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
