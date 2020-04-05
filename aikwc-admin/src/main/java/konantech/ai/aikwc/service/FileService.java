package konantech.ai.aikwc.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;


public interface FileService {

	public void filedown(HttpServletRequest request, HttpServletResponse response, String fileId) throws Exception;
	
	public void excelTempleateDown(HttpServletRequest request, HttpServletResponse response, String templateId) throws Exception;
	
	public void excelUpload(MultipartFile multipartFile) throws Exception;
	
	
}
