package edu.usta;

/**
 * Clase Launcher para ejecutar JavaFX sin problemas de módulos
 * 
 * ¿Por qué necesitamos esto?
 * Cuando ejecutas directamente una clase que extiende Application,
 * Java intenta cargar los módulos de JavaFX DESPUÉS de iniciar el main(),
 * lo que causa errores.
 * 
 * Esta clase "tramposa" NO extiende Application, entonces Java
 * carga los módulos correctamente ANTES de llamar a Main.
 */
public class Launcher {

    public static void main(String[] args) {
        // Simplemente llama al main de la clase Main
        // Esto permite que JavaFX se cargue correctamente
        Main.main(args);
    }
}