import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

public class SinkTest {

    @Test
    public void testEmit(){
        //sink 就是一个广播的功能
        Sinks.Many<String> sink = Sinks.many().multicast().onBackpressureBuffer();

        Flux<String> flux = sink.asFlux();

        flux.subscribe(
                value -> System.out.println("Received 1: " + value),
                error -> System.err.println("Error 1: " + error),
                //接收用于清理资源
                () -> System.out.println("Stream completed 1 ")
        );
        flux.subscribe(
                value -> System.out.println("Received 2: " + value),
                error -> System.err.println("Error 2: " + error),
                //接收用于清理资源
                () -> System.out.println("Stream completed 2")
        );

        sink.tryEmitNext("Hello");
        sink.tryEmitNext("World");
        sink.tryEmitComplete(); // 发送完成信号
    }

    @Test
    public void testSink(){

    }
}
