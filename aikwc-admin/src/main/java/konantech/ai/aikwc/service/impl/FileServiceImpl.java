package konantech.ai.aikwc.service.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import konantech.ai.aikwc.entity.Crawl;
import konantech.ai.aikwc.entity.KTask;
import konantech.ai.aikwc.repository.KTaskRepository;
import konantech.ai.aikwc.service.FileService;

@Service("fileService")
public class FileServiceImpl implements FileService {

	@Autowired
	KTaskRepository KTaskRepository;

	@Override
	public void filedown(HttpServletRequest request, HttpServletResponse response, String fileId) throws Exception {
//		fileUtil.fileDown(request, response, fileInfo.getAtchPath(), fileInfo.getAtchName());
	}

	@Override
	public void excelUpload(MultipartFile multipartFile) throws Exception {

		List<Map<Integer, String>> rows = getRows(multipartFile);
		Object ent = new KTask();
		Field[] member = ent.getClass().getDeclaredFields();
		int length = 0;
		length = member.length;
		String[] fields = new String[length];

		for (int i = 0; i < member.length; i++) {
			Field a = member[i];
			String name = a.getName();
			fields[i] = name;
		}

		Map<Integer, String> headers = rows.get(0);

		if (!(headers.size() == fields.length)) {
			throw new Exception("엑셀이랑 엔티티 갯수 안맞음.");
		}

		ArrayList<KTask> inputData = new ArrayList<KTask>();

		for (int i = 1; i < rows.size(); i++) {
			Map<Integer, String> row = rows.get(i);
			KTask vo = new KTask();
			for (Integer key : row.keySet()) {
				switch (key) {
				case 0:
					vo.setPk(Integer.parseInt(row.get(key)));
					break;
				case 1:
					vo.setAgency(row.get(key));
					break;
				case 2:
					vo.setGrp(row.get(key));
					break;
				case 3:
					vo.setCollector(row.get(key));
					break;
				case 4:
					vo.setCType(row.get(key));
					break;
				case 5:
					vo.setStart(row.get(key));
					break;
				case 6:
					vo.setEnd(row.get(key));
					break;
				case 7:
					vo.setCycleCron(row.get(key));
					break;
				case 8:
					vo.setStatus(row.get(key));
					break;
				case 9:
					vo.setUseyn(row.get(key));
					break;
				case 10:
					vo.setTaskNo(row.get(key));
					break;
				default:
					throw new Exception("범위 벗어난 값 key: "+key);
				}
			}
			inputData.add(vo);
		}
		
//		ArrayList<Crawl> inputData = new ArrayList<Crawl>();
//
//		for (int i = 1; i < rows.size(); i++) {
//			Map<Integer, String> row = rows.get(i);
//			Crawl vo = new Crawl();
//			for (Integer key : row.keySet()) {
//				switch (key) {
//				case 0:
//					vo.setIdx(Integer.parseInt(row.get(key)));
//					break;
//				case 1:
//					vo.setChannel(row.get(key));
//					break;
//				case 2:
//					vo.setSiteName(row.get(key));
//					break;
//				case 3:
//					vo.setBoardName(row.get(key));
//					break;
//				case 4:
//					vo.setUniqkey(row.get(key));
//					break;
//				case 5:
//					vo.setUrl(row.get(key));
//					break;
//				case 6:
//					vo.setTitle(row.get(key));
//					break;
//				case 7:
//					vo.setDoc(row.get(key));
//					break;
//				case 8:
//					vo.setWriteId(row.get(key));
//					break;
//				case 9:
//					vo.setWriteTime(null);
//					break;
//				case 10:
//					vo.setCrawledTime(null);
//					break;
//				case 11:
//					vo.setUpdateTime(null);
//					break;
//				case 12:
//					vo.setPseudo(row.get(key));
//					break;
//				case 13:
//					vo.setWtimeStr(row.get(key));
//					break;
//				case 14:
//					vo.setCollector(row.get(key));
//					break;
//				case 15:
//					vo.setUrl(row.get(key));
//					break;
//
//				default:
//					throw new Exception("범위 벗어난 값 key: "+key);
//				}
//			}
//			inputData.add(vo);
//		}
		
		KTaskRepository.saveAll(inputData);
	}

	@Override
	public void excelTempleateDown(HttpServletRequest request, HttpServletResponse response, String templateId)
			throws Exception {

//		Object ent = new Crawl();
		Object ent = new KTask();
		Field[] member = ent.getClass().getDeclaredFields();
		int length = 0;
		length = member.length;
		String[] headers = new String[length];

		for (int i = 0; i < member.length; i++) {
			Field a = member[i];
			String name = a.getName();
			headers[i] = name;
		}

		makeExcel(request, response, headers);
	}

	public void makeExcel(HttpServletRequest request, HttpServletResponse response, String[] headers)
			throws IOException {
		Workbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet("template");
		Row row = null;
		Cell cell = null;
		int rowNo = 0;

		// 테이블 헤더용 스타일
		CellStyle headStyle = wb.createCellStyle();

		// 가는 경계선을 가집니다.
		headStyle.setBorderTop(BorderStyle.THIN);
		headStyle.setBorderBottom(BorderStyle.THIN);
		headStyle.setBorderLeft(BorderStyle.THIN);
		headStyle.setBorderRight(BorderStyle.THIN);

		// 배경색은 노란색입니다.
		headStyle.setFillForegroundColor(HSSFColorPredefined.YELLOW.getIndex());
		headStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		// 데이터는 가운데 정렬합니다.
		headStyle.setAlignment(HorizontalAlignment.CENTER);

		// 데이터용 경계 스타일 테두리만 지정
		CellStyle bodyStyle = wb.createCellStyle();
		bodyStyle.setBorderTop(BorderStyle.THIN);
		bodyStyle.setBorderBottom(BorderStyle.THIN);
		bodyStyle.setBorderLeft(BorderStyle.THIN);
		bodyStyle.setBorderRight(BorderStyle.THIN);

		// 헤더 생성
		row = sheet.createRow(rowNo++);

		for (int i = 0; i < headers.length; i++) {
			cell = row.createCell(i);
			cell.setCellStyle(headStyle);
			cell.setCellValue(headers[i]);
		}
		// 데이터 부분 생성
//		for (HashMap<String, String> map : list) {
//			row = sheet.createRow(rowNo++);
//			cell = row.createCell(0);
//			cell.setCellStyle(bodyStyle);
//			cell.setCellValue("내용1");
//			cell = row.createCell(1);
//			cell.setCellStyle(bodyStyle);
//			cell.setCellValue("내용2");
//			cell = row.createCell(2);
//			cell.setCellStyle(bodyStyle);
//			cell.setCellValue("내용3");
//		}
		response.setContentType("ms-vnd/excel");
		String browser = getBrowser(request);
		String disposition = getDisposition("template.xlsx", browser);
		response.setHeader("Content-Disposition", disposition);
		wb.write(response.getOutputStream());
		wb.close();

	}

	public List<Map<Integer, String>> getRows(MultipartFile excelFile) throws IOException, InvalidFormatException {
		OPCPackage opcPackage = OPCPackage.open(excelFile.getInputStream());
		XSSFWorkbook workbook = new XSSFWorkbook(opcPackage);
		// 2. 엑셀 시트 조회
		XSSFSheet sheet = workbook.getSheetAt(0);
		List<Map<Integer, String>> rows = new ArrayList<>();
		DataFormatter formatter = new DataFormatter();
		// 3. 각 행 처리
		for (Row row : sheet) {
			// 4. 각 행의 열을 Java Map으로 생성
			Map<Integer, String> rowMap = new HashMap<>();
			for (Cell cell : row) {
				String text = formatter.formatCellValue(cell);
				rowMap.put(cell.getColumnIndex(), text);
			}
			// 5. 행 List에 추가
			rows.add(rowMap);
		}
		workbook.close();
		return rows;
	}

	private String getBrowser(HttpServletRequest request) {
		String header = request.getHeader("User-Agent");
		if (header.indexOf("MSIE") > -1 || header.indexOf("Trident") > -1)
			return "MSIE";
		else if (header.indexOf("Chrome") > -1)
			return "Chrome";
		else if (header.indexOf("Opera") > -1)
			return "Opera";
		return "Firefox";
	}

	private String getDisposition(String filename, String browser) throws UnsupportedEncodingException {
		String dispositionPrefix = "attachment;filename=";
		String encodedFilename = null;
		if (browser.equals("MSIE")) {
			encodedFilename = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
		} else if (browser.equals("Firefox")) {
			encodedFilename = "\"" + new String(filename.getBytes("UTF-8"), "8859_1") + "\"";
		} else if (browser.equals("Opera")) {
			encodedFilename = "\"" + new String(filename.getBytes("UTF-8"), "8859_1") + "\"";
		} else if (browser.equals("Chrome")) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < filename.length(); i++) {
				char c = filename.charAt(i);
				if (c > '~') {
					sb.append(URLEncoder.encode("" + c, "UTF-8"));
				} else {
					sb.append(c);
				}
			}
			encodedFilename = sb.toString();
		}
		return dispositionPrefix + encodedFilename;
	}

}
