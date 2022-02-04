package guru.springframework.sfgrecipeapp.service;

import guru.springframework.sfgrecipeapp.commands.UnitOfMeasureCommand;
import guru.springframework.sfgrecipeapp.converters.UnitOfMeasureToUnitOfMeasureCommand;
import guru.springframework.sfgrecipeapp.model.UnitOfMeasure;
import guru.springframework.sfgrecipeapp.repositories.UnitOfMeasureRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class UnitOfMeasureServiceImplTest {
    UnitOfMeasureToUnitOfMeasureCommand command = new UnitOfMeasureToUnitOfMeasureCommand();
    UnitOfMeasureService unitOfMeasureService;

    @Mock
    UnitOfMeasureRepository repository;

    @BeforeEach
    public void setUp() {
        openMocks(this);

        unitOfMeasureService = new UnitOfMeasureServiceImpl(repository, command);
    }

    @Test
    void listAllUoms() {
        Set<UnitOfMeasure> uoms = new HashSet<>();

        UnitOfMeasure uom1 = new UnitOfMeasure();
        uom1.setId(1L);
        uoms.add(uom1);

        UnitOfMeasure uom2 = new UnitOfMeasure();
        uom2.setId(2L);
        uoms.add(uom2);

        when(repository.findAll()).thenReturn(uoms);

        Set<UnitOfMeasureCommand> commands = unitOfMeasureService.listAllUoms();

        assertEquals(2, commands.size());
        verify(repository).findAll();
    }
}