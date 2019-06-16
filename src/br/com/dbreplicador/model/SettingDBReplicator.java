package br.com.dbreplicador.model;

public class SettingDBReplicator extends AbstractModel{

	private String user, password;

	public SettingDBReplicator() {
	}
	
	public SettingDBReplicator(String user, String password) {
		this.user = user;
		this.password = password;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
