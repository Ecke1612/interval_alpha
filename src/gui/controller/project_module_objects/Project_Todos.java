package gui.controller.project_module_objects;

import gui.controller.CTR_Project_Module;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import object.TodoStorage;


public class Project_Todos {

    private VBox vbox_todos;
    private CTR_Project_Module project;

    public Project_Todos(VBox vbox_todos, CTR_Project_Module project) {
        this.vbox_todos = vbox_todos;
        this.project = project;
    }

    public void add_todo() {
        executeAddTodo("", false);
    }

    public void executeAddTodo(String text, boolean checked) {
        HBox hbox = new HBox(10);
        hbox.setPadding(new Insets(-4,0,-3,-0));
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

        Image img_delete = new Image(getClass().getResourceAsStream("/images/delete.png"));
        Button btn_delete = new Button("", new ImageView(img_delete));
        btn_delete.setStyle("-fx-background-color: transparent");
        btn_delete.setMaxSize(10,10);
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
                    "-fx-background-color: linear-gradient(to right, forestgreen, darkseagreen);" +
                    "-fx-background-radius: 5");
            tf.setDisable(true);
        } else {
            hbox.setStyle("-fx-background-color: transparent;" +
                    "-fx-border-color: transparent");
            tf.setDisable(false);
        }
    }

    public void add_noteFXML() {
        executeAddNote("", 1);
    }

    public void executeAddNote(String text, int rowCount) {
        TextArea textArea = new TextArea();
        textArea.setPrefRowCount(rowCount);
        textArea.setWrapText(true);
        textArea.setText(text);
        textArea.setMinHeight(textArea.getMaxHeight() + (rowCount * textArea.getFont().getSize()*1.5));
        textArea.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                String text = textArea.getText();
                String[] lineArray = text.split("\n");
                textArea.setMinHeight(textArea.getMaxHeight() + (lineArray.length * textArea.getFont().getSize()*1.5));
            }
        });

        textArea.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(!newValue) {
                    saveTodos();
                }
            }
        });

        textArea.setPrefRowCount(rowCount);
        System.out.println("rowcoaunt: " + rowCount);

        HBox hBox = new HBox();
        hBox.setId("note");
        Image img_delete = new Image(getClass().getResourceAsStream("/images/delete.png"));
        Button btn_delete = new Button("", new ImageView(img_delete));
        btn_delete.setStyle("-fx-background-color: transparent");
        btn_delete.setMaxSize(10,10);
        btn_delete.setOnAction(event -> {
            vbox_todos.getChildren().remove(hBox);
            saveTodos();
        });

        HBox.setHgrow(textArea, Priority.ALWAYS);
        hBox.getChildren().addAll(textArea, btn_delete);
        hBox.setAlignment(Pos.CENTER_LEFT);
        vbox_todos.getChildren().add(hBox);
    }

    public void saveTodos() {
        project.getTodos().clear();
        for(int i = 0; i < vbox_todos.getChildren().size(); i++) {
            HBox hbox = (HBox) vbox_todos.getChildren().get(i);
            if(hbox.getId() != null && hbox.getId().equals("note")) {
                TextArea t = (TextArea) hbox.getChildren().get(0);
                project.getTodos().add(new TodoStorage("note", t.getText(), t.getPrefRowCount()));
            } else if(hbox.getId() != null && hbox.getId().equals("todo")) {
                CheckBox ch = (CheckBox) hbox.getChildren().get(0);
                TextField t = (TextField) hbox.getChildren().get(1);
                project.getTodos().add(new TodoStorage("todo", ch.isSelected(), t.getText()));
            }
        }
        System.out.println("Todo's saved");
    }

}
