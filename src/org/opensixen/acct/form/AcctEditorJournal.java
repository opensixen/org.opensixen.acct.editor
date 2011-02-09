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
 *  Indeos Consultoria S.L. - http://www.indeos.es
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
package org.opensixen.acct.form;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;

import org.compiere.grid.ed.VDate;
import org.compiere.grid.ed.VNumber;
import org.compiere.minigrid.ColumnInfo;
import org.compiere.minigrid.IDColumn;
import org.compiere.minigrid.MiniTable;
import org.compiere.model.MElementValue;
import org.compiere.model.MFactAcct;
import org.compiere.model.X_C_ValidCombination;
import org.compiere.model.X_Fact_Acct;
import org.compiere.swing.CLabel;
import org.compiere.swing.CPanel;
import org.compiere.swing.CTextField;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.opensixen.acct.grid.AccountString;
import org.opensixen.acct.grid.TableAccount;

/**
 * 
 * AcctEditorJournal 
 *
 * @author Alejandro González
 * Nexis Servicios Informáticos http://www.nexis.es
 */

public class AcctEditorJournal extends JPanel{
	
	private static TableAccount journaltab;
	private CPanel paneltotals;
	
	private CLabel lAmtDr;
	private VNumber fAmtDr;
	private CLabel lAmtCr;
	private VNumber fAmtCr;
	private CLabel lDifference;
	private VNumber fDifference;
	
	private int RowHeight=40;
	
	public AcctEditorJournal(){
		initComponents();
	}


	public AcctEditorJournal(AcctEditorFormPanel acctEditorFormPanel) {
		initComponents();
	}


	private void initComponents() {
		this.setLayout(new BorderLayout());
		
		paneltotals= new CPanel();
		paneltotals.setLayout(new GridBagLayout());
		
		fillPicks();
		
		paneltotals.add( lAmtDr,new GridBagConstraints( 0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets( 2,2,2,2 ),0,0 ));
		paneltotals.add( fAmtDr,new GridBagConstraints( 1,0,1,1,0.3,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets( 2,2,2,20 ),0,0 ));
		paneltotals.add( lAmtCr,new GridBagConstraints( 2,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets( 2,2,2,2 ),0,0 ));
		paneltotals.add( fAmtCr,new GridBagConstraints( 3,0,1,1,0.3,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets( 2,2,2,20 ),0,0 ));
		paneltotals.add( lDifference,new GridBagConstraints( 4,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets( 2,2,2,2 ),0,0 ));
		paneltotals.add( fDifference,new GridBagConstraints( 5,0,1,1,0.3,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets( 2,2,2,20 ),0,0 ));


		journaltab = new TableAccount();
		this.add(new JScrollPane(journaltab),BorderLayout.CENTER);
		this.add(paneltotals,BorderLayout.SOUTH);
		preparetable();
		
	}
	
	private void fillPicks(){
		lAmtDr= new CLabel();
		fAmtDr = new VNumber();
		lAmtDr.setText(Msg.translate(Env.getCtx(), "AmtAcctDr"));
		lAmtDr.setLabelFor(fAmtDr);
		
		lAmtCr= new CLabel();
		fAmtCr = new VNumber();
		lAmtCr.setText(Msg.translate(Env.getCtx(), "AmtAcctCr"));
		lAmtCr.setLabelFor(fAmtCr);
		
		lDifference= new CLabel();
		fDifference = new VNumber();
		lDifference.setText(Msg.translate(Env.getCtx(), "Difference"));
		lDifference.setLabelFor(fDifference);
	}


	protected void preparetable() {
		
		ColumnInfo[] s_layoutJournal = new ColumnInfo[]{
        		new ColumnInfo(Msg.translate(Env.getCtx(), MFactAcct.COLUMNNAME_JournalNo), MFactAcct.COLUMNNAME_JournalNo, String.class, true, true, null),
        		new ColumnInfo(Msg.translate(Env.getCtx(), MFactAcct.COLUMNNAME_DateTrx), MFactAcct.COLUMNNAME_DateAcct, Timestamp.class, true, true, null),
        		//Columna especial, crear clase nueva para ella con buscador de elementos de cuenta según vamos escribiendo
        		new ColumnInfo(Msg.translate(Env.getCtx(), MFactAcct.COLUMNNAME_Account_ID), MElementValue.COLUMNNAME_Value, AccountString.class, false, true, null),
        		new ColumnInfo(Msg.translate(Env.getCtx(), MElementValue.COLUMNNAME_Name), MElementValue.COLUMNNAME_Name, String.class, true, true, null),
        		new ColumnInfo(Msg.translate(Env.getCtx(), "Concept"), MFactAcct.COLUMNNAME_Description, String.class, false, true, null),
        		new ColumnInfo(Msg.translate(Env.getCtx(), MFactAcct.COLUMNNAME_AmtAcctDr), MFactAcct.COLUMNNAME_AmtAcctDr,BigDecimal.class, false, true, null),
        		new ColumnInfo(Msg.translate(Env.getCtx(), MFactAcct.COLUMNNAME_AmtAcctCr), MFactAcct.COLUMNNAME_AmtAcctCr,BigDecimal.class, false, true, null),
        		new ColumnInfo(Msg.translate(Env.getCtx(), X_C_ValidCombination.COLUMNNAME_C_ValidCombination_ID), X_C_ValidCombination.COLUMNNAME_C_ValidCombination_ID,Integer.class, false, true, null)};

		//Reseteamos el modelo de tabla
		journaltab.setModel(new DefaultTableModel());

		journaltab.setRowHeight(RowHeight);
		journaltab.prepareTable(s_layoutJournal, "", "", true, null);
		journaltab.autoSize();
	}
	
	protected static TableAccount getJournalTable(){
		return journaltab;
	}
}
