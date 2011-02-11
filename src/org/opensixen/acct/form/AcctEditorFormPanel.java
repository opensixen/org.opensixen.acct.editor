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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;

import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;

/**
 * 
 * AcctEditorFormPanel 
 *
 * @author Alejandro González
 * Nexis Servicios Informáticos http://www.nexis.es
 */

public class AcctEditorFormPanel extends JPanel implements FormPanel,ActionListener,VetoableChangeListener,ChangeListener,TableModelListener{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int m_WindowNo = 0;
    private FormFrame m_frame;

	private AcctEditorDefaults defaults;
	private AcctEditorSearch search;
	private AcctEditorJournal journal;
	private AcctEditorDefaultEntry defaultentry;
	
	private JSplitPane split1 = new JSplitPane();
	
	@Override
	public void tableChanged(TableModelEvent arg0) {
		
	}

	@Override
	public void stateChanged(ChangeEvent arg0) {
		
	}

	@Override
	public void vetoableChange(PropertyChangeEvent arg0)
			throws PropertyVetoException {
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		
	}

	@Override
	public void init(int WindowNo, FormFrame frame) {
		m_WindowNo = WindowNo;
	    m_frame    = frame;
	    
	    try {
			jbInit();
	    	frame.getContentPane().add( this,BorderLayout.CENTER );
	    	frame.pack();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void jbInit() throws Exception {
		 	
		this.setLayout(new BorderLayout());
	    //Panel parametros por defecto
	    defaults= new AcctEditorDefaults(this); 	
	    this.add(defaults, BorderLayout.NORTH);
	    this.add(split1,BorderLayout.CENTER);

	    //Panel búsqueda
	    search= new AcctEditorSearch(this);
	    split1.add(search, JSplitPane.RIGHT);
	    //Panel de edicion de asientos
	    journal = new AcctEditorJournal(this);
	    split1.add(journal, JSplitPane.LEFT);
	    
	    //Listener para el posicionamiento del divisor
	    HierarchyListener hierarchyListener = new HierarchyListener() {
	    	@Override
			public void hierarchyChanged(HierarchyEvent e) {
				long flags = e.getChangeFlags();
		          if ((flags & HierarchyEvent.SHOWING_CHANGED) == HierarchyEvent.SHOWING_CHANGED) {
		            split1.setDividerLocation(.80);
		          }
				
			}
	      };
	     split1.addHierarchyListener(hierarchyListener);
	    //Panel de asientos predefinidos
	    defaultentry = new AcctEditorDefaultEntry(this);
	    

	}    // jbInit


	@Override
	public void dispose() {
        if( m_frame != null ) {
            m_frame.dispose();
        }

        m_frame = null;
		
	}

	
	/**
	 * Retorna el panel de parametros
	 * @return 
	 */
	
	protected AcctEditorDefaults getPanelDefaults(){
		return defaults;
	}
	
	/**
	 * Retorna el panel de resultados de posibles facturas
	 * @return 
	 */
	
	protected AcctEditorSearch getPanelSearch(){
		return search;
	}
	
	/**
	 * Retorna el panel de resultados de posibles facturas
	 * @return 
	 */
	
	protected AcctEditorJournal getPanelJournal(){
		return journal;
	}
	
	/**
	 * Retorna el panel de resultados de posibles facturas
	 * @return 
	 */
	
	protected AcctEditorDefaultEntry getPanelDefaultEntry(){
		return defaultentry;
	}
	
	
}
