package com.crm.common.dto;

import com.crm.common.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO implements Serializable {
    private Long id;
    private String username;
    private String email;
    private String name;
    private String domain;
    private Role role;
    private String department;
    private String position;
    private Double salary;
    private String phone;
    private LocalDate joiningDate;
    private Integer leaves;
    private String address;
    private String profilePic;
    private String domainCategory;
    private Long managerId;
    private String managerName;
    private boolean active;
}
