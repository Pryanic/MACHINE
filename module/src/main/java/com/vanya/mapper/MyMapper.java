
package com.vanya.mapper;

import com.vanya.api.DataObject;
import com.vanya.domain.DBObject;
import com.vanya.model.ValueObject;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ObjectFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Mapper
@Component
public interface MyMapper {


    default ValueObject toValueObject(final DataObject obj) {

        return Optional.ofNullable(obj)
                       .map(this::toValueObjectBuilder)
                       .map(ValueObject.ValueObjectBuilder::build)
                       .orElse(null);
    }

    default ValueObject toValueObject(final DBObject obj) {

        return Optional.ofNullable(obj)
                       .map(this::toValueObjectBuilder)
                       .map(ValueObject.ValueObjectBuilder::build)
                       .orElse(null);
    }


    DataObject toEquipmentApi(final ValueObject val);


    @Mappings({
            @Mapping(target = "isBig", source = "big")
    })
    ValueObject.ValueObjectBuilder toValueObjectBuilder(final DataObject obj);

    @Mappings({
            @Mapping(target = "isBig", source = "big")
    })
    ValueObject.ValueObjectBuilder toValueObjectBuilder(final DBObject obj);

    @ObjectFactory
    default ValueObject.ValueObjectBuilder createDomainBuilder() {
        return ValueObject.builder();
    }
}
