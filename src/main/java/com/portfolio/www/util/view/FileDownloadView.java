package com.portfolio.www.util.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.view.AbstractView;

@Component
public class FileDownloadView extends AbstractView {
	
	public FileDownloadView() { //뷰 생성시점에 content-type 설정
		setContentType("application/download; charset=UTF-8");
	}

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		Map<String, Object> fileInfo = (Map<String, Object>) model.get("fileInfo");
		File downloadFile = (File)fileInfo.get("file");
		String orgFileNm = (String)fileInfo.get("orgFileNm");
		
		
		//헤더 세팅(중요함!!)
		response.setContentType(getContentType());
		response.setContentLength((int) downloadFile.length());
		
		String userAgent = request.getHeader("User-Agent");
		
		boolean ie = userAgent.indexOf("MSIE") > -1;
		String fileNm = null;
		if(ie) {
			fileNm = URLEncoder.encode(orgFileNm, "UTF-8");
		}
		else {
			fileNm = new String(orgFileNm.getBytes("UTF-8"), "ISO-8859-1");
		}
		
		response.setHeader("Content-Disposition", "attachment; filename=\""+ fileNm + "\"");
		response.setHeader("Content-Transfer-Encoding", "binary");
		
		//파일 읽어와서 Response의 outputStream에 보내기
		OutputStream out = response.getOutputStream();
		FileInputStream fis = null;
		
		try {
			fis = new FileInputStream(downloadFile);
			FileCopyUtils.copy(fis, out);
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException ioe) {
					
				}
			}
			out.flush();
		}
	}
}
