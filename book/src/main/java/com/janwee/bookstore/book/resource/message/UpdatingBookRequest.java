package com.janwee.bookstore.book.resource.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.janwee.bookstore.book.domain.model.Book;
import com.janwee.bookstore.foundation.validation.ValidationGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Schema(description = "Saving book command")
public class UpdatingBookRequest implements Serializable {
    private static final long serialVersionUID = 6824730576154119263L;

    @Schema(description = "ID")
    @NotNull(groups = ValidationGroup.Modification.class, message = "id required")
    private Long id;

    @Schema(description = "name", required = true)
    @NotBlank(message = "name required")
    private String name;

    @Schema(description = "price", required = true)
    @NotNull(message = "price required")
    @DecimalMin(value = "0.0", message = "price must not be less than zero")
    private BigDecimal price;

    @Schema(description = "amount", required = true)
    @NotNull(message = "amount required")
    @PositiveOrZero(message = "amount must not be less than zero")
    private int amount;

    @Schema(description = "publication date", required = true)
    @NotNull(message = "publish_by required")
    private LocalDate publishBy;

    @Schema(description = "publisher", required = true)
    @NotBlank(message = "publisher required")
    private String publisher;

    @Schema(description = "author's ID", required = true)
    @NotNull(message = "author_id required")
    private Long authorId;

    public UpdatingBookRequest() {
    }

    @JsonIgnore
    public Book toBook() {
        Book book = new Book();
        BeanUtils.copyProperties(this, book);
        return book;
    }
}
