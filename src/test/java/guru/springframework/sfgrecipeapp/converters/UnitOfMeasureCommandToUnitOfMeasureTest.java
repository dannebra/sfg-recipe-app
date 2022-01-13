package guru.springframework.sfgrecipeapp.converters;

import guru.springframework.sfgrecipeapp.commands.UnitOfMeasureCommand;
import guru.springframework.sfgrecipeapp.model.UnitOfMeasure;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnitOfMeasureCommandToUnitOfMeasureTest {
    private final UnitOfMeasureCommandToUnitOfMeasure converter = new UnitOfMeasureCommandToUnitOfMeasure();

    @Test
    void testNullSource() {
        assertNull(converter.convert(null));
    }

    @Test
    void convert() {
        UnitOfMeasureCommand uomc = new UnitOfMeasureCommand();
        Long ID = 1L;
        uomc.setId(ID);
        String DESCRIPTION = "Test";
        uomc.setDescription(DESCRIPTION);

        UnitOfMeasure unitOfMeasure = converter.convert(uomc);

        assertNotNull(unitOfMeasure);
        assertEquals(unitOfMeasure.getId(), ID);
        assertEquals(unitOfMeasure.getDescription(), DESCRIPTION);
    }
}