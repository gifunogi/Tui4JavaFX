package gifunogi.tui4javafx;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

public class OAuth {
	public static AccessToken getOAuthAccessToken(Twitter twitter){
		RequestToken requestToken = null;
		AccessToken accessToken = null;
		try{
			requestToken = twitter.getOAuthRequestToken();
			while (null == accessToken) {
				InputStreamReader ir = new InputStreamReader(System.in);
				BufferedReader br = new BufferedReader(ir);
				String pin = null;
				System.out.println("Open the following URL and grant access to your account:");
				System.out.println(requestToken.getAuthorizationURL());
				System.out.print("Enter the PIN(if aviailable) or just hit enter.[PIN]:");
				try{
					pin = br.readLine();
				}catch(IOException e){
					System.out.println(e);
				}
				try{
					if(pin.length() > 0){
						accessToken = twitter.getOAuthAccessToken(requestToken, pin);
					}
					else{
						accessToken = twitter.getOAuthAccessToken();
					}
				}catch(TwitterException te){
					if(te.getStatusCode() == 401){
						System.out.println("Unable to get the access token.");
					}
					else{
						te.printStackTrace();
						System.exit(1);
					}
				}
			}
		}catch(TwitterException e){
			e.printStackTrace();
		}
		return accessToken;
	}

	public static AccessToken loadAccessToken() {
		File f = createAccessTokenFileName();
		ObjectInputStream is = null;
		try{
			is = new ObjectInputStream(new FileInputStream(f));
			AccessToken accesstoken = (AccessToken) is.readObject();
			return accesstoken;
		}catch(IOException e){
			return null;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			if(is != null){
				try{
					is.close();
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		}
	}

	public static void storeAccessToken(AccessToken accessToken){
		File f = createAccessTokenFileName();
		File d  = f.getParentFile();
		if(!d.exists())
			d.mkdirs();
		ObjectOutputStream os = null;
		try{
			os = new ObjectOutputStream(new FileOutputStream(f));
			os.writeObject(accessToken);
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			if(os != null){
				try{
					os.close();
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		}
	}

	public static File createAccessTokenFileName(){
		String s = "./accesssToken.dat";
		return new File(s);
	}

	public static String getConsumerKey() {
		String consumerKey = null;
		BufferedReader br = null;
		try{
			File f = new File("./ConsumerKey.txt");
			br = new BufferedReader(new FileReader(f));
			consumerKey =  br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return consumerKey;
	}

public static String getConsumerKeySec() {
	String consumerKeySec = null;
	BufferedReader br = null;
	try{
		File f = new File("./ConsumerKeySec.txt");
		br = new BufferedReader(new FileReader(f));
		consumerKeySec =  br.readLine();
	} catch (IOException e) {
		e.printStackTrace();
	} finally {
		try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	return consumerKeySec;
}
}
