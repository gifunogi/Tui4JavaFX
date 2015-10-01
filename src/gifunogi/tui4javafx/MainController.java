package gifunogi.tui4javafx;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import twitter4j.Paging;
import twitter4j.RateLimitStatus;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;

public class MainController implements Initializable{
	@FXML private Button reloadButton;
	@FXML private Button tweetButton;
	@FXML private ImageView reloadImageView = new ImageView(new Image("/icon/Reload.png"));
	@FXML private ImageView tweetImageView = new ImageView(new Image("/icon/Tweet.png"));
	@FXML private TextArea tweetTextArea;
	@FXML private ListView<UserInfo> listView;

	static Twitter twitter = TwitterFactory.getSingleton();

	ResponseList<Status> homeTL = null;
	long lastStatusId = 0;
	Paging page = null;
	private ObservableList<UserInfo> observableList = FXCollections.observableArrayList();

	ArrayList<UserInfo> userInfoList = new ArrayList<UserInfo>();
	HashMap<String, Image> iconMap = new HashMap<String, Image>();

	RateLimitStatus rateLimit;

	@Override
	public void initialize(final URL location, final ResourceBundle resources) {
		String consumerKey = OAuth.getConsumerKey();
		String consumerKeySec = OAuth.getConsumerKeySec();			// 外部テキストから読み込む(直接入力でも可)
		twitter.setOAuthConsumer(consumerKey, consumerKeySec);
		AccessToken accessToken = OAuth.loadAccessToken();

		// OAuth認証
		if(accessToken == null){
			accessToken = OAuth.getOAuthAccessToken(twitter);
			OAuth.storeAccessToken(accessToken);
		} else {
			twitter.setOAuthAccessToken(accessToken);
		}

		// ユーザ情報の管理
		try {
			reloadHomeTL(lastStatusId);
		} catch (TwitterException e) {
			e.printStackTrace();
		}
		/*
		for(UserInfo userInfo : userInfoList){
			observableList.add(0, userInfo);
		}
		listView.setItems(observableList);
		*/
		lastStatusId = homeTL.get(0).getId();

		// API制限の確認(コンソール)
		rateLimit = homeTL.getRateLimitStatus();

		// セル生成
		listView.setCellFactory(new Callback<ListView<UserInfo>, ListCell<UserInfo>>(){
			@Override
			public ListCell<UserInfo> call(ListView<UserInfo> list) {
				return new UserInfoCell();
			}
		});
	}

	@FXML
	public void onReloadButton(ActionEvent event) {
		try {
			reloadHomeTL(lastStatusId);
		} catch (TwitterException e) {
			e.printStackTrace();
		}
	}

	@FXML void onTweetButton(ActionEvent event) {
		String tweet = tweetTextArea.getText();
		try {
			@SuppressWarnings("unused")
			Status status = twitter.updateStatus(tweet);
			reloadHomeTL(lastStatusId);
		} catch (TwitterException e) {
			e.printStackTrace();
		}
		tweetTextArea.clear();
	}

	public void reloadHomeTL(long Id) throws TwitterException {
		User user = null;
		Image icon;
		if(page == null) {
			page = new Paging(1, 20);
		} else {
			page = new Paging(Id);
		}

		homeTL = twitter.getHomeTimeline(page);
		userInfoList.clear();
		if (homeTL.size() == 0) {
			System.out.println("---> 新着無し");
		}else {
			for (Status status : homeTL) {
				user = status.getUser();
				if (iconMap.get(user.getScreenName()) == null) {
					iconMap.put(user.getScreenName(), new Image(user.getBiggerProfileImageURL()));
				}
				icon = iconMap.get(user.getScreenName());
				userInfoList.add(0, new UserInfo(icon, status));
			}
		}
		if(homeTL.size() != 0) {
			for (UserInfo userInfo : userInfoList) {
				observableList.add(0, userInfo);
			}
			lastStatusId = homeTL.get(0).getId();
			listView.setItems(observableList);
		}
		// TwitterAPI制限の確認(コンソール)
		rateLimit = homeTL.getRateLimitStatus();
		System.out.println("残りアクセス可能回数 : " + rateLimit.getRemaining() + "回");
		System.out.println("再アクセス可能まで   : " + rateLimit.getSecondsUntilReset() + "秒");
		if (rateLimit.getRemaining() == 0) {
			System.out.println("API制限だー");
			System.exit(0);
		}
	}
	public static Twitter getTwitter() {
		return twitter;
	}
}
