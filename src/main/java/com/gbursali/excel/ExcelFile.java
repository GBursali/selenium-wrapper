package com.gbursali.excel;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.util.TempFile;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Assert;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Represents an Excel file. Provides methods for creating, opening, and manipulating Excel files.
 */
@SuppressWarnings("unused")
public class ExcelFile {

	/**
	 * The constant DEFAULT_SHEET_NUMBER.
	 */
	public static final int DEFAULT_SHEET_NUMBER = 0;

	/**
	 * The Workbook.
	 */
	public XSSFWorkbook workbook;

	/**
	 * The input file representing the Excel file.
	 */
	public File inputFile;

	/**
	 * The list of Excel sheets within the Excel file.
	 */
	public List<ExcelSheet> sheets = new ArrayList<>();

	/**
	 * Creates an ExcelFile instance from an existing Excel file.
	 *
	 * @param excelFile The File object representing the existing Excel file.
	 * @return An ExcelFile instance representing the existing Excel file.
	 */
	public static ExcelFile fromExisting(File excelFile){
		try {
			OPCPackage pkg = OPCPackage.open(excelFile);
			return new ExcelFile(excelFile,new XSSFWorkbook(pkg));
		} catch (IOException | InvalidFormatException e) {
			throw new RuntimeException("Given excel file is not found");
		}
	}

	/**
	 * Creates a new ExcelFile instance with a temporary file.
	 *
	 * @return A new ExcelFile instance with a temporary file.
	 */
	public static ExcelFile fromTemporary(){
		ExcelFile ef = new ExcelFile(ExcelFile.getTempFile());
		ef.workbook = new XSSFWorkbook();
		return ef;
	}

	/**
	 * Gets a temporary Excel file.
	 *
	 * @return A File object representing a temporary Excel file.
	 */
	private static File getTempFile(){
		try {
			return TempFile.createTempFile("excel_", ".xlsx");
		} catch (IOException e) {
			Assert.fail("Cannot create a temp file");
			throw new RuntimeException();
		}
	}

	/**
	 * Creates a new ExcelFile instance associated with an existing Excel file specified by the given File object.
	 * Use the static factory methods 'fromExisting' or 'fromTemporary' to create instances instead of this constructor.
	 *
	 * @param inputFile The File object representing the existing Excel file.
	 */
	protected ExcelFile(File inputFile){
		this(inputFile,new XSSFWorkbook());
	}

	/**
	 * Creates a new ExcelFile instance associated with an existing Excel file and workbook specified by the given File object.
	 * Use the static factory methods 'fromExisting' or 'fromTemporary' to create instances instead of this constructor.
	 *
	 * @param inputFile The File object representing the existing Excel file.
	 * @param workbook The Workbook if there is one
	 */
	protected ExcelFile(File inputFile,XSSFWorkbook workbook){
		this.workbook = workbook;
		this.inputFile = inputFile;
		Iterator<Sheet> sheets = workbook.sheetIterator();
		while (sheets.hasNext()) {
			this.sheets.add(ExcelSheet.of(this, sheets.next().getSheetName()));
		}
	}

	/**
	 * Gets an ExcelSheet by its name from this Excel file.
	 *
	 * @param name The name of the Excel sheet to retrieve.
	 * @return An ExcelSheet instance representing the named sheet.
	 */
	public ExcelSheet getSheet(String name){
		return ExcelSheet.of(this, name);
	}

	/**
	 * Gets an ExcelSheet by its index from this Excel file.
	 *
	 * @param index The index of the Excel sheet to retrieve.
	 * @return An ExcelSheet instance representing the sheet at the specified index.
	 */
	public ExcelSheet getSheet(int index){
		return ExcelSheet.of(this, index);
	}


	/**
	 * Saves the Excel workbook to a specified file path.
	 *
	 * @param fileName The path where the Excel file should be saved.
	 * @return This ExcelFile instance.
	 */
	public ExcelFile save(Path fileName){
		inputFile = fileName.toFile();
		try (FileOutputStream stream = new FileOutputStream(inputFile)) {
			Files.copy(inputFile.toPath(), fileName, StandardCopyOption.REPLACE_EXISTING);
			workbook.write(stream);
		} catch (IOException e) {
			throw new IllegalArgumentException(fileName + " is not found");
		}
		return this;
	}

}
