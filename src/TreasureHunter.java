import java.util.Scanner;

/**
 * This class is responsible for controlling the Treasure Hunter game.<p>
 * It handles the display of the menu and the processing of the player's choices.<p>
 * It handles all the display based on the messages it receives from the Town object. <p>
 *
 * This code has been adapted from Ivan Turner's original program -- thank you Mr. Turner!
 */

public class TreasureHunter {
    // static variables
    private static final Scanner SCANNER = new Scanner(System.in);

    // instance variables
    private Town currentTown;
    private Hunter hunter;
    private boolean hardMode;
    private String TEXT_RESET  = "\u001B[0m";
    private String BLUE = "\u001B[34m";
    private String RED = "\u001B[31m";
    private String GREEN = "\u001B[32m";
    private int x = 0;
    /**
     * Constructs the Treasure Hunter game.
     */
    public TreasureHunter() {
        // these will be initialized in the play method
        currentTown = null;
        hunter = null;
        hardMode = false;
    }

    /**
     * Starts the game; this is the only public method
     */
    public void play() {
        welcomePlayer();
        enterTown();
        showMenu();
    }

    /**
     * Creates a hunter object at the beginning of the game and populates the class member variable with it.
     */
    private void welcomePlayer() {
        System.out.println("Welcome to TREASURE HUNTER!");
        System.out.println("Going hunting for the big treasure, eh?");
        System.out.print("What's your name, Hunter? ");
        String name = SCANNER.nextLine().toLowerCase();


        System.out.print("Hard mode? (y/n or test): ");
        String hard = SCANNER.nextLine().toLowerCase();

        if (hard.equals("test")) {
            hunter = new Hunter(name, 107);
            hunter.buyItem("water", 1);
            hunter.buyItem("rope", 1);
            hunter.buyItem("machete", 1);
            hunter.buyItem("horse", 1);
            hunter.buyItem("boat", 1);
            hunter.buyItem("boots", 1);
            hunter.buyItem("shovel", 1);
        } else if (hard.equals("y")) {
            hunter = new Hunter(name, 10);
            hardMode = true;
        } else {
            hunter = new Hunter(name, 10);
        }
    }

    /**
     * Creates a new town and adds the Hunter to it.
     */
    private void enterTown() {
        double markdown = 0.5;
        double toughness = 0.4;
        if (hardMode) {
            // in hard mode, you get less money back when you sell items
            markdown = 0.25;

            // and the town is "tougher"
            toughness = 0.75;
        }

        // note that we don't need to access the Shop object
        // outside of this method, so it isn't necessary to store it as an instance
        // variable; we can leave it as a local variable
        Shop shop = new Shop(markdown);

        // creating the new Town -- which we need to store as an instance
        // variable in this class, since we need to access the Town
        // object in other methods of this class
        currentTown = new Town(shop, toughness);

        // calling the hunterArrives method, which takes the Hunter
        // as a parameter; note this also could have been done in the
        // constructor for Town, but this illustrates another way to associate
        // an object with an object of a different class
        currentTown.hunterArrives(hunter);
    }

    /**
     * Displays the menu and receives the choice from the user.<p>
     * The choice is sent to the processChoice() method for parsing.<p>
     * This method will loop until the user chooses to exit.
     */
    private void showMenu() {
        String choice = "";

        while (!choice.equals("x")) {
            System.out.println();
            System.out.println(currentTown.getLatestNews());
            System.out.println("***");
            System.out.println(hunter);
            System.out.println(currentTown);
            System.out.println(BLUE + "(B)uy something at the shop." + TEXT_RESET);
            System.out.println(BLUE + "(S)ell something at the shop." + TEXT_RESET);
            System.out.println(BLUE + "(M)ove on to a different town." + TEXT_RESET);
            System.out.println(BLUE + "(L)ook for trouble!" + TEXT_RESET);
            System.out.println(BLUE + "(H)unt for treasure!" + TEXT_RESET);
            System.out.println(BLUE + "(D)ig for gold!" + TEXT_RESET);
            System.out.println(RED + "Give up the hunt and e(X)it." + TEXT_RESET);
            System.out.println();
            System.out.print(GREEN + "What's your next move? " + TEXT_RESET);
            choice = SCANNER.nextLine().toLowerCase();
            processChoice(choice);
            if (hunter.getGold() < 0){
                System.out.println();
                System.out.println(currentTown.getLatestNews());
                System.out.println("***");
                System.out.println(hunter);
                break;
            }
        }
        System.out.println("Game Over!");
    }

    /**
     * Takes the choice received from the menu and calls the appropriate method to carry out the instructions.
     * @param choice The action to process.
     */
    private void processChoice(String choice) {
        if (choice.equals("b") || choice.equals("s")) {
            currentTown.enterShop(choice);
        } else if (choice.equals("m")) {
            if (currentTown.leaveTown()) {
                // This town is going away so print its news ahead of time.
                System.out.println(currentTown.getLatestNews());
                enterTown();
            }
        } else if (choice.equals("l")) {
            currentTown.lookForTrouble();
        } else if(choice.equals("h")){
            System.out.println("You found " + currentTown.getTreasure());
            if(!currentTown.getSearched()) {
                System.out.println("You found " + currentTown.getTreasure());
                hunter.addTreasure(currentTown.getTreasure());
                currentTown.setSearched();
            }
            else{
                System.out.println("You already dug for gold in this town.");
            }
        } else if (choice.equals("d")){
            if(!currentTown.getSearched()){
                if (hunter.digForGold()){
                    currentTown.setSearched();
                } else{
                    System.out.println("You can't dig for gold without a shovel.");
                }
            } else {
                System.out.println("The town has already been searched");
            }
            Town.printMessage = "";
        } else if (choice.equals("x")) {
            System.out.println("Fare thee well, " + hunter.getHunterName() + "!");
        } else {
            System.out.println("Yikes! That's an invalid option! Try again.");
        }
    }
}