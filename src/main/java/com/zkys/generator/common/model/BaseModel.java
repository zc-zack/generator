package com.zkys.generator.common.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 基础通用字段
 *
 * @author Caixr
 */
@Data
public class BaseModel implements Serializable {

    @TableId(value = "id",type = IdType.AUTO)
	private Long id;

    private String remark;

    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String lastUpdateBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date lastUpdateTime;

    @TableLogic
    private Integer delFlag;

}
