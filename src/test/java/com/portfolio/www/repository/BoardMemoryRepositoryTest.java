package com.portfolio.www.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.portfolio.www.dto.BoardVoteDto;

class BoardMemoryRepositoryTest {
	
	private final BoardMemoryRepository boardRepository = new BoardMemoryRepository();
	
	@Test
	@DisplayName("좋아요/싫어요 처음 누르기")
	void test1() {
		//given,when
		int result1 = boardRepository.addVote(1, 1, 1, "Y", "127.0.0.1");
		int result2 = boardRepository.addVote(2, 1, 1, "N", "127.0.0.1");
		//then
		Assertions.assertThat(result1).isEqualTo(1);
		Assertions.assertThat(result2).isEqualTo(1);
		
		boardRepository.printStore();
	}
	
	@Test
	@DisplayName("좋아요 취소하기")
	void test2() {
		//given
		boardRepository.addVote(1, 1, 1, "Y", "127.0.0.1");
		//when
		int result = boardRepository.deleteVote(1, 1, 1, "Y");
		//then
		Assertions.assertThat(result).isEqualTo(1);
		BoardVoteDto dto = boardRepository.getVote(1, 1, 1);
		Assertions.assertThat(dto).isNull();
		
		boardRepository.printStore();
	}
	
	@Test
	@DisplayName("기존 좋아요를 싫어요로 바꾸기")
	void test3() {
		//given
		boardRepository.addVote(1, 1, 1, "Y", "127.0.0.1");
		//when
		boardRepository.updateVote(1, 1, 1, "N", "127.0.0.1");
		//then
		BoardVoteDto dto = boardRepository.getVote(1, 1, 1);
		Assertions.assertThat(dto.getIsLike()).isEqualTo("N");
		
		boardRepository.printStore();
	}
}
