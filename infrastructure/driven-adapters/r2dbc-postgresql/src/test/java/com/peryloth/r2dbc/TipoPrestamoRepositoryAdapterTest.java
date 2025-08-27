package com.peryloth.r2dbc;

import com.peryloth.model.tipoprestamo.TipoPrestamo;
import com.peryloth.r2dbc.entities.TipoPrestamoEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class TipoPrestamoRepositoryAdapterTest {

    @Mock
    private TipoPrestamoReactRepository repository;

    @Mock
    private ObjectMapper mapper;

    @InjectMocks
    private TipoPrestamoRepositoryAdapter adapter;

    private TipoPrestamo domain;
    private TipoPrestamoEntity entity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        domain = TipoPrestamo.builder()
                .idTipoPrestamo(1L)
                .nombre("Normal")
                .build();

        entity = TipoPrestamoEntity.builder()
                .idTipoPrestamo(1L)
                .nombre("Normal")
                .build();
    }

    @Test
    void testSave() {
        when(mapper.map(domain, TipoPrestamoEntity.class)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(Mono.just(entity));
        when(mapper.map(entity, TipoPrestamo.class)).thenReturn(domain);

        StepVerifier.create(adapter.save(domain))
                .expectNext(domain)
                .verifyComplete();
    }

    @Test
    void testFindById() {
        when(repository.findById(1L)).thenReturn(Mono.just(entity));
        when(mapper.map(entity, TipoPrestamo.class)).thenReturn(domain);

        StepVerifier.create(adapter.findById(1L))
                .expectNext(domain)
                .verifyComplete();
    }

    @Test
    void testFindByName_found() {
        when(repository.findByNombre("Normal")).thenReturn(Mono.just(entity));
        when(mapper.map(entity, TipoPrestamo.class)).thenReturn(domain);

        StepVerifier.create(adapter.findByName("Normal"))
                .expectNext(domain)
                .verifyComplete();
    }

    @Test
    void testFindByName_notFound() {
        when(repository.findByNombre("Inexistente")).thenReturn(Mono.empty());

        StepVerifier.create(adapter.findByName("Inexistente"))
                .expectErrorMatches(ex -> ex instanceof IllegalArgumentException &&
                        ex.getMessage().contains("TipoPrestamo no encontrado"))
                .verify();
    }

    @Test
    void testFindAll() {
        when(repository.findAll()).thenReturn(Flux.just(entity));
        when(mapper.map(entity, TipoPrestamo.class)).thenReturn(domain);

        StepVerifier.create(adapter.findAll())
                .expectNext(domain)
                .verifyComplete();
    }

    @Test
    void testDeleteById() {
        when(repository.deleteById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(adapter.deleteById(1L))
                .verifyComplete();
    }
}
