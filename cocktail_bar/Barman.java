package so.cocktail_bar;

public class Barman extends Thread{
	private Bar bar;
	public Barman( Bar bar ) {
		this.bar = bar;
	}
	
	public void run() {
		try {
			bar.preparaCocktail();
			bar.chiudiBar();
		} catch (InterruptedException e) {}
	}
}//Barman
