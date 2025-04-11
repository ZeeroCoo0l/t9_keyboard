module org.tnine_application.t9_keyboard {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.tnine_application.t9_keyboard to javafx.fxml;
    exports org.tnine_application.t9_keyboard;
}