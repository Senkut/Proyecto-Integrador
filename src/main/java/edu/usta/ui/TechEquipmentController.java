package edu.usta.ui;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;

public class TechEquipmentController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField brandField;

    @FXML
    private TextField modelField;

    @FXML
    private TextField priceField;

    // Método llamado desde onAction="#saveTech"
    @FXML
    public void saveTech(ActionEvent event) {
        String name = nameField.getText();
        String brand = brandField.getText();
        String model = modelField.getText();
        String price = priceField.getText();

        System.out.println("Guardando Equipo Tecnológico:");
        System.out.println(name + " " + brand + " " + model + " " + price);
    }

    // 🚀 MÉTODO QUE FALTABA PARA EVITAR EL ERROR
    // Llamado desde onAction="#cancel"
    @FXML
    public void cancel(ActionEvent event) {
        System.out.println("❌ Cancelando acción: limpiando campos...");

        nameField.clear();
        brandField.clear();
        modelField.clear();
        priceField.clear();
    }
}
