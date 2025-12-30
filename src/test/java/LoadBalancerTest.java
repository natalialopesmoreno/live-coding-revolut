import load_balancer.LoadBalancer;
import load_balancer.RandomSelector;
import load_balancer.RoudRobinSelector;
import load_balancer.SelectionStrategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LoadBalancerTest {


    private LoadBalancer balancer;



    @BeforeEach
    void setUp(){
        SelectionStrategy selector = new RandomSelector();
        balancer = new LoadBalancer(10, selector);

    }

    @Test
    void shouldRegisterAddressSuccessFully(){
        balancer.registerServer("Address1");

    }

    @Test
    void shouldNotRegisterEmptyServerAddress(){

        Assertions.assertThrows(IllegalArgumentException.class,() -> balancer.registerServer(""));

    }

    @Test
    void shouldNotRegisterBlankServerAddress(){

        Assertions.assertThrows(IllegalArgumentException.class,() -> balancer.registerServer(" "));

    }

    @Test
    void shouldNotRegisterNullServerAddress(){

        Assertions.assertThrows(IllegalArgumentException.class,() -> balancer.registerServer(null));

    }

    @Test
    void shouldNotRegisterServerWhenAddressAlreadyExists(){

        balancer.registerServer("Address1");

        Assertions.assertThrows(IllegalArgumentException.class,() -> balancer.registerServer("Address1"));

    }

    @Test
    void shouldNotRegisterServerWhenLoadBalanceIsFull(){
        SelectionStrategy selector = new RandomSelector();
        balancer = new LoadBalancer(2, selector);

        balancer.registerServer("Address1");
        balancer.registerServer("Address2");

        Assertions.assertThrows(IllegalStateException.class,() -> balancer.registerServer("Address3"));
    }

    @Test
    void shouldReturnNextServerInRandomWayWhenListIsNotEmpty(){

        balancer.registerServer("Address1");
        balancer.registerServer("Address2");

        Assertions.assertNotNull(balancer.getServer());
    }

    @Test
    void shouldThrowExceptionWhenGettingServerAndEmptyServerList(){

        Assertions.assertThrows(IllegalStateException.class,() -> balancer.getServer());
    }

    @Test
    void shouldReturnRandomNextServeWhenListIsNotEmpty(){


        balancer.registerServer("Address1");
        balancer.registerServer("Address2");
        balancer.registerServer("Address3");
        balancer.registerServer("Address4");
        balancer.registerServer("Address5");
        balancer.registerServer("Address6");

        String firstRandom = balancer.getServer();
        String secondRandom = balancer.getServer();
        String thirdRandom = balancer.getServer();

        Assertions.assertNotEquals(firstRandom, secondRandom);
        Assertions.assertNotEquals(secondRandom, thirdRandom);
    }



    @Test
    void shouldReturnNextServerBasedOnRoundRobinSelectionWhenServersListIsNotEmpty(){
        SelectionStrategy selector = new RoudRobinSelector();
        balancer = new LoadBalancer(selector);

        balancer.registerServer("Address1");
        balancer.registerServer("Address2");
        balancer.registerServer("Address3");

        String firstSelection = balancer.getServer();
        String secondSelection = balancer.getServer();
        String thirdSelection = balancer.getServer();
        String forthSelection = balancer.getServer();

        Assertions.assertNotEquals(firstSelection, secondSelection);
        Assertions.assertNotEquals(secondSelection, thirdSelection);
        Assertions.assertEquals(firstSelection, forthSelection);
    }
}
