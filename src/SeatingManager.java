import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class SeatingManager {

    private final List<Table> tables;
    private final ArrayList<ArrayList<CustomerGroup>> queues;
    private final HashMap<String, Table> groupTableMap = new HashMap<>();
    private long ticketNumber = 0;

    public SeatingManager(List<Table> tables) {
        this.tables = tables;
        this.tables.sort((t1, t2) -> t1.getSize() - t2.getSize());
        final int maxGroupSize = tables.stream().mapToInt(t -> t.getSize()).max().orElse(0);
        queues = new ArrayList<ArrayList<CustomerGroup>>();
        for (int i = 0; i < maxGroupSize + 1; i++) {
            queues.add(new ArrayList<>());
        }
    }

    /* Group arrives and wants to be seated. */
    public void arrives(CustomerGroup group) {
        final Optional<Table> freeTable = tables.stream().filter(table -> table.canSeatGroup(group))
                .findFirst();
        if (freeTable.isPresent()) {
            freeTable.get().seatGroup(group);
            groupTableMap.put(group.getName(), freeTable.get());
            System.out.println(
                    "Group " + group.getName() + " of size " + group.getSize() + " seated at table of size "
                            + freeTable.get().getSize()
                            + ". Table has " + freeTable.get().getAvailableSeats() + " available seats left.");
            return;
        } else {
            // Give group their ticket number and add them to the queue for their group size
            group.setTicketNumber(ticketNumber++);
            queues.get(group.getSize()).add(group);
            System.out.println(
                    "Group of size " + group.getSize() + " waiting with ticket number " + group.getTicketNumber()
                            + ".");
        }
    }

    /* Whether seated or not, the group leaves the restaurant. */
    public void leaves(final CustomerGroup group) {
        final Table table = locate(group);
        if (table == null) {
            // Group not found at tables, check queues
            final boolean wasGroupPresent = queues.get(group.getSize()).remove(group);
            if (!wasGroupPresent) {
                System.out.println("Group " + group.getName() + " not found.");
                return;
            } else {
                System.out.println("Group " + group.getName() + " of size " + group.getSize() + " left the queue.");
                return;
            }
        } else if (table.removeGroup(group)) {
            groupTableMap.remove(group.getName());
            System.out.println("Group " + group.getName() + " of size " + group.getSize() + " left table "
                    + table.getTableNumber()
                    + " of size " + table.getSize()
                    + ". Table has " + table.getAvailableSeats() + " available seats left.");

            // Seat next group if possible
            final int availableSeats = table.getAvailableSeats();
            final CustomerGroup nextGroup = findNextGroupForFreeSeatsAndRemoveFromQueue(availableSeats);
            if (nextGroup != null) {
                arrives(nextGroup);
            }
        }
    }

    /* Search queues for groups of size <= availableSeats. Choose group with min ticketNumber */
    private CustomerGroup findNextGroupForFreeSeatsAndRemoveFromQueue(final int availableSeats) {
        long minTicketNumber = Long.MAX_VALUE;
        int queueIndex = -1;
        for (int i = 1; i < availableSeats + 1; i++) {
            if (queues.get(i).size() > 0) {
                final CustomerGroup groupOfSizeI = queues.get(i).get(0);
                if (groupOfSizeI.getTicketNumber() < minTicketNumber) {
                    minTicketNumber = queues.get(i).get(0).getTicketNumber();
                    queueIndex = i;
                }
            }
        }
        if (queueIndex >= 0) {
            return queues.get(queueIndex).remove(0);
        }
        return null;
    }

    /*
     * Return the table at which the group is seated, or null if
     * they are not seated (whether they're waiting or already left).
     */
    public Table locate(CustomerGroup group) {
        return groupTableMap.get(group.getName());
    }

    /* Return list of customerGroups of size n waiting in queue */
    public List<CustomerGroup> getWaitingGroupsOfSizeN(int n) {
        return queues.get(n);
    }
}
