package com.portfolio.www.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.portfolio.www.dto.BoardVoteDto;
import com.portfolio.www.repository.BoardMemoryRepository;

class BoardServiceTest {
	BoardService boardService = new BoardService(new BoardMemoryRepository());

	/**
	 * 이전 투표가 없을 때 -> 좋아요 또는 싫어요 바로 insert
	 */
	@Test
	@DisplayName("이전 투표가 없을 때")
	void test1() {
		//given
		//when
		int code = boardService.vote(1, 1, 1, "Y", "127.0.0.1");
		//then
		Assertions.assertThat(code).isEqualTo(0);
		BoardVoteDto dto = boardService.getVote(1, 1, 1);
		Assertions.assertThat(dto).isNotNull();
		Assertions.assertThat(dto.getIsLike()).isEqualTo("Y");
	}
	
	/**
	 * 이전에 좋아요를 누르고
	 * 또 한번 좋아요를 눌렀을 때
	 * -> 좋아요 취소
	 */
	@Test
	@DisplayName("이전 투표 결과와 중복인 투표 요청")
	void test2() {
		//given
		boardService.vote(1, 1, 1, "Y", "127.0.0.1");
		//when
		int code = boardService.vote(1, 1, 1, "Y", "127.0.0.1");
		//then
		Assertions.assertThat(code).isEqualTo(1);
		BoardVoteDto dto = boardService.getVote(1, 1, 1);
		Assertions.assertThat(dto).isNull();
	}
	
	/**
	 * 이전에 좋아요를 누르고
	 * 다음에 싫어요를 누르면
	 * 좋아요 -> 싫어요로 갱신된다.
	 */
	
	@Test
	@DisplayName("이전 투표 결과와 반대인 투표 요청")
	void test3() {
		//given
		boardService.vote(1, 1, 1, "Y", "127.0.0.1");
		//when
		int code = boardService.vote(1, 1, 1, "N", "127.0.0.1");
		//then
		Assertions.assertThat(code).isEqualTo(2);
		BoardVoteDto dto = boardService.getVote(1, 1, 1);
		Assertions.assertThat(dto).isNotNull();
		Assertions.assertThat(dto.getIsLike()).isEqualTo("N");
	}
}
