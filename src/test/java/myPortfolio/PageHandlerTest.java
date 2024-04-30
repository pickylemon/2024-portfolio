/**
 * 
 */
package myPortfolio;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.portfolio.www.util.PageHandler;

/**
 * 
 */
class PageHandlerTest {

	@Test
	@DisplayName("경계값 테스트1")
	void test1() {
		PageHandler ph = new PageHandler(2, 10, 130);
		ph.print();
	}
	
	@Test
	@DisplayName("경계값 테스트2")
	void test2() {
		PageHandler ph = new PageHandler(11, 10, 130);
		ph.print();
	}

	@Test
	@DisplayName("경계값 테스트3")
	void test3() {
		PageHandler ph = new PageHandler(12, 10, 130);
		ph.print();
	}
	
	@Test
	@DisplayName("경계값 테스트4")
	void test4() {
		PageHandler ph = new PageHandler(12, 10, 132);
		ph.print();
	}

}
