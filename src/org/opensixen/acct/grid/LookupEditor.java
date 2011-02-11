package org.opensixen.acct.grid;

import java.awt.Component;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;

import org.compiere.grid.ed.VEditor;
import org.compiere.grid.ed.VLookup;
import org.compiere.minigrid.IDColumn;
import org.compiere.model.MBPartner;
import org.compiere.model.MLookup;
import org.compiere.model.MLookupFactory;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;

public class LookupEditor extends AbstractCellEditor implements TableCellEditor {

	private VEditor m_editor = null;
	
	private JTable      m_table;
	
	public LookupEditor(){

	}
	
	public LookupEditor(Class c) {
		super();
		if(c==MBPartner.class){
			MLookup BPL = MLookupFactory.get (Env.getCtx(), 0, 0, 2893, DisplayType.TableDir);
			m_editor = new VLookup (MBPartner.COLUMNNAME_C_BPartner_ID, true, false, true, BPL);
			
		}
	}


	@Override
	public Object getCellEditorValue() {

		return m_editor.getValue();
	}

	@Override
	public boolean isCellEditable(EventObject arg0) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {

		m_editor.setValue(value);

		//	Set UI
		m_editor.setBorder(null);
	//	m_editor.setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
		m_editor.setFont(table.getFont());
		return (Component)m_editor;
	}

}
