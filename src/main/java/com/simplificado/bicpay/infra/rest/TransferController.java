package com.simplificado.bicpay.infra.rest;

import com.simplificado.bicpay.application.usecase.Transfer;
import com.simplificado.bicpay.domain.Transaction;
import com.simplificado.bicpay.infra.rest.dto.RequestTransferDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping(value = "api/v1/transfer", consumes = "application/json")
public class TransferController{
    private final Transfer transfer;

    public TransferController(Transfer transfer){
        this.transfer = transfer;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> makeTransfer(@Valid @RequestBody RequestTransferDto requestTransferDto){
        var transaction = Transaction.create(requestTransferDto.payer(), requestTransferDto.payee(), requestTransferDto.value());
        var transfer = this.transfer.execute(transaction);
        return  ResponseEntity.status(HttpStatus.CREATED).body(transfer);
    }
}
