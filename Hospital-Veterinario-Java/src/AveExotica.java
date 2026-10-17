// AveExotica: es un Paciente con envergadura
public class AveExotica extends Paciente {
    public static final double PESO_MAXIMO = 15;

    private double envergadura; // en cm

    public AveExotica(String nombre, double peso, int edad, double envergadura) {
        super(nombre, peso, edad, "INGRESADO", 3); // factorDosis = 3
        setEnvergadura(envergadura);
    }

    public double getEnvergadura() {
        return envergadura;
    }

    public void setEnvergadura(double envergadura) {
        if (envergadura > 0) {
            this.envergadura = envergadura;
        }
    }

    @Override
    public String getTipo() {
        return "Ave";
    }

    @Override
    public double getPesoMaximo() {
        return PESO_MAXIMO;
    }

    // R5: las aves solo van en zona B
    @Override
    public String getZonaRequerida() {
        return "B";
    }

    // R4: un ave con envergadura mayor a 60 cm necesita minimo M
    @Override
    public String getTamanoRequerido() {
        String tamano = super.getTamanoRequerido();
        if (envergadura > 60 && tamano.equals("S")) {
            return "M";
        }
        return tamano;
    }
}
