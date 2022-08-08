package _lab;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Propiedades del sistema actual.
 */

public class Properties_ {

	public static void main(String[] args) {

		Properties properties = System.getProperties();

		LinkedHashMap<String, String> collect = properties.entrySet().stream()
				.collect(Collectors.toMap(k -> (String) k.getKey(), e -> (String) e.getValue()))
				.entrySet().stream().sorted(Map.Entry.comparingByKey())
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
						(oldValue, newValue) -> oldValue, LinkedHashMap::new));

		collect.forEach((k, v) -> System.out.println(k + " = " + v));

	}

}
