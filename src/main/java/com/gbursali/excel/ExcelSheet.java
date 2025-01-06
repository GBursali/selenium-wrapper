package com.gbursali.excel;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents an Excel sheet within an Excel file. Provides methods to access and manipulate the sheet's content.
 */
@SuppressWarnings("unused")
public class ExcelSheet {
	private static final int DEFAULT_SHEET_INDEX = 0;

	/**
	 * The Excel file that owns this sheet.
	 */
	public ExcelFile ownerFile;

	/**
	 * The underlying Apache POI sheet.
	 */
	public XSSFSheet underlyingSheet;

	/**
	 * The list of rows in the sheet.
	 */
	public List<ExcelRow> rows;

	/**
	 * Creates a new ExcelSheet instance associated with the given ExcelFile and sheet index.
	 * Use the static factory methods 'of' in ExcelSheet class to create instances instead of this constructor.
	 *
	 * @param ownerFile The ExcelFile to which this sheet belongs.
	 * @param sheetIndex The index of the sheet within the Excel file.
	 */
	protected ExcelSheet(ExcelFile ownerFile, int sheetIndex){
		this.ownerFile = ownerFile;
		this.underlyingSheet = getSheet(sheetIndex);
		this.rows = getRows();
	}

	/**
	 * Creates a new ExcelSheet instance associated with the given ExcelFile and sheet name.
	 * Use the static factory methods 'of' in ExcelSheet class to create instances instead of this constructor.
	 *
	 * @param ownerFile The ExcelFile to which this sheet belongs.
	 * @param sheetName The name of the sheet within the Excel file.
	 */
	protected ExcelSheet(ExcelFile ownerFile, String sheetName){
		this.ownerFile = ownerFile;
		this.underlyingSheet = getSheet(sheetName);
		this.rows = getRows();
	}

	/**
	 * Retrieves a list of ExcelRows representing all the rows in the sheet.
	 *
	 * @return The list of ExcelRows in the sheet.
	 */
	public List<ExcelRow> getRows(){
		Iterator<Row> rowIterator = underlyingSheet.rowIterator();
		List<ExcelRow> rows = new ArrayList<>();
		while (rowIterator.hasNext()) {
			rows.add(new ExcelRow(this, rowIterator.next()));
		}
		return rows;
	}

	/**
	 * Retrieves a specific ExcelRow by its row number.
	 *
	 * @param rowNumber The row number (1-based) of the ExcelRow to retrieve.
	 * @return The ExcelRow at the specified row number.
	 */
	public ExcelRow getRow(int rowNumber){
		final int rownum = rowNumber - 1;
		if(getRows().size()<rowNumber)
			return new ExcelRow(this,underlyingSheet.createRow(rownum));
		return getRows().get(rownum);
	}

	/**
	 * Retrieves an Excel sheet from the Excel file by its index.
	 *
	 * @param index The index of the sheet to retrieve.
	 * @return An XSSFSheet representing the sheet at the specified index.
	 */
	private XSSFSheet getSheet(int index){
		final int numberOfSheets = ownerFile.workbook.getNumberOfSheets();
		if(index > numberOfSheets)
			// If the index is greater than the number of sheets, create a new sheet.
			return ownerFile.workbook.createSheet();

		// Return the sheet at the specified index (0-based).
		return ownerFile.workbook.getSheetAt(index - 1);
	}

	/**
	 * Retrieves an Excel sheet from the Excel file by its name.
	 *
	 * @param sheetName The name of the sheet to retrieve.
	 * @return An XSSFSheet representing the sheet with the specified name.
	 */
	private XSSFSheet getSheet(String sheetName){
		final XSSFSheet sheet = ownerFile.workbook.getSheet(sheetName);
		if(sheet == null)
			// If the sheet with the given name does not exist, create a new sheet with that name.
			return ownerFile.workbook.createSheet(sheetName);

		// Return the sheet with the specified name.
		return sheet;
	}

	/**
	 * Creates a new ExcelSheet instance associated with the given ExcelFile and sheet index.
	 *
	 * @param ownerFile The ExcelFile to which this sheet belongs.
	 * @param sheetIndex The index of the sheet within the Excel file.
	 * @return An ExcelSheet instance representing the specified sheet.
	 */
	public static ExcelSheet of(ExcelFile ownerFile, int sheetIndex){
		return new ExcelSheet(ownerFile, sheetIndex);
	}

	/**
	 * Creates a new ExcelSheet instance associated with the given ExcelFile and sheet name.
	 *
	 * @param ownerFile The ExcelFile to which this sheet belongs.
	 * @param sheetName The name of the sheet within the Excel file.
	 * @return An ExcelSheet instance representing the sheet with the specified name.
	 */
	public static ExcelSheet of(ExcelFile ownerFile, String sheetName){
		return new ExcelSheet(ownerFile, sheetName);
	}

	/**
	 * Creates a new ExcelSheet instance associated with the given ExcelFile using the default sheet index (0).
	 *
	 * @param ownerFile The ExcelFile to which this sheet belongs.
	 * @return An ExcelSheet instance representing the default sheet.
	 */
	public static ExcelSheet of(ExcelFile ownerFile){
		return of(ownerFile, DEFAULT_SHEET_INDEX);
	}

	/**
	 * Retrieves an ExcelTable associated with this sheet.
	 *
	 * @return The ExcelTable representing the content of this sheet.
	 */
	public ExcelTable getTable(){
		return new ExcelTable(this);
	}

	/**
	 * Converts the ExcelSheet to its parent ExcelFile.
	 *
	 * @return The ExcelFile containing this sheet.
	 */
	public ExcelFile toFile(){
		return ownerFile;
	}

	/**
	 * Writes a value to the cell at the specified row and column within this Excel sheet.
	 *
	 * @param rowNumber    The index of the row where the value should be written.
	 * @param columnNumber The index of the column where the value should be written.
	 * @param value        The value to write to the cell.
	 * @return This ExcelSheet instance.
	 */
	public ExcelSheet write(int rowNumber,int columnNumber,String value){
		getRow(rowNumber).write(columnNumber,value);
		return this;
	}

	/**
	 * Writes a value to the cell at the specified row and column within this Excel sheet.
	 *
	 * @param rowNumber    The index of the row where the value should be written.
	 * @param columnNumber The index of the column where the value should be written.
	 * @param value        The value to write to the cell.
	 * @return This ExcelSheet instance.
	 */
	public ExcelSheet write(int rowNumber,int columnNumber,Integer value){
		getRow(rowNumber).write(columnNumber,value);
		return this;
	}

	/**
	 * Reads the value from the cell at the specified row and column within this Excel sheet.
	 *
	 * @param rowNumber    The index(1-based) of the row from which to read the value.
	 * @param columnNumber The index(1-based) of the column from which to read the value.
	 * @return The value read from the specified cell.
	 */
	public String read(int rowNumber,int columnNumber){
		return getRow(rowNumber).read(columnNumber);
	}

	/**
	 * Reads the value from a column at the specified number.
	 * @param columnIndex The index (0-based) of the column from which read the value
	 * @return The cell list from the column
	 */
	public List<ExcelCell> readColumn(int columnIndex){
		return readColumn(columnIndex,1);
	}

	/**
	 * Reads the value from a column at the specified number. Starting from tthe given index
	 * @param columnIndex The index (0-based) of the column from which read the value
	 * @param headerIndex The index (0 based) of the header row.
	 * @return The cell list from the column
	 */
	public List<ExcelCell> readColumn(int columnIndex,int headerIndex){
		return rows.stream()
				.skip(headerIndex)
				.map(x->x.cells)
				.map(x->x.get(columnIndex))
				.collect(Collectors.toList());
	}
}
