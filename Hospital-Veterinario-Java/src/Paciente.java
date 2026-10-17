// Clase base de todos los pacientes de la clinica
public class Paciente {
    private String nombre;
    private double peso;          // en kg
    private int edad;             // en anos
    private String estado;        // INGRESADO, CUARENTENA o EN_TRATAMIENTO
    private double factorDosis;   // mg por kg
    private double dosisAcumulada; // mg
    private Jaula jaula;          // null si no tiene jaula

    public Paciente(String nombre, double peso, int edad, String estado, double factorDosis) {
        setNombre(nombre);
        setPeso(peso);
        setEdad(edad);
        setEstado(estado);
        setFactorDosis(factorDosis);
        this.dosisAcumulada = 0;
        this.jaula = null;
    }

    // ---------- Getters ----------
    public String getNombre() {
        return nombre;
    }

    public double getPeso() {
        return peso;
    }

    public int getEdad() {
        return edad;
    }

    public String getEstado() {
        return estado;
    }

    public double getFactorDosis() {
        return factorDosis;
    }

    public double getDosisAcumulada() {
        return dosisAcumulada;
    }

    public Jaula getJaula() {
        return jaula;
    }

    // ---------- Setters (si el valor es invalido, no cambian nada) ----------
    public void setNombre(String nombre) {
        if (nombre != null && !nombre.trim().isEmpty()) {
            this.nombre = nombre.trim();
        }
    }

    public void setPeso(double peso) {
        if (peso > 0 && peso <= getPesoMaximo()) {
            this.peso = peso;
        }
    }

    public void setEdad(int edad) {
        if (edad >= 0 && edad <= 30) {
            this.edad = edad;
        }
    }

    public void setEstado(String estado) {
        if (estado != null && (estado.equals("INGRESADO")
                || estado.equals("CUARENTENA")
                || estado.equals("EN_TRATAMIENTO"))) {
            this.estado = estado;
        }
    }

    public void setFactorDosis(double factorDosis) {
        if (factorDosis > 0) {
            this.factorDosis = factorDosis;
        }
    }

    public void setDosisAcumulada(double dosisAcumulada) {
        if (dosisAcumulada >= 0) {
            this.dosisAcumulada = dosisAcumulada;
        }
    }

    public void setJaula(Jaula jaula) {
        this.jaula = jaula; // puede ser null (sin jaula)
    }

    // ---------- Calculos de dosis (R7) ----------
    public double getDosisMaxima() {
        return peso * factorDosis;
    }

    public double getTope() {
        return 3 * getDosisMaxima();
    }

    // ---------- Metodos que las subclases sobrescriben ----------
    public String getTipo() {
        return "Paciente";
    }

    public double getPesoMaximo() {
        return 80; // valor por defecto
    }

    // R5: zona de la jaula (perros y gatos en A)
    public String getZonaRequerida() {
        return "A";
    }

    // R4: tamano de jaula segun el peso
    public String getTamanoRequerido() {
        if (peso < 5) {
            return "S";
        }
        if (peso <= 25) {
            return "M";
        }
        return "L";
    }
}
