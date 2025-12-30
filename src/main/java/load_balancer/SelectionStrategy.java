package load_balancer;

import java.util.List;

public interface SelectionStrategy {

    String getNextInstance(List<String> servers);
}
