package com.neu.reliidentyBack.reliidentyUtils;

/**
 * @author jasonR
 * @date 2021/4/30 15:03
 * 用来将枚举类型和数据库类型对应
 *
 */


import com.neu.reliidentyBack.reliidentyUtils.enums.UserTypeEnum;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;


import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * mapper里字段到枚举类的映射。
 * 用法一:
 * 入库：#{enumDataField, typeHandler=com.adu.spring_test.mybatis.typehandler.EnumTypeHandler}
 * 出库：
 * <resultMap>
 * <result property="enumDataField" column="enum_data_field" javaType="com.xxx.MyEnum" typeHandler="com.adu.spring_test.mybatis.typehandler.EnumTypeHandler"/>
 * </resultMap>
 *
 * 用法二：
 * 1）在mybatis-config.xml中指定handler:
 *      <typeHandlers>
 *              <typeHandler handler="com.adu.spring_test.mybatis.typehandler.EnumTypeHandler" javaType="com.xxx.MyEnum"/>
 *      </typeHandlers>
 * 2)在MyClassMapper.xml里直接select/update/insert。
 */
@MappedJdbcTypes(value=JdbcType.INTEGER,includeNullJdbcType = true)
public class EnumTypeHandler extends BaseTypeHandler<UserTypeEnum> {
    private Class<UserTypeEnum> type;

    public EnumTypeHandler(){}
    public EnumTypeHandler(Class<UserTypeEnum> type){
        if(type==null) throw  new IllegalArgumentException("Type argument cannot be null");
        this.type=type;
    }
    //将javaType转换为jdbcType
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, UserTypeEnum userTypeEnum, JdbcType jdbcType) throws SQLException {
        preparedStatement.setInt(i,userTypeEnum.getValue());
    }

    //将jdbcType转换为JavaType
    @Override
    public UserTypeEnum getNullableResult(ResultSet resultSet, String s) throws SQLException {

        return convert(resultSet.getInt(s));
    }

    @Override
    public UserTypeEnum getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return convert(resultSet.getInt(i));
    }

    @Override
    public UserTypeEnum getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return convert(callableStatement.getInt(i));
    }
    private UserTypeEnum convert(int value){
        UserTypeEnum[] objs=type.getEnumConstants();
        for (UserTypeEnum obj : objs) {
            if(obj.getValue()==value)return obj;
        }
        return null;
    }
}
