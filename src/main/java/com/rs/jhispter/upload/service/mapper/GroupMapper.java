package com.rs.jhispter.upload.service.mapper;

import com.rs.jhispter.upload.domain.*;
import com.rs.jhispter.upload.service.dto.GroupDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Group and its DTO GroupDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface GroupMapper extends EntityMapper<GroupDTO, Group> {



    default Group fromId(Long id) {
        if (id == null) {
            return null;
        }
        Group group = new Group();
        group.setId(id);
        return group;
    }
}
