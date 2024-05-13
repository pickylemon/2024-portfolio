package com.portfolio.www.util;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.portfolio.www.exception.FileSaveException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class FileUtil {
	//파일을 물리적으로 저장하는 역할을 하는 클래스
	private static String SAVE_PATH = "C:\\dev\\workspace\\attFile\\";
	
	public File saveFiles(MultipartFile mf) {
		log.info("mf isEmpty?={}", mf.isEmpty());
//		log.info("mf isEmpty?={}", ObjectUtils.isEmpty(mf)); ObjectUtils는 여기서는 제대로된 결과 안나옴.

		File destfile = new File(getSavePath()); //'오늘' 날짜를 기준으로 동적으로 생성한 경로로 File 객체 생성'
		
		try {
			
			if(!destfile.exists()) {
				destfile.mkdirs(); //해당 경로의 파일이 존재하지 않으면 create
			}
			
			destfile = new File(getSavePath(), getUniqueFileNm(mf.getOriginalFilename()));
			mf.transferTo(destfile);
			
			if(ObjectUtils.isEmpty(destfile)) {
				throw new FileSaveException("저장된 파일 크기가 0");
			}

		} catch (FileSaveException e) {
			destfile.delete(); //저장된 파일 크기가 0이면 삭제한다.
			throw e;
			
		} catch (IllegalStateException | IOException e) {
			FileSaveException fe = new FileSaveException("첨부파일 저장 에러");
			fe.initCause(e);
			throw fe;
		}
		
		return destfile;
	}
	
	
	public void deleteFiles(List<File> delFileList) {
		for(File file : delFileList) {
			if(file.exists()) {
				file.delete();
			}
		}
	}
	
	/*
	 * file.delete()는 삭제에 성공하면 true, 실패하면 false다
	 * 삭제에 실패했다고 IOException을 던지진 않는 듯..?
	 * 소스 찾아보면 SecurityException만 명시되어 있음
	 * */
	public void deleteFile(File delFile) {
		if(delFile.exists()) {
			delFile.delete();
		}
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
		log.info("originalFileName = " + originalFileName);
		int idx = originalFileName.lastIndexOf('.');
		log.info("idx={}", idx);
		return originalFileName.substring(idx);
	}

}
