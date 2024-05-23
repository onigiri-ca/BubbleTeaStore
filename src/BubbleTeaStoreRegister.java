import java.time.temporal.ChronoUnit;
import java.util.*;
import java.time.*;

public class BubbleTeaStoreRegister {

//    ArrayList<BubbleTea> drinksInCart = new ArrayList<>();
    static Scanner s = new Scanner(System.in);
    static double taxRate = 0.13;
    static double exchangeRate = 1;
    static LocalDate currDate = LocalDate.now();
    static LocalTime currTime = LocalTime.now().truncatedTo(ChronoUnit.SECONDS);


    public static int getUserChoice(int min, int max, String question){

        System.out.println();
        int opt = 0;

        do{
            try{
                System.out.print(question);
                opt = s.nextInt();
            } catch (InputMismatchException e){
                System.out.println("Invalid input");
            }

            if (opt < min || opt > max){
                System.out.println(opt + " is out of range. ");

            } // End if
            s.nextLine();
        } while (opt < min || opt > max);
        return opt;
    } // int getUserChoice(int, int, String)


    public static boolean yesOrNo(String question){

        String[] availableOptions = {"yes", "no", "y", "n"};
        String opt;


        while (true){
            System.out.println(question);
            opt = s.nextLine().toLowerCase();

            if (Arrays.asList(availableOptions).contains(opt)){
                break;
            }else{
                System.out.println("Enter a valid value. ");
            } // End if
        } // End while

        if (opt.equals("yes") || opt.equals("y")) {
            return true;
        }else{
            return false;
        } // End if
    }



    public static void viewCart(ArrayList<BubbleTea> drinksInCart){
        if (drinksInCart.isEmpty()){
            System.out.println("Your Cart is Empty. ");
        }else{
            for (int i = 0; i < drinksInCart.size(); i++){
//                System.out.println("Drink " + (i+1) + ". " + "\n" +drinksInCart.get(i).toString());
                System.out.println("Drink " + (i+1) + ". " + "\n" +drinksInCart.get(i).printReceiptFormat());
                System.out.println();
            } // End for
        } // End if

    } // void viewCart()


    public static int showMenu(){
        int selection;

        System.out.println("1. Order Drink(s)");
        System.out.println("2. Exit");


        selection = getUserChoice(1,2, "Enter your Choice: ");
        return selection;
    }


    public static void makeSale(BubbleTeaStore store){

        boolean additionalDrinks;

        // To make a sale, we'll need to keep track of the subtotal and the cart
        double subtotal = 0;
        double tax = 0;
        double total = 0;
        ArrayList<BubbleTea> currentCart = new ArrayList<>();



        // Make one drink at first
        currentCart.add(createDrink(store));


        // Ask if more drinks should be made
        additionalDrinks = yesOrNo("Create Drink " + (currentCart.size() + 1) + "?");
        System.out.println();

        // If yes, add as much drinks as needed.
        if (additionalDrinks){
            while (true){
                currentCart.add(createDrink(store));
                additionalDrinks = yesOrNo("Create Drink " + (currentCart.size() + 1) + "?");
                if (!additionalDrinks){
                    break;
                } // End if
            } // End while
        } // End if

        // If not, calculate the subtotal, tax rate, and total
        for (int i = 0; i < currentCart.size(); i++){
            subtotal += currentCart.get(i).getDrinkCost() * exchangeRate;
        } // End for

        tax = (subtotal * taxRate) * exchangeRate;
        total = subtotal + tax;

        System.out.println();
        System.out.println();

        System.out.println(store.getStoreName());
        System.out.println(store.getStoreLocation());
        System.out.println("Sale made on ");
        System.out.println(currDate + " at " + currTime);

        System.out.println("Drinks: --- ");
        System.out.println("=-=-=-=");
        viewCart(currentCart);
        System.out.println("=-=-=-=");

        System.out.println();
        System.out.println();

        System.out.println("Subtotal: $" + String.format("%.2f", subtotal) );
        System.out.println("Tax: $" + String.format("%.2f", tax));
        System.out.println("Total: $" + String.format("%.2f", total));

        System.out.println();

    }


    /**
     * Assemble a drink with a drink base, drink topping(s), sugar level, and ice level.
     * @param store The store to read the available contents from
     * @return A complete drink
     */
    public static BubbleTea createDrink(BubbleTeaStore store){

        int choice;
        int index = 0;
        boolean addToppings;
        ArrayList<String> tempList = new ArrayList<>();
        BubbleTea myDrink = new BubbleTea();

        System.out.println();



        // All drinks start with the base, so ask what drink they would like
        System.out.println("Choose a drink base: ");
        for (Map.Entry<String, Double> entry: store.getDrinkBases().entrySet()){

            System.out.println(index + ". " + entry.getKey() + " - $" + String.format("%.2f", entry.getValue()));
            tempList.add(entry.getKey());
            index++;
        } // End for

        // Get the user's choice
        choice = getUserChoice(0, store.getDrinkBases().size() - 1, "Choose your base: ");



        // Add the drink base and drink cost to the drink
        myDrink.setDrinkBase(tempList.get(choice));
        myDrink.setDrinkCost(myDrink.getDrinkCost() + store.getDrinkBases().get(tempList.get(choice)));

        // Keep track of how much the drink base costs as well
        myDrink.setBaseCost(store.getDrinkBases().get(tempList.get(choice)));

        tempList.clear();
        index = 0;

        // Ask if toppings would like to be added
        System.out.println();
        addToppings = yesOrNo("Add toppings? [Yes or No]");


        // If yes, then add toppings until not required.
        if (addToppings){

            boolean addMoreToppings;

            // This follows the same logic as choosing a drink base, however the user can add as much
            // drink toppings as they would like.

            while (true){

                System.out.println("Choose a topping: ");
                for (Map.Entry<String, Double> entry : store.getDrinkToppings().entrySet()){
                    System.out.println(index + ". " + entry.getKey() + " - $" + String.format("%.2f", entry.getValue()));
                    tempList.add(entry.getKey());
                    index++;
                } // End for

                choice = getUserChoice(0, store.getDrinkToppings().size() - 1, "Choose your topping: ");

                // Add the drink topping and topping cost to the drink
                myDrink.getToppings().add(tempList.get(choice));
                myDrink.setDrinkCost(myDrink.getDrinkCost() + store.getDrinkToppings().get(tempList.get(choice)));

                // Keep track of the cost of the toppings too
                myDrink.getToppingCosts().add(store.getDrinkToppings().get(tempList.get(choice)));

                tempList.clear();
                index = 0;

                // Ask the user if they would like to add another topping
                addMoreToppings = yesOrNo("Add more toppings? ");

                if (!addMoreToppings){
                    break;
                } // End if

            } // End while
        }else{
            System.out.println("No toppings to be added. ");
            System.out.println();
        } // End if

        // After adding toppings (or not), add ice levels:

        System.out.println();


        // Show all ice levels
        for (int i = 0; i < store.getIceLevels().size(); i++){
            System.out.println(i + ". " + store.getIceLevels().get(i));
        } // End for

        // Ask the user what ice level they would like
        // then add the ice level to the drink
        choice = getUserChoice(0, store.getIceLevels().size(), "Choose your Ice Level: ");
        myDrink.setIceLevel(store.getIceLevels().get(choice));

        // After adding the ice, add the sugar

        for (int i = 0; i < store.getSugarLevels().size(); i++){
            System.out.println(i + ". " + store.getSugarLevels().get(i));
        } // End for
        choice = getUserChoice(0, store.getSugarLevels().size(), "Choose your Sugar Level: ");
        myDrink.setSugarLevel(store.getSugarLevels().get(choice));


        return myDrink;
    }







    public static void main(String[] args){

        // Create the store
        BubbleTeaStore store = BubbleTeaStore.createStore();

        int choice;

        while (true){
            System.out.println("Welcome to " + store.getStoreName());
            choice = showMenu();

            if (choice == 1){
                makeSale(store);
            }else{
             break;
            }

        }
    }
}
