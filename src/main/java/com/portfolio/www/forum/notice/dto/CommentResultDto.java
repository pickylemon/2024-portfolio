package com.portfolio.www.forum.notice.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 댓글 리스트를 계층으로 만들어 가져올때 사용하는 DTO
 * @author jyeory
 *
 */
@Getter
@Setter
public class CommentResultDto {
	private int boardSeq;
	private int boardTypeSeq;
	private int commentSeq;
	private int rootCommentSeq;
	private String content;
	private int memberSeq;
	private String path;
	private int lvl;
	private String sortable;
	private String memberNm;
	
	private String regDtm;
	private String updateDtm;
	private String deleteDtm;
	
	private String isLike;
	private int likeTotal;
	private int unlikeTotal;
	
	// 불필요할 경우 삭제 할 컬럼
	private int commentGrp;
	private int ordSeq;
}
