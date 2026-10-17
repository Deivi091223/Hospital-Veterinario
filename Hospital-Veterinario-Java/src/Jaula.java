// Jaula de la clinica
public class Jaula {
    private int numero;
    private String tamano;   // S, M o L
    private String zona;     // A o B
    private Paciente ocupante; // null si esta libre

    public Jaula(int numero, String tamano, String zona) {
        setNumero(numero);
        setTamano(tamano);
        setZona(zona);
        this.ocupante = null;
    }

    public int getNumero() {
        return numero;
    }

    public String getTamano() {
        return tamano;
    }

    public String getZona() {
        return zona;
    }

    public Paciente getOcupante() {
        return ocupante;
    }

    public void setNumero(int numero) {
        if (numero > 0) {
            this.numero = numero;
        }
    }

    public void setTamano(String tamano) {
        if (tamano != null && (tamano.equals("S") || tamano.equals("M") || tamano.equals("L"))) {
            this.tamano = tamano;
        }
    }

    public void setZona(String zona) {
        if (zona != null && (zona.equals("A") || zona.equals("B"))) {
            this.zona = zona;
        }
    }

    public void setOcupante(Paciente ocupante) {
        this.ocupante = ocupante; // null libera la jaula
    }

    // Libre si no tiene ocupante
    public boolean estaLibre() {
        return ocupante == null;
    }
}
