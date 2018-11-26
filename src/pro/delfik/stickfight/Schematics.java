package pro.delfik.lmao.stickfight;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.schematic.MCEditSchematicFormat;
import com.sk89q.worldedit.util.io.file.FilenameException;
import com.sk89q.worldedit.world.DataException;
import org.bukkit.Location;
import org.bukkit.World;
import pro.delfik.lmao.stickfight.util.Vec3i;

import java.io.File;
import java.io.IOException;

/**
 * Класс для управления схематиками.
 * {@code loadSchematic(...)} асинхронно вставляет схематику в мир.
 */
public final class Schematics {
	
	private static final String EXTENSION = "schematic";
	
	private final WorldEdit we;
	private final EditSession editSession;
	
	/**
	 * @param wep Инстанция WorldEditPlugin, обычно получается через {@code Bukkit.getPluginManager().getPlugin("WorldEdit")}.
	 * @param world Мир, к которому будет привязана сессия редактирования, и в котором будут происходить все вставки.
	 */
	public Schematics(WorldEditPlugin wep, World world) {
		we = wep.getWorldEdit();
		editSession = new EditSession(new BukkitWorld(world), we.getConfiguration().maxChangeLimit);
	}
	
	/**
	 * @param file Название схематики, уже находящейся в папке "./plugins/WorldEdit/schematics" без расширения
	 * @param location Место, куда нужно вставить схематику
	 */
	public void loadSchematic(String file, Vec3i location) {
		try {
			loadSchematic(new File("plugins/WorldEdit/schematics/" + file + "." + EXTENSION), location);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	/**
	 * @param saveFile Файл схематики
	 * @param location Место, куда вставлять схематику
	 * @throws FilenameException когда никогда
	 * @throws DataException когда никогда
	 * @throws IOException когда никогда
	 * @throws MaxChangedBlocksException когда никогда
	 */
	public void loadSchematic(File saveFile, Vec3i location) throws FilenameException, DataException, IOException, MaxChangedBlocksException {
		// Преобразовываем файл джавы в "Безопасный" файл WE.
		saveFile = we.getSafeSaveFile(null, saveFile.getParentFile(), saveFile.getName(), EXTENSION, EXTENSION);
		
		// Непонятная магия, которую нельзя трогать.
		editSession.enableQueue();
		MCEditSchematicFormat.getFormat(saveFile).load(saveFile).paste(editSession, location.toWEVector(), true);
		editSession.flushQueue();
		we.flushBlockBag(null, editSession);
	}
	
}
