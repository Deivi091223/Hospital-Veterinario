// Gato: es un Paciente con nivel de estres
public class Gato extends Paciente {
    public static final double PESO_MAXIMO = 15;

    private int nivelEstres; // de 0 a 10

    public Gato(String nombre, double peso, int edad, int nivelEstres) {
        super(nombre, peso, edad, "INGRESADO", 6); // factorDosis = 6
        setNivelEstres(nivelEstres);
    }

    public int getNivelEstres() {
        return nivelEstres;
    }

    public void setNivelEstres(int nivelEstres) {
        if (nivelEstres >= 0 && nivelEstres <= 10) {
            this.nivelEstres = nivelEstres;
        }
    }

    // R9: cada medicacion sube el estres 2 (tope 10)
    public void subirEstres() {
        int nuevo = nivelEstres + 2;
        if (nuevo > 10) {
            nuevo = 10;
        }
        setNivelEstres(nuevo);
    }

    // Con estres de 8 o mas no se le puede medicar
    public boolean tieneEstresAlto() {
        return nivelEstres >= 8;
    }

    @Override
    public String getTipo() {
        return "Gato";
    }

    @Override
    public double getPesoMaximo() {
        return PESO_MAXIMO;
    }
}
