package dreams;

public class Comment {
	
	private String username;
	private String comment;
	private String date;
	private String id;
	private String dreamid;
	
	public Comment(String _id, String _dreamid, String _username, String _comment, String _date){
		
		id = _id;
		dreamid = _dreamid;
		username = _username;
		comment = _comment;
		date = _date;
	}
	
	public String getUsername(){
		return username;
	}
	
	public void setUsername(String _username){
		username = _username;
	}
	
	public String getComment(){
		return comment;
	}
	
	public void setComment(String _com){
		comment = _com;
	}
	
	public String getDate(){
		return date;
	}
	
	public void setDate(String d){
		date = d;
	}
	
	public String getId(){
		return id;
	}
	
	public void setId(String _id){
		id = _id;
	}
	
	public String getDreamid(){
		return dreamid;
	}
	
	public void setDreamid(String _id){
		dreamid = _id;
	}

}
