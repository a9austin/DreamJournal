package dreams;

public class Dream {
	private String title;
	private String description;
	private String dreamid;
	private String userid;
	private String username; 
	private String date; 
	
	public Dream(String _title, String _description, String _dreamid, String _userid, String _date){
		title = _title;
		description = _description;
		dreamid = _dreamid;
		userid = _userid;
		date = _date;
	}
	
	public String getTitle(){
		return title;
	}
	
	public void setTitle(String _title){
		title = _title;
	}
	
	public String getDescription(){
		return description;
	}
	
	public void setDescription(String _description){
		description = _description;
	}
	
	public String getDreamid(){
		return dreamid;
	}
	
	public void setDreamid(String _id){
		dreamid = _id;
	}
	
	public String getUserid(){
		return userid;
	}
	
	public void setUserid(String _id){
		userid = _id;
	}
	
	public String getUsername(){
		return username;
	}
	
	public void setUsername(String _username){
		username = _username;
	}
	
	public String getDate(){
		return date;
	}
	
	public void setDate(String _date){
		date = _date;
	}

}
