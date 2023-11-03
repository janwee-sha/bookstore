package com.janwee.bookstore.author.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TelService {
    private final TelSliceRepository telSliceRepo;

    @Autowired
    public TelService(TelSliceRepository telSliceRepo) {
        this.telSliceRepo = telSliceRepo;
    }

    @Transactional
    public void registerTel(Long userId, String telNumber) {
        telSliceRepo.saveAll(TelSlice.from(userId, telNumber));
    }
}
