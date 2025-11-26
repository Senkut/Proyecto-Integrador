package edu.usta.ui;

import java.io.IOException;

import edu.usta.domain.entities.Provider;
import edu.usta.domain.entities.TechEquipment;
import edu.usta.domain.enums.EquipmentStatus;
import edu.usta.domain.enums.EquipmentType;
import edu.usta.domain.repositories.JDBCProviderRepository;
import edu.usta.domain.repositories.JDBCTechEquipmentRepository;
import edu.usta.infrastructure.db.DatabaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class TechEquipmentController {

    @FXML
    private TextField serialField;

    @FXML
    private TextField brandField;

    @FXML
    private TextField modelField;

    @FXML
    private ComboBox<EquipmentType> typeBox;

    @FXML
    private ComboBox<EquipmentStatus> stateBox;

    @FXML
    private ComboBox<Provider> providerBox;

    @FXML
    private TextField osField;

    @FXML
    private TextField ramField;

    @FXML
    private TextField imageField;

    private JDBCTechEquipmentRepository repository;
    private JDBCProviderRepository providerRepository;

    public TechEquipmentController() {
        // Constructor vacío requerido por JavaFX
    }

    public void initialize() {
        // Inicializar repositorios
        repository = new JDBCTechEquipmentRepository(DatabaseConnection.getInstance());
        providerRepository = new JDBCProviderRepository(DatabaseConnection.getInstance());

        // Cargar enums en ComboBox
        typeBox.getItems().addAll(EquipmentType.values());
        stateBox.getItems().addAll(EquipmentStatus.values());

        // Cargar proveedores
        loadProviders();
    }

    private void loadProviders() {
        try {
            providerBox.getItems().clear();
            providerBox.getItems().addAll(providerRepository.findAll());

            // Configurar visualización del proveedor en el ComboBox
            providerBox.setButtonCell(new javafx.scene.control.ListCell<Provider>() {
                @Override
                protected void updateItem(Provider item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.getName());
                    }
                }
            });

            providerBox.setCellFactory(lv -> new javafx.scene.control.ListCell<Provider>() {
                @Override
                protected void updateItem(Provider item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.getName());
                    }
                }
            });

        } catch (Exception e) {
            showError("Error al cargar proveedores", e.getMessage());
        }
    }

    // ------------------------
    // GUARDAR
    // ------------------------
    @FXML
    private void saveTech() {
        try {
            // Validaciones
            if (!validateFields()) {
                return;
            }

            // Validar RAM (debe ser número)
            int ram;
            try {
                ram = Integer.parseInt(ramField.getText().trim());
                if (ram <= 0) {
                    showWarning("Valor inválido", "La memoria RAM debe ser un número mayor a 0");
                    return;
                }
            } catch (NumberFormatException e) {
                showWarning("Formato inválido", "La memoria RAM debe ser un número entero");
                return;
            }

            TechEquipment equipment = new TechEquipment(
                    serialField.getText().trim(),
                    brandField.getText().trim(),
                    modelField.getText().trim(),
                    typeBox.getValue(),
                    stateBox.getValue(),
                    providerBox.getValue(),
                    imageField.getText().trim(),
                    osField.getText().trim(),
                    ram);

            TechEquipment saved = repository.create(equipment);

            // Alerta de éxito
            showInfo("✓ Registro Exitoso",
                    "El equipo tecnológico se ha registrado correctamente.\n\n" +
                            "ID generado: " + saved.getId());

            clearFields();

        } catch (Exception e) {
            // Alerta de error
            showError("✗ Error al Guardar",
                    "No se pudo registrar el equipo tecnológico.\n\n" +
                            "Detalle: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ------------------------
    // CANCELAR
    // ------------------------
    @FXML
    private void cancel() {
        clearFields();
        showInfo("Cancelado", "Los campos han sido limpiados");
    }

    // ------------------------
    // VOLVER AL MENÚ PRINCIPAL
    // ------------------------
    @FXML
    private void goToMainMenu(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/views/MainView.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(
                getClass().getResource("/css/mainmenu.css").toExternalForm());

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    // ------------------------
    // MÉTODOS AUXILIARES
    // ------------------------
    private boolean validateFields() {
        if (serialField.getText().trim().isEmpty()) {
            showWarning("Campo requerido", "El campo Serial es obligatorio");
            return false;
        }
        if (brandField.getText().trim().isEmpty()) {
            showWarning("Campo requerido", "El campo Marca es obligatorio");
            return false;
        }
        if (modelField.getText().trim().isEmpty()) {
            showWarning("Campo requerido", "El campo Modelo es obligatorio");
            return false;
        }
        if (typeBox.getValue() == null) {
            showWarning("Campo requerido", "Debe seleccionar un Tipo");
            return false;
        }
        if (stateBox.getValue() == null) {
            showWarning("Campo requerido", "Debe seleccionar un Estado");
            return false;
        }
        if (providerBox.getValue() == null) {
            showWarning("Campo requerido", "Debe seleccionar un Proveedor");
            return false;
        }
        if (osField.getText().trim().isEmpty()) {
            showWarning("Campo requerido", "El campo Sistema Operativo es obligatorio");
            return false;
        }
        if (ramField.getText().trim().isEmpty()) {
            showWarning("Campo requerido", "El campo Memoria RAM es obligatorio");
            return false;
        }
        return true;
    }

    private void clearFields() {
        serialField.clear();
        brandField.clear();
        modelField.clear();
        typeBox.getSelectionModel().clearSelection();
        stateBox.getSelectionModel().clearSelection();
        providerBox.getSelectionModel().clearSelection();
        osField.clear();
        ramField.clear();
        imageField.clear();
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showWarning(String title, String message) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}