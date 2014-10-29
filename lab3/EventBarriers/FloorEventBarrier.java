package EventBarriers;
import Elevators.AbstractElevator;
import Elevators.Elevator;

/**
 * 
 * This is the event barrier that is used on each floor of the building. It differs from
 * the parent EventBarrier in that the raise() method waits on a slightly different condition,
 * AND the barrier has an instance of the Elevator that called raise() in the first place.
 * <br> <br>
 * Each floor will have two of these: one for the up waiters, and one for the down waiters
 *
 */

public class FloorEventBarrier extends EventBarrier {
	
	private AbstractElevator currentElevator;
	
	public FloorEventBarrier() {
		super();
		currentElevator = null;
	}
	
	public synchronized void raise(AbstractElevator el) {
		currentElevator = el;
		raise();
		currentElevator = null;
	}
	
	@Override
	public synchronized void raise() {
		eventInProg = true;
		notifyAll();

		while(numUnfinishedThreads > 0 && !currentElevator.isFull()) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		notifyAll();
		eventInProg = false;
	}
	
	public synchronized AbstractElevator getElevator() {
		return currentElevator;
	}
}
