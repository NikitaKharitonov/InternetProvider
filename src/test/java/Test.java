import ru.internetprovider.model.services.InternetState;

import java.util.*;

public class Test {

    public static void main(String[] args) {

        PriorityQueue<InternetState> priorityQueue =
                new PriorityQueue<>(Comparator.comparing(InternetState::getBeginDate));
        long nanoTime = System.currentTimeMillis();
        for (int i = 0; i < 10; ++i)
            priorityQueue.add(new InternetState(new Date(nanoTime + 1_000_000 * i), null, 0, false, null));

        priorityQueue.forEach(o -> {
            Date date = o.getBeginDate();
            System.out.println(date);
        });

        System.out.println("Head " + priorityQueue.peek());
        System.out.println("___________________________");
        priorityQueue.clear();

        for (int i = 9; i > -1; --i)
            priorityQueue.add(new InternetState(new Date(nanoTime + 1_000_000 * i), null, 0, false, null));

        priorityQueue.forEach(o -> {
            Date date = o.getBeginDate();
            System.out.println(date);
        });
        System.out.println("Head " + priorityQueue.peek());







    }
}
