package organisms.g4;

import organisms.Move;
import organisms.g4.codec.Decoder;
import organisms.g4.comm.DataForChild;
import organisms.g4.trackers.FoodTracker;
import organisms.g4.trackers.OrganismTracker;
import organisms.g4.trackers.PersonalSettingsTracker;

@SuppressWarnings("serial")
public class KnowledgePlayer extends Group4BasePlayer {
	private int turnNumber = 0;
	protected FoodTracker foodTracker;
	protected OrganismTracker organismTracker;
	protected PersonalSettingsTracker settingsTracker;
	
	private Decoder decoder = null;
	private int directionOfCommunication;
	
	@Override
	protected void register(int key) {
		setColor(1.0f,0.0f,1.0f);
		if (key == -1) {
			setTurnNumber(1);
			directionOfCommunication = -1;
			
			foodTracker = new FoodTracker();
			organismTracker = new OrganismTracker();
			settingsTracker = new PersonalSettingsTracker();
		} else {
			DataForChild data = DataForChild.decode(key);
			directionOfCommunication = data.getParentLocation();

			foodTracker = new FoodTracker(-data.getOriginX(), -data.getOriginY());
			organismTracker = new OrganismTracker(-data.getOriginX(), -data.getOriginY());
			settingsTracker = new PersonalSettingsTracker();
			setTurnNumber(data.getTurnNumber() + 1);
		}
		
	}
	
	protected Move communicate(int[] neighbors) {
		if (directionOfCommunication != -1) {
		
		}
		return null;
	}
	
	
	@Override
	public Move reproduce(MoveInfo moveInfo) {
		Move m = communicate(moveInfo.getNeighbors());
		if (m != null) {
			return m;
		}
		
		return reproduce(NORTH);
	}

	@Override
	protected void preMoveTrack(MoveInfo moveInfo) {
		foodTracker.add(moveInfo.getFoodleft());
	}

	@Override
	protected void postMoveTrack(Move move, MoveInfo moveInfo) {
		settingsTracker.psTrackerStore(moveInfo.getEnergyleft(), move.type()==REPRODUCE);	
		foodTracker.add(move, moveInfo.getFoodpresent());
		organismTracker.add(move, moveInfo.getNeighbors());
		setTurnNumber(getTurnNumber() + 1);
	}
	
	@Override
	public Move makeMove(MoveInfo moveInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "KnowledgePlayer";
	}

	
	public Move reproduce(int direction) {
		return reproduce(direction, 0, false);
	}
	
	public Move reproduce(int direction, int type) {
		return reproduce(direction, type, false);
	}
	
	protected Move reproduce(int direction, int type, boolean wantsToComm) {
		DataForChild data = null;
		switch(direction) {
		case NORTH:
			data = new DataForChild(reverse(direction),
					-foodTracker.getX(),-(foodTracker.getY() + 1),getTurnNumber());
			break;
		case SOUTH:
			data = new DataForChild(reverse(direction),
					-foodTracker.getX(),-(foodTracker.getY() - 1),getTurnNumber());
			break;
		case EAST:
			data = new DataForChild(reverse(direction),
					-(foodTracker.getX() + 1),-foodTracker.getY(),getTurnNumber());
			break;
		case WEST:
			data = new DataForChild(reverse(direction),
					-(foodTracker.getX() - 1),-foodTracker.getY(),getTurnNumber());
			break;
		
		}
		
		directionOfCommunication = direction;
		if (data != null) {
			return new Move(REPRODUCE, direction, data.encode());
		}
		return null;
	}
	
	protected int reverse(int direction) {
		switch(direction) {
		case NORTH:
			return SOUTH;
		case SOUTH:
			return NORTH;
		case EAST:
			return WEST;
		case WEST:
			return EAST;
		}
		return -1;
	}

	public int getTurnNumber() {
		return turnNumber;
	}

	public void setTurnNumber(int turnNumber) {
		this.turnNumber = turnNumber;
	}
}
