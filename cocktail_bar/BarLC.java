package so.cocktail_bar;

import java.util.LinkedList;
import java.util.concurrent.locks.*;

public class BarLC extends Bar {
	private Lock l = new ReentrantLock();
	private Condition possoOrdinare = l.newCondition();
	private Condition possoBere = l.newCondition();
	private Condition possoPreparare = l.newCondition();
	private Condition possoChiudere = l.newCondition();
	
	LinkedList<Thread> clienti = new LinkedList<>();
	private int[] ordinazioni = {0, 0};
	private int i = -1;
	
	@Override
	public void ordinaCocktail(int tipo) throws InterruptedException {
		l.lock();
		try {
			System.out.println("Il cliente " + Thread.currentThread().getId() + " ha scelto coctail " + tipo + ".");
			clienti.addLast( Thread.currentThread() );
			i++;
			while ( !possoOrdinare() ) 
				possoOrdinare.await();
			ordinazioni[tipo]++;
			if ( ordinazioni[0]+ordinazioni[1] == NUM_CLIENTI )
				possoPreparare.signal();
		} finally {
			l.unlock();
		}
	}
	
	private boolean possoOrdinare() {
		return clienti.get(i) == Thread.currentThread();
	}

	@Override
	public void preparaCocktail() throws InterruptedException {
		l.lock();
		try {
			while ( !possoPreparare() )
				possoPreparare.await();
			int durata = ordinazioni[0]*TEMPO_PREPARAZIONE_COCTAIL[0] +
					ordinazioni[1]*TEMPO_PREPARAZIONE_COCTAIL[1];
			attesa(durata);
			possoBere.signalAll();
		} finally {
			l.unlock();
		}
	}

	private boolean possoPreparare() {
		return ordinazioni[0] + ordinazioni[1] == NUM_CLIENTI;
	}
	
	@Override
	public void beviEpaga(int tipo) throws InterruptedException {
		l.lock();
		try {
			while ( !possoBere() ) 
				possoBere.await();
			clienti.removeFirst();
			System.out.println("Il cliente " + Thread.currentThread().getId() + " paga " + COSTO_COCTAIL[tipo] + ".");
			incasso += COSTO_COCTAIL[tipo];
			if ( clienti.isEmpty() )
				possoChiudere.signal();
		} finally {
			l.unlock();
		}
	}
	
	private boolean possoBere() {
		return ordinazioni[0] + ordinazioni[1] == NUM_CLIENTI && clienti.getFirst() == Thread.currentThread();
	}

	@Override
	public void chiudiBar() throws InterruptedException {
		l.lock();
		try {
			while ( !possoChiudere() ) 
				possoChiudere.await();
			System.out.println("Il Semaforo, incasso: " + incasso);
		} finally {
			l.unlock();
		}
	}
	
	private boolean possoChiudere() {
		return clienti.isEmpty();
	}
	
	
	public static void main( String...args ) {
		Bar b = new BarLC();
		b.test(5); // 1 barman di default
	}
	
}//BarLC
