package pro.delfik.mlg.interact;

import org.bukkit.Bukkit;
import pro.delfik.lmao.command.handle.PersonNotFoundException;
import pro.delfik.lmao.user.Person;
import pro.delfik.mlg.MLGRush;
import pro.delfik.mlg.Sector;
import pro.delfik.lmao.util.U;

import java.util.HashMap;

public class Call {
	
	public static final HashMap<String, Call> list = new HashMap<>();
	
	public final String invoker;
	public final String receiver;
	
	public Call(String invoker, String receiver) {
		this.invoker = invoker;
		this.receiver = receiver;
		list.put(invoker.toLowerCase(), this);
	}
	
	public void call() {
		Person receiver = Person.get(this.receiver);
		Person invoker = Person.get(this.invoker);
		receiver.sendTitle("§f");
		receiver.sendSubtitle("§d§lВас вызвали на дуэль!");
		receiver.sendMessage("§8[§dMLG§8] §aИгрок §e" + invoker.getDisplayName() + "§a вызывает вас на дуэль!");
		U.msg(receiver.getHandle(), U.run("§8[§dMLG§8] §aУ вас есть 60 секунд, чтобы нажать §nсюда§a и принять запрос!",
				"§e>> Нажмите для принятия запроса <<", "/sf " + invoker.getName()));
		Bukkit.getScheduler().scheduleSyncDelayedTask(MLGRush.plugin, () -> {
			if (!list.containsKey(invoker.getName().toLowerCase())) return;
			if (receiver != null) receiver.sendMessage("§8[§dMLG§8] §6Вызов на дуэль от §e" + invoker.getName() + "§6 истёк.");
			if (invoker != null) invoker.sendMessage("§8[§dMLG§8] §e" + receiver.getName() + "§6 не принял ваш вызов.");
			remove();
		}, 1200L);
	}
	
	private void remove() {
		list.remove(invoker.toLowerCase());
	}
	
	public void accept() {
		list.remove(invoker.toLowerCase());
		Person receiver = Person.get(this.receiver);
		Person invoker = Person.get(this.invoker);
		if (invoker == null) {
			receiver.sendMessage("§8[§dMLG§8] §6Ваш соперник вышел из игры. Вызов не может быть принят.");
		} else {
			if (Queue.waiting != null) if (Queue.waiting.equals(invoker) || Queue.waiting.equals(receiver)) Queue.waiting = null;
			try {
				new Sector(invoker, receiver);
			} catch (PersonNotFoundException ignored) {}
		}
		remove();
	}
}
