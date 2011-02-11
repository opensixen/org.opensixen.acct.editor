package org.opensixen.acct.grid;

import java.awt.Component;
import java.awt.event.MouseListener;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import org.compiere.grid.ed.VCellRenderer;
import org.compiere.grid.ed.VLookup;
import org.compiere.minigrid.IDColumn;
import org.compiere.model.GridField;
import org.compiere.model.MBPartner;
import org.compiere.model.MLookup;
import org.compiere.model.MLookupFactory;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;

public class LookupRenderer extends VCellRenderer{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private VLookup vLk;
	

	
	public LookupRenderer(GridField mField) {
		super(mField);
		MLookup BPL = MLookupFactory.get (Env.getCtx(), 0, 0, 2893, DisplayType.TableDir);
		vLk = new VLookup (MBPartner.COLUMNNAME_C_BPartner_ID, true, false, true, BPL);
		for(MouseListener list :vLk.getMouseListeners())
			System.out.println("1Existen mouselisteners");
	}

	public LookupRenderer(int displayType) {
		super(displayType);
		MLookup BPL = MLookupFactory.get (Env.getCtx(), 0, 0, 2893, DisplayType.TableDir);
		vLk = new VLookup (MBPartner.COLUMNNAME_C_BPartner_ID, true, false, true, BPL);
		for(MouseListener list :vLk.getMouseListeners())
			System.out.println("2Existen mouselisteners");
	}
	
	protected void setValue(Object value)
	{
		vLk.setValue(value);
	}   //  setValue
	
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	{
		setValue(value);

		return vLk;
	}   //  setTableCellRenderereComponent
}
