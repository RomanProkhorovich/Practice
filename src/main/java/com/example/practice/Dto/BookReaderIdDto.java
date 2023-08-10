package com.example.practice.Dto;

import com.example.practice.model.BookReaderId;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class BookReaderIdDto {
    @NonNull
    private Long bookId;
    @NonNull
    private Long readerId;

    public BookReaderIdDto(@NonNull Long bookId, @NonNull Long readerId) {
        this.bookId = bookId;
        this.readerId = readerId;
    }

    public static BookReaderId map(BookReaderIdDto dto){
        return new BookReaderId(dto.bookId,dto.readerId);
    }
}
