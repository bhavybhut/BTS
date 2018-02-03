package developer;

public class ViewProjFilter {

	private String name="";
	 
    public String getName() {
        return name;
    }
 
    public void setName(String name) {
        this.name = name==null?"":name.trim();
    }
	
}
