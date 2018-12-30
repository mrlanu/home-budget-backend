package com.lanu.homebudget.controllers;

import com.lanu.homebudget.entities.Transfer;
import com.lanu.homebudget.security.User;
import com.lanu.homebudget.security.UserService;
import com.lanu.homebudget.services.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

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
    public Transfer createTransfer(Principal principal,
                                   @RequestParam(name = "date") Date date,
                                   @RequestParam(name = "accFromId")Long accFromId,
                                   @RequestParam(name = "accToId")Long accToId,
                                   @RequestParam(name = "amount")double amount){
        User user = userService.findByUsername(principal.getName()).get();
        LocalDateTime localDate = LocalDateTime.ofInstant(
                date.toInstant(), ZoneId.systemDefault());
        return transferService.createTransfer(user, localDate, accFromId, accToId, amount);
    }
}
