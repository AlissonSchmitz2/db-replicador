package br.com.dbreplicador.pojos;

public class Database {
	private String code;
	private String description;

	public Database(String code, String description) {
		setCode(code);
		setDescription(description);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return this.description;
	}
}
