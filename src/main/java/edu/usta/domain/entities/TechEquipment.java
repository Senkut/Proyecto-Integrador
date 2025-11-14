package edu.usta.domain.entities;

import edu.usta.domain.enums.EquipmentStatus;
import edu.usta.domain.enums.EquipmentType;

public class TechEquipment extends Equipment {
    private String os;
    private int ramGb;

    // Constructor Parametros
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
     * @param os
     * @param ramGB
     */
    public TechEquipment(String serial, String brand, String model, EquipmentType type, EquipmentStatus state,
            Provider provider, String imagePath, String os, int ramGB) {
        super(serial, brand, model, type, state, provider, imagePath);
        this.os = os;
        this.ramGb = ramGB;
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
     * @param os
     * @param ramGB
     */
    public TechEquipment(String id, String serial, String brand, String model, EquipmentType type,
            EquipmentStatus state,
            Provider provider, String imagePath, String os, int ramGB) {
        super(id, serial, brand, model, type, state, provider, imagePath);
        this.os = os;
        this.ramGb = ramGB;
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
     * Metodo GET que retorna el valor del atributo Os
     * 
     * @return - String con el valor del atributo Os
     */
    public String getOs() {
        return os;
    }

    /**
     * Metodo SET que modifica o actualiza el valor almacenado del atributo Os
     * 
     * @param os - String con el nuevo valor para el atributo Os
     */
    public void setOs(String os) {
        this.os = os;
    }

    /**
     * Metodo GET que retorna el valor del atributo RamGb
     * 
     * @return - String con el valor del atributo RamGb
     */
    public int getRamGb() {
        return ramGb;
    }

    /**
     * Metodo SET que modifica o actualiza el valor almacenado almacenado del
     * atributo RamGb
     * 
     * @param ramGb - String con el nuevo valor del atributo ramGb
     */
    public void setRamGb(int ramGb) {
        this.ramGb = ramGb;
    }

    @Override
    public String toString() {
        return "TechEquipment " + "os = " + os + ", ramGb = " + ramGb;
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
}
