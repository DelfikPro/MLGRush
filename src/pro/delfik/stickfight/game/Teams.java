package pro.delfik.lmao.stickfight.game;

import java.util.EnumMap;
import java.util.function.BiFunction;
import java.util.function.Function;


/**
 * Класс, который нужен во избежание некрасивого огромного кода.
 * Его инстанция хранит данные по соответствию: Цвет команды - Значение
 * Этот класс был создан в качестве замены костылям вроде создания массива и хранения по порядковому номеру команды
 *
 * @param <V> Тип данных, хранимых в мапе
 */
public class Teams<V> extends EnumMap<Map.Color, V> {
	
	public Teams() {
		super(Map.Color.class);
	}
	
	/**
	 * Конвертирует сам себя в Teams в другим типом данных, при этом переносит все данные, используя заданный конвертер
	 *
	 * @param converter Конвертер, который будет превращать старые данные в новые
	 * @param <N> Новый тип данных
	 * @return Teams со всеми переконвертированными данными
	 *
	 * Например, есть {@code Teams<String> strings}, а нужен {@code Teams<Integer>}.
	 * Для этого можно использовать {@code string.convert(Integer::new);}
	 */
	public <N> Teams<N> convert(Function<V, N> converter) {
		Teams<N> t = new Teams<>();
		for (Entry<Map.Color, V> e : entrySet()) t.put(e.getKey(), converter.apply(e.getValue()));
		return t;
	}
	
	/**
	 * Аналогичный метод, только в функцию передаётся ещё и Color.
	 */
	public <N> Teams<N> convert(BiFunction<Map.Color, V, N> converter) {
		Teams<N> t = new Teams<>();
		for (Entry<Map.Color, V> e : entrySet()) t.put(e.getKey(), converter.apply(e.getKey(), e.getValue()));
		return t;
	}
	
}
