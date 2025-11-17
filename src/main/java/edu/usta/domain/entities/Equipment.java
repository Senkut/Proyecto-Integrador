package edu.usta.domain.entities;

import edu.usta.domain.enums.EquipmentStatus;
import edu.usta.domain.enums.EquipmentType;

/**
 * La clase Equipment representa un objeto de tipo Equipo.
 * Esta clase sera reconocida como la clase padre, que tiene
 * por hijas a las clases TechEquipment y BiomedicalEquipmet
 * 
 * Adicional, a este tipo de clase se le conoce como POJO que
 * significa Plain Old Java Object (objeto plano de Java),
 * ya que solo posee atributos constructores,
 * getters and setteers, y el metodo toString()
 * 
 */

public class Equipment {
    /**
     * Attributes:
     * Los atributos seran privados, porque solo se pueden
     * modificar dentro de la clase. Si los intentamos actualizar
     * una creada la instancia, debemos utilizar los metodos getters and
     * setters, por ejemplo: El atributo serial es el privado por lo
     * tanto, solo puede actualizar su valor dentro del codigo de
     * la clase, o utlizando la funcion setSerial(), enviando
     * como parametro el nuevo valor.
     */

    private String id;
    private String serial;
    private String brand;
    private String model;
    private EquipmentType type;
    private EquipmentStatus state;
    private Provider provider;
    private String imagePath;

    /**
     * Constructors:
     * 
     * Los constructores permiten crear una instancia u objeto
     * de la clase, por defecto se genera el constrctor vacio,
     * pero podemos personalizarlo asignando valores por defecto
     * dentro del mismo.
     * 
     * Tambien, podemos hacer sobrecarga de constructores,para
     * aceptar ciertos valores al momento de crear la instancia y
     * asiganarlos como valor inicial (inicializar) a los atributos
     * de la clase.
     */

    /**
     * Constructor con parámetros SIN id
     * Genera automáticamente el id usando UUIDGenerator
     * 
     * @param serial    - Registro serial del equipo
     * @param brand     - Marca del equipo
     * @param model     - Modelo del equipo
     * @param type      - Tipo del equipo
     * @param state     - Estado del equipo
     * @param provider  - Proveedor asociado
     * @param imagePath - Ruta de la imagen cargada
     */
    public Equipment(String serial, String brand, String model, EquipmentType type, EquipmentStatus state,
            Provider provider, String imagePath) {

        this.serial = serial;
        this.brand = brand;
        this.model = model;
        this.type = type;
        this.state = state;
        this.provider = provider;
        this.imagePath = imagePath;
    }

    /**
     * Constructor con todos los parámetros, incluyendo el id
     * 
     * @param id        - Identificador único del equipo
     * @param serial    - Registro serial del equipo
     * @param brand     - Marca del equipo
     * @param model     - Modelo del equipo
     * @param type      - Tipo del equipo
     * @param state     - Estado del equipo
     * @param provider  - Proveedor asociado
     * @param imagePath - Ruta de la imagen cargada
     */
    public Equipment(String id, String serial, String brand, String model, EquipmentType type, EquipmentStatus state,
            Provider provider, String imagePath) {
        this.id = id;
        this.serial = serial;
        this.brand = brand;
        this.model = model;
        this.type = type;
        this.state = state;
        this.provider = provider;
        this.imagePath = imagePath;
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
     * Metodo GET que retorna el valor almacenado del Id
     * 
     * @return - String con el valor del Id
     */
    public String getId() {
        return id;
    }

    /**
     * Metodo GET que retorna el valor Serial
     * 
     * @return - String con el valor del serial
     */
    public String getSerial() {
        return serial;
    }

    /**
     * Metodo SET que modifica o actualiza el valor almacenado del atributo serial
     * 
     * @param serial - String con el nuevo valor del serial
     */
    public void setSerial(String serial) {
        this.serial = serial;
    }

    /**
     * Metodo GET que retorna el valor del atributo brand
     * 
     * @return - String con el valor del brand
     */
    public String getBrand() {
        return brand;
    }

    /**
     * Metodo SET que modifica o actualiza el valor almacenado del atributo brand
     * 
     * @param brand - String con el nuevo valor del atributo brand
     */
    public void setBrand(String brand) {
        this.brand = brand;
    }

    /**
     * Metodo GET que retorna el valor del atributo model
     * 
     * @return - String con el valor del atributo model
     */
    public String getModel() {
        return model;
    }

    /**
     * Metodo SET que modifica o actualiza el valor almacenado del atributo Model
     * 
     * @param model - String con el nuevo valor del atributo model
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * Metodo GET que retorna el valor del atributo type
     * 
     * @return - String con el valor del atributo type
     */
    public EquipmentType getType() {
        return type;
    }

    /**
     * Metodo SET que modifica o actualiza el valor almacenado del atributo Type
     * 
     * @param type - String con el nuevo valor para el atributo type
     */

    public void setType(EquipmentType type) {
        this.type = type;
    }

    /**
     * Metodo GET que retorna el valor del atributo state
     * 
     * @return - String con el valor del atributo state
     */

    public EquipmentStatus getState() {
        return state;
    }

    /**
     * Metodo SET que modifica o actualiza el valor almacenado del atributo State
     * 
     * @param state - String con el nuevo valor para el atributo state
     */

    public void setState(EquipmentStatus state) {
        this.state = state;
    }

    /**
     * Metodo GET que retorna el valor del atributo provider
     * 
     * @return - String con el valor del atributo provider
     */

    public Provider getProvider() {
        return provider;
    }

    /**
     * Metodo SET que modifica o actualiza el valor almacenado del atributo provider
     * 
     * @param provider - String con el nuevo valor del atributo provider
     */

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    /**
     * Metodo GET que retorna el valor del atributo imagenpath
     * 
     * @return - String con el valor del atributo imagenpath
     */

    public String getImagePath() {
        return imagePath;
    }

    /**
     * Metodo SET que modifica o actualiza el valor almacenado del atributo
     * imagenPath
     * 
     * @param imagePath - String con el nuevo valor para el atributo imagePath
     */

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
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
        return "Equipment [id=" + id + ", serial=" + serial + ", brand=" + brand + ",model=" + model + ", type=" + type
                + ", state=" + state + ", provider=" + provider + ", imagePath=" + imagePath + "]";
    }
}
