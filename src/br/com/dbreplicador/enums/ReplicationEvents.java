package br.com.dbreplicador.enums;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum ReplicationEvents {
	PREPARING("PREPARING", "Preparando"),
	READY("READY", "Pronto"),
	EXECUTING("EXECUTING", "Executando"),
	PAUSED("PAUSED", "Pausado"),
	RESUMED("RESUMED", "Retomado"),
	STOPPED("STOPPED", "Parado"),
	FINISHED("FINISHED", "Finalizado"),
	
	ON_PROCESS("ON_PROCESS", "On process"), //Disparado a cada intervalo de processamento da fila
	ON_ERROR("ON_ERROR", "On error"); //Disparado a cada error durante o processamento da fila

	private final String code;
	private final String description;
	private static final Map<String, String> mMap = Collections.unmodifiableMap(initializeMapping());

	private ReplicationEvents(String code, String description) {
		this.code = code;
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}

	public static String getDescriptionByCode(String code) {
		if (mMap == null) {
			initializeMapping();
		}

		if (mMap.containsKey(code)) {
			return mMap.get(code);
		}
		return null;
	}

	public static Map<String, String> getDatabases() {
		if (mMap == null) {
			initializeMapping();
		}

		return mMap;
	}

	private static Map<String, String> initializeMapping() {
		Map<String, String> mMap = new HashMap<String, String>();
		for (ReplicationEvents s : ReplicationEvents.values()) {
			mMap.put(s.code, s.description);
		}
		return mMap;
	}
}
