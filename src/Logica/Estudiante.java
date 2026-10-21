package Logica;

public class Estudiante extends Usuario {

    public Estudiante(String id, String nombre) {
        super(id, nombre, "ESTUDIANTE");
    }

    public boolean matricularMateria(String codigoMateria) {
        return GestorDatos.matricularMateriaEstudiante(this.nombre, codigoMateria);
    }

    public boolean cancelarMateria(String codigoMateria) {
        return GestorDatos.cancelarMateriaEstudiante(this.nombre, codigoMateria);
    }

    public boolean yaEstaMatriculado(String codigoMateria) {
        return GestorDatos.yaEstaMatriculado(this.nombre, codigoMateria);
    }
}