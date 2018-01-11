package dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import entity.User;

@Mapper
public interface UserDAO {

	@Select("select * from user where name = #{name}")
	public User find(String name);
	
	@Select("select * from user where name = #{name} and pwd = #{pwd}")
	public User login(@Param("name")String name, @Param("pwd")String pwd);
}
