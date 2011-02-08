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
package org.opensixen.acct.process;

import java.awt.Dimension;

import java.math.BigDecimal;

import javax.swing.JOptionPane;

import org.compiere.acct.Doc;
import org.compiere.minigrid.ColumnInfo;
import org.compiere.model.I_C_ValidCombination;
import org.compiere.model.I_GL_JournalBatch;
import org.compiere.model.MAcctSchema;
import org.compiere.model.MDocType;
import org.compiere.model.MFactAcct;
import org.compiere.model.MJournal;
import org.compiere.model.MJournalBatch;
import org.compiere.model.MJournalLine;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.opensixen.acct.form.AcctEditorDefaults;
import org.opensixen.acct.form.AcctEditorFormPanel;
import org.opensixen.acct.grid.AccountCellEditor;
import org.opensixen.acct.grid.AccountString;
import org.opensixen.acct.grid.TableAccount;
import org.opensixen.acct.swing.AcctEditorChoose;
/**
 * 
 * CreateJournal 
 *
 * @author Alejandro González
 * Nexis Servicios Informáticos http://www.nexis.es
 */

public class CreateJournal {

	private TableAccount t=null;
	
	public CreateJournal(){
		
	}

	public CreateJournal(TableAccount journalTable) {
		t=journalTable;
		int rows = journalTable.getRowCount();
		
		for (int row = 0; row < rows; row++)
		{
			if(journalTable.getValueAt(row, TableAccount.COLUMN_ValidCombination)!=null)
				CreateJournalBatch((Integer)journalTable.getValueAt(row, TableAccount.COLUMN_ValidCombination),true);

		}
	}
	

	private void CreateJournalBatch(int C_ValidCombiation_ID,boolean newregister){
		//Creamos en primer lugar la cabecera del asiento manual
		MJournalBatch batch = null;
		if(newregister)
			batch = new MJournalBatch(Env.getCtx(),0,null);
		

		//Cogemos los valores por defecto del panel
		batch.setPostingType(MJournalBatch.POSTINGTYPE_Actual);
		batch.setGL_Category_ID((Integer)AcctEditorDefaults.getGLCategory());
		batch.setAD_Org_ID((Integer)AcctEditorDefaults.getOrg());
		batch.setC_Currency_ID(new MAcctSchema(Env.getCtx(),(Integer)AcctEditorDefaults.getAcctSchema(),null).getC_Currency_ID());
		batch.setDateAcct(AcctEditorDefaults.getDateAcct());
		batch.setDateDoc(AcctEditorDefaults.getDateAcct());
		batch.setC_DocType_ID(getDocType(MDocType.DOCBASETYPE_GLJournal));
		batch.setDescription(Msg.translate(Env.getCtx(), "Journal manually made"));
		if(batch.save())
			CreatetoJournal(batch);
		
	}
	
	private void CreatetoJournal(MJournalBatch batch) {
		
		MJournal journal = new MJournal(batch);
		journal.setC_AcctSchema_ID((Integer)AcctEditorDefaults.getAcctSchema());
		journal.setDescription(batch.getDescription());
		journal.setGL_Category_ID((Integer)AcctEditorDefaults.getGLCategory());
		
		if(journal.save()){
			CreateJournalLines(journal);
		}
		
	}

	private void CreateJournalLines(MJournal journal) {
		
		//Para cada linea creamos journalline
		for(int row=0;row<t.getRowCount();row++){
			//Comprobamos que la linea esté asociada
			if(t.getValueAt(row, TableAccount.COLUMN_ValidCombination) != null){
				MJournalLine jline = new MJournalLine(journal);
				//Totales
				jline.setAmtAcct((BigDecimal)t.getValueAt(row, TableAccount.COLUMN_AmtAcctDr), (BigDecimal)t.getValueAt(row, TableAccount.COLUMN_AmtAcctDr));
				//Cuenta Asociada
				jline.setC_ValidCombination_ID((Integer)t.getValueAt(row, TableAccount.COLUMN_ValidCombination));
				jline.save();
			}	
		}
		
	}

	private int getDocType(String DocBaseType){
		//Encontramos los documentos que coincidan con la base busscada
		MDocType[] docs=MDocType.getOfDocBaseType(Env.getCtx(), MDocType.DOCBASETYPE_GLJournal);
		if(docs.length==0){
			JOptionPane.showMessageDialog(null, Msg.translate(Env.getCtx(), "No DocType defined for DocBaseType Journal"), Msg.translate(Env.getCtx(), "No DocType"), JOptionPane.ERROR_MESSAGE);
			return 0;
		}
		if(docs.length>1){
			//Recuperamos el primer documento que sea predefinido
			for(MDocType d : docs){
				if(d.isDefault())
					return d.getC_DocType_ID();
			}
			//Mostrar Dialog con los tipos de documento posibles para la operación
			/*AcctEditorChoose choose = new AcctEditorChoose(AcctEditorChoose.Choose_DocType,docs);
			choose.setPreferredSize(new Dimension(100,150));
			choose.setVisible(true);*/
		}
		//Si llegamos aqui retornamo el primer registro	
		return docs[0].getC_DocType_ID();
	}
	
}
