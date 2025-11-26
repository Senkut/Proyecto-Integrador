package edu.usta.ui;

import java.io.IOException;
import java.util.List;

import edu.usta.domain.entities.BiomedicalEquipment;
import edu.usta.domain.entities.Person;
import edu.usta.domain.entities.Provider;
import edu.usta.domain.entities.TechEquipment;
import edu.usta.domain.repositories.JDBCBiomedicalEquipmentRepository;
import edu.usta.domain.repositories.JDBCPersonRepository;
import edu.usta.domain.repositories.JDBCProviderRepository;
import edu.usta.domain.repositories.JDBCTechEquipmentRepository;
import edu.usta.infrastructure.db.DatabaseConnection;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class BuscarRegistrosController {

    @FXML
    private ComboBox<String> typeSelector;

    @FXML
    private TextField searchField;

    @FXML
    private TableView<SearchResult> resultTable;

    @FXML
    private TableColumn<SearchResult, String> colId;

    @FXML
    private TableColumn<SearchResult, String> colName;

    @FXML
    private TableColumn<SearchResult, String> colDetail;

    @FXML
    private TableColumn<SearchResult, String> colExtra;

    @FXML
    private TableColumn<SearchResult, String> colType;

    @FXML
    private Label resultCountLabel;

    // Repositorios
    private JDBCTechEquipmentRepository techRepo;
    private JDBCBiomedicalEquipmentRepository bioRepo;
    private JDBCPersonRepository personRepo;
    private JDBCProviderRepository providerRepo;

    @FXML
    public void initialize() {
        // Inicializar repositorios
        techRepo = new JDBCTechEquipmentRepository(DatabaseConnection.getInstance());
        bioRepo = new JDBCBiomedicalEquipmentRepository(DatabaseConnection.getInstance());
        personRepo = new JDBCPersonRepository(DatabaseConnection.getInstance());
        providerRepo = new JDBCProviderRepository(DatabaseConnection.getInstance());

        // Configurar ComboBox
        typeSelector.setItems(FXCollections.observableArrayList(
                "Equipos Tecnológicos",
                "Equipos Biomédicos",
                "Personas",
                "Proveedores",
                "Todos"));

        // Configurar columnas de la tabla
        colId.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getId()));
        colName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        colDetail.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDetail()));
        colExtra.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getExtra()));
        colType.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getType()));

        // Inicializar contador
        resultCountLabel.setText("0 registros encontrados");
    }

    // ------------------------
    // BUSCAR
    // ------------------------
    @FXML
    private void search() {
        String type = typeSelector.getValue();
        String query = searchField.getText().trim();

        if (type == null || type.isEmpty()) {
            showWarning("Selección requerida", "Por favor seleccione una categoría para buscar");
            return;
        }

        ObservableList<SearchResult> results = FXCollections.observableArrayList();

        try {
            switch (type) {
                case "Equipos Tecnológicos":
                    results = searchTechEquipment(query);
                    break;
                case "Equipos Biomédicos":
                    results = searchBiomedical(query);
                    break;
                case "Personas":
                    results = searchPersons(query);
                    break;
                case "Proveedores":
                    results = searchProviders(query);
                    break;
                case "Todos":
                    results = searchAll(query);
                    break;
            }

            resultTable.setItems(results);
            resultCountLabel.setText(results.size() + " registro(s) encontrado(s)");

            if (results.isEmpty()) {
                showInfo("Sin resultados", "No se encontraron registros que coincidan con la búsqueda");
            }

        } catch (Exception e) {
            showError("Error en la búsqueda",
                    "Ocurrió un error al buscar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ------------------------
    // BÚSQUEDAS POR TIPO
    // ------------------------
    private ObservableList<SearchResult> searchTechEquipment(String query) {
        ObservableList<SearchResult> results = FXCollections.observableArrayList();

        List<TechEquipment> allEquipment = techRepo.findAll();

        for (TechEquipment eq : allEquipment) {
            // Buscar por ID, serial, marca, modelo
            if (query.isEmpty() ||
                    eq.getId().toString().toLowerCase().contains(query.toLowerCase()) ||
                    eq.getSerial().toLowerCase().contains(query.toLowerCase()) ||
                    eq.getBrand().toLowerCase().contains(query.toLowerCase()) ||
                    eq.getModel().toLowerCase().contains(query.toLowerCase())) {

                results.add(new SearchResult(
                        eq.getId().toString(),
                        eq.getSerial(),
                        eq.getBrand() + " - " + eq.getModel(),
                        "OS: " + eq.getOs() + " | RAM: " + eq.getRamGb() + "GB",
                        "Equipo Tecnológico"));
            }
        }

        return results;
    }

    private ObservableList<SearchResult> searchBiomedical(String query) {
        ObservableList<SearchResult> results = FXCollections.observableArrayList();

        List<BiomedicalEquipment> allEquipment = bioRepo.findAll();

        for (BiomedicalEquipment eq : allEquipment) {
            // Buscar por ID, serial, marca, modelo
            if (query.isEmpty() ||
                    eq.getId().toString().toLowerCase().contains(query.toLowerCase()) ||
                    eq.getSerial().toLowerCase().contains(query.toLowerCase()) ||
                    eq.getBrand().toLowerCase().contains(query.toLowerCase()) ||
                    eq.getModel().toLowerCase().contains(query.toLowerCase())) {

                results.add(new SearchResult(
                        eq.getId().toString(),
                        eq.getSerial(),
                        eq.getBrand() + " - " + eq.getModel(),
                        "Clase: " + eq.getRiskClass() + " | Cert: " + eq.getCalibrationCert(),
                        "Equipo Biomédico"));
            }
        }

        return results;
    }

    private ObservableList<SearchResult> searchPersons(String query) {
        ObservableList<SearchResult> results = FXCollections.observableArrayList();

        List<Person> allPersons = personRepo.findAll();

        for (Person p : allPersons) {
            // Buscar por ID, nombre, documento
            if (query.isEmpty() ||
                    p.getId().toString().toLowerCase().contains(query.toLowerCase()) ||
                    p.getFullname().toLowerCase().contains(query.toLowerCase()) ||
                    p.getDocument().toLowerCase().contains(query.toLowerCase())) {

                results.add(new SearchResult(
                        p.getId().toString(),
                        p.getFullname(),
                        "Documento: " + p.getDocument(),
                        "Rol: " + p.getRole(),
                        "Persona"));
            }
        }

        return results;
    }

    private ObservableList<SearchResult> searchProviders(String query) {
        ObservableList<SearchResult> results = FXCollections.observableArrayList();

        List<Provider> allProviders = providerRepo.findAll();

        for (Provider p : allProviders) {
            // Buscar por ID, nombre, tax ID, email
            if (query.isEmpty() ||
                    p.getId().toString().toLowerCase().contains(query.toLowerCase()) ||
                    p.getName().toLowerCase().contains(query.toLowerCase()) ||
                    p.getTaxId().toLowerCase().contains(query.toLowerCase()) ||
                    p.getContactEmail().toLowerCase().contains(query.toLowerCase())) {

                results.add(new SearchResult(
                        p.getId().toString(),
                        p.getName(),
                        "Tax ID: " + p.getTaxId(),
                        "Email: " + p.getContactEmail(),
                        "Proveedor"));
            }
        }

        return results;
    }

    private ObservableList<SearchResult> searchAll(String query) {
        ObservableList<SearchResult> results = FXCollections.observableArrayList();

        results.addAll(searchTechEquipment(query));
        results.addAll(searchBiomedical(query));
        results.addAll(searchPersons(query));
        results.addAll(searchProviders(query));

        return results;
    }

    // ------------------------
    // LIMPIAR BÚSQUEDA
    // ------------------------
    @FXML
    private void clearSearch() {
        searchField.clear();
        typeSelector.getSelectionModel().clearSelection();
        resultTable.getItems().clear();
        resultCountLabel.setText("0 registros encontrados");
        showInfo("Limpiado", "La búsqueda ha sido limpiada");
    }

    // ------------------------
    // VOLVER AL MENÚ
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

    // ------------------------
    // CLASE INTERNA PARA RESULTADOS
    // ------------------------
    public static class SearchResult {
        private final String id;
        private final String name;
        private final String detail;
        private final String extra;
        private final String type;

        public SearchResult(String id, String name, String detail, String extra, String type) {
            this.id = id;
            this.name = name;
            this.detail = detail;
            this.extra = extra;
            this.type = type;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getDetail() {
            return detail;
        }

        public String getExtra() {
            return extra;
        }

        public String getType() {
            return type;
        }
    }
}