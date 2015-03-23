package son.nt.dota2.facebook.object;

public class FbMe {
	private String id, bio,birthday, email,name,quotes;
	

	public FbMe(String id, String bio, String email, String name, String quotes) {
	    super();
	    this.id = id;
	    this.bio = bio;
	    this.email = email;
	    this.name = name;
	    this.quotes = quotes;
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getQuotes() {
		return quotes;
	}

	public void setQuotes(String quotes) {
		this.quotes = quotes;
	}
	
}
