package org.example;

import java.util.List;

public interface SelectionStrategy {

    String getNextInstance(List<String> servers);
}
