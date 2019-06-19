package gui.controller.project_module_objects;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class NodeInputStage {

    private String text = "";

    public String showStage(String initText) {
        Stage nodestage = new Stage();
        VBox main_vbox = new VBox(5);
        Scene scene = new Scene(main_vbox);
        nodestage.setScene(scene);

        TextArea textArea = new TextArea(initText);
        textArea.setWrapText(true);

        Button btn_abort = new Button("Abbrechen");
        btn_abort.setOnAction(event -> {
            nodestage.close();
        });

        Button btn_ok = new Button("Okay");
        btn_ok.setOnAction(event -> {
            text = textArea.getText();
            nodestage.close();
        });

        HBox hbox_buttons = new HBox(5);
        hbox_buttons.getChildren().addAll(btn_abort, btn_ok);

        main_vbox.getChildren().addAll(textArea, hbox_buttons);

        nodestage.showAndWait();
        return text;
    }

}
