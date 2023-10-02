package com.britenet.gradebook.users;


import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UsersMapper {

    Users map(UsersRequestDto usersRequestDto);
}