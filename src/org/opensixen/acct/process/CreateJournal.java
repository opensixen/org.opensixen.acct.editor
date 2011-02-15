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
package org.opensixen.acct.process;


import java.math.BigDecimal;

import javax.swing.JOptionPane;

import org.compiere.model.MDocType;
import org.compiere.model.MJournal;
import org.compiere.model.MJournalBatch;
import org.compiere.model.MJournalLine;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.opensixen.acct.form.AcctEditorDefaults;
import org.opensixen.acct.grid.TableAccount;

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
		int batch_id=CreateJournalBatch(true);
		
		if(batch_id==0){
			JOptionPane.showMessageDialog(null, Msg.translate(Env.getCtx(), "Generate Journal Error"),Msg.translate(Env.getCtx(), "Error") , JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		//Completamos el batchcreado
		CompleteBatch(batch_id);
	}
	
	/**
	 * Completa el journal creado
	 * @param batch_id
	 */

	private void CompleteBatch(int batch_id) {
		MJournalBatch batch = new MJournalBatch(Env.getCtx(),batch_id,null);
		batch.setDocAction( MJournalBatch.DOCACTION_Complete );
		batch.processIt( MJournalBatch.DOCACTION_Complete );
		
		if (batch.save()){
			JOptionPane.showMessageDialog(null, Msg.translate(Env.getCtx(), "Batch Generated succesfully"),Msg.translate(Env.getCtx(), "Save") , JOptionPane.INFORMATION_MESSAGE);
		}else{
			JOptionPane.showMessageDialog(null, Msg.translate(Env.getCtx(), "Generate Journal Error"),Msg.translate(Env.getCtx(), "Error") , JOptionPane.ERROR_MESSAGE);
		}
		
	}

	private int CreateJournalBatch(boolean newregister){
		//Creamos en primer lugar la cabecera del asiento manual
		MJournalBatch batch = null;
		if(newregister)
			batch = new MJournalBatch(Env.getCtx(),0,null);
		

		//Cogemos los valores por defecto del panel
		batch.setPostingType(MJournalBatch.POSTINGTYPE_Actual);
		batch.setGL_Category_ID((Integer)AcctEditorDefaults.getGLCategory());
		batch.setAD_Org_ID((Integer)AcctEditorDefaults.getOrg());
		batch.setC_Currency_ID((Integer)AcctEditorDefaults.getCurrency());
		batch.setDateAcct(AcctEditorDefaults.getDateAcct());
		batch.setDateDoc(AcctEditorDefaults.getDateAcct());
		batch.setC_DocType_ID(getDocType(MDocType.DOCBASETYPE_GLJournal));
		batch.setDescription(Msg.translate(Env.getCtx(), "Journal manually made"));
		if(batch.save()){
			if(CreatetoJournal(batch))
				return batch.getGL_JournalBatch_ID();
		}
		return 0;
	}
	
	private boolean CreatetoJournal(MJournalBatch batch) {
		
		MJournal journal = new MJournal(batch);
		journal.setC_AcctSchema_ID((Integer)AcctEditorDefaults.getAcctSchema());
		journal.setDescription(batch.getDescription());
		journal.setGL_Category_ID((Integer)AcctEditorDefaults.getGLCategory());
		journal.setC_ConversionType_ID((Integer)AcctEditorDefaults.getConversionType());
		if(journal.save()){
			return CreateJournalLines(journal);
		}
		
		return false;
		
	}

	private boolean CreateJournalLines(MJournal journal) {
		
		//Para cada linea creamos journalline
		try{
			for(int row=0;row<t.getRowCount();row++){
				//Comprobamos que la linea esté asociada
				if(t.getValueAt(row, TableAccount.COLUMN_ValidCombination) != null){
					MJournalLine jline = new MJournalLine(journal);
					//Totales
					BigDecimal lDr=t.getValueAt(row, TableAccount.COLUMN_AmtAcctDr)==null?BigDecimal.ZERO:(BigDecimal)t.getValueAt(row, TableAccount.COLUMN_AmtAcctDr);
					BigDecimal lCr=t.getValueAt(row, TableAccount.COLUMN_AmtAcctCr)==null?BigDecimal.ZERO:(BigDecimal)t.getValueAt(row, TableAccount.COLUMN_AmtAcctCr);
				
					jline.setAmtSourceDr(lDr);
					jline.setAmtSourceCr(lCr);
					jline.setAmtAcct(lDr, lCr);
					//Cuenta Asociada
					jline.setC_ValidCombination_ID((Integer)t.getValueAt(row, TableAccount.COLUMN_ValidCombination));
					jline.save();
				}	
			}
		}
		catch(Exception e){
			//Con cualquier error ocurrido retornamos 
			return false;
		}
		return true;
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
