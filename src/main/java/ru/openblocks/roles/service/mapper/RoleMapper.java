package ru.openblocks.roles.service.mapper;

import org.mapstruct.Mapper;
import ru.openblocks.roles.api.dto.role.create.request.RoleCreateRequest;
import ru.openblocks.roles.api.dto.role.get.response.RoleGetResponse;
import ru.openblocks.roles.persistence.entity.RoleEntity;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleEntity toRole(RoleCreateRequest request);

    RoleGetResponse toDto(RoleEntity role);

    List<RoleGetResponse> toListOfDto(Collection<RoleEntity> roles);
}
