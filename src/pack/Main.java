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

        File file = new File("D:\\ULE\\3º\\SI\\datos_3_alf.txt");
        int[] totalArray = new int[1];

        /** FUENTE DE INFORMACIÓN */
        ArrayList<Alfabeto> lista = generarLista(file,totalArray);
        int total = totalArray[0];
        System.out.println(total);
        double lon_min = (Math.log(total) / Math.log(2));
        System.out.println(lon_min);

        /** NUMERO A MENSAJE */

        /** INFO DE LA FUENTE EXTRA */
        /*for(int i=0; i<lista.size(); i++){
            System.out.print(lista.get(i).getChar() + " - ");
            System.out.println(lista.get(i).getL() + " , " + lista.get(i).getH());
        }*/

        /*for(int i=0; i<lista.size(); i++){
            System.out.print(i+1 + " - ");
            System.out.println(lista.get(i).imprimir());
        }*/
    }

    private static ArrayList<Alfabeto> generarLista(File file, int[] totalOut) throws FileNotFoundException {
        Scanner sc = new Scanner(file);
        ArrayList<Alfabeto> lista = new ArrayList<Alfabeto>();
        int total=0;

        while (true) {
            String nxt = sc.nextLine();
            for(int i=0; i<nxt.length(); i++){
                char c = nxt.charAt(i);
                int x = checkExist(lista, c);
                if(x==-1){
                    Alfabeto newletter = new Alfabeto(c);
                    lista.add(newletter);
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

        totalOut[0] = total;

        return lista;
    }

    private static int checkExist(ArrayList<Alfabeto> list, char c){
        for(int i=0; i<list.size(); i++){
            if(list.get(i).getChar()==c){ return i; }
        }

        return -1;
    }

    private static double calcularEntropia(ArrayList<Alfabeto> list){
        double ent = 0.0;
        for(int i=0; i<list.size(); i++){
            double prob = list.get(i).getProbabilidad();
            double log = (Math.log(1/prob) / Math.log(2));
            ent+= (prob * log);
        }
        return redondearDecimal(ent,5);
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


