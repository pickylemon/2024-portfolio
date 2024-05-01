package com.portfolio.www.util;

import java.util.HashMap;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class MapIntValueConverter implements Converter<HashMap<String,Object>, HashMap<String,Integer>>{

	@Override
	public HashMap<String, Integer> convert(HashMap<String, Object> source) {
		HashMap<String, Integer> map = new HashMap<>();
		for(String key : source.keySet()) {
			map.put(key, Integer.valueOf(source.get(key).toString()));
		}
		
		return map;
	}

}
