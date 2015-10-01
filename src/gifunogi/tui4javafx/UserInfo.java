package gifunogi.tui4javafx;

import javafx.scene.image.Image;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.User;

public class UserInfo {
	private boolean isRetweeted;
	private boolean isFavorited;
	private Image iconImage;
	private String userName;
	private String text;
	private long statusId;

	Status status;
	User user;
	Twitter twitter = MainController.getTwitter();

	public UserInfo(Image icon, Status status){
		this.iconImage = icon;
		this.status = status;
		user =status.getUser();
		this.isRetweeted = status.isRetweeted();
		this.isFavorited = status.isFavorited();
		this.userName = user.getName() + '@' + user.getScreenName();
		this.text = status.getText();
		this.statusId = status.getId();
	}

	public boolean isRetweeted () {
		return isRetweeted;
	}

	public void setIsRetweeted (boolean isRetweeted) {
		this.isRetweeted = isRetweeted;
	}

	public boolean isFavorited () {
		return isFavorited;
	}

	public void setIsFavorited(boolean isFavorited) {
		this.isFavorited = isFavorited;
	}

	public Image getIconImage() {
		return iconImage;
	}

	public String getUserName() {
		return userName;
	}

	public String getText() {
		return text;
	}

	public long getStatusId() {
		return statusId;
	}


}
