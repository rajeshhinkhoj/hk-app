package HinKhoj.Dictionary.UIData;

public class StringIdRowItem {
    
	private int rid;
	private String text;
 
    public StringIdRowItem(int rid,String text) {
        
        this.rid=rid;
        this.text=text;
        
    }
   
    public int  getRId() {
        return rid;
    }
    public void setRId(int rid) {
        this.rid = rid;
    }
    
    
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text=text;
    }
    
}

