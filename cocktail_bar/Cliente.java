package so.cocktail_bar;

public class Cliente extends Thread{
	private Bar bar;
	private int id;
	private static int counter = 0;
	
	public Cliente( Bar bar ) {
		this.bar = bar;
		counter++;
		id = counter;
	}
	
	public void run() {
		try {
			bar.attesaCasuale(2, 10);
			int tipo = scegliCoctail();
			bar.ordinaCocktail(tipo);
			bar.beviEpaga(tipo);
			bar.attesaCasuale(2, 5);
		} catch (InterruptedException e) {}
	}
	
	private int scegliCoctail() { return bar.r.nextInt(2); }
	
	public long getId() { return id; }
	
}//Cliente
