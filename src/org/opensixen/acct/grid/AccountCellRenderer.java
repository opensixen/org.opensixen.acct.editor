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

import javax.swing.JComponent;
import javax.swing.JTable;

import org.adempiere.plaf.AdempierePLAF;
import org.compiere.grid.ed.VCellRenderer;
import org.compiere.model.GridField;
import org.opensixen.acct.utils.AcctEditorSwingUtils;

/**
 * 
 * AccountCellRenderer 
 *
 * @author Alejandro González
 * Nexis Servicios Informáticos http://www.nexis.es
 */

public class AccountCellRenderer extends VCellRenderer{

	public AccountCellRenderer(GridField mField) {
		super(mField);
		// TODO Auto-generated constructor stub
	}


	public AccountCellRenderer(int string) {
		super(string);
	}
	


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	public Component getTableCellRendererComponent(JTable table,Object value,boolean isSelected,boolean hasFocus,int row,int column)
	{
		Component c = null;
			//	returns JLabel
		c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		c.setFont(table.getFont());
		

		//  Background & Foreground
		Color bg = AdempierePLAF.getFieldBackground_Mandatory();
		Color fg = AdempierePLAF.getTextColor_Normal();
		//  Inactive Background
		boolean ro = !table.isCellEditable(row, column); 
		if (ro)
		{
			bg = AdempierePLAF.getFieldBackground_Inactive();
			if (isSelected && !hasFocus)
				bg = bg.darker();
		}
		
		//  Foreground
		int cCode = 0;
		//  Grid
		if (table instanceof org.compiere.grid.VTable)
			cCode = ((org.compiere.grid.VTable)table).getColorCode (row);
		//  MiniGrid
		else if (table instanceof org.compiere.minigrid.MiniTable)
			cCode = ((org.compiere.minigrid.MiniTable)table).getColorCode (row);
		//
		if (cCode == 0)
			;											//	Black
		else if (cCode < 0)
			fg = AdempierePLAF.getTextColor_Issue();		//	Red
		else
			fg = Color.BLUE;		//	Blue
		
		//Comprobamos si la celda esta definida como error
		if(table instanceof TableAccount){
			TableAccount t= (TableAccount)table;
			if(row==t.getRowSelect() && column==t.getColumnSelect()){
				if(c instanceof JComponent)
					c =AcctEditorSwingUtils.setBorder((JComponent)c, true);
				return c;
			}
			
		}	
		if (isSelected)
		{
			//	Windows is white on blue
			bg = table.getGridColor();
			fg = table.getSelectionForeground();
			if (hasFocus)
				bg = org.adempiere.apps.graph.GraphUtil.brighter(bg, .9);
		}
		
		//  Set Color
		c.setBackground(bg);
		c.setForeground(fg);
		
		//  Format it
		setValue(value);
		return c;
	}

}
