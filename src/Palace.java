import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Palace {
    public static void main(String[] args) {
        int[] scoreBoard = new int[5];
        Arrays.fill(scoreBoard,0);
        while (true) {
            Scanner sc = new Scanner(System.in);
            System.out.println("Palace\n1)Play\n2)Manual\n3)Scoreboard\n4)Quit\n*Reading manual first is highly recommended");
            String choice = sc.nextLine().toLowerCase();
            switch (choice) {
                case "3", "scoreboard" -> {
                    StringBuilder str = new StringBuilder();
                    str.append("SCOREBOARD");
                    for(int i = 0;i < scoreBoard.length;i++){
                        str.append("\nPlayer ").append(i + 1).append(": ").append(scoreBoard[i]).append(" points");
                    }
                    System.out.println(str);
                }
                case "4", "quit" -> {
                    return;
                }
                case "2", "manual" -> System.out.println(manual());

                case "1", "play" -> {
                    System.out.println("How many human player (0-5) ?");
                    int numPlayer;
                    int bot = 0;
                    while (true) {
                        try {
                            numPlayer = sc.nextInt();
                            if (numPlayer < 0 || numPlayer > 5) {
                                throw new InputMismatchException();
                            }
                            break;
                        } catch (InputMismatchException e) {
                            System.out.println("Invalid input try again.");
                        }

                    }
                    if ((5 - numPlayer) > 0) {
                        int minBot = 2;
                        minBot -= numPlayer;
                        if (minBot < 0) {
                            minBot = 0;
                        }
                        System.out.println("How many bot (" + minBot + "-" + (5 - numPlayer) + ")");
                        while (true) {
                            try {
                                bot = sc.nextInt();
                                if (bot < minBot || bot > (5 - numPlayer)) {
                                    throw new InputMismatchException();
                                }
                                break;
                            } catch (InputMismatchException e) {
                                System.err.println("Invalid input try again.(Wrong input type, invalid number of player)");
                            }

                        }
                    }
                    Table t = new Table(numPlayer + bot);
                    Player p = new Player(numPlayer, bot);
                    System.out.println(t);
                    System.out.println("Phase 1: Swap your card");
                    int curPlayer = 0;
                    while (curPlayer < bot + numPlayer) {
                        if (p.isHuman(curPlayer)) {
                            System.out.println("Player " + (curPlayer + 1) + " do you want to swap card(y/n/a(auto-swap))");
                            char ans = sc.next().charAt(0);
                            switch (ans) {
                                case 'y' -> {
                                    nested:
                                    while (true) {
                                        try {
                                            System.out.println("Player " + (curPlayer + 1) + "\nInput the card on the playing deck");
                                            int fcard = sc.nextInt() - 1;
                                            System.out.println("Input the card on the showing deck");
                                            int scard = sc.nextInt() - 1;
                                            t.preGame(curPlayer, fcard, scard);
                                            System.out.println(t);
                                            System.out.println("Are you done(y/n) ?");
                                            char c = sc.next().charAt(0);
                                            switch (c) {
                                                case 'y':
                                                    curPlayer++;
                                                    break nested;
                                                case 'n':
                                                    break;
                                                default:
                                                    throw new InputMismatchException();

                                            }
                                        } catch (InputMismatchException e) {
                                            System.err.println("Invalid value");
                                            sc.next();
                                        } catch (IllegalArgumentException e) {
                                            System.err.println("Either value is wrong or out of bounds, please try again.");
                                        }
                                    }
                                }
                                case 'n' -> curPlayer++;
                                case 'a' -> t.autoSwap(curPlayer);
                                default -> System.err.println("Invalid value");
                            }
                        } else {
                            t.autoSwap(curPlayer);
                            curPlayer++;
                        }
                        pause(2);
                    }
                    System.out.println("Play Order\n" + p.whoGoFirst());
                    System.out.println(t);
                    boolean turnOver = false;
                    boolean gameOver = false;
                    int turn = 0;
                    int total = bot + numPlayer;
                    pause(3);
                    System.out.println("      Phase two: Playing");
                    while (!gameOver) {
                        System.out.println(" Current Player:  " + (p.currentPlayer(turn) + 1));
                        if (p.isHuman(turn)) {
                            System.out.println("Input numbers of card you want to play Ex:1 or 1,2,3. Input m for more options and gameplay.");
                        }
                        while (!turnOver) {
                            if (p.isHuman(turn)) {
                                try {
                                    String input = sc.nextLine();
                                    if (input.length() == 1) {
                                        Pattern pattern = Pattern.compile("[0-9]");
                                        Matcher matcher = pattern.matcher(input);
                                        if (matcher.find()) {
                                            int[] selection = new int[]{Integer.parseInt(input) - 1};
                                            turnOver = t.play(selection, p.currentPlayer(turn));
                                        } else {
                                            turnOver = t.play(input.charAt(0), p.currentPlayer(turn));
                                        }
                                    } else if (input.length() > 1) {
                                        String[] inputSeparated = input.split(",");
                                        int[] selections = new int[inputSeparated.length];
                                        for (int i = 0; i < selections.length; i++) {
                                            selections[i] = Integer.parseInt(inputSeparated[i]) - 1;
                                        }
                                        turnOver = t.play(selections, p.currentPlayer(turn));
                                    }
                                } catch (NumberFormatException e) {
                                    System.err.println("Invalid choice(garbage input),try again");
                                } catch (ArrayIndexOutOfBoundsException e) {
                                    System.err.println("Invalid Card number");
                                }
                            } else {
                                turnOver = t.autoPlay(p.currentPlayer(turn));
                            }
                        }
                        gameOver = t.verify(p.currentPlayer(turn));
                        if (gameOver) {
                            System.out.println("Player " + (p.currentPlayer(turn) + 1) + " won !!!!!!!!!!!");
                            scoreBoard[p.currentPlayer(turn)]++;
                            System.out.println(t);
                            System.out.println("What do you want to do now ?\n1)Quit\n2)Go to menu");
                            boolean goToMenu=false;
                            while (!goToMenu){
                                char c = sc.next().charAt(0);
                                switch (c){
                                    case '1' -> {
                                        return;
                                    }
                                    case '2' -> goToMenu=true;
                                    default -> System.err.println("Invalid input try again");
                                }
                            }
                        } else {
                            turn++;
                            turnOver = false;
                            System.out.println("\n\n" + t);
                            if (turn == total) {
                                turn = 0;
                            }
                        }
                        pause(3);
                    }


                }
                default -> System.err.println("Invalid input please try again");
            }
        }
    }

    public static String manual() {
        return "Game rules can be found here: https://gamerules.com/rules/shithead-card-game/\nDuring phase two of the game,\nClick s to sort\n\nClick d for draw from the deck and play(use when you want to test your chance when you dont have a big enough card to play,\nif drew card is smaller than current card on playing pile , you pick up the pile and the drew card)\n\nClick p: pick the pile, Use when you have no playable card and dont want to risk your chance\n\nClick r for recommendation\n\nClick a for autoplay";
    }

    public static void pause(int sec) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}