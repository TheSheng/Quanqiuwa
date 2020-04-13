package com.bishe.java.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bishe.java.pojo.User;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 席思诚123
 * @since 2020-01-13
 */
public interface UserMapper extends BaseMapper<User> {
     int login(@Param("user") User user);
     int hasRegister(@Param("user") User user);

}
