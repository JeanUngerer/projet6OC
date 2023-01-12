package com.buddyapp.paymybuddy.DTOs;

import com.buddyapp.paymybuddy.models.MyTransaction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MyTransactionsDTO {
    private List<MyTransaction> myTransactionList;
}
