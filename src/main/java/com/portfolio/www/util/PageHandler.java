package com.portfolio.www.util;

import lombok.Data;

@Data
public class PageHandler {
	private Integer startPage;
	private final Integer currPage;
	private Integer endPage;
	private final Integer naviSize = 10;
	private final Integer pageSize;
	private final Integer offset;
	private Integer totalPage;
	
	public PageHandler(Integer currPage, Integer pageSize) {
		this.currPage = currPage;
		this.pageSize = pageSize;
		this.offset = (currPage - 1) * pageSize;
	}
	
	public void calculatePage(Integer totalPost) {
		this.startPage = ((currPage-1)/10)*10 + 1;
		this.totalPage = (totalPost-1) / pageSize + 1;
		this.endPage = Math.min(startPage + naviSize - 1, totalPage);
	}
	
//	public PageHandler(Integer currPage, Integer pageSize, Integer totalPost) {
//		this.currPage = currPage;
//		this.pageSize = pageSize;
//		this.offset = (currPage - 1) * pageSize;
//		this.startPage = ((currPage-1)/10)*10 + 1;
//		this.totalPage = (totalPost-1) / pageSize + 1;
//		this.endPage = Math.min(startPage + naviSize - 1, totalPage);
//	}

	public void print() {
		System.out.println("currPage = " + this.currPage);
		if(this.startPage != 1) {
			System.out.print("<< ");
		}
		for(int i=startPage; i<=endPage; i++) {
			System.out.print(i+" ");
		}
		if(this.endPage!=this.totalPage) {
			System.out.println(">>");
		}
		System.out.println();
		
	}
	

}
