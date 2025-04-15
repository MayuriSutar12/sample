package com.example.demo.ExternalDto;

import java.util.HashMap;
import java.util.Map;

public enum QuestionType {
	IMAGE(1), TEXT(2), NUMBER(3);

	private static Map<Integer, QuestionType> mappings = new HashMap<>();

	static {
		for (QuestionType type : QuestionType.values()) {
			mappings.put(type.getCode(), type);
		}
	}

	private int code;

	private QuestionType(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public static QuestionType getType(int code) {
		return mappings.get(code);
	}

}
