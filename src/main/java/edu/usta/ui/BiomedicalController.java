package edu.usta.ui;

import java.io.IOException;
import java.util.UUID;

import edu.usta.domain.entities.BiomedicalEquipment;
import edu.usta.domain.entities.Provider;
import edu.usta.domain.enums.EquipmentStatus;
import edu.usta.domain.enums.EquipmentType;
import edu.usta.domain.repositories.JDBCBiomedicalEquipmentRepository;
import edu.usta.domain.repositories.JDBCProviderRepository;
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

public class BiomedicalController {

    @FXML
    private TextField serialField;

    @FXML
    private TextField brandField;

    @FXML
    private TextField modelField;

    @FXML
    private ComboBox<EquipmentType> typeField;

    @FXML
    private ComboBox<EquipmentStatus> stateField;

    @FXML
    private ComboBox<Provider> providerField;

    @FXML
    private ComboBox<String> riskClassField;

    @FXML
    private TextField calibrationCertField;

    @FXML
    private TextField imagePathField;

    private JDBCBiomedicalEquipmentRepository repository;
    private JDBCProviderRepository providerRepository;

    public BiomedicalController() {
        // JavaFX exige constructor vacío
    }

    public void initialize() {
        // Crear repositories
        repository = new JDBCBiomedicalEquipmentRepository(DatabaseConnection.getInstance());
        providerRepository = new JDBCProviderRepository(DatabaseConnection.getInstance());

        // Cargar enums en los ComboBox
        typeField.getItems().addAll(EquipmentType.values());
        stateField.getItems().addAll(EquipmentStatus.values());

        // Cargar opciones de clase de riesgo
        riskClassField.getItems().addAll("Clase I", "Clase IIa", "Clase IIb", "Clase III");

        // Cargar proveedores desde la base de datos
        loadProviders();
    }

    private void loadProviders() {
        try {
            providerField.getItems().clear();
            providerField.getItems().addAll(providerRepository.findAll());

            // Configurar cómo se muestra el proveedor en el ComboBox
            providerField.setButtonCell(new javafx.scene.control.ListCell<Provider>() {
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

            providerField.setCellFactory(lv -> new javafx.scene.control.ListCell<Provider>() {
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
    private void onGuardar() {
        try {
            // Validaciones
            if (!validateFields()) {
                return;
            }

            BiomedicalEquipment equipment = new BiomedicalEquipment(
                    serialField.getText().trim(),
                    brandField.getText().trim(),
                    modelField.getText().trim(),
                    typeField.getValue(),
                    stateField.getValue(),
                    providerField.getValue(),
                    imagePathField.getText().trim(),
                    riskClassField.getValue(),
                    calibrationCertField.getText().trim());

            BiomedicalEquipment saved = repository.create(equipment);

            // Alerta de éxito
            showInfo("✓ Registro Exitoso",
                    "El equipo biomédico se ha registrado correctamente.\n\n" +
                            "ID generado: " + saved.getId());

            clearFields();

        } catch (Exception e) {
            // Alerta de error
            showError("✗ Error al Guardar",
                    "No se pudo registrar el equipo biomédico.\n\n" +
                            "Detalle: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ------------------------
    // BUSCAR POR ID
    // ------------------------
    @FXML
    private void onBuscar() {
        try {
            String idText = serialField.getText().trim();
            if (idText.isEmpty()) {
                showWarning("Campo vacío", "Ingrese un ID en el campo Serial para buscar");
                return;
            }

            UUID id = UUID.fromString(idText);
            var found = repository.findById(id);

            if (found.isPresent()) {
                BiomedicalEquipment e = found.get();

                serialField.setText(e.getSerial());
                brandField.setText(e.getBrand());
                modelField.setText(e.getModel());

                typeField.setValue(e.getType());
                stateField.setValue(e.getState());
                providerField.setValue(e.getProvider());

                riskClassField.setValue(e.getRiskClass());
                calibrationCertField.setText(e.getCalibrationCert());
                imagePathField.setText(e.getImagePath());

                showInfo("Encontrado", "Equipo cargado correctamente");
            } else {
                showWarning("No encontrado", "No existe un equipo con ese ID");
            }

        } catch (IllegalArgumentException e) {
            showError("ID inválido", "El formato del ID no es válido");
        } catch (Exception e) {
            showError("Error al buscar", e.getMessage());
            e.printStackTrace();
        }
    }

    // ------------------------
    // ACTUALIZAR
    // ------------------------
    @FXML
    private void onActualizar() {
        try {
            String idText = serialField.getText().trim();
            if (idText.isEmpty()) {
                showWarning("Campo vacío", "Ingrese un ID en el campo Serial para actualizar");
                return;
            }

            if (!validateFields()) {
                return;
            }

            UUID id = UUID.fromString(idText);

            BiomedicalEquipment updated = new BiomedicalEquipment(
                    id.toString(),
                    serialField.getText().trim(),
                    brandField.getText().trim(),
                    modelField.getText().trim(),
                    typeField.getValue(),
                    stateField.getValue(),
                    providerField.getValue(),
                    imagePathField.getText().trim(),
                    riskClassField.getValue(),
                    calibrationCertField.getText().trim());

            repository.update(updated);
            showInfo("Éxito", "Equipo actualizado correctamente");

        } catch (IllegalArgumentException e) {
            showError("ID inválido", "El formato del ID no es válido");
        } catch (Exception e) {
            showError("Error al actualizar", e.getMessage());
            e.printStackTrace();
        }
    }

    // ------------------------
    // ELIMINAR
    // ------------------------
    @FXML
    private void onEliminar() {
        try {
            String idText = serialField.getText().trim();
            if (idText.isEmpty()) {
                showWarning("Campo vacío", "Ingrese un ID en el campo Serial para eliminar");
                return;
            }

            UUID id = UUID.fromString(idText);
            boolean deleted = repository.delete(id);

            if (deleted) {
                showInfo("Éxito", "Equipo eliminado correctamente");
                clearFields();
            } else {
                showWarning("No encontrado", "No se encontró el equipo para eliminar");
            }

        } catch (IllegalArgumentException e) {
            showError("ID inválido", "El formato del ID no es válido");
        } catch (Exception e) {
            showError("Error al eliminar", e.getMessage());
            e.printStackTrace();
        }
    }

    // ------------------------
    // CANCELAR
    // ------------------------
    @FXML
    private void onCancelar() {
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
        if (typeField.getValue() == null) {
            showWarning("Campo requerido", "Debe seleccionar un Tipo");
            return false;
        }
        if (stateField.getValue() == null) {
            showWarning("Campo requerido", "Debe seleccionar un Estado");
            return false;
        }
        if (providerField.getValue() == null) {
            showWarning("Campo requerido", "Debe seleccionar un Proveedor");
            return false;
        }
        if (riskClassField.getValue() == null) {
            showWarning("Campo requerido", "Debe seleccionar una Clase de Riesgo");
            return false;
        }
        return true;
    }

    private void clearFields() {
        serialField.clear();
        brandField.clear();
        modelField.clear();
        typeField.getSelectionModel().clearSelection();
        stateField.getSelectionModel().clearSelection();
        providerField.getSelectionModel().clearSelection();
        riskClassField.getSelectionModel().clearSelection();
        calibrationCertField.clear();
        imagePathField.clear();
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