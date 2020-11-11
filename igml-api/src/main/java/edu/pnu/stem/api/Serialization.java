package edu.pnu.stem.api;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import edu.pnu.stem.binder.IndoorGMLMap;
import edu.pnu.stem.feature.core.CellSpace;
import edu.pnu.stem.feature.core.State;

public class Serialization {

	public static void main(String[] args) {
		IndoorGMLMap newMap = new IndoorGMLMap();
		newMap.setDocId("1234");
		CellSpace newFeature = new CellSpace(newMap, "123");
		State newFeature2 = new State(newMap, "S123");
		newFeature.setDuality(newFeature2);
		newFeature2.setDuality(newFeature);
		newMap.setFeature("123", "CellSpace", newFeature);
		serializeIndoorGMLMap(null, newMap);
		newMap.setFeature("S123", "State", newFeature2);
		serializeIndoorGMLMap(null,newMap);
		System.out.print("Serialized HashMap data is saved in hashmap.ser");

		IndoorGMLMap result = null;
		try {
			FileInputStream fis = new FileInputStream("hashmap.ser");
			ObjectInputStream ois = new ObjectInputStream(fis);
			// result = (IndoorGMLMap) ois.readObject();
			Object temp = ois.readObject();
			if (temp instanceof IndoorGMLMap)
				result = (IndoorGMLMap) temp;
			ois.close();
			fis.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return;
		} catch (ClassNotFoundException c) {
			System.out.println("Class not found");
			c.printStackTrace();
			return;
		}
		System.out.println("Deserialized HashMap..");

		// Display content using Iterator
		assert result != null;
		ConcurrentHashMap<String, Object> container = result.getFeatureContainer("State");
		State state1 = (State) container.get("S123");
		System.out.println(state1.getId());
	}

	public IndoorGMLMap deSerializeIndoorGMLMap(String fileName) {
		IndoorGMLMap result = null;
		FileInputStream fis;
		try {
			fis = new FileInputStream(Objects.requireNonNullElse(fileName, "hashmap.ser"));
			ObjectInputStream ois = new ObjectInputStream(fis);
			result = (IndoorGMLMap) ois.readObject();
			ois.close();
			fis.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (ClassNotFoundException c) {
			System.out.println("Class not found");
			c.printStackTrace();
		}

		return result;
	}

	public static void serializeIndoorGMLMap(String fileName, IndoorGMLMap map) {
		try {
			FileOutputStream fos;
			fos = new FileOutputStream(Objects.requireNonNullElse(fileName, "hashmap.ser"));
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(map);
			oos.close();
			fos.close();
			System.out.print("Serialized HashMap data is saved in hashmap.ser");
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}
