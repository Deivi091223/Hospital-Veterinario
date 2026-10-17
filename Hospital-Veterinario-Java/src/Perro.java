// Perro: es un Paciente con raza y vacuna
public class Perro extends Paciente {
    public static final double PESO_MAXIMO = 80;

    private String raza;
    private boolean vacunado;

    public Perro(String nombre, double peso, int edad, String raza, boolean vacunado) {
        // R3: si no esta vacunado ingresa en CUARENTENA. factorDosis = 10
        super(nombre, peso, edad, vacunado ? "INGRESADO" : "CUARENTENA", 10);
        setRaza(raza);
        this.vacunado = vacunado;
    }

    public String getRaza() {
        return raza;
    }

    public boolean isVacunado() {
        return vacunado;
    }

    public void setRaza(String raza) {
        if (raza != null && !raza.trim().isEmpty()) {
            this.raza = raza.trim();
        }
    }

    // Vacuna al perro y lo saca de cuarentena
    public void vacunar() {
        this.vacunado = true;
        setEstado("INGRESADO");
    }

    @Override
    public String getTipo() {
        return "Perro";
    }

    @Override
    public double getPesoMaximo() {
        return PESO_MAXIMO;
    }
}
