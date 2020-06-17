package cn.cnic.security.configuration.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 权限表
 * 
 * @author xuhuipeng
 * @email xuhuipeng@cnic.com
 * @date 2020-06-04 14:41:02
 */
@Data
@TableName("ca_role")
public class RoleEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Integer roleId;
	/**
	 * 
	 */
	private String roleNane;
	/**
	 * 
	 */
	private String remark;

}
