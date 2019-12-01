
package com.tiamo.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 
 * @author
 * @email
 * @date 2018-06-20 14:32:37
 */
public class CustomBeanAndSuperUtils {
	private static final Map<String, Map<String, Field>> cacheFields = new ConcurrentHashMap();
	private static final Set<Class> basicClass = new HashSet();

	public CustomBeanAndSuperUtils() {
	}

	public static <T> T convertPojo(Object orig, Class<T> targetClass) {
		if (orig == null) {
			throw new RuntimeException("java空指针异常：java.lang.NullPointException");
		} else {
			try {
				T target = targetClass.newInstance();
				List<Field> list = new CopyOnWriteArrayList<>();
				Class<?> origClass = orig.getClass();
				while (origClass != null) {
					list.addAll(Arrays.asList(origClass.getDeclaredFields()));
					origClass = origClass.getSuperclass();
				}
				Field[] fields = list.toArray(new Field[] {});
				Field[] var4 = fields;
				int var5 = fields.length;

				for (int var6 = 0; var6 < var5; ++var6) {
					Field field = var4[var6];
					if (!isStatic(field)) {
						Field targetField = getTargetField(targetClass, field.getName());
						if (targetField != null) {
							Object value = getFiledValue(field, orig);
							if (value != null) {
								Class type1 = field.getType();
								Class type2 = targetField.getType();
								boolean sameType = type1.equals(type2);
								if (isBasicType(type1)) {
									if (sameType) {
										setFieldValue(targetField, target, value);
									}
								} else if (value instanceof Map && Map.class.isAssignableFrom(type2)) {
									setMap((Map) value, field, targetField, target);
								} else if (value instanceof Set && Set.class.isAssignableFrom(type2)) {
									setCollection((Collection) value, field, targetField, target);
								} else if (value instanceof List && List.class.isAssignableFrom(type2)) {
									setCollection((Collection) value, field, targetField, target);
								} else if (value instanceof Enum && Enum.class.isAssignableFrom(type2)) {
									setEnum((Enum) value, field, targetField, target);
								} else if (value instanceof Date && Date.class.isAssignableFrom(type2)) {
									setDate((Date) value, targetField, type2, target, sameType);
								}
							}
						}
					}
				}

				return target;
			} catch (Throwable var13) {
				throw new RuntimeException(var13.getMessage());
			}
		}
	}

	private static Object getFiledValue(Field field, Object obj) throws IllegalAccessException {
		boolean access = field.isAccessible();

		Object var3;
		try {
			field.setAccessible(true);
			var3 = field.get(obj);
		} finally {
			field.setAccessible(access);
		}

		return var3;
	}

	private static void setFieldValue(Field field, Object obj, Object value) throws IllegalAccessException {
		boolean access = field.isAccessible();

		try {
			field.setAccessible(true);
			field.set(obj, value);
		} finally {
			field.setAccessible(access);
		}

	}

	public static <T> List<T> convertPojos(List orig, Class<T> targetClass) {
		List<T> list = new ArrayList(orig.size());
		Iterator var3 = orig.iterator();

		while (var3.hasNext()) {
			Object object = var3.next();
			list.add(convertPojo(object, targetClass));
		}

		return list;
	}

	private static <T> void setMap(Map value, Field origField, Field targetField, T targetObject)
			throws IllegalAccessException, InstantiationException {
		Type origType = origField.getGenericType();
		Type targetType = targetField.getGenericType();
		if (origType instanceof ParameterizedType && targetType instanceof ParameterizedType) {
			ParameterizedType origParameterizedType = (ParameterizedType) origType;
			Type[] origTypes = origParameterizedType.getActualTypeArguments();
			ParameterizedType targetParameterizedType = (ParameterizedType) targetType;
			Type[] targetTypes = targetParameterizedType.getActualTypeArguments();
			if (origTypes != null && origTypes.length == 2 && targetTypes != null && targetTypes.length == 2) {
				Class clazz = (Class) origTypes[1];
				if (!isBasicType(clazz) && !clazz.equals(targetTypes[1])) {
					Set<Entry> entries = value.entrySet();
					Map targetMap = (Map) value.getClass().newInstance();
					Iterator var13 = entries.iterator();

					while (var13.hasNext()) {
						Entry entry = (Entry) var13.next();
						targetMap.put(entry.getKey(), convertPojo(entry.getValue(), (Class) targetTypes[1]));
					}

					setFieldValue(targetField, targetObject, targetMap);
					return;
				}
			}
		}

		setFieldValue(targetField, targetObject, value);
	}

	private static <T> void setCollection(Collection value, Field origField, Field targetField, T targetObject)
			throws IllegalAccessException, InstantiationException {
		Type origType = origField.getGenericType();
		Type targetType = targetField.getGenericType();
		if (origType instanceof ParameterizedType && targetType instanceof ParameterizedType) {
			ParameterizedType origParameterizedType = (ParameterizedType) origType;
			Type[] origTypes = origParameterizedType.getActualTypeArguments();
			ParameterizedType targetParameterizedType = (ParameterizedType) targetType;
			Type[] targetTypes = targetParameterizedType.getActualTypeArguments();
			if (origTypes != null && origTypes.length == 1 && targetTypes != null && targetTypes.length == 1) {
				Class clazz = (Class) origTypes[0];
				if (!isBasicType(clazz) && !clazz.equals(targetTypes[0])) {
					Collection collection = (Collection) value.getClass().newInstance();
					Iterator var12 = value.iterator();

					while (var12.hasNext()) {
						Object obj = var12.next();
						collection.add(convertPojo(obj, (Class) targetTypes[0]));
					}

					setFieldValue(targetField, targetObject, collection);
					return;
				}
			}
		}

		setFieldValue(targetField, targetObject, value);
	}

	private static <T> void setEnum(Enum value, Field origField, Field targetField, T targetObject) throws Exception {
		if (origField.equals(targetField)) {
			setFieldValue(targetField, targetObject, value);
		} else {
			Method method = targetField.getType().getMethod("valueOf", String.class);
			setFieldValue(targetField, targetObject, method.invoke((Object) null, value.toString()));
		}

	}

	private static <T> void setDate(Date value, Field targetField, Class targetFieldType, T targetObject,
			boolean sameType) throws IllegalAccessException {
		Date date = null;
		if (sameType) {
			date = value;
		} else if (targetFieldType.equals(java.sql.Date.class)) {
			date = new java.sql.Date(value.getTime());
		} else if (targetFieldType.equals(Date.class)) {
			date = new Date(value.getTime());
		} else if (targetFieldType.equals(Timestamp.class)) {
			date = new Timestamp(value.getTime());
		}

		setFieldValue(targetField, targetObject, date);
	}

	public static Field getTargetField(Class clazz, String fieldName) {
		String classKey = clazz.getName();
		Map<String, Field> fieldMap = (Map) cacheFields.get(classKey);
		if (fieldMap == null) {
			fieldMap = new HashMap();
			List<Field> list = new CopyOnWriteArrayList<>();
			while (clazz != null) {
				list.addAll(Arrays.asList(clazz.getDeclaredFields())); // 将父类的元素页加入其中
				clazz = clazz.getSuperclass();
			}
			Field[] fields = list.toArray(new Field[] {});
			Field[] var5 = fields;
			int var6 = fields.length;

			for (int var7 = 0; var7 < var6; ++var7) {
				Field field = var5[var7];
				if (!isStatic(field)) {
					((Map) fieldMap).put(field.getName(), field);
				}
			}

			cacheFields.put(classKey, fieldMap);
		}

		return (Field) ((Map) fieldMap).get(fieldName);
	}

	public static boolean isBasicType(Class clazz) {
		return clazz.isPrimitive() || basicClass.contains(clazz);
	}

	public static boolean isStatic(Field field) {
		return (8 & field.getModifiers()) == 8;
	}

	static {
		basicClass.add(Integer.class);
		basicClass.add(Character.class);
		basicClass.add(Byte.class);
		basicClass.add(Float.class);
		basicClass.add(Double.class);
		basicClass.add(Boolean.class);
		basicClass.add(Long.class);
		basicClass.add(Short.class);
		basicClass.add(String.class);
		basicClass.add(BigDecimal.class);
	}
}

