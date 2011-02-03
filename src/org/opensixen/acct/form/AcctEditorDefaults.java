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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.sql.Timestamp;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.compiere.grid.ed.VDate;
import org.compiere.grid.ed.VLookup;
import org.compiere.model.MLookup;
import org.compiere.model.MLookupFactory;
import org.compiere.swing.CLabel;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.Msg;

/**
 * 
 * AcctEditorDefaults 
 *
 * @author Alejandro González
 * Nexis Servicios Informáticos http://www.nexis.es
 */

public class AcctEditorDefaults extends JPanel implements VetoableChangeListener{

	private static final long serialVersionUID = 1L;


	/**
	 * Descripción de variables globales
	 */
	
	int m_WindowNo=0;
	
	
	/**
	 * Descripción de swings
	 */
	

	
	//Esquema Contable
	private CLabel lAcctSchema = new CLabel();
	private VLookup vAcctSchema;
	
	//Fecha Contable
	private CLabel lDateAcct = new CLabel();
	private VDate vDateAcct= new VDate();
	
	//Organización
	private CLabel lOrg = new CLabel();
	private VLookup vOrg;
	
	
	public AcctEditorDefaults(){
		initComponents();
	}
	
	public AcctEditorDefaults(AcctEditorFormPanel acctEditorFormPanel) {
		// TODO Auto-generated constructor stub
	}

	private void initComponents(){
		fillPicks();
		this.setLayout(new GridBagLayout());
        //Columna,fila,?,?,withx,withy

		this.add( lDateAcct,new GridBagConstraints( 0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets( 2,2,2,2 ),0,0 ));
        this.add( vDateAcct,new GridBagConstraints( 1,0,1,1,0.3,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets( 2,2,2,20 ),0,0 ));
		this.add( lAcctSchema,new GridBagConstraints( 2,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets( 2,2,2,2 ),0,0 ));
        this.add( vAcctSchema,new GridBagConstraints( 3,0,1,1,0.3,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets( 2,2,2,20 ),0,0 ));
        this.add( lOrg,new GridBagConstraints( 4,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets( 2,2,2,2 ),0,0 ));
        this.add( vOrg,new GridBagConstraints( 5,0,1,1,0.3,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets( 2,2,2,20 ),0,0 ));

	}
	
	private void fillPicks(){
		
		//Organizacion
		MLookup orgL = MLookupFactory.get (Env.getCtx(), m_WindowNo, 0, 528, DisplayType.TableDir);
		vOrg = new VLookup ("AD_Org_ID", true, false, true, orgL);
		lOrg.setText(Msg.translate(Env.getCtx(), "AD_Org_ID"));
		vOrg.addVetoableChangeListener(this);
		lOrg.setLabelFor(vOrg);
		
		//Esquema Contable
		MLookup AcctSchemaL = MLookupFactory.get (Env.getCtx(), m_WindowNo, 0, 2463, DisplayType.TableDir);
		vAcctSchema = new VLookup ("C_AcctSchema_ID", true, false, true, AcctSchemaL);
		lAcctSchema.setText(Msg.translate(Env.getCtx(), "C_AcctSchema_ID"));
		vAcctSchema.addVetoableChangeListener(this);
		lAcctSchema.setLabelFor(vAcctSchema);
		
		//Fecha Contable
		lDateAcct.setText(Msg.translate(Env.getCtx(), "DateAcct"));
		lDateAcct.setLabelFor(vDateAcct);
		
		
	}

	@Override
	public void vetoableChange(PropertyChangeEvent arg0)
			throws PropertyVetoException {
		// TODO Auto-generated method stub
		
	}
	
	
	/**
	 * 
	 * @return Fecha Contable
	 */
	
	public Timestamp getDateAcct(){
		return vDateAcct.getTimestamp();
	}
	

	/**
	 * 
	 * @return Esquema Contable
	 */
	
	public Object getAcctSchema(){
		return vAcctSchema.getValue();
	}
	

	/**
	 * 
	 * @return Organización
	 */
	
	public Object getOrg(){
		return vOrg.getValue();
	}
}
