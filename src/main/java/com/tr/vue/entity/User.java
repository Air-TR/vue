package com.tr.vue.entity;

import com.tr.vue.entity.base.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.Fetch;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

import static org.hibernate.annotations.FetchMode.SELECT;

/**
 * @author taorun
 * @date 2023/2/13 18:15
 */
@Data
@Entity
public class User extends BaseEntity {

    @NotBlank
    private String username;
    @NotBlank
    private String password;
    private String realName;
    private String nickName;
    private String IdNo;
    private String phone;
    private String email;
    private Integer age;
    private Date birthDay;
    private String address;

    @ManyToMany
    @Fetch(value = SELECT)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private List<Role> roles;

}
