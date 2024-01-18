package com.javaex.dao;

import java.util.List;
import com.javaex.vo.GuestbookVo;

public interface GuestbookDao {
	public List<GuestbookVo> getList(); // 방명록 목록 조회
	public int insert(GuestbookVo vo);  // 방명록 정보 등록
	public int delete(GuestbookVo vo);  // 방명록 정보 삭제
}
