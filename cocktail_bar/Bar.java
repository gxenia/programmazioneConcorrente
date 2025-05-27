package so.cocktail_bar;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public abstract class Bar {
	public Random r = new Random();
	
	protected final int[] TIPI_COCTAIL = {0, 1};
	protected final int[] TEMPO_PREPARAZIONE_COCTAIL = {1, 2};
	protected final int[] COSTO_COCTAIL = {6, 8};
	
	protected int incasso = 0;
	protected int NUM_CLIENTI;
	
	public abstract void ordinaCocktail(int tipo) throws InterruptedException; 
	/* permette al cliente di ordinare il proprio cocktail. Riceve come
	 * parametro la tipologia di cocktail (normale o speciale) scelto in maniera random.
	 */
	
	public abstract void preparaCocktail() throws InterruptedException;
	/* una volta raccolte tutte le ordinazioni, permette al barman di preparare i
	 * cocktail scelti dai clienti. Al termine della preparazione di tutti i cocktail, 
	 * questi vengono serviti ai clienti.
	 */
	
	public abstract void beviEpaga(int tipo) throws InterruptedException;
	/* permette al cliente di bere il proprio cocktail e dirigersi alla cassa per
	 * pagare. Quando la cassa è libera il cliente paga, stampa a schermo l’importo pagato 
	 * e lascia il posto al prossimo cliente in fila alla cassa. Dopodiché lascia il bar.
	 */
	
	public abstract void chiudiBar() throws InterruptedException;
	/* permette al barman di chiudere il locale dopo che tutti i clienti della comitiva
	 * sono usciti.
	 */
	
	public void test( int N ) {
		NUM_CLIENTI = N;
		for ( int i = 0; i < N; ++i )
			new Cliente(this).start();
		Barman bm = new Barman(this);
		bm.setDaemon(true);
		bm.start();
	}
	
	public void attesaCasuale( int min, int max ) throws InterruptedException { 
		TimeUnit.SECONDS.sleep(r.nextInt(max-min+1)+min);
	}
	
	public void attesa( int len ) throws InterruptedException { 
		TimeUnit.SECONDS.sleep(len);
	}
}//Bar
