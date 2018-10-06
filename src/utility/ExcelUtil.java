package utility;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtil {

	public static Object[][] excel_data(String book, String sheet) throws FileNotFoundException, IOException{
		XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(book));
		XSSFSheet sh = wb.getSheet(sheet);
		int row_count = sh.getPhysicalNumberOfRows();
		int cell_count = sh.getRow(0).getPhysicalNumberOfCells();
		Object[][] data = new Object[row_count-1][cell_count];
		for(int j=1;j<row_count;j++)
		{
		XSSFRow row = sh.getRow(j);
		int col_count = row.getPhysicalNumberOfCells();
		for(int i=0;i<col_count;i++)
		{
		XSSFCell cell = row.getCell(i);
		try
		{
		//System.out.print(cell.getNumericCellValue()+"\t");
		data[j][i] = cell.getNumericCellValue();
		}
		catch(IllegalStateException e)
		{
			if(e.getMessage().contains("STRING"))
			{
			//	System.out.print(cell.getStringCellValue()+"\t");
				data[j][i] = cell.getStringCellValue();
			}
			else if (e.getMessage().contains("BOOLEAN"))
			{
			//	System.out.print(cell.getBooleanCellValue()+"\t");
				data[j][i] = cell.getBooleanCellValue();
			}
		}
		}
		//System.out.println();
		}
		return data;
	}
}
