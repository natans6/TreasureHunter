/**
 * The Town Class is where it all happens.
 * The Town is designed to manage all the things a Hunter can do in town.
 * This code has been adapted from Ivan Turner's original program -- thank you Mr. Turner!
 */

public class Town {
    // instance variables
    private Hunter hunter;
    private Shop shop;
    private Terrain terrain;
    private String printMessage;
    private boolean toughTown;
    private String TEXT_RESET  = "\u001B[0m";
    private String YELLOW = "\u001B[33m";
    private String RED = "\u001B[31m";
    private String GREEN = "\u001B[32m";

    /**
     * The Town Constructor takes in a shop and the surrounding terrain, but leaves the hunter as null until one arrives.
     *
     * @param shop The town's shoppe.
     * @param toughness The surrounding terrain.
     */
    public Town(Shop shop, double toughness) {
        this.shop = shop;
        this.terrain = getNewTerrain();

        // the hunter gets set using the hunterArrives method, which
        // gets called from a client class
        hunter = null;

        printMessage = "";

        // higher toughness = more likely to be a tough town
        toughTown = (Math.random() < toughness);
    }

    public String getLatestNews() {
        return printMessage;
    }

    /**
     * Assigns an object to the Hunter in town.
     *
     * @param hunter The arriving Hunter.
     */
    public void hunterArrives(Hunter hunter) {
        this.hunter = hunter;
        printMessage = YELLOW + "Welcome to town, " + hunter.getHunterName() + "." + TEXT_RESET;

        if (toughTown) {
            printMessage += YELLOW + "\nIt's pretty rough around here, so watch yourself." + TEXT_RESET;
        } else {
            printMessage += YELLOW + "\nWe're just a sleepy little town with mild mannered folk." + TEXT_RESET;
        }
    }

    /**
     * Handles the action of the Hunter leaving the town.
     *
     * @return true if the Hunter was able to leave town.
     */
    public boolean leaveTown() {
        boolean canLeaveTown = terrain.canCrossTerrain(hunter);
        if (canLeaveTown) {
            String item = terrain.getNeededItem();
            printMessage = YELLOW + "You used your " + item + " to cross the " + terrain.getTerrainName() + "." + TEXT_RESET;
            if (checkItemBreak()) {
                hunter.removeItemFromKit(item);
                printMessage += YELLOW + "\nUnfortunately, your " + item + " broke." + TEXT_RESET;
            }

            return true;
        }

        printMessage = RED + "You can't leave town, " + hunter.getHunterName() + ". You don't have a " + terrain.getNeededItem() + "." + TEXT_RESET;
        return false;
    }

    /**
     * Handles calling the enter method on shop whenever the user wants to access the shop.
     *
     * @param choice If the user wants to buy or sell items at the shop.
     */
    public void enterShop(String choice) {
        shop.enter(hunter, choice);
    }

    /**
     * Gives the hunter a chance to fight for some gold.<p>
     * The chances of finding a fight and winning the gold are based on the toughness of the town.<p>
     * The tougher the town, the easier it is to find a fight, and the harder it is to win one.
     */
    public void lookForTrouble() {
        double noTroubleChance;
        if (toughTown) {
            noTroubleChance = 0.66;
        } else {
            noTroubleChance = 0.33;
        }

        if (Math.random() > noTroubleChance) {
            printMessage = RED + "You couldn't find any trouble" + TEXT_RESET;
        } else {
            printMessage = "You want trouble, stranger!  You got it!\nOof! Umph! Ow!\n";
            int goldDiff = (int) (Math.random() * 10) + 1;
            if (Math.random() > noTroubleChance) {
                printMessage += GREEN + "Okay, stranger! You proved yer mettle. Here, take my gold." + TEXT_RESET;
                printMessage += GREEN + "\nYou won the brawl and receive " + goldDiff + " gold." + TEXT_RESET;
                hunter.changeGold(goldDiff);
            } else {
                printMessage += RED + "That'll teach you to go lookin' fer trouble in MY town! Now pay up!" + TEXT_RESET;
                printMessage += RED + "\nYou lost the brawl and pay " + goldDiff + " gold." + TEXT_RESET;
                hunter.changeGold(-goldDiff);
            }
        }
    }

    public String toString() {
        return "This nice little town is surrounded by " + terrain.getTerrainName() + ".";
    }

    /**
     * Determines the surrounding terrain for a town, and the item needed in order to cross that terrain.
     *
     * @return A Terrain object.
     */
    private Terrain getNewTerrain() {
        int rnd = (int) (Math.random() * 6);
        if (rnd == 0) {
            return new Terrain("Mountains", "Rope");
        } else if (rnd == 1) {
            return new Terrain("Ocean", "Boat");
        } else if (rnd == 2) {
            return new Terrain("Plains", "Horse");
        } else if (rnd == 3) {
            return new Terrain("Desert", "Water");
        } else if (rnd == 4){
            return new Terrain("Jungle", "Machete");
        } else {
            return new Terrain("Marsh", "Boots");
        }
    }

    /**
     * Determines whether a used item has broken.
     *
     * @return true if the item broke.
     */
    private boolean checkItemBreak() {
        double rand = Math.random();
        return (rand < 0.5);
    }
}