package com.portfolio.www.repository;

import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.concurrent.ConcurrentHashMap;

import com.portfolio.www.dto.BoardDto;
import com.portfolio.www.dto.BoardModifyDto;
import com.portfolio.www.dto.BoardSaveDto;
import com.portfolio.www.dto.BoardVoteDto;
import com.portfolio.www.entity.Board;
import com.portfolio.www.util.PageHandler;
import com.portfolio.www.util.SearchCondition;


public class BoardMemoryRepository implements BoardRepository{
	private Map<String, Board> store = new ConcurrentHashMap<>();

	@Override
	public List<BoardDto> getList(PageHandler ph, SearchCondition sc) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getTotalCnt(SearchCondition sc) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public BoardDto getOne(Integer boardSeq) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * 좋아요/싫어요 투표 결과 가져오기
	 */
	@Override
	public BoardVoteDto getVote(int boardSeq, int boardTypeSeq, int memberSeq) {
		String pk = mkPrimaryKey(boardSeq, boardTypeSeq, memberSeq);
		Board board = store.get(pk);
		if(board == null) return null;
		return toBoardVoteDto(board);
	}

	@Override
	public int addVote(int boardSeq, int boardTypeSeq, int memberSeq, String isLike, String ip) {
		Board board = new Board(boardSeq, boardTypeSeq, memberSeq, isLike, ip);
		
		String pk = mkPrimaryKey(boardSeq, boardTypeSeq, memberSeq);
		store.put(pk, board);
		if(store.get(pk) == board) {
			return 1;
		} 
		return -999;
	}

	@Override
	public int deleteVote(int boardSeq, int boardTypeSeq, int memberSeq, String isLike) {
		String pk = mkPrimaryKey(boardSeq, boardTypeSeq, memberSeq);
		if(store.remove(pk)!=null) {
			return 1;
		} 
		return -999;
	}

	@Override
	public int updateVote(int boardSeq, int boardTypeSeq, int memberSeq, String isLike, String ip) {
		String pk = mkPrimaryKey(boardSeq, boardTypeSeq, memberSeq);
		Board newBoard = new Board(boardSeq, boardTypeSeq, memberSeq, isLike, ip);
		store.put(pk, newBoard);
		if(store.get(pk) == newBoard) {
			return 1;
		}
		return -999;
		
	}
	
	@Override
	public int save(BoardSaveDto dto, int memberSeq) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public int update(BoardModifyDto dto) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	private BoardVoteDto toBoardVoteDto(Board board) {
		return new BoardVoteDto(
				board.getBoardSeq(), 
				board.getBoardTypeSeq(), 
				board.getMemberSeq(),
				board.getIp(),
				board.getIsLike(),
				board.getRegDtm());
	}
	
	private String mkPrimaryKey(int boardSeq, int boardTypeSeq, int memberSeq) {
		//boardSeq, boardTypeSeq, memberSeq가 복합 pk
		StringJoiner sj = new StringJoiner("-");
		
		sj.add(String.valueOf(boardSeq));
		sj.add(String.valueOf(boardTypeSeq));
		sj.add(String.valueOf(memberSeq)); 
		
		return sj.toString();
	}
	
	public void printStore() {
		System.out.println("size = " + store.size());
		store.entrySet().forEach(System.out::println);
		System.out.println("==========================");
	}




	


}
