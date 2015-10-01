package gifunogi.tui4javafx;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ListCell;

public class UserInfoCell extends ListCell<UserInfo> {

	private CellController controller;

	@Override
	public void updateItem(UserInfo item, boolean empty) {
		super.updateItem(item, empty);
		if (item == null|| empty) {
			setGraphic(null);
			setText(null);
			controller = null;
			return;
		}

		if(controller == null || getGraphic() == null){
			try{
			FXMLLoader fxmlLoader = new FXMLLoader();
			Node node = fxmlLoader.load(getClass().getResourceAsStream("/UserInfoCell.fxml"));
			controller = fxmlLoader.getController();
			setGraphic(node);
			controller.update(item);
			} catch (IOException e) {
				System.out.println(e);
			}
		}else {
			controller.update(item);
		}

	}
}
