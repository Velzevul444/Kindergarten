package utils;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

public class ExcelExporter {

    public static void exportAllTables() {
        String fileName = "database_export.xlsx";

        try (
                Connection connection = Database.getConnection();
                Workbook workbook = new XSSFWorkbook();
                FileOutputStream outputStream =
                        new FileOutputStream(fileName)
        ) {
            DatabaseMetaData metadata = connection.getMetaData();

            try (ResultSet tables = metadata.getTables(
                    connection.getCatalog(),
                    "public",
                    "%",
                    new String[]{"TABLE"}
            )) {
                while (tables.next()) {
                    String tableName = tables.getString("TABLE_NAME");
                    exportTable(connection, workbook, tableName);
                }
            }

            workbook.write(outputStream);

            System.out.println(
                    "Excel file created: "
                            + java.nio.file.Path.of(fileName)
                            .toAbsolutePath()
            );

        } catch (Exception error) {
            System.out.println(
                    "Excel export error: " + error.getMessage()
            );
        }
    }

    private static void exportTable(
            Connection connection,
            Workbook workbook,
            String tableName
    ) throws Exception {

        Sheet sheet = workbook.createSheet(tableName);

        String sql = "SELECT * FROM \"" + tableName + "\"";

        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql)
        ) {
            ResultSetMetaData resultMetadata =
                    resultSet.getMetaData();

            int columnCount = resultMetadata.getColumnCount();
            int rowNumber = 0;

            Row headerRow = sheet.createRow(rowNumber++);

            for (int column = 1; column <= columnCount; column++) {
                headerRow.createCell(column - 1)
                        .setCellValue(
                                resultMetadata.getColumnName(column)
                        );
            }

            while (resultSet.next()) {
                Row row = sheet.createRow(rowNumber++);

                for (int column = 1; column <= columnCount; column++) {
                    Object value = resultSet.getObject(column);

                    row.createCell(column - 1)
                            .setCellValue(
                                    value == null
                                            ? ""
                                            : value.toString()
                            );
                }
            }

            for (int column = 0; column < columnCount; column++) {
                sheet.autoSizeColumn(column);
            }
        }
    }
}