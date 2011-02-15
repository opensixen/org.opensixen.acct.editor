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
package org.opensixen.acct.swing;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import org.compiere.apps.ConfirmPanel;
import org.compiere.grid.ed.VLookup;
import org.compiere.minigrid.ColumnInfo;
import org.compiere.minigrid.IDColumn;
import org.compiere.minigrid.MiniTable;
import org.compiere.model.MDocType;
import org.compiere.model.PO;
import org.compiere.swing.CLabel;
import org.compiere.swing.CPanel;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

/**
 * 
 * AcctEditorChoose 
 *
 * @author Alejandro González
 * Nexis Servicios Informáticos http://www.nexis.es
 */

public class AcctEditorChoose extends org.compiere.swing.CDialog implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static int Choose_DocType=1;
	
	private CPanel p1 = null;
	
	private CLabel l1 = null;
	private VLookup v1 = null;
	private MiniTable t1 = null;
	private ConfirmPanel cp1 = null;
	
	private int Choose =0;
	private String m_sqlChoose="";
	protected CLogger log = CLogger.getCLogger(getClass());
	
	public AcctEditorChoose(){
		this(Choose_DocType);
	}
	
	public AcctEditorChoose(int ChooseType){
		//initComponents();
	}
	


	public AcctEditorChoose(int ChooseType, PO[] docs) {
		// TODO Auto-generated constructor stub
		Choose=ChooseType;
		if(ChooseType==Choose_DocType)
			initComponents(docs);
		this.pack();
	}

	private void initComponents(PO[] docs) {
		setLayout(new BorderLayout());
		
		p1= new CPanel();
		p1.setLayout(new GridBagLayout());
		
		cp1 = new ConfirmPanel();
		cp1.addActionListener(this);
		
		l1 = new CLabel(Msg.translate(Env.getCtx(), "Choose a DocType"));
		setTitle(l1.getText());
		
		t1 = new MiniTable();
		
		add(new JScrollPane(t1),BorderLayout.CENTER);
		add(cp1,BorderLayout.SOUTH);
		
		preparetable(docs);
	}
	
	private void preparetable(PO[] docs) {
		
	
		String s_sqlFrom=MDocType.Table_Name;
		String s_sqlWhere=getWhere(docs);
		
		ColumnInfo[] s_layoutChoose = new ColumnInfo[]{
        		new ColumnInfo(Msg.translate(Env.getCtx(), MDocType.COLUMNNAME_C_DocType_ID), MDocType.COLUMNNAME_C_DocType_ID, IDColumn.class, true, true, null),
        		new ColumnInfo(Msg.translate(Env.getCtx(), MDocType.COLUMNNAME_Name), MDocType.COLUMNNAME_Name, String.class, true, true, null)};

		//Reseteamos el modelo de tabla

		m_sqlChoose = t1.prepareTable(s_layoutChoose, s_sqlFrom, s_sqlWhere, true, null);
		
		t1.setRowSelectionAllowed(true);
        t1.autoSize();
        executeQuery();
	}

	private String getWhere(PO[] docs) {
		String Where="1=1";
		if(Choose ==Choose_DocType){
			Where+=" AND "+MDocType.COLUMNNAME_C_DocType_ID+" in (";
			for(PO p : docs){
				MDocType doc = (MDocType)p;
				Where+=doc.getC_DocType_ID()+",";
			}
			if(docs.length>0)
				Where=Where.substring(0, Where.length()-1);
			Where+=")";
		}
		return Where;
	}
	
	/**
	 * Ejecuta sentencia
	 */

	public void executeQuery(){

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(m_sqlChoose, null);
			rs = pstmt.executeQuery();
			t1.loadTable(rs);

		}
		catch (Exception e)
		{
			log.log(Level.WARNING, "Exception=", e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;

		}

	}
	
	public void actionPerformed(ActionEvent arg0) {

		if(arg0.getActionCommand().equals(ConfirmPanel.A_OK)){
			if(!checkOK()){
				JOptionPane.showMessageDialog(null, "", "", JOptionPane.ERROR_MESSAGE);
				return;
			}
		}

	}

	private boolean checkOK() {
		int rows=t1.getRowCount();
		for(int i=0;i<rows;i++){
			IDColumn col = (IDColumn)t1.getValueAt(i, 0);
			if(col.isSelected())
				return true;
		}
		
		return true;
	}
}
