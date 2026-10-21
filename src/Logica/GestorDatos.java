package Logica;

public class GestorDatos {

    // Matriz de 50 filas x 5 columnas para materias generales
    // Columnas: [0] Código | [1] Nombre | [2] Cupos Totales | [3] Cupos Restantes | [4] Estado
    private static Object[][] matrizMaterias = new Object[50][5];
    private static int contadorMaterias = 0;

    // Matriz de 100 filas x 3 columnas para registros de matrículas
    // Columnas: [0] ID Estudiante | [1] Código Materia | [2] Nombre Materia
    private static Object[][] matrizMatriculasEstudiantes = new Object[100][3];
    private static int contadorMatriculas = 0;

    // Carga inicial
    static {
        agregarMateria("MAT101", "Matemáticas", 30);
        agregarMateria("ING101", "Inglés I", 25);
        agregarMateria("EMP101", "Emprendimiento", 20);
        agregarMateria("RED101", "Redes", 15);
    }

    // FUNCIONES DEL DIRECTOR 

    public static boolean agregarMateria(String codigo, String nombre, int cuposTotales) {
        for (int i = 0; i < contadorMaterias; i++) {
            if (matrizMaterias[i][0] != null && matrizMaterias[i][0].toString().equalsIgnoreCase(codigo)) {
                return false; // Código repetido
            }
        }

        if (contadorMaterias < matrizMaterias.length) {
            matrizMaterias[contadorMaterias][0] = codigo;
            matrizMaterias[contadorMaterias][1] = nombre;
            matrizMaterias[contadorMaterias][2] = cuposTotales;
            matrizMaterias[contadorMaterias][3] = cuposTotales;
            matrizMaterias[contadorMaterias][4] = (cuposTotales > 0) ? "DISPONIBLE" : "SIN CUPO";
            contadorMaterias++;
            return true;
        }
        return false;
    }

    public static boolean eliminarMateria(String codigo) {
        for (int i = 0; i < contadorMaterias; i++) {
            if (matrizMaterias[i][0] != null && matrizMaterias[i][0].toString().equalsIgnoreCase(codigo)) {
                for (int j = i; j < contadorMaterias - 1; j++) {
                    matrizMaterias[j] = matrizMaterias[j + 1];
                }
                matrizMaterias[contadorMaterias - 1] = new Object[5];
                contadorMaterias--;
                return true;
            }
        }
        return false;
    }

    // FUNCIONES DEL ESTUDIANTE 

    // Valida si el estudiante YA matriculó esa materia
    public static boolean yaEstaMatriculado(String estudianteId, String codigoMateria) {
        for (int i = 0; i < contadorMatriculas; i++) {
            if (matrizMatriculasEstudiantes[i][0] != null &&
                matrizMatriculasEstudiantes[i][0].toString().equalsIgnoreCase(estudianteId) &&
                matrizMatriculasEstudiantes[i][1].toString().equalsIgnoreCase(codigoMateria)) {
                return true; // Ya está matriculado
            }
        }
        return false;
    }

    // Matricular materia
    public static boolean matricularMateriaEstudiante(String estudianteId, String codigoMateria) {
        // 1. Validar si ya la inscribió previamente
        if (yaEstaMatriculado(estudianteId, codigoMateria)) {
            return false; 
        }

        //  Buscar materia y descontar cupo
        for (int i = 0; i < contadorMaterias; i++) {
            if (matrizMaterias[i][0] != null && matrizMaterias[i][0].toString().equalsIgnoreCase(codigoMateria)) {
                int restantes = Integer.parseInt(matrizMaterias[i][3].toString());
                if (restantes > 0) {
                    restantes--;
                    matrizMaterias[i][3] = restantes;
                    if (restantes == 0) {
                        matrizMaterias[i][4] = "SIN CUPO";
                    }

                    // Guardar registro del estudiante
                    matrizMatriculasEstudiantes[contadorMatriculas][0] = estudianteId;
                    matrizMatriculasEstudiantes[contadorMatriculas][1] = codigoMateria;
                    matrizMatriculasEstudiantes[contadorMatriculas][2] = matrizMaterias[i][1].toString(); // Nombre
                    contadorMatriculas++;

                    return true;
                }
            }
        }
        return false;
    }

    // Cancelar/Eliminar materia (Devuelve el cupo)
    public static boolean cancelarMateriaEstudiante(String estudianteId, String codigoMateria) {
        boolean registroEncontrado = false;

        //  Quitar de la matriz del estudiante
        for (int i = 0; i < contadorMatriculas; i++) {
            if (matrizMatriculasEstudiantes[i][0] != null &&
                matrizMatriculasEstudiantes[i][0].toString().equalsIgnoreCase(estudianteId) &&
                matrizMatriculasEstudiantes[i][1].toString().equalsIgnoreCase(codigoMateria)) {

                for (int j = i; j < contadorMatriculas - 1; j++) {
                    matrizMatriculasEstudiantes[j] = matrizMatriculasEstudiantes[j + 1];
                }
                matrizMatriculasEstudiantes[contadorMatriculas - 1] = new Object[3];
                contadorMatriculas--;
                registroEncontrado = true;
                break;
            }
        }

        //  Si se quitó, le devolvemos 1 cupo a la materia general
        if (registroEncontrado) {
            for (int i = 0; i < contadorMaterias; i++) {
                if (matrizMaterias[i][0] != null && matrizMaterias[i][0].toString().equalsIgnoreCase(codigoMateria)) {
                    int restantes = Integer.parseInt(matrizMaterias[i][3].toString());
                    restantes++;
                    matrizMaterias[i][3] = restantes;
                    matrizMaterias[i][4] = "DISPONIBLE";
                    break;
                }
            }
            return true;
        }

        return false;
    }

    // Obtener las materias inscritas por un estudiante específico
    public static Object[][] getMateriasInscritasEstudiante(String estudianteId) {
        int conteoPropio = 0;
        for (int i = 0; i < contadorMatriculas; i++) {
            if (matrizMatriculasEstudiantes[i][0] != null &&
                matrizMatriculasEstudiantes[i][0].toString().equalsIgnoreCase(estudianteId)) {
                conteoPropio++;
            }
        }

        Object[][] resultado = new Object[conteoPropio][2]; // [0] Código | [1] Nombre
        int pos = 0;
        for (int i = 0; i < contadorMatriculas; i++) {
            if (matrizMatriculasEstudiantes[i][0] != null &&
                matrizMatriculasEstudiantes[i][0].toString().equalsIgnoreCase(estudianteId)) {
                resultado[pos][0] = matrizMatriculasEstudiantes[i][1]; // Código
                resultado[pos][1] = matrizMatriculasEstudiantes[i][2]; // Nombre
                pos++;
            }
        }

        return resultado;
    }

    public static Object[][] getMatrizMaterias() {
        Object[][] matrizReducida = new Object[contadorMaterias][5];
        for (int i = 0; i < contadorMaterias; i++) {
            matrizReducida[i] = matrizMaterias[i];
        }
        return matrizReducida;
    }
}