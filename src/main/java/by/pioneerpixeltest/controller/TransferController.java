package by.pioneerpixeltest.controller;

import by.pioneerpixeltest.dao.dto.TransferRequestDto;
import by.pioneerpixeltest.service.TransferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transfers")
@RequiredArgsConstructor
public class TransferController {
    private final TransferService transferService;

    @PostMapping
    public ResponseEntity<Void> transfer(@Valid @RequestBody TransferRequestDto transferRequest) {
        transferService.transferMoney(transferRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
