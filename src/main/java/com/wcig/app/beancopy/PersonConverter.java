package com.wcig.app.beancopy;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PersonConverter {
    PersonConverter INSTANCE = Mappers.getMapper(PersonConverter.class);

    PersonDTO do2dto(PersonDO person);
}
