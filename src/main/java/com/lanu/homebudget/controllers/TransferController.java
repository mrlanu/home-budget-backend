package com.lanu.homebudget.controllers;

import com.lanu.homebudget.entities.Transfer;
import com.lanu.homebudget.services.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class TransferController {

    @Autowired
    private TransferService transferService;

    @GetMapping("/transfers/{transferId}")
    public Transfer getTransferById(@PathVariable(name = "transferId")Long transferId){
        return transferService.getTransferById(transferId);
    }

    @PostMapping("/transfers")
    public Transfer createTransfer(@RequestParam(name = "budgetId") Long budgetId, @Valid @RequestBody Transfer transfer){
        /*LocalDateTime localDate = LocalDateTime.ofInstant(
                date.toInstant(), ZoneId.systemDefault());*/
        return transferService.createTransfer(budgetId, transfer);
    }

    @PutMapping("/transfers")
    public Transfer updateTransfer(@Valid @RequestBody Transfer transfer) {
        return transferService.editTransfer(transfer);
    }

    @DeleteMapping("/transfers")
    public ResponseEntity<?> deleteTransfer(@RequestParam(name = "transferId") Long transferId) {
        return transferService.deleteTransfer(transferId);
    }
}
