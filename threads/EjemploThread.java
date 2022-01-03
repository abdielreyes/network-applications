public class EjemploThread extends Thread{
	public EjemploThread(String str){
		super(str);
	}
	public void run(){
		for(int i = 0;i<10;i++){
			System.out.println(i+" "+getName());
		}
		System.out.println("Termina thread "+getName());
	}
	public static void main(String[] args){
		new EjemploThread("Enrique").start();
		new EjemploThread("Abdiel").start();
		System.out.println("Termina thread main");
	}
}
