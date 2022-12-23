package com.buddyapp.paymybuddy.mappers;

import com.buddyapp.paymybuddy.DTOs.UserDTO;
import com.buddyapp.paymybuddy.entities.UserEntity;
import com.buddyapp.paymybuddy.helper.CycleAvoidingMappingContext;
import com.buddyapp.paymybuddy.models.MyUser;
import org.mapstruct.*;


import java.util.List;

@Mapper(unmappedSourcePolicy = ReportingPolicy.WARN, unmappedTargetPolicy = ReportingPolicy.WARN,
        typeConversionPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface UserMapper {
    UserDTO modelToDto(MyUser myUser);
    List<UserDTO> modelsToDtos(List<MyUser> myUser);
    MyUser dtoToModel(UserDTO dto);
    List<MyUser> dtosToModels(List<UserDTO> dtos);

    UserEntity modelToEntity(MyUser myUser);
    List<UserEntity> modelsToEntities(List<MyUser> myUsers);
    MyUser entityToModel(UserEntity entity);
    List<MyUser> entitiesToModel(List<UserEntity> entities);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromModel(MyUser model, @MappingTarget UserEntity entity, @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);
}
