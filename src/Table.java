import java.util.ArrayList;
import java.util.List;

public class Table {
    private final int tableNumber; // table number (only used for console output)
    private final int size; // number of chairs around this table
    private int availableSeats; // number of available seats at this table
    private final List<CustomerGroup> seatedGroups = new ArrayList<>();

    public Table(int tableNumber, int size) {
        this.tableNumber = tableNumber;
        this.size = size;
        this.availableSeats = size;
    }

    public boolean canSeatGroup(CustomerGroup group) {
        return availableSeats >= group.getSize();
    }

    public boolean seatGroup(CustomerGroup group) {
        if (availableSeats >= group.getSize()) {
            seatedGroups.add(group);
            availableSeats -= group.getSize();
            return true;
        }
        return false;
    }

    public boolean removeGroup(CustomerGroup group) {
        if (seatedGroups.remove(group)) {
            availableSeats += group.getSize();
            return true;
        }
        return false;
    }

    public int getSize() {
        return size;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public boolean hasGroup(CustomerGroup group) {
        return seatedGroups.contains(group);
    }

    public int getTableNumber() {
        return tableNumber;
    }
}
