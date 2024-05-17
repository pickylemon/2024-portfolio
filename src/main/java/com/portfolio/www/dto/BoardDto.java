package com.portfolio.www.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(of= {"boardSeq", "boardTypeSeq", "attFileCnt", "boardCommentCnt"})
public class BoardDto {
	private int boardSeq;
	private int boardTypeSeq;
	private String title;
	private String content;
	private int hit;
	private String delYn;
	private String regDtm;
	private int regMemberSeq;
	private String updateDtm;
	private int updateMemberSeq;	
	
	private String regMemberId; //이건 board테이블에 없고, member테이블에 있는 정보.
	
	private int attFileCnt;
	private int boardCommentCnt;
	
	private int likeTotal;
	private int unlikeTotal;

}
