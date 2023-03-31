package kt.reactive.mywebflux.handler;



import kt.reactive.mywebflux.entity.Customer;
import kt.reactive.mywebflux.exception.CustomAPIException;
import kt.reactive.mywebflux.repository.R2CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CustomerHandler {
    private final R2CustomerRepository customerRepository;
    private Mono<ServerResponse> response406 =
            ServerResponse.status(HttpStatus.NOT_ACCEPTABLE).build();

    public Mono<ServerResponse> getCustomers(ServerRequest request) {
        Flux<Customer> customerFlux = customerRepository.findAll();
        return ServerResponse.ok() //ServerResponse.BodyBuilder
                .contentType(MediaType.APPLICATION_JSON) //ServerResponse.BodyBuilder
                .body(customerFlux, Customer.class); //Mono<ServerResponse>
    }

    public Mono<ServerResponse> getCustomer(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));
        return customerRepository.findById(id)
                .flatMap(customer -> ServerResponse.ok()  //ServerResponse.BodyBuilder
                        .contentType(MediaType.APPLICATION_JSON)  //ServerResponse.BodyBuilder
                        .body(BodyInserters.fromValue(customer))
                ).switchIfEmpty(getError(id));
    }

    private Mono<ServerResponse> getError(Long id) {
        return Mono.error(new CustomAPIException("Customer Not Found with id " + id, HttpStatus.NOT_FOUND));
    }

    public Mono<ServerResponse> saveCustomer(ServerRequest request) {
        Mono<Customer> unSavedCustomerMono = request.bodyToMono(Customer.class);
        return unSavedCustomerMono.flatMap(customer ->
                customerRepository.save(customer)
                        .flatMap(savedCustomer ->
                                ServerResponse.accepted()
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(savedCustomer)
                        )
        ).switchIfEmpty(response406);
    }

    public Mono<ServerResponse> updateCustomer(ServerRequest request) {
        long id = Long.parseLong(request.pathVariable("id")); //ctrl + alt + v 는 리턴값 알려줌.
        Mono<Customer> unUpdatedCustomerMono = request.bodyToMono(Customer.class);

        Mono<Customer> updatedCustomerMono = unUpdatedCustomerMono.flatMap(customer ->
                customerRepository.findById(id)
                        .flatMap(existCustomer -> {
                            existCustomer.setFirstName(customer.getFirstName());
                            existCustomer.setLastName(customer.getLastName());
                            return customerRepository.save(existCustomer);
                        })
        );
        return updatedCustomerMono.flatMap(customer -> ServerResponse.accepted()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(customer)).switchIfEmpty(getError(id));
    }
}
/* 세번째
import kt.reactive.mywebflux.entity.Customer;
import kt.reactive.mywebflux.exception.CustomAPIException;
import kt.reactive.mywebflux.repository.R2CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CustomerHandler {
    private final R2CustomerRepository customerRepository;
    private Mono<ServerResponse> response406 =
            ServerResponse.status(HttpStatus.NOT_ACCEPTABLE).build();
    public Mono<ServerResponse> getCustomers(ServerRequest request) {
        Flux<Customer> customerFlux = customerRepository.findAll();
        return ServerResponse.ok() //ServerResponse.BodyBuilder
                .contentType(MediaType.APPLICATION_JSON) //ServerResponse.BodyBuilder
                .body(customerFlux, Customer.class); //Mono<ServerResponse>
    }
    public Mono<ServerResponse> getCustomer(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));
        return customerRepository.findById(id)
                .flatMap(customer -> ServerResponse.ok()  //ServerResponse.BodyBuilder
                        .contentType(MediaType.APPLICATION_JSON)  //ServerResponse.BodyBuilder
                        .body(BodyInserters.fromValue(customer))
                ).switchIfEmpty(getError(id));
    }
    private Mono<ServerResponse> getError(Long id) {
        return Mono.error(new CustomAPIException("Customer Not Found with id " + id, HttpStatus.NOT_FOUND));
    }

    public Mono<ServerResponse> saveCustomer(ServerRequest request) {
        Mono<Customer> unSavedCustomerMono = request.bodyToMono(Customer.class);
        return unSavedCustomerMono.flatMap(customer ->
                customerRepository.save(customer)
                        .flatMap(savedCustomer ->
                                ServerResponse.accepted()
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(savedCustomer)
                        )
        ).switchIfEmpty(response406);
    }
}*/

/* 두번째
import kt.reactive.mywebflux.entity.Customer;
import kt.reactive.mywebflux.exception.CustomAPIException;
import kt.reactive.mywebflux.repository.R2CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CustomerHandler {
    private final R2CustomerRepository customerRepository;
    private Mono<ServerResponse> response406 = ServerResponse.status(HttpStatus.NOT_ACCEPTABLE).build();
    public Mono<ServerResponse> getCustomers(ServerRequest request) {
        Flux<Customer> customerFlux = customerRepository.findAll();
        return ServerResponse.ok() //ServerResponse.BodyBuilder
                .contentType(MediaType.APPLICATION_JSON) //ServerResponse.BodyBuilder
                .body(customerFlux, Customer.class); //Mono<ServerResponse>
    }
    public Mono<ServerResponse> getCustomer(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));
        return customerRepository.findById(id)
                .flatMap(customer -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(customer))
                ).switchIfEmpty(getError(id));
    }
    private Mono<ServerResponse> getError(Long id) {
        return Mono.error(new CustomAPIException("Customer Not Found with id " + id, HttpStatus.NOT_FOUND));
    }
}*/

/* 첫번째 CustomerHandler
import kt.reactive.mywebflux.entity.Customer;
import kt.reactive.mywebflux.repository.R2CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CustomerHandler {

    private final R2CustomerRepository customerRepository;
    private Mono<ServerResponse> response406 = ServerResponse.status(HttpStatus.NOT_ACCEPTABLE).build();

    public Mono<ServerResponse> getCustomers(ServerRequest request) {
        Flux<Customer> customerFlux = customerRepository.findAll();
        return ServerResponse.ok() //ServerResponse.BodyBuilder
                .contentType(MediaType.APPLICATION_JSON) //ServerResponse.BodyBuilder
                .body(customerFlux, Customer.class); //Mono<ServerResponse>
    }
}  */