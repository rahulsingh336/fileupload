package com.rs.jhispter.upload.domain;



import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Group.
 */
@Entity
@Table(name = "jhi_group")
public class Group implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "group_name")
    private String groupName;

    @Column(name = "group_code")
    private String groupCode;

    @Column(name = "group_owner")
    private String groupOwner;

    @Column(name = "group_category_id")
    private String groupCategoryId;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public Group groupName(String groupName) {
        this.groupName = groupName;
        return this;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public Group groupCode(String groupCode) {
        this.groupCode = groupCode;
        return this;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getGroupOwner() {
        return groupOwner;
    }

    public Group groupOwner(String groupOwner) {
        this.groupOwner = groupOwner;
        return this;
    }

    public void setGroupOwner(String groupOwner) {
        this.groupOwner = groupOwner;
    }

    public String getGroupCategoryId() {
        return groupCategoryId;
    }

    public Group groupCategoryId(String groupCategoryId) {
        this.groupCategoryId = groupCategoryId;
        return this;
    }

    public void setGroupCategoryId(String groupCategoryId) {
        this.groupCategoryId = groupCategoryId;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Group group = (Group) o;
        if (group.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), group.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Group{" +
            "id=" + getId() +
            ", groupName='" + getGroupName() + "'" +
            ", groupCode='" + getGroupCode() + "'" +
            ", groupOwner='" + getGroupOwner() + "'" +
            ", groupCategoryId='" + getGroupCategoryId() + "'" +
            "}";
    }
}
