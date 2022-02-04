package guru.springframework.sfgrecipeapp.service;

import guru.springframework.sfgrecipeapp.commands.UnitOfMeasureCommand;

import java.util.Set;

public interface UnitOfMeasureService {

    Set<UnitOfMeasureCommand> listAllUoms();
}
