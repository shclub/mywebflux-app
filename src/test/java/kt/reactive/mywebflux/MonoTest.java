package kt.reactive.mywebflux;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class MonoTest {
    @Test
    public void justMono() {
        Mono<String> stringMono = Mono.just("Welcome to Webflux")
                .map(msg -> msg.concat(".com"))
                .map(msg -> msg.toUpperCase())
                .log();
        //System.out.println(stringMono);
        stringMono.subscribe(System.out::println);

        StepVerifier.create(stringMono)
                .expectNext("WELCOME TO WEBFLUX.COM")
                .verifyComplete();
    }

    @Test
    public void errorMono() {
        Mono<String> errorMono =
                Mono.error(new RuntimeException("Check Error Mono"));

        errorMono.subscribe(
                value -> {
                    System.out.println("onNext " + value);
                },
                error -> {
                    System.out.println("onError " + error.getMessage());
                },
                () -> {
                    System.out.println("OnComplete ");
                }
        );

        StepVerifier.create(errorMono)
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    public void fromSupplier() {
        //Supplier<String> StrSupplier = () -> "Supplier Message";
        Mono<String> stringMono = Mono.fromSupplier(() -> "Supplier Message").log();
        stringMono.subscribe(System.out::println);

        //verifyComplete() = expectComplete() + verify()
        StepVerifier.create(stringMono)
                .expectNext("Supplier Message")
                //.verifyComplete();
                .expectComplete()
                .verify();
    }

}