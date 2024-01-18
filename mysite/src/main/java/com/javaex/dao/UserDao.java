package com.javaex.dao;

import com.javaex.vo.UserVo;

public interface UserDao{
  public int update(UserVo vo); // 회원수정
	public int insert(UserVo vo); // 회원가입
	public UserVo getUser(String email, String password); // 정보가 맞는 회원정보를 리턴
	public UserVo getUser(int no); // 정보가 맞는 회원정보를 리턴
  public String idCheck(String email); // // 정보가 맞는 회원정보를 리턴
}
