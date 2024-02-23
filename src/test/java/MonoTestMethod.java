import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.util.function.Tuple2;

import java.time.Duration;
import java.util.function.Function;
import java.util.function.Predicate;

public class MonoTestMethod {

    @Test
    public void testCache(){

        // 创建一个 Mono，每次订阅时会生成一个随机数
        Mono<Integer> sourceMono = Mono.fromSupplier(() -> (int) (Math.random() * 100));

        // 不带缓存的情况
        System.out.println("Without cache:");
        for (int i = 0; i < 5; i++) {
            sourceMono.subscribe(value -> System.out.println("Subscriber 1: " + value));
        }

        // 带有缓存的情况
        System.out.println("\nWith cache:");
        Mono<Integer> cachedMono = sourceMono.cache(Duration.ofMillis(100));
        for (int i = 0; i < 10; i++) {
            cachedMono.subscribe(value -> System.out.println("Subscriber 2: " + value));
        }


    }


    @Test
    public void testMap() throws InterruptedException {

        //null值就不会被向下传递
        // 创建一个 Mono，每次订阅时会生成一个随机数
        String[] strings = new String[]{null, null, "123", null, "bdfg", null};
        Mono<String> mono = Mono.fromSupplier(() -> {
            int i = (int) (Math.random() * 6);

            return strings[i];
        });
        for (int i = 0; i < 30; i++) {
            mono
                    .map(ele -> {
                        System.out.println("map ---"+ele+ "  --");
                        return ele;
                    }).subscribe(s -> System.out.println("sub---"+s));

        }


    }

    @Test
    public void testHandle() {

        //null值就不会被向下传递
        // 创建一个 Mono，每次订阅时会生成一个随机数
        String[] strings = new String[]{null, null, "123", null, "bdfg", null};
        Mono<String> mono = Mono.fromSupplier(() -> {
            int i = (int) (Math.random() * 6);

            return strings[i];
        });
        for (int i = 0; i < 30; i++) {
            Mono<Integer> handledMono = mono.handle((s, sink) -> {
                if ("123".equals(s)) {
                    sink.next(12345);
                } else {
                    //sink.complete();
                }
            });

            handledMono.subscribe(
                    System.out::println,  // 成功处理时的回调函数
                    error -> System.err.println("Error: " + error.getMessage()),  // 处理失败时的回调函数
                    ()->{
                        System.out.println("完成");
                    }
            );
        }


    }





    @Test
    //从发射的时候将时间戳放置好，在消费终端就做减法获取差值
    public void testElapsed() {
        // 创建一个简单的 Mono，这里用一个延迟的方式来模拟
        String[] strings = new String[]{"123asd", null, "123", null, "bdfg", null};
        Mono<String> mono = Mono.fromSupplier(() -> {
            int i = (int) (Math.random() * 6);

            return strings[i];
        });
        ;
        for (int i = 0; i < 30; i++) {
             mono.delayElement(Duration.ofSeconds(1)).
                     handle((s, sink) -> {
                if (s!=null) {
                    sink.next(s);
                } else {
                    //sink.complete();
                }
            })
                     .elapsed()
                     .subscribe(tuple -> {
                         long duration = tuple.getT1(); // 获取经过的时间
                         Object value = tuple.getT2(); // 获取原始元素
                         System.out.println("Received value: " + value +"     Time elapsed: " + duration + " milliseconds");
                     });

        }

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


        @Test
        public void testCacheInvalidateIf() {

            String[] strings = new String[]{"123asd", null, "123", null, "bdfg", null};
            Mono<String> mono = Mono.fromSupplier(() -> {
                int i = (int) (Math.random() * 6);
                System.out.println("index "+i +"   "+strings[i] );
                return strings[i];
            }).cacheInvalidateIf(s -> {
                if(s!=null){
                    System.out.println("handler "+s +"   ---");
                    return true;
                }else {
                    return false;
                }});

            for (int i = 0; i < 20; i++) {
                mono.subscribe(e->{
                    System.out.println("rec 1 "+e);
                });
                mono.subscribe(e->{
                    System.out.println("rec 2 "+e);
                });
            }


        }


    @Test
    public void testd(){

        String[] strings = new String[]{"123asd", null, "123", null, "bdfg", null};
        Mono<String> mono = Mono.fromSupplier(() -> {
            int i = (int) (Math.random() * 6);
            System.out.println("index "+i +"   "+strings[i] );
            return strings[i];
        });

    }


}
