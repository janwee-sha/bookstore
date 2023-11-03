package com.janwee.bookstore.author.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "tbl_tel_slice")
public class TelSlice {
    private static final int LENGTH = 4;

    @Id
    @GeneratedValue(generator = "tbl_tel_slice_id_seq")
    private Long id;

    @NotNull
    @Column(name = "order", nullable = false)
    private int order;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @NotBlank
    @Column(name = "value", nullable = false)
    private String value;

    public TelSlice(int order, Long userId, String value) {
        this.order = order;
        this.userId = userId;
        this.value = value;
    }

    public static List<TelSlice> from(Long userId, String telNumber) {
        int len = telNumber.length();
        int order = 0;
        List<TelSlice> slices = new ArrayList<>();
        for (int i = 0; i <= len - LENGTH; i++) {
            slices.add(new TelSlice(order++, userId, telNumber.substring(i, i + LENGTH)));
        }
        return slices;
    }
}
