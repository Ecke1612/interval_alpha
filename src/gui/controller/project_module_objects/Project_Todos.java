package gui.controller.project_module_objects;

import gui.controller.CTR_Project_Module;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;
import object.TodoStorage;


public class Project_Todos {

    private VBox vbox_todos;
    private CTR_Project_Module project;
    //private double oldHeight;
    //private String nodetext = "";

    public Project_Todos(VBox vbox_todos, CTR_Project_Module project) {
        this.vbox_todos = vbox_todos;
        this.project = project;
    }

    public void add_todo() {
        executeAddTodo("", false);
    }

    public void executeAddTodo(String text, boolean checked) {
        HBox hbox = new HBox(10);
        //hbox.setPadding(new Insets(-4,0,-3,-0));
        hbox.setId("todo");
        CheckBox checkbox = new CheckBox("");
        checkbox.setSelected(checked);
        TextField textField = new TextField(text);
        textField.setId("textfield");
        textField.setText(text);
        HBox.setHgrow(textField, Priority.ALWAYS);
        hbox.setAlignment(Pos.CENTER_LEFT);
        styleChBox(hbox, checkbox, textField);


        checkbox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                styleChBox(hbox, checkbox, textField);
                saveTodos();
            }
        });

        textField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(!newValue) {
                    saveTodos();
                }
            }
        });

        Button btn_delete = new Button("\uE107");
        btn_delete.setStyle(
                "-fx-font-family: 'Segoe MDL2 Assets';" +
                        "-fx-background-color: transparent;" +
                        "-fx-text-fill: darkred;" +
                        "-fx-font-size: 18;" +
                        "-fx-padding: -4"
        );
        btn_delete.setOnAction(event -> {
            vbox_todos.getChildren().remove(hbox);
            saveTodos();
        });

        hbox.getChildren().addAll(checkbox, textField, btn_delete);
        vbox_todos.getChildren().add(hbox);
    }

    private void styleChBox(HBox hbox, CheckBox ch, TextField tf) {
        if(ch.isSelected()) {
            hbox.setStyle("-fx-border-color: transparent;" +
                    "-fx-border-radius: 5;" +
                    "-fx-border-width: 3; " +
                    "-fx-background-color: linear-gradient(to right, rgba(45,208,45,0.62), #c8e8c4);" +
                    "-fx-background-radius: 5");
            tf.setDisable(true);
        } else {
            hbox.setStyle("-fx-background-color: transparent;" +
                    "-fx-border-color: transparent");
            tf.setDisable(false);
        }
    }


    public void executeAddNote(String nodetext, boolean loading) {
        if(!loading) {
            nodetext = nodeStage(nodetext);
        }
        if(!nodetext.equals("")) {
            TextFlow textFlow = new TextFlow();
            textFlow.setStyle(
                    "-fx-background-color: white;"
            );

            Text textNodes = new Text(nodetext);
            textNodes.setStyle(
                    "-fx-font-size: 12;"
            );

            HBox hBox = new HBox();
            hBox.setId("note");

            Button btn_edit = new Button("\uE104");
            btn_edit.setStyle(
                    "-fx-font-family: 'Segoe MDL2 Assets';" +
                            "-fx-background-color: transparent;" +
                            "-fx-font-size: 16"
            );
            btn_edit.setOnAction(event -> {
                textNodes.setText(nodeStage(textFlowToString(textFlow)));
            });

            Button btn_delete = new Button("\uE107");
            btn_delete.setStyle(
                    "-fx-font-family: 'Segoe MDL2 Assets';" +
                            "-fx-background-color: transparent;" +
                            "-fx-text-fill: darkred;" +
                            "-fx-font-size: 18;" +
                            "-fx-padding: -4"
            );
            btn_delete.setOnAction(event -> {
                vbox_todos.getChildren().remove(hBox);
                saveTodos();
            });

            //textNodes.textProperty().bind(textArea.textProperty());

            HBox.setHgrow(textFlow, Priority.ALWAYS);
            hBox.getChildren().addAll(textFlow, btn_edit, btn_delete);
            hBox.setAlignment(Pos.CENTER_LEFT);

            textFlow.getChildren().clear();
            textFlow.getChildren().add(textNodes);

            vbox_todos.getChildren().add(hBox);
        }
    }

    private String nodeStage(String initText) {
        Stage nodestage = new Stage();
        VBox main_vbox = new VBox(5);
        Scene scene = new Scene(main_vbox);
        nodestage.setScene(scene);

        TextArea textArea = new TextArea(initText);
        textArea.setWrapText(true);

        Button btn_abort = new Button("Abbrechen");
        btn_abort.setOnAction(event -> {
            textArea.setText(initText);
            nodestage.close();
        });

        Button btn_ok = new Button("Okay");
        btn_ok.setOnAction(event -> {
            nodestage.close();
        });

        HBox hbox_buttons = new HBox(5);
        hbox_buttons.getChildren().addAll(btn_abort, btn_ok);

        main_vbox.getChildren().addAll(textArea, hbox_buttons);

        nodestage.showAndWait();
        return textArea.getText();
    }

    public void saveTodos() {
        project.getTodos().clear();
        for(int i = 0; i < vbox_todos.getChildren().size(); i++) {
            HBox hbox = (HBox) vbox_todos.getChildren().get(i);
            if(hbox.getId() != null && hbox.getId().equals("note")) {
                TextFlow t = (TextFlow) hbox.getChildren().get(0);
                project.getTodos().add(new TodoStorage("note", textFlowToString(t)));
            } else if(hbox.getId() != null && hbox.getId().equals("todo")) {
                CheckBox ch = (CheckBox) hbox.getChildren().get(0);
                TextField t = (TextField) hbox.getChildren().get(1);
                project.getTodos().add(new TodoStorage("todo", ch.isSelected(), t.getText()));
            }
        }
        System.out.println("Todo's saved");
    }

    private String textFlowToString(TextFlow t) {
        StringBuilder sb = new StringBuilder();
        for (Node node : t.getChildren()) {
            if (node instanceof Text) {
                sb.append(((Text) node).getText());
            }
        }
        String fullText = sb.toString();
        return fullText;
    }

}
