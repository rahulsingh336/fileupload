package com.rs.jhispter.upload.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Group entity.
 */
public class GroupDTO implements Serializable {

    private Long id;

    private String groupName;

    private String groupCode;

    private String groupOwner;

    private String groupCategoryId;

    public GroupDTO() {
    }

    public GroupDTO(String groupName, String groupCode, String groupOwner, String groupCategoryId) {
        this.groupName = groupName;
        this.groupCode = groupCode;
        this.groupOwner = groupOwner;
        this.groupCategoryId = groupCategoryId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getGroupOwner() {
        return groupOwner;
    }

    public void setGroupOwner(String groupOwner) {
        this.groupOwner = groupOwner;
    }

    public String getGroupCategoryId() {
        return groupCategoryId;
    }

    public void setGroupCategoryId(String groupCategoryId) {
        this.groupCategoryId = groupCategoryId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        GroupDTO groupDTO = (GroupDTO) o;
        if (groupDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), groupDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "GroupDTO{" +
            "id=" + getId() +
            ", groupName='" + getGroupName() + "'" +
            ", groupCode='" + getGroupCode() + "'" +
            ", groupOwner='" + getGroupOwner() + "'" +
            ", groupCategoryId='" + getGroupCategoryId() + "'" +
            "}";
    }
}
