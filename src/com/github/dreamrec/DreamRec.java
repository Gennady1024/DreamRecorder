package com.github.dreamrec;

public class DreamRec {
    public static void main(String[] args) {
        ApplicationProperties applicationProperties = new ApplicationProperties();
        Controller controller = new Controller(applicationProperties);
        Model model = new Model();
        MainWindow mainWindow = new MainWindow(controller, model, applicationProperties);
        controller.setMainWindow(mainWindow);
    }
}
