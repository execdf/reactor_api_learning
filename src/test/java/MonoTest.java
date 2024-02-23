
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.time.Duration;
import java.util.function.Consumer;

public class MonoTest {

    static Logger logger = LoggerFactory.getLogger(MonoTest.class.getName());





    @Test
    public void testCheckPoint(){
        Mono.just("Hello")
                .checkpoint("before map")
                .map(value -> {
                    if (value.equals("Hello")) {
                        logger.debug("This is a debug message.");
                        throw new RuntimeException("Error occurred!");

                    }
                    return value.toUpperCase();
                })
                .doOnNext(value -> System.out.println("doOnNext: " + value))
                .checkpoint("After map")
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) {
                        System.out.println(s);
                    }
                });
    }
}
