package org.opensixen.acct.utils;

import java.sql.Timestamp;

public class AcctEditorJournalNo {
	
	private int Journalno=-1;
	private Timestamp DateAcct=null;
	private int AD_Org=-1;
	
	public AcctEditorJournalNo(int journalno,int ad_org_id,Timestamp date){
		Journalno=journalno;
		AD_Org=ad_org_id;
		DateAcct=date;
	}
	
	public void setJournalNo(int journal){
		Journalno=journal;
	}
	
	public int getJournalNo(){
		return Journalno;
	}
	
	public void setAD_Org(int org){
		AD_Org=org;
	}
	
	public int getAD_Org(){
		return AD_Org;
	}
	
	public void setDateAcct(Timestamp date){
		DateAcct=date;
	}
	
	public Timestamp getDateAcct(){
		return DateAcct;
	}

}
