import Board.Game;

public class GameServerMain {
    public static void main(String[] args) {
        Game game = Game.getInstance(1);
        System.out.println("End of main!");
    }
}
