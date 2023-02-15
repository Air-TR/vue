package com.tr.vue.entity;

import com.tr.vue.entity.base.BaseEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;

/**
 * @author taorun
 * @date 2023/2/13 18:15
 */
@Data
@Entity
public class Role extends BaseEntity {

    @NotBlank
    private String role;
    @NotBlank
    private String description;

}
