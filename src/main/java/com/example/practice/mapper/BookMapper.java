package com.example.practice.mapper;

import com.example.practice.Dto.BookDto;
import com.example.practice.model.Book;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookMapper {
    Book toBook(BookDto dto);
    BookDto toDto(Book book);
}
