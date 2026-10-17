import java.util.Scanner;

// Main: unica clase que usa Scanner y System.out
public class Main {
    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        Hospital hospital = new Hospital();
        boolean salir = false;

        while (!salir && teclado.hasNextLine()) {
            String linea = teclado.nextLine().trim();
            if (linea.isEmpty()) {
                continue; // ignora lineas vacias
            }

            String[] partes = linea.split("\\s+");
            String comando = partes[0].toLowerCase();

            if (comando.equals("salir")) {
                salir = true;
            } else if (comando.equals("ingresar")) {
                System.out.println(procesarIngreso(hospital, partes));
            } else if (comando.equals("vacunar") && partes.length == 2) {
                System.out.println(hospital.vacunar(partes[1]));
            } else if (comando.equals("asignar") && partes.length == 2) {
                System.out.println(hospital.asignar(partes[1]));
            } else if (comando.equals("medicar")) {
                if (partes.length == 3) {
                    System.out.println(hospital.medicar(partes[1], partes[2]));
                } else {
                    System.out.println("ERROR: datos invalidos");
                }
            } else if (comando.equals("alta") && partes.length == 2) {
                System.out.println(hospital.alta(partes[1]));
            } else if (comando.equals("reporte") && partes.length == 1) {
                System.out.println(hospital.reporte());
            } else {
                System.out.println("ERROR: comando desconocido");
            }
        }
        teclado.close();
    }

    // Decide que tipo de paciente se ingresa segun la segunda palabra
    private static String procesarIngreso(Hospital hospital, String[] partes) {
        if (partes.length < 2) {
            return "ERROR: comando desconocido";
        }
        String tipo = partes[1].toLowerCase();

        if (tipo.equals("perro")) {
            if (partes.length != 7) {
                return "ERROR: datos invalidos";
            }
            return hospital.ingresarPerro(partes[2], partes[3], partes[4], partes[5], partes[6]);
        }
        if (tipo.equals("gato")) {
            if (partes.length != 5) {
                return "ERROR: datos invalidos";
            }
            return hospital.ingresarGato(partes[2], partes[3], partes[4]);
        }
        if (tipo.equals("ave")) {
            if (partes.length != 6) {
                return "ERROR: datos invalidos";
            }
            return hospital.ingresarAve(partes[2], partes[3], partes[4], partes[5]);
        }
        return "ERROR: comando desconocido";
    }
}
