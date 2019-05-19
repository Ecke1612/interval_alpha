package handling;

public class Designer {

    private String grey = "120, 120, 120";
    private String lightgrey = "180, 180, 180";

    private String green = "77, 168, 37";
    private String greenGradient = "-fx-background-color: linear-gradient(to bottom right, rgba(156,215,106, 1) 8%, rgba(77, 168, 37, 1) 100%);";
    private String lightblue = "0, 168, 219";

    public String returner(String scene, String style) {
        switch(scene) {
            case "standard":
                return standard(style);
            case "menu":
                return menu(style);
            case "newProject":
                return newProject(style);
            default:
                return null;
        }
    }

    private String standard(String style) {
        switch(style) {
            case "default":
                return "-fx-background-color: rgb(" + grey + ");";
            case "green":
                return "-fx-background-color: rgb(" + green + ");";
            default:
                return null;
        }
    }

    private String menu(String style) {
        switch(style) {
            case "default":
                return "-fx-background-color: rgb(" + lightgrey + ");";
            case "green":
                return greenGradient;
            default:
                return null;
        }
    }

    private String newProject(String style) {
        switch(style) {
            case "default":
                return "-fx-background-color: rgb(" + green + ");";
            case "green":
                return "-fx-background-color: rgb(" + lightblue + ");";
        }
    }

}
