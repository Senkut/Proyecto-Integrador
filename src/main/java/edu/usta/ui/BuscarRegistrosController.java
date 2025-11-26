package edu.usta.ui;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class BuscarRegistrosController {

    @FXML
    private ComboBox<String> typeSelector;

    @FXML
    private TextField searchField;

    @FXML
    private TableView<Object> resultTable;

    @FXML
    public void initialize() {
        typeSelector.setItems(FXCollections.observableArrayList(
                "Equipos Tecnológicos",
                "Equipos Biomédicos",
                "Personas",
                "Proveedores"));
    }

    @FXML
    private void search() {
        String type = typeSelector.getValue();
        String query = searchField.getText().trim();

        if (type == null || query.isEmpty()) {
            System.out.println("Por favor selecciona tipo y escribe un criterio.");
            return;
        }

        switch (type) {
            case "Equipos Tecnológicos":
                loadResults(searchTechEquipment(query));
                break;
            case "Equipos Biomédicos":
                loadResults(searchBiomedical(query));
                break;
            case "Personas":
                loadResults(searchPersons(query));
                break;
            case "Proveedores":
                loadResults(searchProviders(query));
                break;
        }
    }

    private void loadResults(ObservableList<Object> items) {
        resultTable.setItems(items);
    }

    /** -------------- CONSULTAS PARA CADA TIPO --------------- */

    private ObservableList<Object> searchTechEquipment(String q) {
        // Aquí llamas tu repository / DAO
        // Ejemplo:
        // return FXCollections.observableArrayList(techRepo.search(q));
        return FXCollections.observableArrayList();
    }

    private ObservableList<Object> searchBiomedical(String q) {
        return FXCollections.observableArrayList();
    }

    private ObservableList<Object> searchPersons(String q) {
        return FXCollections.observableArrayList();
    }

    private ObservableList<Object> searchProviders(String q) {
        return FXCollections.observableArrayList();
    }
}
