package org.example;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RandomSelector implements  SelectionStrategy{
    @Override
    public String getNextInstance(List<String> servers) {
        return servers.get(ThreadLocalRandom.current().nextInt(servers.size()));
    }
}
