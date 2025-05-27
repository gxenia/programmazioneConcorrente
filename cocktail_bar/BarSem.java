package so.cocktail_bar;

import java.util.concurrent.Semaphore;

public class BarSem extends Bar{
	private Semaphore possoOrdinare;
	private Semaphore possoBere = new Semaphore(0, true);
	private int[] ordinazioni = {0, 0};
	
	private Semaphore possoPreparare = new Semaphore(0);
	private Semaphore possoChiudere = new Semaphore(0);
	
	private Semaphore mutex = new Semaphore(1);
	
	@Override
	public void ordinaCocktail(int tipo) throws InterruptedException {
		possoOrdinare.acquire();
		mutex.acquire();
		System.out.println("Il cliente " + Thread.currentThread().getId() + " ha scelto coctail " + tipo + ".");
		ordinazioni[tipo]++;
		if ( ordinazioni[0]+ordinazioni[1] == NUM_CLIENTI )
			possoPreparare.release();
		mutex.release();
	}

	@Override
	public void preparaCocktail() throws InterruptedException {
		possoPreparare.acquire();
		int durata = ordinazioni[0]*TEMPO_PREPARAZIONE_COCTAIL[0] +
						ordinazioni[1]*TEMPO_PREPARAZIONE_COCTAIL[1];
		attesa(durata);
		possoBere.release(ordinazioni[0]+ordinazioni[1]);
	}

	@Override
	public void beviEpaga(int tipo) throws InterruptedException {
		possoBere.acquire();
		mutex.acquire();
		System.out.println("Il cliente " + Thread.currentThread().getId() + " paga " + COSTO_COCTAIL[tipo] + ".");
		incasso += COSTO_COCTAIL[tipo];
		ordinazioni[tipo]--;
		if ( ordinazioni[0]+ordinazioni[1] == 0 )
			possoChiudere.release();
		mutex.release();
	}

	@Override
	public void chiudiBar() throws InterruptedException {
		possoChiudere.acquire();
		System.out.println("Il Semaforo, incasso: " + incasso);
	}
	
	public void test( int N ) {
		possoOrdinare = new Semaphore(N, true);
		super.test(N);
	}
	
	public static void main( String...args ) {
		Bar b = new BarSem();
		b.test(5); // 1 barman di default
	}
}//BarSem
