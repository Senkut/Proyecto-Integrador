package edu.usta.domain.entities;

import edu.usta.domain.enums.EquipmentStatus;
import edu.usta.domain.enums.EquipmentType;

public class BiomedicalEquipment extends Equipment {
    private String riskClass;
    private String calibrationCert;

    // Constructor de Parametros
    /**
     * Constructor sin Id
     * 
     * @param serial
     * @param brand
     * @param model
     * @param type
     * @param state
     * @param provider
     * @param imagePath
     * @param riskClass
     * @param calibrationCert
     */
    public BiomedicalEquipment(String serial, String brand, String model,
            EquipmentType type, EquipmentStatus state,
            Provider provider, String imagePath,
            String riskClass, String calibrationCert) {

        super(serial, brand, model, type, state, provider, imagePath);
        this.riskClass = riskClass;
        this.calibrationCert = calibrationCert;
    }

    /**
     * Constructor con Id
     * 
     * @param id
     * @param serial
     * @param brand
     * @param model
     * @param type
     * @param state
     * @param provider
     * @param imagePath
     * @param riskClass
     * @param calibrationCert
     */
    public BiomedicalEquipment(String id, String serial, String brand, String model,
            EquipmentType type, EquipmentStatus state,
            Provider provider, String imagePath,
            String riskClass, String calibrationCert) {

        super(id, serial, brand, model, type, state, provider, imagePath);
        this.riskClass = riskClass;
        this.calibrationCert = calibrationCert;
    }

    /**
     * Getters and Setters:
     * 
     * Los getters nos permiten consultar el valor de un atributo.
     * Los Setter nos permiten actualizar el valor de un atributo.
     * 
     * Ambos son muy utiles cuando trabajamos con atributos
     * cuya visibilidad sea private o protected.
     * 
     * 
     */

    /**
     * Metodo GET que retorna el valor riskClass.
     * 
     * @return - String con el valor de riskClass
     */
    public String getRiskClass() {
        return riskClass;
    }

    /**
     * Metodo SET que modifica o actualiza el valor almacenado
     * del atributo riskClass
     * 
     * @param riskClass - String con el nuevo valor del RiskClass
     */
    public void setRiskClass(String riskClass) {
        this.riskClass = riskClass;
    }

    /**
     * Metodo GET que retorna el valor del atributo calibrationCert
     * 
     * @return - String con el valor del atributo calibrationCert
     */
    public String getCalibrationCert() {
        return calibrationCert;
    }

    /**
     * Metodo SET que modifica o actualiza el valor almacenado
     * del atributo calibrationCert
     * 
     * @param calibrationCert - String con el nuevo valor del calibrationCert
     */
    public void setCalibrationCert(String calibrationCert) {
        this.calibrationCert = calibrationCert;
    }

    /**
     * El metodo toString, es un metodo que esta deinido por
     * Java para mostrar el identificador de la ubicacion en
     * memoria en la que se encuentra una instancia u objeto.
     * 
     * Usando el decorador @overrride para sobrescribir la
     * funcionalidad original del metodo, para lograr que se
     * pueda mostrar el contenido de la instancia de una manera
     * mas clara al momento de leerlo.
     */
    @Override
    public String toString() {
        return "BiomedicalEquipment " + " , riskClass = " + riskClass + " , calibrationCert = " + calibrationCert;
    }
}