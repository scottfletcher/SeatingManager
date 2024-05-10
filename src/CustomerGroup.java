public class CustomerGroup {
    private final String name; // Group name (only used for console output)
    private final int size; // number of people in the group
    private Long ticketNumber; // this group's number in the waiting queue

    public CustomerGroup(int size) {
        this.name = Math.random() + "";
        this.size = size;
    }

    public CustomerGroup(String name, int size) {
        this.name = name;
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public void setTicketNumber(long ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public long getTicketNumber() {
        if (ticketNumber == null) {
            return -1;
        } else {
            return ticketNumber;
        }
    }

    public String getName() {
        return name;
    }
}
