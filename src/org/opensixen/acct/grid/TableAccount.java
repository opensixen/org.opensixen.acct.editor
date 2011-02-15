 /******* BEGIN LICENSE BLOCK *****
 * Versión: GPL 2.0/CDDL 1.0/EPL 1.0
 *
 * Los contenidos de este fichero están sujetos a la Licencia
 * Pública General de GNU versión 2.0 (la "Licencia"); no podrá
 * usar este fichero, excepto bajo las condiciones que otorga dicha 
 * Licencia y siempre de acuerdo con el contenido de la presente. 
 * Una copia completa de las condiciones de de dicha licencia,
 * traducida en castellano, deberá estar incluida con el presente
 * programa.
 * 
 * Adicionalmente, puede obtener una copia de la licencia en
 * http://www.gnu.org/licenses/gpl-2.0.html
 *
 * Este fichero es parte del programa opensiXen.
 *
 * OpensiXen es software libre: se puede usar, redistribuir, o
 * modificar; pero siempre bajo los términos de la Licencia 
 * Pública General de GNU, tal y como es publicada por la Free 
 * Software Foundation en su versión 2.0, o a su elección, en 
 * cualquier versión posterior.
 *
 * Este programa se distribuye con la esperanza de que sea útil,
 * pero SIN GARANTÍA ALGUNA; ni siquiera la garantía implícita 
 * MERCANTIL o de APTITUD PARA UN PROPÓSITO DETERMINADO. Consulte 
 * los detalles de la Licencia Pública General GNU para obtener una
 * información más detallada. 
 *
 * TODO EL CÓDIGO PUBLICADO JUNTO CON ESTE FICHERO FORMA PARTE DEL 
 * PROYECTO OPENSIXEN, PUDIENDO O NO ESTAR GOBERNADO POR ESTE MISMO
 * TIPO DE LICENCIA O UNA VARIANTE DE LA MISMA.
 *
 * El desarrollador/es inicial/es del código es
 *  FUNDESLE (Fundación para el desarrollo del Software Libre Empresarial).
 *  Nexis Servicios Informáticos S.L. - http://www.nexis.es
 *
 * Contribuyente(s):
 *  Alejandro González <alejandro@opensixen.org> 
 *
 * Alternativamente, y a elección del usuario, los contenidos de este
 * fichero podrán ser usados bajo los términos de la Licencia Común del
 * Desarrollo y la Distribución (CDDL) versión 1.0 o posterior; o bajo
 * los términos de la Licencia Pública Eclipse (EPL) versión 1.0. Una 
 * copia completa de las condiciones de dichas licencias, traducida en 
 * castellano, deberán de estar incluidas con el presente programa.
 * Adicionalmente, es posible obtener una copia original de dichas 
 * licencias en su versión original en
 *  http://www.opensource.org/licenses/cddl1.php  y en  
 *  http://www.opensource.org/licenses/eclipse-1.0.php
 *
 * Si el usuario desea el uso de SU versión modificada de este fichero 
 * sólo bajo los términos de una o más de las licencias, y no bajo los 
 * de las otra/s, puede indicar su decisión borrando las menciones a la/s
 * licencia/s sobrantes o no utilizadas por SU versión modificada.
 *
 * Si la presente licencia triple se mantiene íntegra, cualquier usuario 
 * puede utilizar este fichero bajo cualquiera de las tres licencias que 
 * lo gobiernan,  GPL 2.0/CDDL 1.0/EPL 1.0.
 *
 * ***** END LICENSE BLOCK ***** */
package org.opensixen.acct.grid;

import java.awt.Color;
import java.awt.Component;
import java.math.BigDecimal;
import java.sql.Timestamp;
import javax.swing.JComponent;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import org.compiere.grid.ed.VHeaderRenderer;
import org.compiere.minigrid.ColumnInfo;
import org.compiere.minigrid.IMiniTable;
import org.compiere.minigrid.MiniTable;
import org.compiere.util.DisplayType;
import org.opensixen.acct.utils.AcctEditorSwingUtils;

/**
 * 
 * TableAccount 
 *
 * @author Alejandro González
 * Nexis Servicios Informáticos http://www.nexis.es
 */

public class TableAccount extends MiniTable implements IMiniTable {


	private static final long serialVersionUID = 1L;
	
	public static int COLUMN_JournalNo=0;   
	public static int COLUMN_DateAcct=1;
	public static int COLUMN_Value=2;   
	public static int COLUMN_Name=3;   
	public static int COLUMN_Description=4;
	public static int COLUMN_AmtAcctDr=5;   
	public static int COLUMN_AmtAcctCr=6;   
	public static int COLUMN_ValidCombination=7;
	
	private int RowSelect=-1;
	private int ColumnSelect=-1;
	
	protected static Color ColorError =Color.RED;
	
	public void setColumnClass (int index, Class c, boolean readOnly, String header)
	{
		super.setColumnClass(index,c,readOnly,header);

		TableColumn tc = getColumnModel().getColumn(index);
		if (tc == null)
			return;

		setColumnReadOnly(index, readOnly);
		if (c == AccountString.class)
		{
			tc.setPreferredWidth(50);
			tc.setCellRenderer(new AccountCellRenderer(DisplayType.String));
			tc.setCellEditor(new AccountCellEditor(AccountString.class));			
			tc.setHeaderRenderer(new VHeaderRenderer(DisplayType.String));
		}else if (c == Timestamp.class)
		{
			tc.setCellRenderer(new AccountCellRenderer(DisplayType.Date));
		}else if (c == BigDecimal.class)
		{
			tc.setCellRenderer(new AccountCellRenderer(DisplayType.Amount));
		}else if (c == Double.class)
		{
			tc.setCellRenderer(new AccountCellRenderer(DisplayType.Number));
		}else if (c == Integer.class)
		{
			tc.setCellRenderer(new AccountCellRenderer(DisplayType.Integer));
		}else{
			tc.setCellRenderer(new AccountCellRenderer(DisplayType.String));
		}
		
		if(index==COLUMN_ValidCombination){
			//No mostramos la columna
			tc.setPreferredWidth(0);
			tc.setMaxWidth(0);
			tc.setMinWidth(0);
			tc.setResizable(false);
		}
	}   //  setColumnClass
	
	/**
	 * Devuelve la clase AccountString relacionada con la fila
	 * @param row
	 * @return
	 */
	
	public AccountString getValueAccount(int row){
		ColumnInfo[] info=this.getLayoutInfo();
		AccountString acts=null;
		
		this.getValueAt(row, TableAccount.COLUMN_Value);
		//Nos aseguramos que se la clase correcta
		if(info[TableAccount.COLUMN_Value].getColClass().equals(AccountString.class)){
			AccountCellEditor editor =(AccountCellEditor) this.getColumn(TableAccount.COLUMN_Value).getCellEditor();
			acts =(AccountString)editor.getEditorAt(this, row, TableAccount.COLUMN_Value);
		}
		return acts;
	}
	
	public BigDecimal getAmtAcctDr(int row){
		
		return getValueAt(row, TableAccount.COLUMN_AmtAcctDr)==null?BigDecimal.ZERO:(BigDecimal)getValueAt(row, TableAccount.COLUMN_AmtAcctDr);
	}
	
	public BigDecimal getAmtAcctCr(int row){
		return getValueAt(row, TableAccount.COLUMN_AmtAcctCr)==null?BigDecimal.ZERO:(BigDecimal)getValueAt(row, TableAccount.COLUMN_AmtAcctCr);
	}
	
	public BigDecimal getSumDr(){
		BigDecimal amtdr=BigDecimal.ZERO;
		for(int i=0;i<this.getRowCount();i++){
			amtdr=amtdr.add(getAmtAcctDr(i));
		}
		return amtdr;
	}
	
	public BigDecimal getSumCr(){
		BigDecimal amtcr=BigDecimal.ZERO;
		for(int i=0;i<this.getRowCount();i++){
			amtcr=amtcr.add(getAmtAcctCr(i));
		}
		return amtcr;
	}
	
	/**
	 * Actualiza el borde en caso de error
	 * @param column
	 * @param row
	 */
	
	public void ColorCell(TableColumn column, int row) 
	{

		this.RowSelect=row;
		this.ColumnSelect=column.getModelIndex();
		AccountCellRenderer renderer = new AccountCellRenderer(DisplayType.String);
		column.setCellRenderer(renderer);
		this.repaint();
		//
	}
	
	/**
	 * Elimina el borde en caso de error
	 * @param column
	 * @param row
	 */
	
	public void RemoveColorCell(TableColumn column, int row) 
	{

		this.RowSelect=-1;
		this.ColumnSelect=-1;
		AccountCellRenderer renderer = new AccountCellRenderer(DisplayType.String);
		column.setCellRenderer(renderer);
		this.repaint();
		//
	}
	
	public Component prepareRenderer(TableCellRenderer renderer, int rowIndex,
			int vColIndex) {
		Color blue = new Color(163, 182, 218);
		Component c = super.prepareRenderer(renderer, rowIndex, vColIndex);
		if (c==null) return c;

		if (!this.isCellEditable(rowIndex, vColIndex) || isCellSelected(rowIndex, vColIndex) )
			return c; 
		if (rowIndex % 2 == 0) { 
			c.setBackground(blue);
		} else {
			// If not shaded, match the table's background
			c.setBackground(getBackground());
		}
		if((rowIndex==RowSelect && vColIndex==ColumnSelect)){
			//c.setBackground(ColorError);
			if(c instanceof JComponent){
				JComponent s =(JComponent) ((AccountCellRenderer)c).getTableCellRendererComponent(this, getValueAt(rowIndex, vColIndex), false, false, rowIndex, vColIndex);
				c =AcctEditorSwingUtils.setBorder(s, true);
			}
				
		}
		return c; 
	}
	
	public int getRowSelect(){
		return RowSelect;
	}
	
	public int getColumnSelect(){
		return ColumnSelect;
	}
	
}
