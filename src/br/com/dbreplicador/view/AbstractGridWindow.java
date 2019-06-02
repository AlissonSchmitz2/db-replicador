package br.com.dbreplicador.view;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JDesktopPane;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

public abstract class AbstractGridWindow extends AbstractWindowFrame {
	private static final long serialVersionUID = -8203026366064920547L;

	protected JScrollPane grid = null;
	protected TableCellRenderer renderer = new EvenOddRenderer();

	public AbstractGridWindow(String nameWindow, int width, int height, JDesktopPane desktop, boolean activeFrame) {
		super(nameWindow, width, height, desktop);

		if (activeFrame) {
//			 Abre a janela grid automaticamente
			desktop.add(this);
			showFrame();
		}
	}

	public void resizeGrid(JScrollPane grid, int x, int y, int width, int height) {
		grid.setBounds(x, y, width, height);
	}

	public JScrollPane getGridContent() {
		return grid;
	}

	class EvenOddRenderer implements TableCellRenderer {
		public DefaultTableCellRenderer DEFAULT_RENDERER = new DefaultTableCellRenderer();

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			Component renderer = DEFAULT_RENDERER.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
					column);

			((JLabel) renderer).setOpaque(true);
			Color background;
			if (isSelected) {
				background = new Color(65, 105, 225);
			} else {
				if (row % 2 == 0) {
					background = new Color(220, 220, 220);
				} else {
					background = Color.WHITE;
				}
			}

			renderer.setBackground(background);
			return renderer;
		}
	}
}
