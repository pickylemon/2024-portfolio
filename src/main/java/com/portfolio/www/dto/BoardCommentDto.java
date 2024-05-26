package com.portfolio.www.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BoardCommentDto {
	private int commentSeq;
	private int lvl;
	private String content;
	private int boardSeq;
	private int boardTypeSeq;
	private int memberSeq;
	private String memberNm;//이건 memberTable에서 온 정보
	
	private int parentCommentSeq;	// 부모 댓글 seq
	private int rootCommentSeq;		// 최상위 댓글 seq
	private int commentGrp;			// 댓글 그룹 번호
	private int ordSeq;				// 댓글 그룹 내 정렬순번
	
	private String regDtm;
	private String updateDtm;
	private String deleteDtm;
	
	private String isLike;
	private int likeTotal;
	private int unlikeTotal;
	
	private int gtOrdSeq;	// 댓글 그룹 내 특정 정렬순번 이상 가져오기 위한 조건 변수
}
