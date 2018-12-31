package com.lanu.homebudget.controllers;

import com.lanu.homebudget.entities.Transaction;
import com.lanu.homebudget.entities.Transfer;
import com.lanu.homebudget.security.User;
import com.lanu.homebudget.security.UserService;
import com.lanu.homebudget.services.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
public class TransferController {

    @Autowired
    private TransferService transferService;

    @Autowired
    private UserService userService;

    @GetMapping("/transfers/{transferId}")
    public Transfer getTransferById(@PathVariable(name = "transferId")Long transferId){
        return transferService.getTransferById(transferId);
    }

    @PostMapping("/transfers")
    public Transfer createTransfer(Principal principal, @Valid @RequestBody Transfer transfer){
        User user = userService.findByUsername(principal.getName()).get();
        /*LocalDateTime localDate = LocalDateTime.ofInstant(
                date.toInstant(), ZoneId.systemDefault());*/
        return transferService.createTransfer(user, transfer);
    }

    @PutMapping("/transfers")
    public Transfer updateTransfer(@Valid @RequestBody Transfer transfer) {
        return transferService.editTransfer(transfer);
    }
}
