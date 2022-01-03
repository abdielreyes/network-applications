import java.io.Serializable;


public class Persona implements Serializable {
    private String nombre;
    private float altura;
    private int edad;
    private transient double peso;
    private transient boolean casado;
    private transient String nacionalidad;

    public Persona(String nombre, float altura, int edad, double peso, boolean casado, String nacionalidad) {
        this.nombre = nombre;
        this.altura = altura;
        this.edad = edad;
        this.peso = peso;
        this.casado = casado;
        this.nacionalidad = nacionalidad;
    }


    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public float getAltura() {
        return this.altura;
    }

    public void setAltura(float altura) {
        this.altura = altura;
    }

    public int getEdad() {
        return this.edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public double getPeso() {
        return this.peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public boolean isCasado() {
        return this.casado;
    }

    public boolean getCasado() {
        return this.casado;
    }

    public void setCasado(boolean casado) {
        this.casado = casado;
    }

    public String getNacionalidad() {
        return this.nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }


    @Override
    public String toString() {
        return "{" +
            " nombre='" + getNombre() + "'" +
            ", altura='" + getAltura() + "'" +
            ", edad='" + getEdad() + "'" +
            ", peso='" + getPeso() + "'" +
            ", casado='" + isCasado() + "'" +
            ", nacionalidad='" + getNacionalidad() + "'" +
            "}";
    }
}
