/*
 * Copyright (C) 2022 H. KASSIMI
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package localexam.template.generators;

import java.awt.Desktop;
import java.io.File;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import static localexam.Settings.PREF_BUNDLE;
import localexam.bones.Room;
import localexam.bones.StudentExamParameters;
import net.mdrassty.object.Student;
import net.mdrassty.util.Misc;

/**
 *
 * @author H. KASSIMI (@mdrassty)
 */
public class StatementXLSXGenerator extends DocumentGenerator<Workbook, Sheet> {

    private int i;
    private final List<String> HEADER = Arrays.asList(PREF_BUNDLE.get("STUDENT_NUMERO_STRING"), 
                PREF_BUNDLE.get("STUDENT_MASSAR_CODE_STRING"), 
                PREF_BUNDLE.get("STUDENT_EXAM_CODE_STRING"), 
                PREF_BUNDLE.get("ROOM_STRING"),
                PREF_BUNDLE.get("STUDENT_FULLNAME_STRING"));

    public StatementXLSXGenerator() {
        super();
    }

    public Boolean generate(Room room) {
        return generateByRoom(stu -> Objects.equals(room, ((StudentExamParameters) stu.getUserData()).getRoom()));
    }

    public Boolean generate(String groupName) {
        return generateByGroup(stu -> Objects.equals(groupName, ((StudentExamParameters) stu.getUserData()).getGroup()));
    }

    public Boolean generateByRoom() {
        return generateByRoom(stu -> true);
    }

    public Boolean generateByGroup() {
        return generateByGroup(stu -> true);
    }

    private Boolean generateByGroup(Predicate<Student> predicate) {
        Map<String, List<Student>> map = EXAM.getStudents().stream().filter(predicate).collect(Collectors.groupingBy(stu -> ((StudentExamParameters) stu.getUserData()).getGroup()));
        ArrayList<String> matters = new ArrayList(HEADER);
        matters.addAll(EXAM.getPlannings().stream().sorted((p1, p2) -> {
            int c = Objects.compare(p1.getDate(), p2.getDate(), Comparator.reverseOrder());
            return (c == 0) ? Objects.compare(p1.getStartTime(), p2.getStartTime(), Comparator.reverseOrder()) : c;
        }).map(p -> p.getLabel()).distinct().collect(Collectors.toList()));
        processing.set(true);
        saved.set(false);
        baseFile = new XSSFWorkbook();
        map.keySet().stream().sorted((g1, g2) -> Objects.compare(Misc.getGroupWeight(g1), Misc.getGroupWeight(g2), Comparator.naturalOrder())).forEach(group -> {
            pg = baseFile.createSheet(group);
            pg.setRightToLeft(true);
            createHeaderRow(baseFile, pg, matters);
            i = 1;
            map.get(group).stream().sorted((s1, s2) -> Integer.compare(s1.getNum(), s2.getNum())).forEach(stu -> {
                Row row = pg.createRow(i++);
                CellStyle cellStyle = baseFile.createCellStyle();
                cellStyle.setBorderTop(BorderStyle.THIN);
                cellStyle.setTopBorderColor(IndexedColors.BLACK.index);
                cellStyle.setBorderRight(BorderStyle.THIN);
                cellStyle.setRightBorderColor(IndexedColors.BLACK.index);
                cellStyle.setBorderBottom(BorderStyle.THIN);
                cellStyle.setBottomBorderColor(IndexedColors.BLACK.index);
                cellStyle.setBorderLeft(BorderStyle.THIN);
                cellStyle.setLeftBorderColor(IndexedColors.BLACK.index);
                Cell cell = row.createCell(0);
                cell.setCellValue(stu.getNum());
                cell.setCellStyle(cellStyle);
                cell = row.createCell(1);
                cell.setCellValue(stu.getCode());
                cell.setCellStyle(cellStyle);
                cell = row.createCell(2);
                cell.setCellValue(((StudentExamParameters) stu.getUserData()).getExamCode());
                cell.setCellStyle(cellStyle);
                cell = row.createCell(3);
                cell.setCellValue(((StudentExamParameters) stu.getUserData()).getRoom().getLabel());
                cell.setCellStyle(cellStyle);
                cell = row.createCell(4);
                cell.setCellValue(stu.getFullName());
                cell.setCellStyle(cellStyle);
                for (int j = 5, n = matters.size(); j < n; j++) {
                    cell = row.createCell(j);
                    try {
                        cell.setCellValue(stu.getMarks().getExamMark(EXAM.getType(), matters.get(j)));
                    } catch (NullPointerException npl) {
                    }
                    cell.setCellStyle(cellStyle);
                }
            });
            for (i = 0; i < matters.size(); i++) {
                pg.autoSizeColumn(i);
            }
        });
        processing.set(false);
        try (OutputStream outputStream = new FileOutputStream(PREF_BUNDLE.get("OUTPUT_DIR_STRING") + "/" + PREF_BUNDLE.get("XLSX_STATEMENT_FILENAME_PREFIX_STRING") + DateTimeFormatter.ofPattern("yyyy.MM.dd.HH.mm.ss").format(LocalDateTime.now()) + "." + "xlsx")) {
            baseFile.write(outputStream);
            saved.set(true);
            return true;
        } catch (IOException e) {
        }
        return false;
    }

    private Boolean generateByRoom(Predicate<Student> predicate) {
        Map<Room, List<Student>> map = EXAM.getStudents().stream().filter(predicate).collect(Collectors.groupingBy(stu -> ((StudentExamParameters) stu.getUserData()).getRoom()));
        ArrayList<String> matters = new ArrayList(HEADER);
        matters.addAll(EXAM.getPlannings().stream().sorted((p1, p2) -> {
            int c = Objects.compare(p1.getDate(), p2.getDate(), Comparator.reverseOrder());
            return (c == 0) ? Objects.compare(p1.getStartTime(), p2.getStartTime(), Comparator.reverseOrder()) : c;
        }).map(p -> p.getLabel()).distinct().collect(Collectors.toList()));
        processing.set(true);
        saved.set(false);
        baseFile = new XSSFWorkbook();
        map.keySet().stream().sorted((r1, r2) -> Objects.compare(r1.getNumero(), r2.getNumero(), Comparator.naturalOrder())).forEach(room -> {
            pg = baseFile.createSheet(room.getLabel());
            pg.setRightToLeft(true);
            createHeaderRow(baseFile, pg, matters);
            i = 1;
            map.get(room).forEach(stu -> {
                Row row = pg.createRow(i++);
                CellStyle cellStyle = baseFile.createCellStyle();
                cellStyle.setBorderTop(BorderStyle.THIN);
                cellStyle.setTopBorderColor(IndexedColors.BLACK.index);
                cellStyle.setBorderRight(BorderStyle.THIN);
                cellStyle.setRightBorderColor(IndexedColors.BLACK.index);
                cellStyle.setBorderBottom(BorderStyle.THIN);
                cellStyle.setBottomBorderColor(IndexedColors.BLACK.index);
                cellStyle.setBorderLeft(BorderStyle.THIN);
                cellStyle.setLeftBorderColor(IndexedColors.BLACK.index);
                Cell cell = row.createCell(0);
                cell.setCellValue(stu.getNum());
                cell.setCellStyle(cellStyle);
                cell = row.createCell(1);
                cell.setCellValue(stu.getCode());
                cell.setCellStyle(cellStyle);
                cell = row.createCell(2);
                cell.setCellValue(((StudentExamParameters) stu.getUserData()).getExamCode());
                cell.setCellStyle(cellStyle);
                cell = row.createCell(3);
                cell.setCellValue(((StudentExamParameters) stu.getUserData()).getGroup());
                cell.setCellStyle(cellStyle);
                cell = row.createCell(4);
                cell.setCellValue(stu.getFullName());
                cell.setCellStyle(cellStyle);
                for (int j = 5, n = matters.size(); j < n; j++) {
                    cell = row.createCell(j);
                    try {
                        cell.setCellValue(stu.getMarks().getExamMark(EXAM.getType(), matters.get(j)));
                    } catch (NullPointerException npl) {
                    }
                    cell.setCellStyle(cellStyle);
                }
            });
            for (i = 0; i < matters.size(); i++) {
                pg.autoSizeColumn(i);
            }
        });
        processing.set(false);
        try (OutputStream outputStream = new FileOutputStream(PREF_BUNDLE.get("OUTPUT_DIR_STRING") + "/" + PREF_BUNDLE.get("XLSX_STATEMENT_FILENAME_PREFIX_STRING") + DateTimeFormatter.ofPattern("yyyy.MM.dd.HH.mm.ss").format(LocalDateTime.now()) + "." + "xlsx")) {
            baseFile.write(outputStream);
            saved.set(true);
            return true;
        } catch (IOException e) {
        }
        return false;
    }

    private void createHeaderRow(Workbook workbook, Sheet sheet, List<String> headers) {
        Row headerRow = sheet.createRow(0);
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.index);
        headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerCellStyle.setBorderTop(BorderStyle.THIN);
        headerCellStyle.setTopBorderColor(IndexedColors.BLACK.index);
        headerCellStyle.setBorderRight(BorderStyle.THIN);
        headerCellStyle.setRightBorderColor(IndexedColors.BLACK.index);
        headerCellStyle.setBorderBottom(BorderStyle.THIN);
        headerCellStyle.setBottomBorderColor(IndexedColors.BLACK.index);
        headerCellStyle.setBorderLeft(BorderStyle.THIN);
        headerCellStyle.setLeftBorderColor(IndexedColors.BLACK.index);
        i = 0;
        headers.forEach(header -> {
            Cell cell = headerRow.createCell(i++);
            cell.setCellValue(header);
            cell.setCellStyle(headerCellStyle);
        });
    }
}
