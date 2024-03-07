package space.gbsdev.excel;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Represents a table within an Excel sheet. Provides methods to retrieve values by column name and row number.
 */
@SuppressWarnings("unused")
public class ExcelTable {
	/**
	 * The default header index (usually the first row) for the table.
	 */
	public static final int DEFAULT_HEADER_INDEX = 1;

	/**
	 * The default row index (usually the first row) for the table.
	 */
	public static final int DEFAULT_ROW_INDEX = 1;

	/**
	 * The Excel file associated with this table.
	 */
	public ExcelSheet sheet;

	/**
	 * The list of column names in the table.
	 */
	public List<String> columns;

	/**
	 * The index of the header row in the table.
	 */
	private final int headerIndex;

	/**
	 * Constructs a new ExcelTable associated with the given ExcelSheet, using the default sheet index (1) and header index (1).
	 *
	 * @param sheet The ExcelSheet representing the Excel document.
	 */
	public ExcelTable(ExcelSheet sheet){
		this(sheet,ExcelFile.DEFAULT_SHEET_NUMBER);
	}

	/**
	 * Constructs a new ExcelTable with the given ExcelFile and sheet index, using the default header index (1).
	 *
	 * @param sheet The ExcelFile representing the Excel document.
	 * @param sheetIndex The index of the sheet within the Excel document.
	 */
	public ExcelTable(ExcelSheet sheet,int sheetIndex){
		this(sheet,sheetIndex,DEFAULT_HEADER_INDEX);
	}

	/**
	 * Constructs a new ExcelTable with the given ExcelSheet, sheet index, and header index.
	 *
	 * @param sheet       The ExcelSheet representing the Excel document.
	 * @param sheetIndex  The index of the sheet within the Excel document.
	 * @param headerIndex The index of the header row in the table.
	 */
	public ExcelTable(ExcelSheet sheet,int sheetIndex,int headerIndex){
		Objects.requireNonNull(sheet, "sheet cannot be null");
		if (sheetIndex < 0 || headerIndex < 1) {
			throw new IllegalArgumentException("Invalid sheetIndex or headerIndex");
		}
		this.sheet = sheet;
		this.headerIndex = headerIndex;
		this.columns = sheet.getRow(headerIndex-1).cells.stream().map(ExcelCell::stringValue).collect(Collectors.toList());
	}

	/**
	 * Get the value from the specified column and row.
	 *
	 * @param columnName Name of the column.
	 * @param rowNumber Index of the row.
	 * @return The value in the specified cell.
	 * @throws NoSuchElementException if the column or row is not found.
	 */
	public String get(String columnName,int rowNumber){
		int columnIndex = columns.indexOf(columnName);
		if(columnIndex == -1)
			throw getColumnNotFoundError(columnName);
		final int rowIndex = headerIndex + rowNumber-1;
		try {
			final ExcelRow requestedRow = sheet.getRow(rowIndex);
			final ExcelCell requestedCell = requestedRow.getCell(columnIndex+1);
			return requestedCell.stringValue();
		} catch (IndexOutOfBoundsException e) {
			throw getRowNotFoundError(rowIndex);
		}
	}
	/**
	 * Get the value from the specified column's first row
	 *
	 * @param columnName Name of the column.
	 * @return The value in the specified cell.
	 * @throws NoSuchElementException if the column or row is not found.
	 */
	public String get(String columnName){
		return get(columnName,DEFAULT_ROW_INDEX);
	}

	/**
	 * Get row not found error no such element exception.
	 *
	 * @param rowIndex the row index
	 * @return the no such element exception
	 */
	private NoSuchElementException getRowNotFoundError(int rowIndex){
		return new NoSuchElementException("Row not found at index: " + rowIndex);
	}

	/**
	 * Get column not found error no such element exception.
	 *
	 * @param columnName the column name
	 * @return the no such element exception
	 */
	private NoSuchElementException getColumnNotFoundError(String columnName){
		return new NoSuchElementException(columnName + " column not found in list: " + String.join(",", columns));
	}
}
