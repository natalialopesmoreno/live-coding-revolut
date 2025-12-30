package org.example;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class LoadBalancer {

    private static final int MAX_CAPACITY_DEFAULT = 10;

    private final List<String> servers = new CopyOnWriteArrayList<>();

    private final  int maxCapacity ;

    private final SelectionStrategy selector;


    public LoadBalancer(int maxCapacity, SelectionStrategy selector) {
        this.selector = selector;
        if(maxCapacity < 1)throw new IllegalArgumentException("Max capacity could not be less than 1");
        this.maxCapacity = maxCapacity;
    }

    public LoadBalancer(SelectionStrategy selector){
        this(MAX_CAPACITY_DEFAULT, selector);

    }

    public void registerServer(String address){
        validateAddress(address);
        synchronized (this){
            if(servers.contains(address)) throw new IllegalArgumentException("The server could not be register because this address already exists");
            if(servers.size() >= maxCapacity) throw new IllegalStateException("The Load Balancer is full. Max capacity: " +maxCapacity);
            servers.add(address);
        }

    }

    public String getServer(){
        if(servers.isEmpty()) throw new IllegalStateException("No servers available");
        return selector.getNextInstance(servers);
    }

    private void validateAddress(String address) {
        if(address == null || address.isEmpty() || address.isBlank()) throw new IllegalArgumentException("Address cannot be null, empty or blank");
    }

}
