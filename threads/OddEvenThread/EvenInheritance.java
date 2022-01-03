class EvenInheritance extends Thread {
    EvenInheritance(String str) {
        super(str);
    }

    public void run() {
        for (int i = 0; i < 10; i++) {
            if (i % 2 == 0)
                System.out.println(getName()+" "+i);
        }
    }
}