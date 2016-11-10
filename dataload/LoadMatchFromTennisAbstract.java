package dataload;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
//import org.apache.poi.ss.usermodel.WorkbookFactory;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import counts.Match;
import counts.MatchInfo;
import counts.TennisAbstractPoint;
import objects.MatchDate;
import player.Player;
import tools.Tools;

public class LoadMatchFromTennisAbstract {

	/*public static Match loadDataFromXLXS(String filename) throws IOException {

		Match m = new Match();
		FileInputStream fis = new FileInputStream(new File(filename));
		Workbook workbook = new XSSFWorkbook(fis);
		int numberOfSheets = workbook.getNumberOfSheets();
		Sheet sheet = workbook.getSheetAt(0);
		Iterator rowIterator = sheet.iterator();

		while (rowIterator.hasNext()) {

			Row row = (Row) rowIterator.next();
			Iterator cellIterator = row.cellIterator();

			while (cellIterator.hasNext()) {
				Cell cell = (Cell) cellIterator.next();
				// System.out.println(cell.getStringCellValue());
			}
		}

		return m;

	}*/

	public static ArrayList<TennisAbstractPoint> readXLSFile(String filename) throws IOException {
		InputStream ExcelFileToRead = new FileInputStream(filename);
		HSSFWorkbook wb = new HSSFWorkbook(ExcelFileToRead);

		HSSFSheet sheet = wb.getSheetAt(0);
		HSSFRow row;
		HSSFCell cell;

		Iterator rows = sheet.rowIterator();
		ArrayList<TennisAbstractPoint> taps = new ArrayList<TennisAbstractPoint>();
		ArrayList<String> serverlist = new ArrayList<String>();
		while (rows.hasNext()) {
			int columncounter = 0;
			row = (HSSFRow) rows.next();
			Iterator cells = row.cellIterator();
			while (cells.hasNext()) {
				cell = (HSSFCell) cells.next();

				if (columncounter != 4534534) {
					String s = "";
					if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
						// System.out.println(cell.getStringCellValue()+" ");
						s = cell.getStringCellValue();
						// System.out.println("columncounter = " +
						// columncounter);
						// columncounter++;
					} else if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
						// System.out.println(cell.getNumericCellValue()+"
						// vgddgh ");
						s = Double.toString(cell.getNumericCellValue());
						// System.out.println("columncounter = " +
						// columncounter);
					} else {
						// U Can Handel Boolean, Formula, Errors
					}
					if (!s.equals("")) {
						TennisAbstractPoint tap = new TennisAbstractPoint(s);
						// System.out.println(tap.getStrings());
						taps.add(tap);
					}

				}
				columncounter++;

			}
			// System.out.println();
		}
		taps.remove(0);
		return taps;
	}

	public static MatchInfo readCSVFile(String filename) throws IOException {
		FileReader csvFileToRead = new FileReader(filename);
		if(new File(filename).exists()){
			BufferedReader br = new BufferedReader(csvFileToRead);
	
			ArrayList<TennisAbstractPoint> taps = new ArrayList<TennisAbstractPoint>();
			ArrayList<Integer> serverlist = new ArrayList<Integer>();
			ArrayList<Integer> winnerlist = new ArrayList<Integer>();
			String line = "";
			String location = "";
			String date = "";
			String round = "";
			while ((line = br.readLine()) != null) {
				String[] row = line.split(",");
	
				if (row.length >= 41) {
					String[] match_setting = row[0].split("-");
					date = match_setting[0];
					location = match_setting[2];
					round = match_setting[3];
					// if(!row[37].equals("")){
					// System.out.println(row[27]);
					TennisAbstractPoint tap = new TennisAbstractPoint(row[29], row[27], row[28]); // jetzt
																									// 29
																									// statt
																									// 37
					taps.add(tap);
					if (isNumeric(row[11])) {
	
						serverlist.add(Integer.parseInt(row[11]));
					}
					if (isNumeric(row[40])) {
						// System.out.println(row[38] + " , " + row[39] + " , " +
						// row[40]);
						winnerlist.add(Integer.parseInt(row[40]));
					}
					// }
					if (line.contains("Pt")) {
						taps.clear();
					}
				}
			}
			br.close();
			csvFileToRead.close();
			MatchInfo mi = new MatchInfo(taps, serverlist, winnerlist);
			mi.setDate(date);
			mi.setLocation(location);
			mi.setRound(round);
			return mi;
		}
		else{
			return null;
		}
	}

	public static Match readCSVFile(File file) throws IOException {
		MatchInfo mi = readCSVFile(file.getAbsolutePath());
		String[] names_part = file.getName().replace(".csv", "").split("/");
		String[] names = names_part[names_part.length - 1].split("-");
		String player1name = names[0];
		Player p1 = new Player(player1name);
		String player2name = names[1];
		Player p2 = new Player(player2name);
		p1.setHand(LoadValues.loadPlayingHand(player1name));
		p2.setHand(LoadValues.loadPlayingHand(player2name));
		String date_string = names[names.length - 1];
		MatchDate date = Tools.stringToDate(date_string);

		Match m = new Match(mi, p1, p2);
		m.setDate(date);
		return m;
	}

	public static boolean isNumeric(String str) {
		try {
			double d = Double.parseDouble(str);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

}
