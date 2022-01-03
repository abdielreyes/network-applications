public class OddEvenThread {
	public static void main(String[] args){
		//creamos los hilos que usan herencia 
		new EvenInheritance("Even Inheritance").start();
		new OddInheritance("Odd Inheritance").start();
		//creamos los hilos que usan interfaces
		//debemos crear un hilo a base de este, ya que no es de tipo thread
		new Thread(new EvenInterface(),"Even Interface").start();
		new Thread(new OddInterface(),"Odd Interface").start();
	}
}
