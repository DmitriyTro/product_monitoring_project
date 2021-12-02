package com.springboot.product_monitoring.mappers;

import com.springboot.product_monitoring.dto.UserDTO;
import com.springboot.product_monitoring.entities.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {

	UserDTO toUserDTO(User user);

	List<UserDTO> toUsersDTOs(List<User> users);
}
