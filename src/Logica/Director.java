package Logica;

public class Director extends Usuario {

    public Director(String id, String nombre) {
        super(id, nombre, "DIRECTOR");
    }

    // El director interactúa directamente con el Gestor de Datos
    public boolean agregarMateria(String codigo, String nombre, int cupos) {
        return GestorDatos.agregarMateria(codigo, nombre, cupos);
    }

    public boolean eliminarMateria(String codigo) {
        return GestorDatos.eliminarMateria(codigo);
    }
}
