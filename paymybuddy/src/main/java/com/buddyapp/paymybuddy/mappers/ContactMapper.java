package com.buddyapp.paymybuddy.mappers;

import com.buddyapp.paymybuddy.DTOs.ContactDTO;
import com.buddyapp.paymybuddy.entities.ContactEntity;
import com.buddyapp.paymybuddy.helper.CycleAvoidingMappingContext;
import com.buddyapp.paymybuddy.models.Contact;
import org.mapstruct.*;


import java.util.List;

@Mapper(unmappedSourcePolicy = ReportingPolicy.WARN, unmappedTargetPolicy = ReportingPolicy.WARN,
        typeConversionPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface ContactMapper {
    ContactDTO modelToDto(Contact contact);
    List<ContactDTO> modelsToDtos(List<Contact> contacts);
    Contact dtoToModel(ContactDTO dto);
    List<Contact> dtosToModels(List<ContactDTO> dtos);

    ContactEntity modelToEntity(Contact contact);
    List<ContactEntity> modelsToEntities(List<Contact> contacts);
    Contact entityToModel(ContactEntity entity);
    List<Contact> entitiesToModels(List<ContactEntity> entities);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateContactFromDto(ContactDTO dto, @MappingTarget Contact model, @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);
}
