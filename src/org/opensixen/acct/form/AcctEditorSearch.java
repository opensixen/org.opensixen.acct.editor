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
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import org.compiere.acct.AcctViewer;
import org.compiere.grid.ed.VLookup;
import org.compiere.minigrid.ColumnInfo;
import org.compiere.minigrid.IDColumn;
import org.compiere.minigrid.MiniTable;
import org.compiere.model.I_C_ValidCombination;
import org.compiere.model.MBPartner;
import org.compiere.model.MElementValue;
import org.compiere.swing.CButton;
import org.compiere.swing.CMenuItem;
import org.compiere.swing.CPanel;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.opensixen.acct.grid.AccountCellEditor;
import org.opensixen.acct.grid.AccountString;
import org.opensixen.acct.grid.ElementsTable;
import org.opensixen.acct.grid.TableAccount;
import org.opensixen.acct.process.CreateJournal;
import org.opensixen.acct.utils.AcctEditorMouseAdapter;

/**
 * 
 * AcctEditorSearch 
 *
 * @author Alejandro González
 * Nexis Servicios Informáticos http://www.nexis.es
 */

public class AcctEditorSearch extends JPanel implements ActionListener{
	
	private static ElementsTable elementstab;
	private static String SearchParam=null;
	private static String sql_Where;
	private static String m_sql;
	
	private CButton newjournal;
	private CButton savejournal;
	private CButton saveasdefault;
	
	private CPanel totalPanel;
	private AcctEditorFormPanel principalPanel;
	
	JPopupMenu 					popupMenu = new JPopupMenu();
	private CMenuItem 			mZoomPartner;
	private CMenuItem 			mZoomAccounts;
	private CMenuItem			mZoomProduct;

	
	private MouseListener mouseListener = new AcctEditorMouseAdapter( this );
	
	public AcctEditorSearch(){
		initComponents();
	}


	public AcctEditorSearch(AcctEditorFormPanel acctEditorFormPanel) {
		principalPanel=acctEditorFormPanel;
		initComponents();

	}


	private void initComponents() {

		elementstab = new ElementsTable();
		
		this.setLayout(new BorderLayout());
		
		newjournal = new CButton(Msg.translate(Env.getCtx(), "New Journal"));
		savejournal = new CButton(Msg.translate(Env.getCtx(), "Save Journal"));
		saveasdefault = new CButton(Msg.translate(Env.getCtx(), "Save As Default"));
		
		newjournal.addActionListener(this);
		savejournal.addActionListener(this);
		saveasdefault.addActionListener(this);
		
		elementstab.addMouseListener( mouseListener );
		this.add(new JScrollPane(elementstab),BorderLayout.NORTH);
		preparetable();
		addMenuPopup();
	}

	public static void setParameter(Object parameter){
		SearchParam=parameter.toString();
		searchElements();
	}
	
	private static void searchElements(){
		//Establecemos las condiciones de búsqueda
		sql_Where="m.".concat(MElementValue.COLUMNNAME_Value).concat(" LIKE '").concat(SearchParam).concat("%'");
		sql_Where+=" AND ";
		sql_Where+="m.".concat(MElementValue.COLUMNNAME_IsSummary).concat(" LIKE 'N'");
		preparetable();
		executeQuery();
	}

	private static void preparetable() {
		
		String s_sqlFrom=I_C_ValidCombination.Table_Name.concat(" v ");
		s_sqlFrom+=" INNER JOIN ".concat(MElementValue.Table_Name).concat(" m ").concat(" ON ");
		s_sqlFrom+=" v.".concat(I_C_ValidCombination.COLUMNNAME_Account_ID).concat("=").concat("m.").concat(MElementValue.COLUMNNAME_C_ElementValue_ID);
		
		ColumnInfo[] s_layoutJournal = new ColumnInfo[]{
				new ColumnInfo(Msg.translate(Env.getCtx(), I_C_ValidCombination.COLUMNNAME_C_ValidCombination_ID), I_C_ValidCombination.COLUMNNAME_C_ValidCombination_ID, IDColumn.class, false, true, null),
         		new ColumnInfo(Msg.translate(Env.getCtx(), MElementValue.COLUMNNAME_Value), MElementValue.COLUMNNAME_Value, String.class, false, true, null),
        		new ColumnInfo(Msg.translate(Env.getCtx(), I_C_ValidCombination.COLUMNNAME_C_BPartner_ID),  "v."+I_C_ValidCombination.COLUMNNAME_C_BPartner_ID, MBPartner.class, false, true, null)};


		elementstab.setModel(new DefaultTableModel());
		elementstab.setRowHeight(20);
		m_sql =elementstab.prepareTable(s_layoutJournal, s_sqlFrom,sql_Where, false, null);

		elementstab.autoSize();
	}
	
	
	private static void executeQuery(){

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(m_sql, null);
			rs = pstmt.executeQuery();
			elementstab.loadTable(rs);

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;

		}

	}
	
	/**
	 * Setea los valores de la cuenta seleccionada
	 * @param row
	 */
	private void setSelectedValues(int row){
		TableAccount act =AcctEditorJournal.getJournalTable();
		int prow=act.getSelectedRow();
		IDColumn c =(IDColumn) elementstab.getValueAt(row, ElementsTable.COLUMN_ID);
		act.setValueAt(elementstab.getValueAt(row, ElementsTable.COLUMN_VALUE), prow, TableAccount.COLUMN_Value);
		act.setValueAt(elementstab.getValueAt(row, ElementsTable.COLUMN_DESCRIPTION), prow, TableAccount.COLUMN_Name);
		act.setValueAt(c.getRecord_ID(), prow, TableAccount.COLUMN_ValidCombination);

	}

	private void addMenuPopup() {
		
		mZoomPartner = new CMenuItem(Msg.translate(Env.getCtx(), "ZoomPartner"));
		mZoomAccounts = new CMenuItem(Msg.translate(Env.getCtx(), "ZoomAccount"));
		mZoomProduct = new CMenuItem(Msg.translate(Env.getCtx(), "ZoomProduct"));
		
		mZoomPartner.addActionListener(this);
		mZoomAccounts.addActionListener(this);
		mZoomProduct.addActionListener(this);
		
		popupMenu.add(mZoomPartner);
		popupMenu.add(mZoomAccounts);
		popupMenu.add(mZoomProduct);
		
		popupMenu.addMouseListener(mouseListener);
	}


	public void mouseClicked(MouseEvent e) {
		 if( e.getSource() instanceof MiniTable ) {
			 //Detectamos doble click
			 if (e.getClickCount() == 2) {
	            setSelectedValues(elementstab.getSelectedRow());
	            
	         }
	      }    
		  if (SwingUtilities.isRightMouseButton(e))
				popupMenu.show(this, e.getX(), e.getY());
		  else
				popupMenu.setVisible(false);
		
	}


	@Override
	public void actionPerformed(ActionEvent arg0) {
		 if(arg0.getSource().equals(mZoomPartner)){
			
		}else if(arg0.getSource().equals(mZoomProduct)){
			
		}else if(arg0.getSource().equals(mZoomAccounts)){
			//Visor de cuentas con los campos de búsqueda rellenos con los valores de la validcombination seleccionada
			//AccountViewer view = new AccountViewer(); //Este es el visor de cuentas de un registro en concreto
			//AcctViewer view = new AcctViewer(); //Este es el visor de cuentas mediante búsqueda de la aplicación
			AcctViewer view = new AcctViewer(1000008,318,1013450);
		}
		
	}
}
