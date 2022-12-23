package com.buddyapp.paymybuddy.mappers;

import com.buddyapp.paymybuddy.DTOs.TransactionDTO;
import com.buddyapp.paymybuddy.DTOs.UserDTO;
import com.buddyapp.paymybuddy.entities.TransactionEntity;
import com.buddyapp.paymybuddy.entities.UserEntity;
import com.buddyapp.paymybuddy.helper.CycleAvoidingMappingContext;
import com.buddyapp.paymybuddy.models.Transaction;
import com.buddyapp.paymybuddy.models.User;
import org.mapstruct.*;


import java.util.List;

@Mapper(unmappedSourcePolicy = ReportingPolicy.WARN, unmappedTargetPolicy = ReportingPolicy.WARN,
        typeConversionPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface UserMapper {
    UserDTO modelToDto(User user);
    List<UserDTO> modelsToDtos(List<User> user);
    User dtoToModel(UserDTO dto);
    List<User> dtosToModels(List<UserDTO> dtos);

    UserEntity modelToEntity(User user);
    List<UserEntity> modelsToEntities(List<User> users);
    User entityToModel(UserEntity entity);
    List<User> entitiesToModel(List<UserEntity> entities);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromModel(User model, @MappingTarget UserEntity entity, @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);
}
