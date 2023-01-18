package com.buddyapp.paymybuddy.mappers;

import com.buddyapp.paymybuddy.DTOs.TransactionDTO;
import com.buddyapp.paymybuddy.entities.TransactionEntity;
import com.buddyapp.paymybuddy.helper.CycleAvoidingMappingContext;
import com.buddyapp.paymybuddy.models.Contact;
import com.buddyapp.paymybuddy.models.MyContact;
import com.buddyapp.paymybuddy.models.MyTransaction;
import com.buddyapp.paymybuddy.models.Transaction;
import org.mapstruct.*;


import java.util.List;

@Mapper(unmappedSourcePolicy = ReportingPolicy.WARN, unmappedTargetPolicy = ReportingPolicy.WARN,
        typeConversionPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface TransactionMapper {

    TransactionDTO modelToDto(Transaction transaction);
    List<TransactionDTO> modelsToDtos(List<Transaction> transactions);
    Transaction dtoToModel(TransactionDTO dto);
    List<Transaction> dtosToModels(List<TransactionDTO> dtos);

    TransactionEntity modelToEntity(Transaction transaction);
    List<TransactionEntity> modelsToEntities(List<Transaction> transactions);
    Transaction entityToModel(TransactionEntity entity);
    List<Transaction> entitiesToModel(List<TransactionEntity> entities);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromDto(TransactionDTO dto, @MappingTarget Transaction model, @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);
}
