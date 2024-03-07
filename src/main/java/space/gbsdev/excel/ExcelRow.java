package space.gbsdev.excel;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;

/**
 * Represents a row within an Excel sheet. Provides methods to access and manipulate the cells in the row.
 * This class is typically associated with an ExcelSheet and belongs to an ExcelFile.
 */
@SuppressWarnings("unused")
public class ExcelRow {

	private final ExcelSheet parent;

	/**
	 * The list of cells in the row.
	 */
	public List<ExcelCell> cells = new ArrayList<>();

	/**
	 * The underlying Apache POI row.
	 */
	public Row underlyingRow;

	/**
	 * Instantiates a new ExcelRow associated with the given ExcelSheet and underlying Apache POI Row.
	 *
	 * @param parentSheet   The ExcelSheet to which this row belongs.
	 * @param underlyingRow The underlying Apache POI Row.
	 */
	public ExcelRow(ExcelSheet parentSheet, Row underlyingRow) {
		this.parent = parentSheet;
		this.underlyingRow = underlyingRow;
		Iterator<Cell> cellIterator = underlyingRow.cellIterator();
		while (cellIterator.hasNext()) {
			cells.add(new ExcelCell(this, cellIterator.next()));
		}
	}

	/**
	 * Converts the ExcelRow to its parent ExcelSheet.
	 *
	 * @return The ExcelSheet containing this row.
	 */
	public ExcelSheet toSheet() {
		return parent;
	}

	/**
	 * Converts the ExcelRow to its parent ExcelFile.
	 *
	 * @return The ExcelFile containing this row.
	 */
	public ExcelFile toFile() {
		return parent.toFile();
	}

	/**
	 * Writes a value to the specified column in the row. If the value is null, an empty cell will be created.
	 *
	 * @param column The column number (1-based) where the value should be written.
	 * @param value  The value to write (nullable).
	 * @return This ExcelRow instance.
	 */
	public ExcelRow write(int column, String value) {
		getCell(column).write(value);
		return this;
	}

	/**
	 * Writes a value to the specified column in the row.
	 *
	 * @param column The column number (1-based) where the value should be written.
	 * @param value  The value to write.
	 * @return This ExcelRow instance.
	 */
	public ExcelRow write(int column, int value) {
		getCell(column).write(value);
		return this;
	}

	/**
	 * Retrieves the cell at the specified column (1-based) in the row.
	 *
	 * @param colNumber The column number (1-based) of the cell to retrieve.
	 * @return The ExcelCell at the specified column.
	 */
	public ExcelCell getCell(int colNumber) {
		final int columnNumber = colNumber - 1;
		if(cells.size() < colNumber){
			return new ExcelCell(this,underlyingRow.createCell(columnNumber));
		}
		return cells.get(columnNumber);
	}

	/**
	 * Reads the value from the cell at the specified column within this Excel row.
	 *
	 * @param columnNumber The index (1-based) of the column from which to read the value.
	 * @return The value read from the specified cell, or an empty string if the column doesn't exist in this row.
	 */
	public String read(int columnNumber){
		return getCell(columnNumber).stringValue();
	}

	/**
	 * Asserts whether the cell value at the specified column matches the given regular expression pattern.
	 *
	 * @param columnNumber  The index (1-based) of the column to check.
	 * @param regexPattern  The regular expression pattern to match against.
	 * @return true if the cell value matches the pattern, false otherwise.
	 */
	public boolean isTemplated(int columnNumber, String regexPattern){
		return extractMatcher(columnNumber,regexPattern).find();
	}

	/**
	 * Extracts a Matcher object for the cell value at the specified column using the provided regex pattern.
	 *
	 * @param columnNumber  The index (1-based) of the column to extract the Matcher from.
	 * @param regexPattern  The regular expression pattern to use for matching.
	 * @return A Matcher object for the cell value.
	 */
	public Matcher extractMatcher(int columnNumber, String regexPattern){
		return getCell(columnNumber).extractValue(regexPattern);
	}

}
