package org.example.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Date;

@ApiModel(description = "operation_log")
@Schema
@TableName(value = "operation_log")
public class OperationLog extends Model<OperationLog> implements Serializable {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "")
    @Schema(description = "")
    private String id;

    @TableField(value = "access_time")
    @ApiModelProperty(value = "")
    @Schema(description = "")
    private Date accessTime;

    @TableField(value = "\"operation\"")
    @ApiModelProperty(value = "")
    @Schema(description = "")
    private String operation;

    @TableField(value = "ip")
    @ApiModelProperty(value = "")
    @Schema(description = "")
    private String ip;

    @TableField(value = "\"state\"")
    @ApiModelProperty(value = "")
    @Schema(description = "")
    private String state;

    public static final String COL_ID = "id";

    public static final String COL_ACCESS_TIME = "access_time";

    public static final String COL_OPERATION = "operation";

    public static final String COL_IP = "ip";

    public static final String COL_STATE = "state";

    /**
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return access_time
     */
    public Date getAccessTime() {
        return accessTime;
    }

    /**
     * @param accessTime
     */
    public void setAccessTime(Date accessTime) {
        this.accessTime = accessTime;
    }

    /**
     * @return operation
     */
    public String getOperation() {
        return operation;
    }

    /**
     * @param operation
     */
    public void setOperation(String operation) {
        this.operation = operation;
    }

    /**
     * @return ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * @param ip
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * @return state
     */
    public String getState() {
        return state;
    }

    /**
     * @param state
     */
    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", accessTime=").append(accessTime);
        sb.append(", operation=").append(operation);
        sb.append(", ip=").append(ip);
        sb.append(", state=").append(state);
        sb.append("]");
        return sb.toString();
    }
}