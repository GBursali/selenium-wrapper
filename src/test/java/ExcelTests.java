import org.junit.Test;
import com.gbursali.excel.ExcelCell;
import com.gbursali.excel.ExcelFile;
import com.gbursali.excel.ExcelSheet;
import com.gbursali.excel.ExcelTable;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class ExcelTests {

	final File testFile = Path.of("src", "test", "resources", "excelTestFile.xlsx").toFile();
	final ExcelFile testObject = ExcelFile.fromExisting(testFile);

	@Test
	public void checkIfWeCanReadTheExcelFile() {
		final String expected = "No";
		final String actual = testObject
				.getSheet(1)
				.read(1, 1);

		assertEquals(expected, actual);
	}

	@Test
	public void checkIfWeCanReadTheExcelFileAnySheet() {
		final String expected = "No";
		final String actual = testObject
				.getSheet(2)
				.read(1, 1);

		assertEquals(expected, actual);
	}

	@Test
	public void checkIfWeCanFindTheTable() {
		final ExcelSheet sheet = testObject
				.getSheet(1);
		var table = new ExcelTable(sheet);

		final var expected = List.of("1.0", "Name1", "Surname1");
		final var actual = List.of(
				table.get("No", 1),
				table.get("Name", 1),
				table.get("Surname", 1)
		);

		assertEquals(expected, actual);
	}

	@Test
	public void checkIfWeCanFindTheTablesAnyRow() {
		final ExcelSheet sheet = testObject
				.getSheet(1);
		var table = new ExcelTable(sheet);

		final var expected = List.of("2.0", "Name2", "Surname2");
		final var actual = List.of(
				table.get("No", 2),
				table.get("Name", 2),
				table.get("Surname", 2)
		);

		assertEquals(expected, actual);
	}
	@Test
	public void checkIfWeCanFindTheTablesColumn() {
		final ExcelSheet sheet = testObject
				.getSheet(1);
		var table = new ExcelTable(sheet);

		final var expected = List.of("1.0", "2.0");
		final var actual = table.getColumnValues("No")
				.stream()
				.map(ExcelCell::stringValue)
				.collect(Collectors.toList());

		assertEquals(expected, actual);
	}
}
