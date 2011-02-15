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
package org.opensixen.acct.form;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.compiere.grid.ed.VDate;
import org.compiere.grid.ed.VLookup;
import org.compiere.grid.ed.VNumber;
import org.compiere.minigrid.ColumnInfo;
import org.compiere.minigrid.IDColumn;
import org.compiere.minigrid.MiniTable;
import org.compiere.model.MElementValue;
import org.compiere.model.MFactAcct;
import org.compiere.model.X_C_ValidCombination;
import org.compiere.model.X_Fact_Acct;
import org.compiere.print.MPrintFont;
import org.compiere.swing.CButton;
import org.compiere.swing.CLabel;
import org.compiere.swing.CPanel;
import org.compiere.swing.CTextField;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.opensixen.acct.grid.AccountString;
import org.opensixen.acct.grid.TableAccount;
import org.opensixen.acct.process.CreateJournal;
import org.opensixen.acct.utils.AcctEditorSwingUtils;

/**
 * 
 * AcctEditorJournal 
 *
 * @author Alejandro González
 * Nexis Servicios Informáticos http://www.nexis.es
 */

public class AcctEditorJournal extends JPanel implements PropertyChangeListener,ActionListener{
	
	private static TableAccount journaltab;
	private CPanel paneltotals;
	private CPanel panelbuttons;
	private CPanel p2;
	
	private CLabel lAmtDr;
	private VNumber fAmtDr;
	private CLabel lAmtCr;
	private VNumber fAmtCr;
	private CLabel lDifference;
	private static VNumber fDifference;
	
	private int RowHeight=40;
	private CButton newjournal;
	private CButton savejournal;
	private CButton saveasdefault;
	
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
		
		panelbuttons= new CPanel();
		panelbuttons.setLayout(new GridBagLayout());
		
		p2= new CPanel();
		p2.setLayout(new BorderLayout());
		
		newjournal = new CButton(Msg.translate(Env.getCtx(), "New Journal"));
		savejournal = new CButton(Msg.translate(Env.getCtx(), "Save Journal"));
		saveasdefault = new CButton(Msg.translate(Env.getCtx(), "Save As Default"));
		savejournal.setBorder( BorderFactory.createEtchedBorder());
		
		newjournal.addActionListener(this);
		savejournal.addActionListener(this);
		saveasdefault.addActionListener(this);
		
		fillPicks();
		
		paneltotals.add( lAmtDr,new GridBagConstraints( 0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets( 2,2,2,2 ),0,0 ));
		paneltotals.add( fAmtDr,new GridBagConstraints( 1,0,1,1,0.3,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets( 2,2,2,20 ),0,0 ));
		paneltotals.add( lAmtCr,new GridBagConstraints( 2,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets( 2,2,2,2 ),0,0 ));
		paneltotals.add( fAmtCr,new GridBagConstraints( 3,0,1,1,0.3,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets( 2,2,2,20 ),0,0 ));
		paneltotals.add( lDifference,new GridBagConstraints( 4,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets( 2,2,2,2 ),0,0 ));
		paneltotals.add( fDifference,new GridBagConstraints( 5,0,1,1,0.3,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets( 2,2,2,20 ),0,0 ));
		
		panelbuttons.add( saveasdefault,new GridBagConstraints( 0,0,1,2,0.0,0.5,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets( 5,10,2,10 ),5,20 ));
		panelbuttons.add( newjournal,new GridBagConstraints( 1,0,1,2,0.0,0.5,GridBagConstraints.CENTER,GridBagConstraints.BOTH,new Insets( 5,10,2,10 ),10,25 ));
		panelbuttons.add( savejournal,new GridBagConstraints( 2,0,1,2,0.0,0.5,GridBagConstraints.EAST,GridBagConstraints.BOTH,new Insets( 5,10,2,2 ),10,25 ));

		p2.add(paneltotals,BorderLayout.NORTH);
		p2.add(panelbuttons,BorderLayout.CENTER);
		
		journaltab = new TableAccount();
		this.add(new JScrollPane(journaltab),BorderLayout.CENTER);
		this.add(p2,BorderLayout.SOUTH);
		preparetable();
		journaltab.addPropertyChangeListener(this);
		
	}
	
	/**
	 * Setea los valores mínimos y máximos para las columnas
	 */
	
	public void setColumnWith(){        
		//Columna Valor de Cuenta ancho fijo
	    TableColumn columnAcct = journaltab.getColumnModel().getColumn(TableAccount.COLUMN_Value) ;
	    columnAcct.setPreferredWidth(120); 
	    columnAcct.setMinWidth(120);
	    
	    //Columna Descripción de cuenta
	    columnAcct = journaltab.getColumnModel().getColumn(TableAccount.COLUMN_Name) ;
	    columnAcct.setPreferredWidth(200); 
	    columnAcct.setMinWidth(200);
	    
	    //Columna Numero Asiento
	    columnAcct = journaltab.getColumnModel().getColumn(TableAccount.COLUMN_JournalNo) ;
	    columnAcct.setPreferredWidth(80); 
	    columnAcct.setMaxWidth(80);
	    
	    //Columna Fecha
	    columnAcct = journaltab.getColumnModel().getColumn(TableAccount.COLUMN_DateAcct) ;
	    columnAcct.setPreferredWidth(80); 
	    columnAcct.setMaxWidth(80);
	   
	}  
	
	private void fillPicks(){
		lAmtDr= new CLabel();
		fAmtDr = new VNumber();
		fAmtDr.setReadWrite(false);
		lAmtDr.setText(Msg.translate(Env.getCtx(), "AmtAcctDr"));
		lAmtDr.setLabelFor(fAmtDr);
		
		lAmtCr= new CLabel();
		fAmtCr = new VNumber();
		fAmtCr.setReadWrite(false);
		lAmtCr.setText(Msg.translate(Env.getCtx(), "AmtAcctCr"));
		lAmtCr.setLabelFor(fAmtCr);
		
		lDifference= new CLabel();
		fDifference = new VNumber();
		fDifference.setReadWrite(false);
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
		//journaltab.autoSize();
		journaltab.setAutoResizeMode(TableAccount.AUTO_RESIZE_ALL_COLUMNS);
		setColumnWith();
	}
	
	protected static TableAccount getJournalTable(){
		return journaltab;
	}


	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		//Cada vez que ocurran cambios en el panel recalculamos la suma debe, haber y diferencia 

		//Suma Debe
		fAmtDr.setValue(journaltab.getSumDr());
		//Suma Haber
		fAmtCr.setValue(journaltab.getSumCr());
		//Diferencia
		fDifference.setValue(((BigDecimal)fAmtDr.getValue()).subtract((BigDecimal)fAmtCr.getValue()));
		
		if(((BigDecimal)fDifference.getValue()).equals(BigDecimal.ZERO)){
			fDifference.setForeground(Color.BLACK);
			fDifference=(VNumber) AcctEditorSwingUtils.setBorder(fDifference, true,Color.BLACK);
		}else{
			fDifference.setForeground(Color.RED);
			fDifference=(VNumber)AcctEditorSwingUtils.setBorder(fDifference, true);
		}
	}
	
	public BigDecimal getDifference(){
		return (BigDecimal)fDifference.getValue();
	}

	/**
	 * Checkea los valores necesarios para guardar un asiento
	 * @return
	 */
	
	private boolean checkvalues(){
		//Verificamos los valores del panel defaults
		if(!checkDefaults())
			return false;
		//Verificamos que el asiento cuadre
		if(!checkDifference())
			return false;
		//Verificamos que todas las filas estén correctamente rellenadas
		if(!checkRows())
			return false;
		//Verificamos que el apunte no sea 0
		if(!checkSums()){
			System.out.println("La suma de debe y la de haber da 0");
			fAmtDr=(VNumber) AcctEditorSwingUtils.setBorder(fAmtDr, true);
			fAmtCr=(VNumber) AcctEditorSwingUtils.setBorder(fAmtCr, true);
			return false;
		}
		return true;
	}
	
	/**
	 * Checkea que la suma del debe y el haber no sean 0 ambas
	 * @return
	 */
	
	private boolean checkSums() {
		//Reseteamos los valores
		fAmtDr=(VNumber) AcctEditorSwingUtils.removeBorder(fAmtDr);
		fAmtCr=(VNumber) AcctEditorSwingUtils.removeBorder(fAmtCr);
		System.out.println("Suma de debe="+(BigDecimal)fAmtDr.getValue());
		System.out.println("Suma de haber="+(BigDecimal)fAmtCr.getValue());
		if( ((BigDecimal)fAmtDr.getValue()).compareTo(BigDecimal.ZERO)==0 )
			if(((BigDecimal)fAmtCr.getValue()).compareTo(BigDecimal.ZERO)==0)
				return false;
		//return !( ((BigDecimal)fAmtDr.getValue()).compareTo(BigDecimal.ZERO)==0 ) && ( ((BigDecimal)fAmtCr.getValue()).compareTo(BigDecimal.ZERO)==0 );
		return true;
	}


	/**
	 * Checkea que todas las filas con valores tengan una cuenta asignada
	 * @return
	 */

	private boolean checkRows() {
		
		for(int row=0;row<journaltab.getRowCount();row++){
			//Para cada celda reseteamos el borde de error antes de la recomprobación
			journaltab.RemoveColorCell(journaltab.getColumn(TableAccount.COLUMN_Value),row);
			if( (journaltab.getValueAt(row, TableAccount.COLUMN_AmtAcctDr)!=null) || 
				(journaltab.getValueAt(row, TableAccount.COLUMN_AmtAcctCr)!=null) ||
				(journaltab.getValueAt(row, TableAccount.COLUMN_Value)!=null) ||
				(journaltab.getValueAt(row, TableAccount.COLUMN_Description)!=null)){
				
				if(journaltab.getValueAt(row, TableAccount.COLUMN_ValidCombination)==null){
					//Component c=journaltab.getCellRenderer(row, TableAccount.COLUMN_Value).getTableCellRendererComponent(journaltab, null, false, false, row, TableAccount.COLUMN_Value);
					journaltab.ColorCell(journaltab.getColumn(TableAccount.COLUMN_Value),row);

					return false;	
				}
			}
		}
		return true;
	}


	/**
	 * Checkea si el asiento está cuadrado
	 * @return
	 */
	
	private boolean checkDifference() {

		return ((BigDecimal)fDifference.getValue()).compareTo(BigDecimal.ZERO)==0;
	}


	/**
	 * Checkea valores por defecto
	 * @return
	 */
	private boolean checkDefaults() {
		if(AcctEditorDefaults.getAcctSchema()==null){
			AcctEditorSwingUtils.setBorder(AcctEditorDefaults.vAcctSchema,true);
			return false;
		}if(AcctEditorDefaults.getGLCategory()==null){
			AcctEditorSwingUtils.setBorder(AcctEditorDefaults.vglCategory,true);
			return false;
		}if(AcctEditorDefaults.getOrg()==null){
			AcctEditorSwingUtils.setBorder(AcctEditorDefaults.vOrg,true);
			return false;
		}if(AcctEditorDefaults.getCurrency()==null){
			AcctEditorSwingUtils.setBorder(AcctEditorDefaults.vCurrency,true);
			return false;
		}
		//En otro caso reseteamos los border
		AcctEditorSwingUtils.setBorder(AcctEditorDefaults.vAcctSchema,false);
		AcctEditorSwingUtils.setBorder(AcctEditorDefaults.vglCategory,false);
		AcctEditorSwingUtils.setBorder(AcctEditorDefaults.vOrg,false);
		AcctEditorSwingUtils.setBorder(AcctEditorDefaults.vCurrency,false);
		
		return true;
	}


	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource().equals(newjournal)){
			//Nuevo asiento
			//Reseteamos el panel y ponemos el número inicial de filas
			preparetable();
			journaltab.setRowCount(10);
			AcctEditorDefaults.setDateOnJournal();
		}else if(arg0.getSource().equals(savejournal)){
			//Checkeamos valores
			if(!checkvalues())
				return;
			//Guardar asiento actual
			new CreateJournal(journaltab); 
		}else if(arg0.getSource().equals(saveasdefault)){
			//Guardar como asiento predefinido
		}
		
	}
}
