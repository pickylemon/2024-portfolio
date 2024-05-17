package com.portfolio.www.service;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import com.portfolio.www.dto.BoardCommentDto;
import com.portfolio.www.dto.BoardCommentVoteDto;
import com.portfolio.www.repository.BoardCommentRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class BoardCommentService {
	private final BoardCommentRepository boardCommentRepository;

	public int addComment(BoardCommentDto commentDto) {
		int code = -1;
		try {
			code = boardCommentRepository.save(commentDto);
		} catch(DataAccessException e) {
			e.printStackTrace();
		}
	
		return code;
	}
	
	@Transactional
	public List<BoardCommentDto> getCommentList(int boardSeq, int boardTypeSeq, int memberSeq){
//		return boardCommentRepository.getList(boardSeq, boardTypeSeq);
		List<BoardCommentDto> commentDtoList = boardCommentRepository.getList(boardSeq, boardTypeSeq);
		for(BoardCommentDto commentDto : commentDtoList) {
			try {
				//투표 결과가 있는 commentDto에 한해서 투표 결과를 commentDto에 담는다.
				//지금 로그인한 유저가 각 댓글에 대해 표시한 투표 결과를 가져오는 것.
				BoardCommentVoteDto voteDto = boardCommentRepository.getVote(commentDto.getCommentSeq(), memberSeq);
				int likeTotal = boardCommentRepository.getLikeTotal(commentDto.getCommentSeq());
				int unlikeTotal = boardCommentRepository.getUnlikeTotal(commentDto.getCommentSeq());
				commentDto.setIsLike(voteDto.getIsLike());
				commentDto.setLikeTotal(likeTotal);
				commentDto.setUnlikeTotal(unlikeTotal);
			} catch (EmptyResultDataAccessException e) {
				log.info(e.getMessage());
			}
		}
		return commentDtoList;
	}

	public int deleteComment(int commentSeq) {
		int code = -1;
		try {
			code = boardCommentRepository.delete(commentSeq);
		} catch(DataAccessException e) {
			e.printStackTrace();
		}
		return code;
	}

	public int modifyComment(BoardCommentDto commentDto) {
		int code = -1;
		try {
			code = boardCommentRepository.modify(commentDto);
		} catch(DataAccessException e) {
			e.printStackTrace();
		}
		return code;
	}
	
	@Transactional
	public int vote(BoardCommentVoteDto voteDto) {
		//이전 투표 결과가 있는지 체크
		int code = -1;
		BoardCommentVoteDto savedCommentVoteDto = getVote(voteDto.getCommentSeq(), voteDto.getMemberSeq());
		try {
			if(ObjectUtils.isEmpty(savedCommentVoteDto)) { 
				//이전의 투표 결과가 없다. -> INSERT
				boardCommentRepository.addVote(voteDto);
				code = 0;
			} else if(savedCommentVoteDto.getIsLike().equals(voteDto.getIsLike())) { 
				//이전 투표결과와 현재 투표 결과가 같다. -> DELETE
				boardCommentRepository.deleteVote(voteDto.getCommentSeq());
				code = 1;
			} else {
				//이전 투표 결과와 다르다 -> UPDATE
				boardCommentRepository.updateVote(voteDto);
				code = 2;
			}
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return code;
	}
	
	
	
	public BoardCommentVoteDto getVote(int commentSeq, int memberSeq) {
		BoardCommentVoteDto commentVoteDto = null;
		try {
			commentVoteDto = boardCommentRepository.getVote(commentSeq, memberSeq);
		} catch(EmptyResultDataAccessException e) {
			log.info(e.getMessage());
		}
		return commentVoteDto;
	}
}
