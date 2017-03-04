import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Created by nafeedgbhs on 12/9/2015.
 */


/**
 * Created by nafeedgbhs on 12/8/2015.
 */
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class PlayListController {

    Main main;

    public void setMain(Main main){this.main = main;}

    @FXML
    private TableView<ToPlay> tableView;

    @FXML
    private TableColumn<ToPlay, String> filePathColumn;

    @FXML
    private Button close;

    @FXML
    void closeButtonAction(ActionEvent event) {
        main.playListStage.close();
        main.playListStage = null;
    }



    public void setTableViewItem(ObservableList<ToPlay> toPlayObservableList)
    {
        tableView.setItems( toPlayObservableList );
    }

    public void setFilePathColumn()
    {
        filePathColumn.setCellValueFactory(new PropertyValueFactory<ToPlay, String>("path"));
    }
}

