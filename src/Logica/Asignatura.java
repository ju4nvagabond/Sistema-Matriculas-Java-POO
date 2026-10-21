package Logica;

public class Asignatura {
    private String codigo;
    private String nombre;
    private int cuposTotales;
    private int cuposRestantes;

    public Asignatura(String codigo, String nombre, int cuposTotales) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.cuposTotales = cuposTotales;
        this.cuposRestantes = cuposTotales; 
    }

    // Getters y Setters
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getCuposTotales() { return cuposTotales; }
    public void setCuposTotales(int cuposTotales) { this.cuposTotales = cuposTotales; }

    public int getCuposRestantes() { return cuposRestantes; }
    public void setCuposRestantes(int cuposRestantes) { this.cuposRestantes = cuposRestantes; }

    // Métodos para la gestión de cupos
    public boolean descontarCupo() {
        if (this.cuposRestantes > 0) {
            this.cuposRestantes--;
            return true;
        }
        return false;
    }

    public String getEstado() {
        return (this.cuposRestantes > 0) ? "DISPONIBLE" : "SIN CUPO";
    }
}