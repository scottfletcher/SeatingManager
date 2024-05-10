import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class SeatingManagerTest {

    private final List<Table> tables = new ArrayList<Table>();

    @Before
    public void setUp() {
        tables.add(new Table(1, 2));
        tables.add(new Table(2, 2));
        tables.add(new Table(3, 2));
        tables.add(new Table(4, 3));
        tables.add(new Table(5, 4));
        tables.add(new Table(6, 5));
        tables.add(new Table(7, 5));
        tables.add(new Table(8, 6));
    }

    @Test
    public void testFillAllTablesNooneInQueue() {
        SeatingManager seatingManager = new SeatingManager(tables);

        seatingManager.arrives(new CustomerGroup(2));
        seatingManager.arrives(new CustomerGroup(2));
        seatingManager.arrives(new CustomerGroup(2));
        seatingManager.arrives(new CustomerGroup(3));
        seatingManager.arrives(new CustomerGroup(4));
        seatingManager.arrives(new CustomerGroup(5));
        seatingManager.arrives(new CustomerGroup(5));
        seatingManager.arrives(new CustomerGroup(6));

        final List<CustomerGroup> groupsSize2 = seatingManager.getWaitingGroupsOfSizeN(2);
        assertNotNull(groupsSize2);
        assertTrue(groupsSize2.isEmpty());
        final List<CustomerGroup> groupsSize3 = seatingManager.getWaitingGroupsOfSizeN(3);
        assertNotNull(groupsSize3);
        assertTrue(groupsSize3.isEmpty());
        final List<CustomerGroup> groupsSize4 = seatingManager.getWaitingGroupsOfSizeN(4);
        assertNotNull(groupsSize4);
        assertTrue(groupsSize4.isEmpty());
        final List<CustomerGroup> groupsSize5 = seatingManager.getWaitingGroupsOfSizeN(5);
        assertNotNull(groupsSize5);
        assertTrue(groupsSize5.isEmpty());
        final List<CustomerGroup> groupsSize6 = seatingManager.getWaitingGroupsOfSizeN(6);
        assertNotNull(groupsSize6);
        assertTrue(groupsSize6.isEmpty());
    }

    @Test
    public void freeingTableShouldSeatNextGroup() {
        SeatingManager seatingManager = new SeatingManager(tables);
        final CustomerGroup groupToLeave = new CustomerGroup(2);

        // Fill all tables
        seatingManager.arrives(groupToLeave);
        seatingManager.arrives(new CustomerGroup(2));
        seatingManager.arrives(new CustomerGroup(2));
        seatingManager.arrives(new CustomerGroup(3));
        seatingManager.arrives(new CustomerGroup(4));
        seatingManager.arrives(new CustomerGroup(5));
        seatingManager.arrives(new CustomerGroup(5));
        seatingManager.arrives(new CustomerGroup(6));

        // next group arrives, waits
        seatingManager.arrives(new CustomerGroup(2));
        final List<CustomerGroup> groupsSize2 = seatingManager.getWaitingGroupsOfSizeN(2);
        assertNotNull(groupsSize2);
        assertTrue(groupsSize2.size() == 1);

        // group of size 2 leaves
        seatingManager.leaves(groupToLeave);

        // waiting group of size 2 should be seated
        final List<CustomerGroup> groupsSize2AfterLeaving = seatingManager.getWaitingGroupsOfSizeN(2);
        assertNotNull(groupsSize2AfterLeaving);
        assertTrue(groupsSize2AfterLeaving.isEmpty());
    }

    @Test
    public void exampleFromPrompt() {
        SeatingManager seatingManager = new SeatingManager(tables);

        final CustomerGroup groupOf2SittingAtTableFor6 = new CustomerGroup(2);
        final CustomerGroup groupOf2WhoArrivesLater = new CustomerGroup(2);

        seatingManager.arrives(new CustomerGroup(2));
        seatingManager.arrives(new CustomerGroup(2));
        seatingManager.arrives(new CustomerGroup(2));
        seatingManager.arrives(new CustomerGroup(3));
        seatingManager.arrives(new CustomerGroup(4));
        seatingManager.arrives(new CustomerGroup(5));
        seatingManager.arrives(new CustomerGroup(5));
        seatingManager.arrives(groupOf2SittingAtTableFor6);

        // group of 6 arrives, group of 2 is already at table for 6
        seatingManager.arrives(new CustomerGroup(6));

        // group of 6 is waiting
        final List<CustomerGroup> groupsSize6 = seatingManager.getWaitingGroupsOfSizeN(6);
        assertNotNull(groupsSize6);
        assertTrue(groupsSize6.size() == 1);

        // group of 2 arrives, group of 6 is waiting
        seatingManager.arrives(groupOf2WhoArrivesLater);

        // group of 2 gets seated at table for 6, group of 6 is waiting
        final List<CustomerGroup> groupsSize2 = seatingManager.getWaitingGroupsOfSizeN(2);
        assertNotNull(groupsSize2);
        assertTrue(groupsSize2.isEmpty());

        // group of 6 is waiting
        final List<CustomerGroup> groupsSize6AfterArrival = seatingManager.getWaitingGroupsOfSizeN(6);
        assertNotNull(groupsSize6AfterArrival);
        assertTrue(groupsSize6AfterArrival.size() == 1);

        // both groups of 2 leaves, group of 6 is finally seated
        seatingManager.leaves(groupOf2SittingAtTableFor6);
        seatingManager.leaves(groupOf2WhoArrivesLater);
        final List<CustomerGroup> groupsSize6AfterLeaving = seatingManager.getWaitingGroupsOfSizeN(6);
        assertNotNull(groupsSize6AfterLeaving);
    }
}
