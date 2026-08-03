package com.crm.common.enums;

public enum Role {
    ROLE_SUPER_ADMIN,
    ROLE_ADMIN,
    ROLE_HR,
    ROLE_MANAGER,
    ROLE_EMPLOYEE;

    public String getShortName() {
        return this.name().replace("ROLE_", "");
    }
}
