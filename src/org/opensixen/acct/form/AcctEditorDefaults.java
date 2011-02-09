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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.compiere.grid.ed.VDate;
import org.compiere.grid.ed.VLookup;
import org.compiere.grid.ed.VNumber;
import org.compiere.model.MClientInfo;
import org.compiere.model.MConversionType;
import org.compiere.model.MCurrency;
import org.compiere.model.MGLCategory;
import org.compiere.model.MLookup;
import org.compiere.model.MLookupFactory;
import org.compiere.model.MOrg;
import org.compiere.swing.CCheckBox;
import org.compiere.swing.CLabel;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.opensixen.model.POFactory;
import org.opensixen.model.QParam;

/**
 * 
 * AcctEditorDefaults 
 *
 * @author Alejandro González
 * Nexis Servicios Informáticos http://www.nexis.es
 */

public class AcctEditorDefaults extends JPanel implements VetoableChangeListener, ActionListener{

	private static final long serialVersionUID = 1L;


	/**
	 * Descripción de variables globales
	 */
	
	int m_WindowNo=0;
	
	
	/**
	 * Descripción de swings
	 */
	
	//Categoría Contable
	private CLabel lglCategory = new CLabel();
	private static VLookup vglCategory;
	
	//Esquema Contable
	private CLabel lAcctSchema = new CLabel();
	private static VLookup vAcctSchema;
	
	//Fecha Contable
	private CLabel lDateAcct = new CLabel();
	private static VDate vDateAcct= new VDate();
	
	//Organización
	private CLabel lOrg = new CLabel();
	private static VLookup vOrg;
	
	//Multimoneda
	private static CCheckBox multicurrency;
	
	//Moneda
	private CLabel lCurrency = new CLabel();
	private static VLookup vCurrency;
	
	//Tipo de Conversión
	private CLabel lConversionType = new CLabel();
	private static VLookup vConversionType;
	
	//Tipo de Conversión
	private CLabel lCurrencyRate = new CLabel();
	private static VNumber vCurrencyRate;
	
	public AcctEditorDefaults(){
		initComponents();
	}
	
	public AcctEditorDefaults(AcctEditorFormPanel acctEditorFormPanel) {
		initComponents();
	}

	private void initComponents(){
		fillPicks();
		this.setLayout(new GridBagLayout());
        //Columna,fila,?,?,withx,withy
		this.add( lglCategory,new GridBagConstraints( 0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets( 2,2,2,2 ),0,0 ));
        this.add( vglCategory,new GridBagConstraints( 1,0,1,1,0.3,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets( 2,2,2,10 ),0,0 ));
		this.add( lDateAcct,new GridBagConstraints( 2,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets( 2,2,2,2 ),0,0 ));
        this.add( vDateAcct,new GridBagConstraints( 3,0,1,1,0.3,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets( 2,2,2,10 ),0,0 ));
		this.add( lAcctSchema,new GridBagConstraints( 4,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets( 2,2,2,2 ),0,0 ));
        this.add( vAcctSchema,new GridBagConstraints( 5,0,1,1,0.3,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets( 2,2,2,10 ),0,0 ));
        this.add( lOrg,new GridBagConstraints( 6,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets( 2,2,2,2 ),0,0 ));
        this.add( vOrg,new GridBagConstraints( 7,0,1,1,0.3,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets( 2,2,2,20 ),0,0 ));
        this.add( multicurrency,new GridBagConstraints( 0,1,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets( 2,2,2,2 ),0,0 ));
        this.add( lCurrency,new GridBagConstraints( 2,1,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets( 2,2,2,2 ),0,0 ));
        this.add( vCurrency,new GridBagConstraints( 3,1,1,1,0.3,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets( 2,2,2,20 ),0,0 ));
        this.add( lConversionType,new GridBagConstraints( 4,1,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets( 2,2,2,2 ),0,0 ));
        this.add( vConversionType,new GridBagConstraints( 5,1,1,1,0.3,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets( 2,2,2,20 ),0,0 ));
        this.add( lCurrencyRate,new GridBagConstraints( 6,1,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets( 2,2,2,2 ),0,0 ));
        this.add( vCurrencyRate,new GridBagConstraints( 7,1,1,1,0.3,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets( 2,2,2,20 ),0,0 ));

        //Valores por defecto para los campos de moneda
        vCurrency.setEnabled(false);
        lConversionType.setVisible(false);
        vConversionType.setVisible(false);
        lCurrencyRate.setVisible(false);
        vCurrencyRate.setVisible(false);
        
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
		
		//Categoria Contable
		MLookup GLCategoryL = MLookupFactory.get (Env.getCtx(), m_WindowNo, 0, 1530, DisplayType.TableDir);
		vglCategory = new VLookup ("GL_Category_ID", true, false, true, GLCategoryL);
		lglCategory.setText(Msg.translate(Env.getCtx(), "GL_Category_ID"));
		vglCategory.addVetoableChangeListener(this);
		lglCategory.setLabelFor(vglCategory);
		
		//Fecha Contable
		lDateAcct.setText(Msg.translate(Env.getCtx(), "DateAcct"));
		lDateAcct.setLabelFor(vDateAcct);
		
		
		//Multimoneda
		multicurrency= new CCheckBox(Msg.translate(Env.getCtx(), "MultiCurrency"));
		multicurrency.addActionListener(this);
		
		//Moneda
		MLookup CurrencyL = MLookupFactory.get (Env.getCtx(), m_WindowNo, 0, 457, DisplayType.TableDir);
		vCurrency = new VLookup ("C_Currency_ID", true, false, true, CurrencyL);
		lCurrency.setText(Msg.translate(Env.getCtx(), "C_Currency_ID"));
		vCurrency.addVetoableChangeListener(this);
		lCurrency.setLabelFor(vCurrency);
		
		//Tipo de Conversión
		MLookup ConversionTypeL = MLookupFactory.get (Env.getCtx(), m_WindowNo, 0, 10269, DisplayType.TableDir);
		vConversionType = new VLookup ("C_ConversionType_ID", true, false, true, ConversionTypeL);
		lConversionType.setText(Msg.translate(Env.getCtx(), "C_ConversionType_ID"));
		vConversionType.addVetoableChangeListener(this);
		lConversionType.setLabelFor(vConversionType);
		
		//Tasa de Cambio
		vCurrencyRate = new VNumber();
		lCurrencyRate.setText(Msg.translate(Env.getCtx(), "CurrencyRate"));
		lCurrencyRate.setLabelFor(vCurrencyRate);
		
		//Valor por defecto al panel
		defaultvalues();
		
	}
	
	/**
	 * Setea los valores por defecto
	 */
	
	private void defaultvalues(){
		//Fecha Contable, por defecto fecha actual
		Calendar c2 = new GregorianCalendar();
		vDateAcct.setValue(new Timestamp(c2.getTimeInMillis()));
		
		//Euros por defecto, cogido de los valores de entorno
		MCurrency currency = new MCurrency(Env.getCtx(),Env.getContextAsInt(Env.getCtx(), "$C_Currency_ID"),null);
		vCurrency.setValue(currency.getC_Currency_ID());
		
		//Tipo de conversión por defecto
		MConversionType config = POFactory.get(MConversionType.class, new QParam(MConversionType.COLUMNNAME_IsDefault, "Y"));
		vConversionType.setValue(config.getC_ConversionType_ID());
		
		//Categoria CG
		MGLCategory category = POFactory.get(MGLCategory.class, new QParam[]{
								new QParam(MGLCategory.COLUMNNAME_IsDefault, "Y"),
								new QParam(MGLCategory.COLUMNNAME_AD_Client_ID,Env.getAD_Client_ID(Env.getCtx()))});
		vglCategory.setValue(category.getGL_Category_ID());
		
		//Esquema Contable (escogemos el asociado al grupo por defecto)
		MClientInfo cinfo = POFactory.get(MClientInfo.class, new QParam(MClientInfo.COLUMNNAME_AD_Client_ID, Env.getAD_Client_ID(Env.getCtx())));
		vAcctSchema.setValue(cinfo.getC_AcctSchema1_ID());
		
		//Empresa por defecto (la logueada)
		MOrg org = POFactory.get(MOrg.class, new QParam(MOrg.COLUMNNAME_AD_Org_ID, Env.getAD_Org_ID(Env.getCtx())));
		vOrg.setValue(org.getAD_Org_ID());
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
	
	public static Timestamp getDateAcct(){
		return vDateAcct.getTimestamp();
	}
	

	/**
	 * 
	 * @return Esquema Contable
	 */
	
	public static Object getAcctSchema(){
		return vAcctSchema.getValue();
	}
	

	/**
	 * 
	 * @return Organización
	 */
	
	public static Object getOrg(){
		return vOrg.getValue();
	}
	
	/**
	 * 
	 * @return Currency
	 */
	
	public static Object getCurrency(){
		return vCurrency.getValue();
	}
	
	/**
	 * 
	 * @return MultiCurrency
	 */
	
	public static boolean isMultiCurrency(){
		return multicurrency.isSelected();
	}
	
	/**
	 * 
	 * @return ConversionType
	 */
	
	public static Object getConversionType(){
		return vConversionType.getValue();
	}
	
	/**
	 * 
	 * @return ConversionType
	 */
	
	public static Object getCurrencyRate(){
		return vCurrencyRate.getValue();
	}
	
	
	/**
	 * 
	 * @return Organización
	 */
	
	public static Object getGLCategory(){
		return vglCategory.getValue();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource().equals(multicurrency)){
			if(multicurrency.isSelected()){
		        vCurrency.setEnabled(true);
		        lConversionType.setVisible(true);
		        vConversionType.setVisible(true);
		        lCurrencyRate.setVisible(true);
		        vCurrencyRate.setVisible(true);
			}else{
		        vCurrency.setEnabled(false);
		        lConversionType.setVisible(false);
		        vConversionType.setVisible(false);
		        lCurrencyRate.setVisible(false);
		        vCurrencyRate.setVisible(false);
			}
		}
		
	}
	
}
