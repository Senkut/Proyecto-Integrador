package edu.usta.ui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import java.util.UUID;

public class MainController {

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtBrand;

    @FXML
    private TextField txtModel;

    @FXML
    private TextArea txtDescription;

    @FXML
    private ComboBox<String> cmbEquipmentType;

    @FXML
    private TextField txtSpecialField;

    @FXML
    private Label lblGeneratedId;

    @FXML
    private Label lblSpecialField;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnClear;

    @FXML
    private TextArea txtOutput;

    @FXML
    public void initialize() {
        System.out.println("🎮 Inicializando MainController...");

        // Configurar ComboBox con los tipos de equipamiento
        cmbEquipmentType.setItems(FXCollections.observableArrayList(
                "Technical Equipment",
                "Biomedical Equipment"));

        // Listener para cambiar el label según el tipo seleccionado
        cmbEquipmentType.setOnAction(event -> {
            String selected = cmbEquipmentType.getValue();
            if (selected != null) {
                if (selected.equals("Technical Equipment")) {
                    lblSpecialField.setText("Voltage:");
                    txtSpecialField.setPromptText("Ej: 220V");
                } else if (selected.equals("Biomedical Equipment")) {
                    lblSpecialField.setText("Risk Class:");
                    txtSpecialField.setPromptText("Ej: Class IIa");
                }
            }
        });

        // Generar ID automáticamente al iniciar
        generateNewId();

        System.out.println("✅ MainController inicializado correctamente!");
    }

    @FXML
    private void handleSave() {
        System.out.println("💾 Guardando equipment...");

        // Validar campos
        if (!validateFields()) {
            showAlert("Error de Validación", "Por favor complete todos los campos obligatorios.");
            return;
        }

        // Obtener datos del formulario
        String id = lblGeneratedId.getText();
        String name = txtName.getText();
        String brand = txtBrand.getText();
        String model = txtModel.getText();
        String description = txtDescription.getText();
        String type = cmbEquipmentType.getValue();
        String specialValue = txtSpecialField.getText();

        // Mostrar en el área de salida
        StringBuilder output = new StringBuilder();
        output.append("=== EQUIPMENT GUARDADO ===\n");
        output.append("ID: ").append(id).append("\n");
        output.append("Nombre: ").append(name).append("\n");
        output.append("Marca: ").append(brand).append("\n");
        output.append("Modelo: ").append(model).append("\n");
        output.append("Descripción: ").append(description).append("\n");
        output.append("Tipo: ").append(type).append("\n");

        if (type.equals("Technical Equipment")) {
            output.append("Voltaje: ").append(specialValue).append("\n");
        } else {
            output.append("Clase de Riesgo: ").append(specialValue).append("\n");
        }

        output.append("========================\n\n");

        txtOutput.appendText(output.toString());

        System.out.println("✅ Equipment guardado: " + name);

        // Limpiar formulario y generar nuevo ID
        handleClear();

        showAlert("Éxito", "Equipment guardado correctamente!");
    }

    @FXML
    private void handleClear() {
        System.out.println("🧹 Limpiando formulario...");

        txtName.clear();
        txtBrand.clear();
        txtModel.clear();
        txtDescription.clear();
        txtSpecialField.clear();
        cmbEquipmentType.setValue(null);
        lblSpecialField.setText("Campo especial:");
        generateNewId();
    }

    private void generateNewId() {
        String uuid = UUID.randomUUID().toString();
        lblGeneratedId.setText(uuid);
        System.out.println("🆔 Nuevo ID generado: " + uuid);
    }

    private boolean validateFields() {
        return !txtName.getText().trim().isEmpty() &&
                !txtBrand.getText().trim().isEmpty() &&
                !txtModel.getText().trim().isEmpty() &&
                !txtDescription.getText().trim().isEmpty() &&
                cmbEquipmentType.getValue() != null &&
                !txtSpecialField.getText().trim().isEmpty();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}