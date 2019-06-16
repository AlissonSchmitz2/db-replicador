package br.com.dbreplicador.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;

import br.com.dbreplicador.database.ConnectionFactory;
import br.com.dbreplicador.image.MasterImage;
import br.com.dbreplicador.model.SettingDBReplicator;
import br.com.dbreplicador.util.ManipulateFile;

public class Window extends JFrame {
	private static final long serialVersionUID = 3283754083146407662L;

	// Janelas do Menu
	private ConnectionFormWindow frameConnectionsForm;
	private ProcessFormWindow frameProcessForm;
	private TableFormWindow frameTableForm;
	private DirectionFormWindow frameDirectionForm;
	private ReplicationWindow frameReplicatorWindow;

	private JMenu menuSistema;
	private JMenu menuCadastros;
	private JMenu menuProcessos;
	private JMenu menuAjuda;

	private JSeparator separador;

	private JDesktopPane desktop;
	
	private JLabel wallpaper;

	private Connection connection;
	
	private ManipulateFile man = new ManipulateFile();
	
	public Window() {
		super();

		desktop = new JDesktopPane();
		desktop.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
		desktop.setVisible(true);
		setContentPane(desktop);

		startingWindow();

		setIconImage(MasterImage.replicator_16x16.getImage());


		wallpaper = new JLabel(MasterImage.Wallpaper);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		wallpaper.setBounds(0, 0, screenSize.width, screenSize.height - 100);
		getContentPane().add(wallpaper);

		// Full screen
		setExtendedState(Frame.MAXIMIZED_BOTH);
	}

	private String getDateTime() {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date date = new Date();
		return dateFormat.format(date);
	}

	private void startingWindow() {
		String dataLogin = getDateTime();
		this.setTitle(
				"Replicador Banco de dados v1.0.0-betha\t\t" + "Usuário Logado: " + " - Último Login: " + dataLogin);
		this.setJMenuBar(getWindowMenuBar());
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setBounds(new Rectangle(0, 0, 796, 713));
		this.setFocusableWindowState(true);
		getContentPane().setBackground(new Color(247, 247, 247));
	}

	/*
	 * MENU DE NAVEGAÇÃO
	 */
	private JMenuBar getWindowMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(getMenuSistema());
		menuBar.add(getMenuCadastros());
		menuBar.add(getMenuProcessos());
		menuBar.add(getMenuAjuda());

		// Remove o atalho "F10" padrão no JMenuBar (Vem padrão nos SO)
		InputMap iMap = menuBar.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		Object action = iMap.get(KeyStroke.getKeyStroke("F10"));
		ActionMap actionMap = menuBar.getActionMap();
		actionMap.getParent().remove(action);

		return menuBar;
	}

	// Menu Sistema
	private JMenu getMenuSistema() {
		separador = new JSeparator();
		menuSistema = new JMenu("Sistema");
		menuSistema.setFont(getDefaultFont());

		menuSistema.add(getMenuItemConfig());
		menuSistema.add(separador);
		menuSistema.add(getMenuItemSair());

		return menuSistema;
	}

	private JMenuItem getMenuItemConfig() {
		JMenuItem menuItem = new JMenuItem("Configurar", MasterImage.setting_16x16);
		menuItem.setFont(getDefaultFont());

		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO: Configurações
				
				SettingFormWindow config = new SettingFormWindow(desktop);
				abrirFrame(config);
				
			}
		});

		return menuItem;
	}

	private JMenuItem getMenuItemSair() {
		JMenuItem menuItem = new JMenuItem("Sair", MasterImage.exit_16x16);
		menuItem.setFont(getDefaultFont());

		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		return menuItem;
	}

	// Menu Cadastros
	private JMenu getMenuCadastros() {
		menuCadastros = new JMenu("Cadastros");
		menuCadastros.setFont(getDefaultFont());

		menuCadastros.add(getMenuItemConexao());
		menuCadastros.add(getMenuItemProcessos());
		menuCadastros.add(getMenuItemTabelas());
		menuCadastros.add(getMenuItemDirecao());

		return menuCadastros;
	}

	private JMenuItem getMenuItemConexao() {
		JMenuItem menuItem = new JMenuItem("Conex\u00E3o", MasterImage.aplication_16x16);
		menuItem.setFont(getDefaultFont());

		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frameConnectionsForm = new ConnectionFormWindow(desktop, getConnection());
				abrirFrame(frameConnectionsForm);
			}
		});

		return menuItem;
	}

	private JMenuItem getMenuItemProcessos() {
		JMenuItem menuItem = new JMenuItem("Processos", MasterImage.process_16x16);
		menuItem.setFont(getDefaultFont());

		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frameProcessForm = new ProcessFormWindow(desktop, getConnection());
				abrirFrame(frameProcessForm);
			}
		});

		return menuItem;
	}

	private JMenuItem getMenuItemTabelas() {
		JMenuItem menuItem = new JMenuItem("Tabelas", MasterImage.details_16x16);
		menuItem.setFont(getDefaultFont());

		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frameTableForm = new TableFormWindow(desktop, getConnection());
				abrirFrame(frameTableForm);
			}
		});

		return menuItem;
	}

	private JMenuItem getMenuItemDirecao() {
		JMenuItem menuItem = new JMenuItem("Dire\u00E7\u00E3o", MasterImage.direction_16x16);
		menuItem.setFont(getDefaultFont());

		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frameDirectionForm = new DirectionFormWindow(desktop, connection);
				abrirFrame(frameDirectionForm);
			}
		});

		return menuItem;
	}

	// Menu Processos
	private JMenu getMenuProcessos() {
		menuProcessos = new JMenu("Processos");
		menuProcessos.setFont(getDefaultFont());

		menuProcessos.add(getMenuReplicar());

		return menuProcessos;
	}

	private JMenuItem getMenuReplicar() {
		JMenuItem menuItem = new JMenuItem("Replicar", MasterImage.replicator_16x16);
		menuItem.setFont(getDefaultFont());

		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frameReplicatorWindow = new ReplicationWindow(desktop);
				abrirFrame(frameReplicatorWindow);
			}
		});

		return menuItem;
	}

	// MENU AJUDA
	private JMenu getMenuAjuda() {
		menuAjuda = new JMenu("Ajuda");
		menuAjuda.setFont(getDefaultFont());

		menuAjuda.add(getMenuItemSobre());

		return menuAjuda;
	}

	private JMenuItem getMenuItemSobre() {
		JMenuItem menuItem = new JMenuItem("Sobre", MasterImage.information_16x16);
		menuItem.setFont(getDefaultFont());

		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Desenvolvimento:\n\nAlisson Schmitz\n"
						+ "Edvaldo da Rosa\nGiovane Santiago\n" + "Wilian Hendler", "Informação",
						JOptionPane.INFORMATION_MESSAGE);
			}
		});

		return menuItem;
	}

	private void abrirFrame(AbstractWindowFrame frame) {
		boolean frameAlreadyExists = false;

		// Percorre todos os frames adicionados
		for (JInternalFrame addedFrame : desktop.getAllFrames()) {

			// Se o frame adiconado ja estiver
			if (addedFrame.getClass().toString().equalsIgnoreCase(frame.getClass().toString())) {
				// Remove janelas duplicadas
				addedFrame.moveToFront();
				frameAlreadyExists = true;
			}

		}

		try {
			if (!frameAlreadyExists) {
				desktop.add(frame);
				frame.moveToFront();
			}

			frame.setSelected(true);
			frame.setVisible(true);
		} catch (PropertyVetoException e) {
			JOptionPane.showMessageDialog(rootPane, "Houve um erro ao abrir a janela", "", JOptionPane.ERROR_MESSAGE,
					null);
		}
	}
	
	private Connection getConnection() {
			SettingDBReplicator settingDBReplicator = new SettingDBReplicator();

			settingDBReplicator = man.RecoverData();

			settingDBReplicator.getUser();
			settingDBReplicator.getPassword();
			
			connection = ConnectionFactory.getConnection(settingDBReplicator.getUser(), settingDBReplicator.getPassword());
			
			return connection;
	}
	
	private Font getDefaultFont() {
		return new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12);
	}
}