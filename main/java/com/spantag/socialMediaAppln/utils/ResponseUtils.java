
package com.spantag.socialMediaAppln.utils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.persistence.Tuple;

import org.modelmapper.ModelMapper;

public class ResponseUtils {

	public static Object getResponseObject(ResultSet resultSet, Class<?> c) {

		Object responseObject = null;
		try {
			responseObject = c.newInstance();
			Field[] fields = responseObject.getClass().getDeclaredFields();
			responseObject = c.newInstance();
			while (resultSet.next()) {
				for (int i = 0; i < fields.length; i++) {
					Field field = fields[i];
					Object obj = resultSet.getObject(field.getName());
					if (obj != null) {
						field.setAccessible(true);
						fields[i].set(responseObject, changeDataType(obj.getClass().getName(), field.getType().getName(), obj));
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return responseObject;
	}

	public static List<?> getResponseList(ResultSet resultSet, Class<?> c) {

		List<Object> respObjList = null;
		Object responseObject = null;
		try {
			respObjList = new ArrayList<Object>();
			responseObject = c.newInstance();
			Field[] fields = responseObject.getClass().getDeclaredFields();
			responseObject = c.newInstance();
			while (resultSet.next()) {
				responseObject = c.newInstance();
				for (int i = 0; i < fields.length; i++) {
					Field field = fields[i];
					Object obj = resultSet.getObject(field.getName());
					if (obj != null) {
						field.setAccessible(true);
						fields[i].set(responseObject, changeDataType(obj.getClass().getName(), field.getType().getName(), obj));
					}
				}
				respObjList.add(responseObject);
			}

		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return respObjList;
	}

	public static Object changeDataType(String srcDataTye, String tarDataType, Object srcValue) {

		if ("java.math.BigDecimal".equals(srcDataTye) && "java.lang.Long".equals(tarDataType)) {
			return ((BigDecimal) srcValue).longValue();
		}
		else if ("java.lang.String".equals(srcDataTye) && "java.lang.String".equals(tarDataType)) {
			return (String) srcValue;
		}
		else if ("java.lang.String".equals(srcDataTye) && "java.lang.Double".equals(tarDataType)) {
			return Double.valueOf((String) srcValue);
		}
		else if ("java.math.BigDecimal".equals(srcDataTye) && "java.lang.Double".equals(tarDataType)) {
			return ((BigDecimal) srcValue).doubleValue();
		}
		else if ("java.lang.Long".equals(srcDataTye) && "long".equals(tarDataType)) {
			return srcValue;
		}
		return null;
	}

	/*
	 * Function will turn the list of tuple to list of linked hash map in : List of
	 * Tuple Data
	 */
	public static List<LinkedHashMap<String, Object>> lisTupleToLisMap(List<Tuple> in) {

		in.get(0).getElements();

		List<LinkedHashMap<String, Object>> out = IntStream.range(0, in.size()).mapToObj(e -> {
			LinkedHashMap<String, Object> temp = new LinkedHashMap<String, Object>();
			/* List tl = */ IntStream.range(0, in.get(e).getElements().size()).mapToObj(f -> {
				LinkedHashMap<String, Object> temp2 = new LinkedHashMap<String, Object>();
				temp.put(in.get(e).getElements().get(f).getAlias(), in.get(e).get(f));
				return temp2;
			}).collect(Collectors.toList());
			return temp;
		}).collect(Collectors.toList());

		return out;

	}

	/*
	 * Function will turn the list of tuple to list of linked hash map in : List of
	 * Tuple Data
	 */
	public static <T> List<?> lisTupleToLisMap(List<Tuple> in, Class<?> className) {

		in.get(0).getElements();
		List<Object> out = IntStream.range(0, in.size()).mapToObj(e -> {

			Object responseObject;
			try {
				responseObject = className.newInstance();
				/* List tl = */ IntStream.range(0, in.get(e).getElements().size()).mapToObj(f -> {
					LinkedHashMap<String, Object> temp2 = new LinkedHashMap<String, Object>();
					Field field;
					try {
						field = className.getDeclaredField(in.get(e).getElements().get(f).getAlias());
						field.setAccessible(true);
						Object obj = in.get(e).get(f);
						field.set(responseObject, changeDataType(obj.getClass().getName(), field.getType().getName(), obj));
					}
					catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					return temp2;
				}).collect(Collectors.toList());
				return responseObject;
			}
			catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return null;
			}
		}).collect(Collectors.toList());

		return out;

	}

	/*
	 * Function will turn the Forward only result set to list of linked hash map
	 * resultSet : Refcursor Result Set length : MaxLength of the resultset which
	 * need to be configured in Property File
	 */
	@SuppressWarnings({
			"rawtypes", "unchecked"
	})
	public static List<LinkedHashMap<String, Object>> resultSetToListMap(ResultSet resultSet) throws SQLException {

		List<LinkedHashMap<String, Object>> out = new ArrayList();

		/*
		 * out = IntStream.range(0, length).mapToObj(e->{ try {
		 */
		if (resultSet.next()) {
			LinkedHashMap<String, Object> temp = new LinkedHashMap<String, Object>();
			try {
				/* List tl = */ IntStream.range(0, resultSet.getMetaData().getColumnCount()).mapToObj(f -> {
					LinkedHashMap<String, Object> temp2 = new LinkedHashMap<String, Object>();
					try {
						// System.out.println(f+1 + "-" + resultSet.getMetaData().getColumnName(f+1)
						// +"-"+ resultSet.getObject(resultSet.getMetaData().getColumnName(f+1)));
						temp.put(resultSet.getMetaData().getColumnName(f + 1), resultSet.getObject(resultSet.getMetaData().getColumnName(f + 1)));
					}
					catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					return temp2;
				}).collect(Collectors.toList());
			}
			catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			out.add(temp);
		}
		// out.removeIf(Objects::isNull);

		return out;
	}

	@SuppressWarnings("rawtypes")
	public static <T> List<?> resultSetToObject(ResultSet resultSet, Class<?> className) throws SQLException {

		@SuppressWarnings("unchecked")
		List<Object> out = new ArrayList();
		// Object responseObject = null;
		try {
			while (resultSet.next()) {
				Object responseObject = className.newInstance();
				/* List tl = */ IntStream.range(0, resultSet.getMetaData().getColumnCount()).mapToObj(f -> {
					LinkedHashMap<String, Object> temp2 = new LinkedHashMap<String, Object>();
					try {
						Field field = className.getDeclaredField(resultSet.getMetaData().getColumnName(f + 1));
						field.setAccessible(true);
						Object obj = resultSet.getObject(field.getName());
						field.set(responseObject, changeDataType(obj.getClass().getName(), field.getType().getName(), obj));
					}
					catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					return temp2;
				}).collect(Collectors.toList());

				out.add(responseObject);
			}
		}
		catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return out;
	}

	public static <T> List<?> listMapper(List<?> listData, Class<?> entityClass) throws SQLException {

		List<?> out = IntStream.range(0, listData.size()).mapToObj(in -> {
			Object input = listData.get(in);
			ModelMapper m = new ModelMapper();
			Object im = m.map(input, entityClass);
			return im;
		}).collect(Collectors.toList());

		return out;

	}

}
