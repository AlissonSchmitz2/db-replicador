package br.com.dbreplicador.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import br.com.dbreplicador.model.SettingDBReplicator;

public class ManipulateFile {
	private File fileSetting;
	private static String SEPARATOR = ";;";
	
	private BufferedReader bufferedReader;
	
	private SettingDBReplicator settingDBReplicator;

	public ManipulateFile() {
		try {
			createDirFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void createDirFile() throws IOException {
		File dir = new File(System.getProperty("user.dir") + "\\settingDb");
		
		if (!dir.exists()) {
			dir.mkdir();
		}
		
		fileSetting = new File(System.getProperty("user.dir") + "\\settingDb\\setting.txt");
		
		if(!fileSetting.exists()) {
			fileSetting.createNewFile();
			//Setado valores por padrao ao criar
			inserirDadosNoArquivo("postgres", "postgres");
		}
	}

	public SettingDBReplicator RecoverData() {

		try {
			FileReader fileReader = new FileReader(fileSetting.getAbsolutePath());
			bufferedReader = new BufferedReader(fileReader);
			String line = bufferedReader.readLine();

			while (line != null) {

				String[] atributte = line.split(SEPARATOR);

				settingDBReplicator = createAtributtes(atributte);

				line = bufferedReader.readLine();
			}

			return settingDBReplicator;

		} catch (IOException e) {
			System.err.printf("Seguinte arquivo não está criado: %s.\n", e.getMessage());
		}

		return null;
	}
	
	public void inserirDadosNoArquivo(String user, String password) {

		try {
			FileWriter fileWriter = new FileWriter(fileSetting.getAbsolutePath());
			PrintWriter printWriter = new PrintWriter(fileWriter);

			removerDadosDoArquivo();
			printWriter.println(user + SEPARATOR + password);
			fileWriter.close();

		} catch (IOException e) {
			System.err.printf("Não foi possível gravar o arquivo: %s.\n", e.getMessage());
		}

	}

	private SettingDBReplicator createAtributtes(String[] atributte) {
		SettingDBReplicator settingDBReplicator = new SettingDBReplicator();

		settingDBReplicator.setUser(atributte[0]);
		settingDBReplicator.setPassword(atributte[1]);
		
		return settingDBReplicator;
	}


	private void removerDadosDoArquivo() {
		try {
			FileReader arq = new FileReader(fileSetting.getAbsolutePath());
			bufferedReader = new BufferedReader(arq);
			StringBuffer inputBuffer = new StringBuffer();

			String inputStr = inputBuffer.toString();

			bufferedReader.close();

			FileOutputStream fileOut = new FileOutputStream(fileSetting.getAbsolutePath());
			fileOut.write(inputStr.getBytes());
			fileOut.close();
		} catch (IOException e) {
			System.err.printf("Não foi possível remover dados do arquivo: %s.\n", e.getMessage());
		}
	}
}
