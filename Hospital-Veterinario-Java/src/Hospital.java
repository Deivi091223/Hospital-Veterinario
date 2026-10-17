import java.util.Locale;

// Hospital: contiene toda la logica de gestion. No lee ni imprime: devuelve Strings.
public class Hospital {
    private static final double TOLERANCIA = 0.000001; // evita errores de redondeo en decimales

    private Paciente[] pacientes;
    private int contador;
    private Jaula[] jaulas;

    public Hospital() {
        pacientes = new Paciente[10];
        contador = 0;
        jaulas = new Jaula[6];
        cargarDatosIniciales();
    }

    // Datos con los que arranca el programa (todos sin jaula)
    public void cargarDatosIniciales() {
        jaulas[0] = new Jaula(1, "S", "A");
        jaulas[1] = new Jaula(2, "M", "A");
        jaulas[2] = new Jaula(3, "M", "A");
        jaulas[3] = new Jaula(4, "L", "A");
        jaulas[4] = new Jaula(5, "S", "B");
        jaulas[5] = new Jaula(6, "M", "B");

        agregar(new Perro("Rocky", 30.0, 4, "labrador", true));
        agregar(new Gato("Misifu", 4.0, 3, 0));
        agregar(new AveExotica("Kiwi", 0.8, 2, 70));
    }

    // ---------- INGRESAR (R1, R2, R3) ----------
    public String ingresarPerro(String nombre, String pesoTxt, String edadTxt, String raza, String vacunadoTxt) {
        double peso = leerDecimal(pesoTxt);
        int edad = leerEntero(edadTxt);
        boolean vacunadoValido = vacunadoTxt.equalsIgnoreCase("si") || vacunadoTxt.equalsIgnoreCase("no");

        if (!datosBasicosValidos(nombre, peso, edad, Perro.PESO_MAXIMO) || !vacunadoValido) {
            return "ERROR: datos invalidos";
        }
        String error = revisarDuplicadoYCupo(nombre);
        if (error != null) {
            return error;
        }

        boolean vacunado = vacunadoTxt.equalsIgnoreCase("si");
        agregar(new Perro(nombre, peso, edad, raza, vacunado));
        if (vacunado) {
            return "OK: " + nombre + " ingresado";
        }
        return "OK: " + nombre + " ingresado en CUARENTENA";
    }

    public String ingresarGato(String nombre, String pesoTxt, String edadTxt) {
        double peso = leerDecimal(pesoTxt);
        int edad = leerEntero(edadTxt);

        if (!datosBasicosValidos(nombre, peso, edad, Gato.PESO_MAXIMO)) {
            return "ERROR: datos invalidos";
        }
        String error = revisarDuplicadoYCupo(nombre);
        if (error != null) {
            return error;
        }

        agregar(new Gato(nombre, peso, edad, 0));
        return "OK: " + nombre + " ingresado";
    }

    public String ingresarAve(String nombre, String pesoTxt, String edadTxt, String envergaduraTxt) {
        double peso = leerDecimal(pesoTxt);
        int edad = leerEntero(edadTxt);
        double envergadura = leerDecimal(envergaduraTxt);

        if (!datosBasicosValidos(nombre, peso, edad, AveExotica.PESO_MAXIMO) || !(envergadura > 0)) {
            return "ERROR: datos invalidos";
        }
        String error = revisarDuplicadoYCupo(nombre);
        if (error != null) {
            return error;
        }

        agregar(new AveExotica(nombre, peso, edad, envergadura));
        return "OK: " + nombre + " ingresado";
    }

    // ---------- VACUNAR ----------
    public String vacunar(String nombre) {
        int pos = buscarIndice(nombre);
        if (pos == -1) {
            return "ERROR: paciente no encontrado";
        }
        Paciente p = pacientes[pos];
        if (!(p instanceof Perro)) {
            return "ERROR: solo aplica a perros";
        }
        Perro perro = (Perro) p;
        if (perro.isVacunado()) {
            return "ERROR: ya vacunado";
        }
        perro.vacunar();
        return "OK: " + perro.getNombre() + " vacunado (pasa a INGRESADO)";
    }

    // ---------- ASIGNAR (R4, R5, R6) ----------
    public String asignar(String nombre) {
        int pos = buscarIndice(nombre);
        if (pos == -1) {
            return "ERROR: paciente no encontrado";
        }
        Paciente p = pacientes[pos];
        if (p.getEstado().equals("CUARENTENA")) {
            return "ERROR: paciente en cuarentena";
        }
        if (p.getJaula() != null) {
            return "ERROR: paciente ya tiene jaula";
        }

        // Primera jaula libre que cumpla tamano y zona
        for (int i = 0; i < jaulas.length; i++) {
            Jaula j = jaulas[i];
            if (j.estaLibre()
                    && j.getTamano().equals(p.getTamanoRequerido())
                    && j.getZona().equals(p.getZonaRequerida())) {
                j.setOcupante(p);
                p.setJaula(j);
                return "OK: " + p.getNombre() + " asignado a jaula " + j.getNumero();
            }
        }
        return "ERROR: no hay jaula compatible";
    }

    // ---------- MEDICAR (R7, R8, R9) ----------
    public String medicar(String nombre, String mgTxt) {
        int pos = buscarIndice(nombre);
        if (pos == -1) {
            return "ERROR: paciente no encontrado";
        }
        Paciente p = pacientes[pos];

        double mg = leerDecimal(mgTxt);
        if (Double.isNaN(mg) || mg <= 0) {
            return "ERROR: datos invalidos";
        }
        if (p.getEstado().equals("CUARENTENA")) {
            return "ERROR: paciente en cuarentena";
        }
        if (p.getJaula() == null) {
            return "ERROR: paciente sin jaula";
        }
        if (p instanceof Gato && ((Gato) p).tieneEstresAlto()) {
            return "ERROR: gato con estres alto";
        }
        if (mg > p.getDosisMaxima() + TOLERANCIA) {
            return "ERROR: dosis excede el maximo";
        }
        if (p.getDosisAcumulada() + mg > p.getTope() + TOLERANCIA) {
            return "ERROR: tope acumulado superado";
        }

        // Medicacion valida
        p.setDosisAcumulada(p.getDosisAcumulada() + mg);
        p.setEstado("EN_TRATAMIENTO");
        if (p instanceof Gato) {
            ((Gato) p).subirEstres();
        }
        return "OK: " + p.getNombre() + " medicado con " + mgTxt + " mg";
    }

    // ---------- ALTA (R10) ----------
    public String alta(String nombre) {
        int pos = buscarIndice(nombre);
        if (pos == -1) {
            return "ERROR: paciente no encontrado";
        }
        Paciente p = pacientes[pos];
        if (!p.getEstado().equals("EN_TRATAMIENTO")) {
            return "ERROR: paciente no apto para alta";
        }

        // Libera la jaula
        if (p.getJaula() != null) {
            p.getJaula().setOcupante(null);
            p.setJaula(null);
        }

        // Compacta el vector: los siguientes se desplazan una posicion
        for (int i = pos; i < contador - 1; i++) {
            pacientes[i] = pacientes[i + 1];
        }
        pacientes[contador - 1] = null;
        contador--;

        return "OK: " + p.getNombre() + " dado de alta";
    }

    // ---------- REPORTE ----------
    public String reporte() {
        String texto = "--- REPORTE ---\n";
        texto += "Pacientes: " + contador + "/" + pacientes.length + "\n";
        for (int i = 0; i < contador; i++) {
            Paciente p = pacientes[i];
            String numeroJaula = "-";
            if (p.getJaula() != null) {
                numeroJaula = String.valueOf(p.getJaula().getNumero());
            }
            texto += p.getNombre() + " | " + p.getTipo() + " | "
                    + String.format(Locale.US, "%.1f", p.getPeso()) + " kg | "
                    + p.getEstado() + " | jaula: " + numeroJaula + "\n";
        }
        texto += "Jaulas libres: " + contarJaulasLibres();
        return texto;
    }

    // ---------- Metodos auxiliares ----------

    // Agrega un paciente al final del vector
    private void agregar(Paciente p) {
        pacientes[contador] = p;
        contador++;
    }

    // Busca por nombre sin distinguir mayusculas. Devuelve la posicion o -1
    private int buscarIndice(String nombre) {
        for (int i = 0; i < contador; i++) {
            if (pacientes[i].getNombre().equalsIgnoreCase(nombre)) {
                return i;
            }
        }
        return -1;
    }

    // R2: devuelve el mensaje de error, o null si todo esta bien
    private String revisarDuplicadoYCupo(String nombre) {
        if (buscarIndice(nombre) != -1) {
            return "ERROR: nombre duplicado";
        }
        if (contador >= pacientes.length) {
            return "ERROR: hospital lleno";
        }
        return null;
    }

    // R1: validaciones comunes a todos los pacientes
    private boolean datosBasicosValidos(String nombre, double peso, int edad, double pesoMaximo) {
        boolean nombreOk = nombre != null && !nombre.trim().isEmpty();
        boolean pesoOk = peso > 0 && peso <= pesoMaximo;
        boolean edadOk = edad >= 0 && edad <= 30;
        return nombreOk && pesoOk && edadOk;
    }

    private int contarJaulasLibres() {
        int libres = 0;
        for (int i = 0; i < jaulas.length; i++) {
            if (jaulas[i].estaLibre()) {
                libres++;
            }
        }
        return libres;
    }

    // Convierte texto a decimal. Si no es un numero valido devuelve NaN
    private double leerDecimal(String texto) {
        try {
            double valor = Double.parseDouble(texto);
            if (Double.isInfinite(valor)) {
                return Double.NaN;
            }
            return valor;
        } catch (NumberFormatException e) {
            return Double.NaN;
        }
    }

    // Convierte texto a entero. Si no es un numero valido devuelve -1 (edad invalida)
    private int leerEntero(String texto) {
        try {
            return Integer.parseInt(texto);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
