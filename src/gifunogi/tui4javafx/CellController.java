package gifunogi.tui4javafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import twitter4j.Twitter;
import twitter4j.TwitterException;

public class CellController {
	private long statusId;
	@FXML private ImageView iconImageView;
	@FXML private Label userNameLabel;
	@FXML private Label textLabel;
	@FXML private Button retweetButton;
	@FXML private Button favButton;

	@FXML private ImageView replyImageView;
	@FXML private ImageView retweetImageView;
	@FXML private ImageView favImageView;

	private Image replyImage = new Image("/icon/Reply.png");
	private Image retweetImage = new Image("/icon/RetweetOff.png");
	private Image retweetOnImage = new Image("/icon/RetweetOn.png");
	private Image favImage = new Image("/icon/FavOff.png");
	private Image favOnImage = new Image("/icon/FavOn.png");

	private UserInfo item;

	Twitter twitter = MainController.getTwitter();

	long myRetweetedId;

	public void update(UserInfo item){
		setStatusId(item.getStatusId());
		iconImageView.setImage(item.getIconImage());
		userNameLabel.setText(item.getUserName());
		textLabel.setText(item.getText());

		replyImageView.setImage(replyImage);
		// リツイート済みか判定
		if (item.isRetweeted()) {
			// RetweetedId = status.getCurrentUserRetweetId();
			retweetImageView.setImage(retweetOnImage);
		} else {
			retweetImageView.setImage(retweetImage);
		}
		// お気に入り済みか判定
		if (item.isFavorited()) {
			favImageView.setImage(favOnImage);
		} else {
			favImageView.setImage(favImage);
		}
		this.item = item;
	}

	public void setStatusId(long statusId){
		this.statusId = statusId;
	}

	@FXML void onRetweetButton(ActionEvent event) {
		try {
			if (twitter.showStatus(statusId).isRetweeted()) {
				twitter.destroyStatus(myRetweetedId);
				retweetImageView.setImage(retweetImage);
			} else {
				myRetweetedId = twitter.retweetStatus(statusId).getId();
				retweetImageView.setImage(retweetOnImage);
			}
		} catch (TwitterException e) {
			e.printStackTrace();
		}
	}

	@FXML void onFavButton(ActionEvent event) {
		try {
			if (twitter.showStatus(statusId).isFavorited()) {
				twitter.destroyFavorite(statusId);
				favImageView.setImage(favImage);
				item.setIsFavorited(false);
			} else {
				twitter.createFavorite(statusId);
				favImageView.setImage(favOnImage);
				item.setIsFavorited(true);
			}
		} catch (TwitterException e) {
			e.printStackTrace();
		}
	}
}
