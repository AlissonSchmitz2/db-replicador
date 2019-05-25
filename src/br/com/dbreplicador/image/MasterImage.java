package br.com.dbreplicador.image;

import javax.swing.ImageIcon;

public final class MasterImage extends ImageIcon{
	private static final long serialVersionUID = -1243931450819789591L;

	//13x13
	public static final MasterImage
	ok_13x13 = LoadImage("13x13/ok.png");
	
	//16x16
	public static final MasterImage
	search_16x16 = LoadImage("16x16/localizar.png");
	
	public static final MasterImage
	user_16x16 = LoadImage("16x16/user.png");
	
	public static final MasterImage
	financial_16x16 = LoadImage("16x16/financeiro.png");
	
	public static final MasterImage
	report_16x16 = LoadImage("16x16/relatorio.png");
	
	public static final MasterImage
	new_16x16 = LoadImage("16x16/novo.png");
	
	public static final MasterImage
	student_16x16 = LoadImage("16x16/estudante.png");
	
	public static final MasterImage
	aplication_16x16 = LoadImage("16x16/aplicacao.png");
	
	public static final MasterImage
	yingyang_16x16 = LoadImage("16x16/yingyang.png");
	
	public static final MasterImage
	control_16x16 = LoadImage("16x16/control.png");
	
	public static final MasterImage
	backup_restore_16x16 = LoadImage("16x16/stock-refresh.png");
	
	public static final MasterImage
	unlock_16x16 = LoadImage("16x16/liberar.png");
	
	public static final MasterImage
	new_alternative_16x16 = LoadImage("16x16/novo_alternativo.png");
	
	public static final MasterImage
	information_16x16 = LoadImage("16x16/informacao.png");
	
	public static final MasterImage
	exit_16x16 = LoadImage("16x16/sair.png");
	
	public static final MasterImage
	setting_16x16 = LoadImage("16x16/configurar.png");
	
	//22x22
	public static final MasterImage
	search_22x22 = LoadImage("22x22/localizar.png");
	
	public static final MasterImage	
	add_22x22 = LoadImage("22x22/adicionar.png");
	
	public static final MasterImage
	remove_22x22 = LoadImage("22x22/remover.png");
	
	public static final MasterImage
	save_22x22 = LoadImage("22x22/salvar.png");
	
	//32x32
	public static final MasterImage
	information_32x32 = LoadImage("32x32/informacao.png");
	
	private MasterImage(final String titulo) {
		super(MasterImage.class.getResource(titulo));
	}
	
	/**
	 * Carrega a imagem de acordo com o texto passado por
	 * parametro.
	 * @param tituloIcone
	 * @return ImageIcon
	 */
	
	public static MasterImage LoadImage(final String tituloIcone) {
		return new MasterImage(tituloIcone);
	}
}

