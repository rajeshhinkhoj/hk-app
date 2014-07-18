package HinKhoj.Dictionary.UIData;

public class ToolsRowItem {
    
	private String name;
	private int iconName;
 
    public ToolsRowItem(String name,int iconName) {
        
        this.name=name;
        this.iconName=iconName;
        
    }
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name=name;
    }
    
    
    public int getIconName() {
        return iconName;
    }
    public void setIconName(int iconName) {
        this.iconName=iconName;
    }
    
    
    
}

