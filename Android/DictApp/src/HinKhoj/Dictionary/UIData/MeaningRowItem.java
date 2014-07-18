package HinKhoj.Dictionary.UIData;

public class MeaningRowItem {
    
	private int rid;
	private String meaningDisplay;
 
    public MeaningRowItem(int rid,String meaningDisplay) {
        
        this.rid=rid;
        this.meaningDisplay=meaningDisplay;
        
    }
   
    public int  getRId() {
        return rid;
    }
    public void setRId(int rid) {
        this.rid = rid;
    }
    
    
    public String getMeaningDisplay() {
        return meaningDisplay;
    }
    public void setMeaningDisplay(String meaningDisplay) {
        this.meaningDisplay=meaningDisplay;
    }
    
}

