package com.shengfei.dto;

import com.shengfei.entity.Permission;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;


@Data
public class UserMenuAdd {

    @NotNull
    private Integer userId;

    @NotNull
    private List<Integer>permissionIds;

}