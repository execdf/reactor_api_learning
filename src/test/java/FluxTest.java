import org.junit.Test;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SynchronousSink;

import java.time.Duration;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class FluxTest {


    @Test
    public void testTake() throws InterruptedException {
        String[] strings = new String[]{"123", "asd", "123", null, "bdfg", null};
        Flux.fromArray(strings)
                .filter(Objects::nonNull)  // 过滤掉null元素
                .onErrorResume((Function<Throwable, Mono<String>>) throwable -> Mono.just("123"))
                .handle((BiConsumer<String, SynchronousSink<String>>) (s, sink) ->{
                    sink.next(s);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                })
                .take(Duration.ofMillis(300))
                .subscribe(e-> System.out.println(e));
    }


    public void checkCache(){

    }




}
