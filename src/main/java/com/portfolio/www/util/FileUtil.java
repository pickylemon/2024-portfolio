package com.portfolio.www.util;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.portfolio.www.exception.FileSaveException;

@Component
public class FileUtil {
	//파일을 물리적으로 저장하는 역할을 하는 클래스
	private static String SAVE_PATH = "C:\\dev\\workspace\\attFile\\";
	
	public File saveFiles(MultipartFile mf) {
		
		File destfile = new File(getSavePath()); //'오늘' 날짜를 기준으로 동적으로 생성한 경로로 File 객체 생성'
		
		try {
			
			if(!destfile.exists()) {
				destfile.mkdirs(); //해당 경로의 파일이 존재하지 않으면 create
			}
			
			destfile = new File(getSavePath(), getUniqueFileNm(mf.getOriginalFilename()));
			mf.transferTo(destfile);

		} catch (IllegalStateException | IOException e) {
			FileSaveException fe = new FileSaveException("첨부파일 저장 에러");
			fe.initCause(e);
			throw fe;
		}
		
		return destfile;
	}
	
	/**
	 * 파일의 저장 경로(SAVE_PATH\YYYY\MM\DD)를 구하는 메서드
	 * @return
	 */
	private String getSavePath() {
		String date = LocalDateTime.now()
				.format(DateTimeFormatter.ISO_DATE)
				.substring(0,10).replaceAll("-", "//");
		
		return SAVE_PATH + date;
	}
	
	/**
	 * UUID와 확장자를 이용하여 중복 없는 파일 이름을 만드는 메서드
	 * @param originalFileName
	 * @return
	 */
	private String getUniqueFileNm(String originalFileName) {
		String chngFileNm = UUID.randomUUID().toString().replaceAll("-", "");
		return chngFileNm + getExtension(originalFileName);
	}
	
	/**
	 * 주어진 파일명에서 확장자를 추출하는 메서드
	 * @param originalFileName
	 * @return
	 */
	private String getExtension(String originalFileName) {
		int idx = originalFileName.lastIndexOf('.');
		return originalFileName.substring(idx);
	}
	

}
