
class Main{
    public static void main(String [] args){
        /*Make sure sleep timer is 501 in Grid>Walker>run before submitting! Changing that number to 0 or 1
        * makes testing go incredibly fast and is not how it should be. */

        /*Defaults: walker: 4, grid: 10x10, sleep timer: 0-500 ms (501)*/

        Grid game = new Grid(10,10);
        game.startGame();
        game.startGame(game.getWinner());

        System.out.println("Game Over");

    }
}
