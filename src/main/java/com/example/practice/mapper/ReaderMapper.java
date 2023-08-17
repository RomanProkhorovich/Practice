package com.example.practice.mapper;

import com.example.practice.Dto.AuthDto;
import com.example.practice.Dto.RegDto;
import com.example.practice.model.Reader;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public interface ReaderMapper {
    Reader toReader(RegDto dto);

    @Mapping(source = "email",target = "username")
    AuthDto toAuthDto(RegDto dto);
}
