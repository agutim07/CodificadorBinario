package pack;

import java.math.BigDecimal;

public class Alfabeto {
    private char caracter;
    private int frecuencia;
    private double probabilidad;
    private int pos;
    private String cod;

    Alfabeto(char c){
        this.frecuencia=1;
        this.caracter = c;
    }

    ///GETTERS AND SETTERS///
    public char getChar(){ return this.caracter; }

    public void setFrecuencia(int f){ this.frecuencia = f; }
    public int getFrecuencia(){ return this.frecuencia; }
    public void aumentarFrecuencia(){ this.frecuencia++; }

    public void setProbabilidad(int total){ this.probabilidad = (double) frecuencia/total; }
    public double getProbabilidad(){ return this.probabilidad; }

    public void setPosicion(int posicion){ this.pos = posicion; }
    public void setCod(int lon){
        String cod = Integer.toBinaryString(pos-1);
        int len = cod.length();
        for(int i=len; i<lon; i++){
            cod = "0"+cod;
        }
        this.cod=cod;
    }

    public String getCod(){ return this.cod; }

    public String imprimir(){
        String data;
        data = "Símbolo: " +this.caracter +" | Frecuencia: " +this.frecuencia+" | Probabilidad: "+redondearDecimal(this.probabilidad);
        return data;
    }

    private double redondearDecimal(double x){
        double resultado = x;
        double parteEntera = Math.floor(resultado);
        resultado=(resultado-parteEntera)*Math.pow(10, 4);
        resultado=Math.round(resultado);
        resultado=(resultado/Math.pow(10, 4))+parteEntera;
        return resultado;
    }


}
