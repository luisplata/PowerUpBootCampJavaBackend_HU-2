package com.peryloth.r2dbc;

import com.peryloth.model.estados.Estados;
import com.peryloth.r2dbc.entities.EstadoEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EstadoRepositoryAdapterTest {

    private EstadoReactRepository repository;
    private ObjectMapper mapper;
    private EstadoRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(EstadoReactRepository.class);
        mapper = Mockito.mock(ObjectMapper.class);
        adapter = new EstadoRepositoryAdapter(repository, mapper);
    }

    @Test
    void saveEstado_shouldReturnSavedEstado() {
        // Arrange
        Estados domain = new Estados(1L, "Pendiente");
        EstadoEntity entity = new EstadoEntity(1L, "Pendiente", "Pendiente");

        when(mapper.map(domain, EstadoEntity.class)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(Mono.just(entity));
        when(mapper.map(entity, Estados.class)).thenReturn(domain);

        // Act
        Mono<Estados> result = adapter.saveEstado(domain);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(e -> e.getIdEstado().equals(1L) && e.getNombre().equals("Pendiente"))
                .verifyComplete();

        verify(repository).save(any(EstadoEntity.class));
    }

    @Test
    void getEstadoById_shouldReturnEstado() {
        // Arrange
        Long id = 1L;
        EstadoEntity entity = new EstadoEntity(id, "Pendiente", "Pendiente");
        Estados domain = new Estados(id, "Pendiente");

        when(repository.findById(id)).thenReturn(Mono.just(entity));
        when(mapper.map(entity, Estados.class)).thenReturn(domain);

        // Act
        Mono<Estados> result = adapter.getEstadoById(id);

        // Assert
        StepVerifier.create(result)
                .expectNext(domain)
                .verifyComplete();

        verify(repository).findById(id);
    }

    @Test
    void getEstadoById_shouldReturnErrorWhenNotFound() {
        // Arrange
        Long id = 1L;
        when(repository.findById(id)).thenReturn(Mono.empty());

        // Act
        Mono<Estados> result = adapter.getEstadoById(id);

        // Assert
        StepVerifier.create(result)
                .expectErrorMatches(e -> e instanceof IllegalArgumentException &&
                        e.getMessage().equals("Estado no encontrado con id=" + id))
                .verify();

        verify(repository).findById(id);
    }

    @Test
    void getAllEstados_shouldReturnFluxOfEstados() {
        // Arrange
        EstadoEntity entity1 = new EstadoEntity(1L, "Pendiente", "Pendiente");
        EstadoEntity entity2 = new EstadoEntity(2L, "Aprobado", "Aprobado");

        Estados domain1 = new Estados(1L, "Pendiente");
        Estados domain2 = new Estados(2L, "Aprobado");

        when(repository.findAll()).thenReturn(Flux.just(entity1, entity2));
        when(mapper.map(entity1, Estados.class)).thenReturn(domain1);
        when(mapper.map(entity2, Estados.class)).thenReturn(domain2);

        // Act
        Flux<Estados> result = adapter.getAllEstados();

        // Assert
        StepVerifier.create(result)
                .expectNext(domain1, domain2)
                .verifyComplete();

        verify(repository).findAll();
    }
}
