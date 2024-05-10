import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        // Create tables
        final List<Table> tables = new ArrayList<>();
        tables.add(new Table(1, 2));
        tables.add(new Table(2, 2));
        tables.add(new Table(3, 2));
        tables.add(new Table(4, 3));
        tables.add(new Table(5, 4));
        tables.add(new Table(6, 5));
        tables.add(new Table(7, 5));
        tables.add(new Table(8, 6));

        final SeatingManager seatingManager = new SeatingManager(tables);

        final String helpText = "Hello to my SeatingManager simulation! Here's what you can do:\narrive <group name> <group size> - to add a group to the queue\nleave <group name> - to remove a group from the restaurant\nwhere <group name> - to find out where a group is seated\nexit - to exit the simulation\nhelp - to see this message again\n";

        System.out.println(helpText);

        final HashMap<String, CustomerGroup> groupMap = new HashMap<>();

        final Scanner scanner = new Scanner(System.in);
        while (true) {
            final String command = scanner.nextLine();
            final String[] commandParts = command.split(" ");
            if (commandParts[0].equals("arrive")) {
                final String name = commandParts[1];
                final int size = Integer.parseInt(commandParts[2]);
                final CustomerGroup group = new CustomerGroup(name, size);
                groupMap.put(name, group);
                seatingManager.arrives(group);
            } else if (commandParts[0].equals("leave")) {
                final String name = commandParts[1];
                final CustomerGroup group = groupMap.get(name);
                if (group == null) {
                    System.out.println("Group " + name + " not found.");
                    continue;
                }
                seatingManager.leaves(group);
            } else if (commandParts[0].equals("where")) {
                final String name = commandParts[1];
                final Table table = seatingManager.locate(new CustomerGroup(name, 2));
                if (table == null) {
                    System.out.println("Group " + name + " not found.");
                } else {
                    System.out.println("Group " + name + " is seated at table " + table.getTableNumber() + ".");
                }
            } else if (commandParts[0].equals("exit")) {
                break;
            } else if (commandParts[0].equals("help")) {
                System.out.println(helpText);
            } else {
                System.out.println("Invalid command. Please try again.");
            }
        }
        scanner.close();
    }
}
