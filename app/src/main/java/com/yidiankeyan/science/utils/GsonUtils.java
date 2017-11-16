package com.yidiankeyan.science.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @类名称: GsonUtils
* @类描述: TODO
* @创建时间：2016-1-20 下午6:19:25
* @备注：
 */
public class GsonUtils {

	/**
	* @方法名称:getJsonElement
	* @描述: 获取JSON中一个属性对应文
	* @创建时间：2016-1-20 下午6:17:16
	* @备注：    
	* @param jsonStr
	* @param elementPath
	* @return  
	* @返回类型：String
	 */
	public static String getJsonElement(String jsonStr, String elementPath) {
		String elementJson = null;
		JsonElement topele = null;// gson.fromJson(jsonStr, JsonElement.class);
		topele = new JsonParser().parse(jsonStr);
		System.out.println("top json obj is:" + topele.toString());
		if (!topele.isJsonObject()) {
			throw new NullPointerException("非法JSON对象!");
		}
		if (topele.getAsJsonObject().has(elementPath))
			elementJson = topele.getAsJsonObject().get(elementPath).toString();
		else
			System.out.println("JSon 对象中没有属" + elementPath);
		return elementJson;
	}

	static Gson createGsonProccser() {
		Gson gson = null;
		GsonBuilder gb = new GsonBuilder();
		gb.disableInnerClassSerialization();
		gb.serializeNulls();
		gson = gb.create();
		return gson;
	}

	/**
	* @方法名称:parseJsonToArray
	* @描述: 解析JSON到数组
	* @创建时间：2016-1-20 下午6:16:35
	* @备注：    
	* @param jsonStr
	* @param elementType
	* @return  
	* @返回类型：List
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List json2List(String jsonStr, Class elementType) {
		JsonElement arrayElement = null;
		List objlist = new ArrayList();
		Object listElement = null;
		Gson gson = null;
		try {
			gson = createGsonProccser();
			arrayElement = new JsonParser().parse(jsonStr);
			if (arrayElement.isJsonArray()) {
				JsonArray jsonArray = arrayElement.getAsJsonArray();
				for (JsonElement jsonobj : jsonArray) {
					listElement = gson.fromJson(jsonobj, elementType);
					objlist.add(listElement);
				}
			}
		} catch (Exception e) {
			System.err.println("解析JSON到数组错");
			e.printStackTrace();
			// TODO: handle exception
		}
		return objlist;
	}

	/**
	* @方法名称:buildJsonContentFromBean
	* @描述: 从对象转换成JSON
	* @创建时间：2016-1-20 下午6:16:18
	* @备注：    
	* @param bean
	* @return  
	* @返回类型：String
	 */
	public static String obj2Json(Object bean) {
		Gson gson = null;
		String jsontext = null;
		try {
			gson = createGsonProccser();
			jsontext = gson.toJson(bean);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return jsontext;
	}

	/**
	* @方法名称:parseJsonToBean
	* @描述: 转换JSON到JAVA对象
	* @创建时间：2016-1-20 下午6:15:49
	* @备注：    
	* @param jsonStr
	* @param beanClass
	* @return  
	* @返回类型：Object
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Object json2Bean(String jsonStr, Class beanClass) {
		Object bean = null;
		Gson gson = null;
		try {
			gson = new GsonBuilder().setPrettyPrinting().create();
			bean = gson.fromJson(jsonStr, beanClass);
		} catch (Exception e) {
			// TODO: handle exception
			System.err.println("解析JSON对象失败!");
			e.printStackTrace();
		}
		return bean;
	}

	/**
	* @方法名称:parseJsonToMap
	* @描述: 转换JSON到map
	* @创建时间：2016-1-20 下午6:15:16
	* @备注：    
	* @param jsonStr
	* @return  
	* @返回类型：Object
	 */
	public static Object json2Map(String jsonStr) {
		Object bean = null;
		Gson gson = null;
		try {
			Map<String, String> map = new HashMap<String, String>();
			Type maptype = TypeToken.get(map.getClass()).getType();
			gson = new GsonBuilder().setPrettyPrinting().create();
			bean = gson.fromJson(jsonStr, maptype);
		} catch (Exception e) {
			// TODO: handle exception
			System.err.println("解析JSON对象失败!");
			e.printStackTrace();
		}
		return bean;
	}

	/**
	* @方法名称:buildJsonContentWithMap
	* @描述: 转换MAP到JSON
	* @创建时间：2016-1-20 下午6:17:40
	* @备注：    
	* @param params
	* @return  
	* @返回类型：String
	 */
	@SuppressWarnings("rawtypes")
	public static String map2Json(Map params) {
		String jsonstr = null;
		Gson gson = null;
		try {
			gson = createGsonProccser();
			jsonstr = gson.toJson(params);
		} catch (Exception e) {
			System.err.println("构建JSON对象错误!");
			e.printStackTrace();
		}
		return jsonstr;
	}

}
