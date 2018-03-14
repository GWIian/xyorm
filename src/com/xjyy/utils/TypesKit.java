package com.xjyy.utils;

import java.lang.reflect.Field;
import java.sql.Types;

public class TypesKit {
	public static String getTypesName(int typeCode) {
		for (Field x : Types.class.getFields()) {
			try {
				if (typeCode == (int) x.get(null)) {
					return x.getName();
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
