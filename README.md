# ⚖️ Java Load Balancer

![Java](https://img.shields.io/badge/Java-17%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Status](https://img.shields.io/badge/Status-Stable-green?style=for-the-badge)
![License](https://img.shields.io/badge/License-MIT-blue?style=for-the-badge)

A lightweight, **thread-safe**, and extensible in-memory **Load Balancer** implementation written in pure Java. This project employs robust design patterns to ensure high performance and safety in concurrent environments.

## 🚀 Features

* **Thread-Safety:** Built with concurrency in mind.
  * Uses `CopyOnWriteArrayList` for extremely fast, non-blocking reads of the server list.
  * Uses `AtomicInteger` for atomic counters in the Round Robin algorithm.
  * Synchronized writing (`synchronized`) for safe server registration.
* **Strategy Pattern:** The selection logic is decoupled, allowing for easy switching of algorithms (e.g., Random vs. Round Robin) without modifying the core class.
* **Configurable Capacity:** Set a maximum server limit to prevent excessive memory consumption.
* **Robust Validation:** Built-in protection against null, empty, or duplicate server addresses.

---

## 🛠️ Architecture

The project follows the **Strategy Pattern** to define how the next server instance is selected.

* **`LoadBalancer`**: The context that manages the server registry and delegates the selection to a strategy.
* **`SelectionStrategy`**: Interface defining the selection contract.
* **`RandomSelector`**: Selects a server randomly (using `ThreadLocalRandom` for better multi-threaded performance).
* **`RoudRobinSelector`**: Selects servers sequentially and circularly.

---

## 📦 How to Use

### 1. Installation
Clone the repository and import the project into your favorite IDE (IntelliJ, Eclipse, VS Code). Ensure you have the **JDK** installed.

### 2. Code Examples

#### Using Round Robin (Sequential)
Ideal for distributing load equally across servers.

```java
import load_balancer.LoadBalancer;
import load_balancer.RoudRobinSelector;

public class Main {
    public static void main(String[] args) {
        // 1. Instantiate the strategy
        RoudRobinSelector strategy = new RoudRobinSelector();
        
        // 2. Create the Load Balancer with a capacity of 5 servers
        LoadBalancer lb = new LoadBalancer(5, strategy);

        // 3. Register servers
        lb.registerServer("192.168.0.1");
        lb.registerServer("192.168.0.2");

        // 4. Get the next instance
        System.out.println(lb.getServer()); // Output: 192.168.0.1
        System.out.println(lb.getServer()); // Output: 192.168.0.2
        System.out.println(lb.getServer()); // Output: 192.168.0.1 (Loop)
    }
}
```


#### Using Random
Ideal when servers have similar capacities and strict ordering is not required.

```java
import load_balancer.LoadBalancer;
import load_balancer.RandomSelector;

public class App {
    public static void main(String[] args) {
        LoadBalancer lb = new LoadBalancer(new RandomSelector());
        
        lb.registerServer("Server_A");
        lb.registerServer("Server_B");
        lb.registerServer("Server_C");

        // Returns a random server from the list
        System.out.println("Destination: " + lb.getServer());
    }
}

```


## 🧪 Testing

The project includes a comprehensive unit test suite using **JUnit 5**. The tests cover:

* ✅ Successful server registration.
* ✅ Error validation (null, empty, duplicates).
* ✅ Capacity limits (Max Capacity).
* ✅ Strategy behavior verification (Random vs. Round Robin).
* ✅ State exceptions (Empty server list).

To run the tests, execute the following command in the project root (if using Maven):

```bash
mvn test
```

## ⚙️ Technical Decisions

| Component | Decision | Rationale |
| :--- | :--- | :--- |
| **Server List** | `CopyOnWriteArrayList` | Load Balancers typically handle **many reads** (`getServer`) and **few writes** (`registerServer`). This list ensures thread-safe reads without the overhead of heavy locking mechanisms. |
| **Concurrency** | `AtomicInteger` | In `RoudRobinSelector`, we use atomic integers to ensure that two threads do not grab the same index simultaneously, avoiding the performance cost of a `synchronized` block. |
| **Randomness** | `ThreadLocalRandom` | More performant than `Math.random()` or `new Random()` in multi-threaded environments, as it avoids contention on the random number generator's seed. |
