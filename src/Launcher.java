import controller.GameController;
import view.GameBoard;

public class Launcher {

	public static void main(String[]args){
		GameController controller = GameController.getController();
		controller.parseDeck();
		GameBoard board = new GameBoard(controller);
		controller.getYou().setObserver(board);
		controller.getOpponent().setObserver(board);
		controller.setBoard(board);
		board.setVisible(true);

	}
}
