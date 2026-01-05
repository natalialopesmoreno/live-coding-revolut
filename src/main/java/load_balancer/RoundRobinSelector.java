package load_balancer;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class RoundRobinSelector implements SelectionStrategy{

    private AtomicInteger counter = new AtomicInteger(0);
    @Override
    public String getNextInstance(List<String> servers) {

        int index = Math.abs(counter.getAndIncrement() % servers.size());

        return servers.get(index);
    }
}
