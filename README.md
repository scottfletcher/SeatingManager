Welcome to my SeatingManager simulation! Feel free to run App.java to try it out in the console :)

Here's what my seating manager can do:

Assuming we have groups up to size 6 and 20 tables

- seat groups or put them in the queue in O(20) time
- find the next group to be seated in O(6) time
- find where a group is seated in O(1) time (on average, using a HashMap)

My simulation uses storage space of O(20 + Nc + Nc) where 20 is the number of tables, and Nc is the number of customers
    - this is broken down into an array of Table objects, a HashMap of Customer names to the Tables they're sitting at, and arrays containing the waiting customers / the customers at each table (each customerGroup present exactly once in one of the arrays)

Here is my concept:
- When a group arrives, they are seated at the smallest table which will fit them (worst case: searching all tables - O(20))
- If they cannot be seated, they are given an incrementing ticketNumber and put into a queue specifically for groups of their size (6 queues in total)
- When a group leaves, the seatingManager checks the queues for sizes equal to or lower than the newly available seats and selects the group with the lowest ticket number (O(6))

This method ensures that seating is done opportunistically (every free seat is filled if possible) and fairly (people who arrived earlier always have precedence).
Giving groups a ticket number and putting them into separate queues is optimal over putting them all in one sorted queue because it avoids the worst case scenario of searching through thousands of groups that are too big to find the next group to seat (O(Nc) where Nc is the number of customers).

Possible improvements
- I realized after finishing that removing the first object in an ArrayList actually has a time complexity of O(n) as all other items need to be shifted. I could have used Linked Lists instead, with pointers to the start and finish, to make adding and removing truly constant time.
- I didn't consider that groups can also leave while standing in the queue and implemented it using a simple ArrayList::remove which is O(Nq) where Nq is the number of groups in that queue. It could have been constant if I used a HashMap lookup for the groups' position in the queues, increasing the storage complexity.
- There's at least one case that I overlooked, which is when there is space leftover after the next group sits down. For example, a group of 6 leaves and a group of 2 sits down. If there is a group of 4 waiting, they could fill up the remaining seats. This could have been accomplished with a simple while loop, making the complexity of filling up a table after someone leaves O(6 * 6), with the worst case being 6 groups of 1 person.

Thank you for reading, I look forward to your feedback!
ご覧くださり、ありがとうございます！

Scott
