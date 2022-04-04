package pack;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.Math;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author Alberto Gutiérrez Morán
 */

public class Main {

    public static int DECIMALS = 32;

    public static void main(String[] args) throws FileNotFoundException {

        File file = new File("D:\\Alberto GM\\ULE\\3º\\SI\\datos_alf.txt");
        int[] total = new int[1];

        /** ALFABETO, CODIFICAR */
        ArrayList<Alfabeto> lista = generarLista(file,total);
        int lon_min = (int) Math.ceil((Math.log(total[0]) / Math.log(2)));
        String listaBinaria = getCodificacionBinaria(lista,lon_min);

        /** MATRIZ GENERADORA */
        int[][] matriz = {{1,1,0},{1,0,1},{0,1,1}};
        int mSize = matriz.length;
        int[][] matrizG = generarMatrizG(matriz, "izquierda");

        /** CODIFICACION LINEAL */
        String msgCodificado = getMensajeCodificado(listaBinaria,matrizG);

        /** DESCODIFICAR MENSAJE */
        File file2 = new File("D:\\Alberto GM\\ULE\\3º\\SI\\datos.txt");
        String secuencia = getSecuencia(file2);
        String listaBinariaDes = getListaBinaria(secuencia,mSize,"izquierda");
        String message = descodifyMessage(lista, listaBinariaDes, lon_min);
        System.out.println(message);
    }

    private static String getMensajeCodificado(String lista, int[][] matriz){
        String cod = "";
        int len = matriz.length;
        int i=0;

        while(i+len<=lista.length()){
            String sub = lista.substring(i,(i+len));
            String add = multiplyBinary(sub,matriz);
            cod = cod+add;
            i+=len;
        }

        if(i<lista.length()){
            String sub = lista.substring(i);
            cod = cod+sub;
        }

        return cod;

    }

    private static ArrayList<Alfabeto> generarLista(File file, int[] totalArray) throws FileNotFoundException {
        Scanner sc = new Scanner(file);
        ArrayList<Alfabeto> lista = new ArrayList<Alfabeto>();
        int total=0, totalSimbolos=0;

        while (true) {
            String nxt = sc.nextLine();
            for(int i=0; i<nxt.length(); i++){
                char c = nxt.charAt(i);
                int x = checkExist(lista, c);
                if(x==-1){
                    Alfabeto newletter = new Alfabeto(c);
                    lista.add(newletter);
                    totalSimbolos++;
                }else{
                    lista.get(x).aumentarFrecuencia();
                }
                total++;
            }

            if(sc.hasNextLine()){
                cambioDeLinea(lista);
                total+=2;
            }else{
                break;
            }
        }

        for(int i=0; i<lista.size(); i++){
            lista.get(i).setProbabilidad(total);
        }

        totalArray[0] = totalSimbolos;

        return lista;
    }

    static private String getSecuencia(File file) throws FileNotFoundException {
        Scanner sc = new Scanner(file);
        String out = "";

        while (true) {
            String nxt = sc.nextLine();
            for(int i=0; i<nxt.length(); i++){
                char c = nxt.charAt(i);
                if(c=='1' || c=='0'){
                    out = out + c;
                }
            }

            if(!sc.hasNextLine()){
                break;
            }
        }

        return out;
    }

    static private String descodifyMessage(ArrayList<Alfabeto> listaAlf, String binary, int len){
        String msg = "";
        int i=0;

        while(i+len<=binary.length()){
            String sub = binary.substring(i,(i+len));
            char c = ' ';
            for(int x=0; x<listaAlf.size(); x++){
                if(listaAlf.get(x).getCod().equals(sub)){
                    c = listaAlf.get(x).getChar();
                }
            }
            msg=msg+c;
            i+=len;
        }

        return msg;
    }

    static private String getListaBinaria(String secuencia, int size, String pos){
        String out = "";
        int i=0;

        if(pos=="izquierda") {
            while (i + size <= secuencia.length()) {
                String sub = secuencia.substring(i, (i + size));
                out = out + sub;
                i += (size * 2);
            }

            if (i < secuencia.length()) {
                String sub = secuencia.substring(i);
                out = out + sub;
            }
        }

        if(pos=="derecha") {
            i=size-1;
            while (i + size <= secuencia.length()) {
                String sub = secuencia.substring(i, (i + size));
                out = out + sub;
                i += (size * 2);
            }

            if (i < secuencia.length()) {
                String sub = secuencia.substring(i);
                out = out + sub;
            }
        }

        return out;
    }

    static private String getCodificacionBinaria(ArrayList<Alfabeto> list, int lon){
        String lista = "";

        for(int i=0; i<list.size(); i++){
            list.get(i).setPosicion(i+1);
            list.get(i).setCod(lon);
            lista = lista + list.get(i).getCod();
        }

        return lista;
    }

    static private int[][] generarMatrizG(int[][] matriz, String pos){
        int size = matriz.length;
        int sizeG = size*2;
        int[][] matrizG = new int[size][sizeG];

        if(pos=="izquierda"){
            for(int i=0; i<size; i++){
                for(int x=0; x<size; x++){
                    if(x==i){
                        matrizG[i][x] = 1;
                    }else{
                        matrizG[i][x] = 0;
                    }
                }
            }
            for(int i=0; i<size; i++){
                for(int x=size; x<sizeG; x++){
                    matrizG[i][x] = matriz[i][x-size];
                }
            }
        }

        if(pos=="derecha"){
            for(int i=0; i<size; i++){
                for(int x=0; x<size; x++){
                    matrizG[i][x] = matriz[i][x];
                }
            }
            for(int i=0; i<size; i++) {
                for (int x = size; x < sizeG; x++) {
                    if ((x-size) == i) {
                        matrizG[i][x] = 1;
                    } else {
                        matrizG[i][x] = 0;
                    }
                }
            }
        }

        return matrizG;
    }

    private static String multiplyBinary(String sub, int[][] matriz){
        String out = "";
        int[] fila = new int[matriz.length];

        for(int i=0; i<sub.length(); i++){
            fila[i] = Character.getNumericValue(sub.charAt(i));
        }

        for(int x=0; x<(matriz.length*2); x++){
            int sum=0;
            for(int i=0; i<(matriz.length); i++){
                sum+= matriz[i][x] * fila[i];
            }
            if(sum>1) sum=sum%2;
            out = out + sum;
        }

        return out;
    }

    private static int checkExist(ArrayList<Alfabeto> list, char c){
        for(int i=0; i<list.size(); i++){
            if(list.get(i).getChar()==c){ return i; }
        }

        return -1;
    }

    private static void cambioDeLinea(ArrayList<Alfabeto> list){
        char c=' ';
        int x = checkExist(list, c);
        if(x==-1){
            Alfabeto newletter = new Alfabeto(c);
            newletter.aumentarFrecuencia();
            list.add(newletter);
        }else{
            list.get(x).aumentarFrecuencia();
            list.get(x).aumentarFrecuencia();
        }
    }

    private static double redondearDecimal(double x, int dec){
        double resultado = x;
        double parteEntera = Math.floor(resultado);
        resultado=(resultado-parteEntera)*Math.pow(10, dec);
        resultado=Math.round(resultado);
        resultado=(resultado/Math.pow(10, dec))+parteEntera;
        return resultado;
    }

}


