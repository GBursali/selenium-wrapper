package space.gbsdev.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a cell within an Excel sheet. Provides methods to retrieve and set cell values.
 */
@SuppressWarnings("unused")
public class ExcelCell {
	private final ExcelRow parent;
	public Cell underlyingCell;

	/**
	 * Constructs a new ExcelCell associated with the given ExcelRow and underlying Apache POI Cell.
	 *
	 * @param parentRow      The ExcelRow to which this cell belongs.
	 * @param underlyingCell The underlying Apache POI Cell.
	 */
	public ExcelCell(ExcelRow parentRow, Cell underlyingCell) {
		this.parent = parentRow;
		this.underlyingCell = underlyingCell;
	}

	/**
	 * Retrieves the string value of the cell. If the cell contains a numeric value, it is converted to a string.
	 *
	 * @return The string value of the cell.
	 */
	public String stringValue() {
		if (underlyingCell.getCellType().equals(CellType.NUMERIC))
			return intValue().toString();
		return underlyingCell.getStringCellValue();
	}

	/**
	 * Retrieves the numeric (integer or floating-point) value of the cell.
	 *
	 * @return The numeric value of the cell.
	 */
	public Double intValue() {
		return underlyingCell.getNumericCellValue();
	}

	/**
	 * Converts the ExcelCell to its parent ExcelRow.
	 *
	 * @return The ExcelRow to which this cell belongs.
	 */
	public ExcelRow toRow() {
		return parent;
	}

	/**
	 * Converts the ExcelCell to its parent ExcelSheet.
	 *
	 * @return The ExcelSheet containing this cell.
	 */
	public ExcelSheet toSheet() {
		return parent.toSheet();
	}

	/**
	 * Converts the ExcelCell to its parent ExcelFile.
	 *
	 * @return The ExcelFile containing this cell.
	 */
	public ExcelFile toFile() {
		return toSheet().toFile();
	}

	/**
	 * Sets the value of the cell to the specified string value.
	 *
	 * @param value The string value to set in the cell.
	 * @return This ExcelCell instance.
	 */
	public ExcelCell write(String value) {
		underlyingCell.setCellValue(value);
		return this;
	}

	/**
	 * Sets the value of the cell to the specified integer value.
	 *
	 * @param value The integer value to set in the cell.
	 * @return This ExcelCell instance.
	 */
	public ExcelCell write(int value) {
		underlyingCell.setCellValue(value);
		return this;
	}

	/**
	 * Extracts a Matcher object for the cell value using the provided regex pattern.
	 *
	 * @param pattern The regular expression pattern to use for matching.
	 * @return A Matcher object for the cell value.
	 */
	public Matcher extractValue(String pattern){
		return Pattern.compile(pattern).matcher(stringValue());
	}
}
