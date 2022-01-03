import java.util.ArrayList;
import java.util.Random;

public class RandomVector {
    ArrayList r;
    public void init(){
        //inicializamos el array
        r = createRandomArray();
        //iniciamos los hilos
        new Thread(new HiloSum(),"sum").start();
        new Thread(new HiloPow(),"pow").start();
        new Thread(new HiloAvg(),"avg").start();
    }
    public static void main(String[] args){
        RandomVector r = new RandomVector();
        r.init();
    }
    public class HiloSum implements Runnable{
        public void run(){
            int s = 0;
            for(int i = 0;i<r.size();i++){
                //tenemos que hacer el casting para operar con tipo Object
                s = s+ (int) r.get(i);
            }
            System.out.println("The sum of the array is "+s);
        }
    }
    public class HiloPow implements Runnable{
        public void run(){
            double s = 0;
            for(int i = 0;i<r.size();i++){
                //usamos la libreria para hacer la potencia al cuadrado de lo que vamos a sumar
                s = s+ Math.pow(((int) r.get(i)),2);
            }
            System.out.println("The sum of the powed numbers in array is "+s);
        }
    }
    public class HiloAvg implements Runnable{
        public void run(){
            double s = 0;
            for(int i = 0;i<r.size();i++){
                s = s+ (int) r.get(i);
            }
            //usamos el resultado para hacer el promedio
            s = s/r.size();
            System.out.println("The avg of the numbers in array is "+ s);
        }
    }
    ArrayList createRandomArray(){
        //creamos un entero con limites de 1 a 1000
        Random r = new Random();
        int ri = r.nextInt(1,1000);
        ArrayList a = new ArrayList();
        //lo llenamos
        for(int i = 0; i<ri;i++){
            a.add(r.nextInt(0,1000));
        }
        System.out.println("Created random array w/size "+a.size());
        return a;
    }
}
