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

import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import static localexam.Settings.PREF_BUNDLE;
import localexam.bones.Room;
import localexam.bones.StudentExamParameters;
import localexam.template.metrics.StatementPage;
import net.mdrassty.object.Student;
import net.mdrassty.util.Misc;

/**
 *
 * @author H. KASSIMI (@mdrassty)
 */
public class StatementPDFFileGenerator extends PDFDocumentGenerator {

    private StatementPage metrics;
    private int i;
    private final DecimalFormat decimalFormat = new DecimalFormat("#00.00");

    public StatementPDFFileGenerator() {
        super("/localexam/template/resources/STATEMENT_TEMPLATE.pdf");
        metrics = null;
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
        boolean result;
        baseFile.reset();
        if (metrics == null) {
            metrics = new StatementPage();
        }
        processing.set(true);
        saved.set(false);
        pg = baseFile.getPage(0);
        Map<String, List<Student>> map = EXAM.getStudents().stream().filter(predicate).collect(Collectors.groupingBy(stu -> ((StudentExamParameters) stu.getUserData()).getGroup()));
        List<String> matters = EXAM.getPlannings().stream().sorted((p1, p2) -> {
            int c = Objects.compare(p1.getDate(), p2.getDate(), Comparator.reverseOrder());
            return (c == 0) ? Objects.compare(p1.getStartTime(), p2.getStartTime(), Comparator.reverseOrder()) : c;

        }).map(p -> p.getLabel()).distinct().collect(Collectors.toList());
        i = 0;
        matters.forEach(mat -> {
            try {
                baseFile.resizeNWrapString(metrics.getMatterHeaderCol(i++, matters.size()), pg, mat);
            } catch (CloneNotSupportedException ex) {
            }
        });
        try {
            metrics.prepareVDividers(matters.size()).forEach(div -> {
                baseFile.drawNFill(div, pg);
            });
        } catch (CloneNotSupportedException ex) {
        }
        map.keySet().stream().sorted((g1, g2) -> Objects.compare(Misc.getGroupWeight(g1), Misc.getGroupWeight(g2), Comparator.naturalOrder())).forEach(group -> {
            pg = baseFile.clonePage(0);
            baseFile.placeMultilineString(metrics.getMinistery(), pg, PREF_BUNDLE.get("KINGDOM_AR_MULTILINE").replaceAll("%new_line%", "\n"));
            baseFile.placeMultilineString(metrics.getSchool(), pg, EXAM.getInfos().getAcademy() + "\n" + EXAM.getInfos().getDirection() + "\n" + EXAM.getInfos().getSchool());
            baseFile.resizeNPlaceString(metrics.getTitle(), pg, EXAM.getLabel());
            baseFile.resizeNPlaceString(metrics.getSubtitle(), pg, PREF_BUNDLE.get("STATEMENT_TITLE_STRING"));
            baseFile.placeNRotateString(metrics.getYear(), pg, EXAM.getInfos().getYear());
            baseFile.placeString(metrics.getLevel(), pg, "المستوى : " + EXAM.getLevel().getName());
            baseFile.resizeNPlaceString(metrics.getGroup(), pg, "القسم : " + group);
            baseFile.resizeNPlaceString(metrics.getCapacity(), pg, "عدد الممتحنين : " + map.get(group).size());
            metrics.prepareRows(map.get(group).size());
            try {
                metrics.prepareDividers().forEach(div -> {
                    baseFile.drawNFill(div, pg);
                });
            } catch (CloneNotSupportedException ex) {
            }
            i = 0;
            map.get(group).forEach(stu -> {
                metrics.prepareRow(i++);
                baseFile.resizeNPlaceString(metrics.getCode(), pg, ((StudentExamParameters) stu.getUserData()).getExamCode() + "");
                baseFile.resizeNPlaceString(metrics.getFullName(), pg, stu.getFullName());
                for (int j = 0, n = matters.size(); j < n; j++) {
                    try {
                        baseFile.resizeNPlaceString(metrics.getMatterCol(j, n), pg, decimalFormat.format(stu.getMarks().getExamMark(EXAM.getType(), matters.get(j))));
                    } catch (CloneNotSupportedException | IllegalArgumentException ex) {
                    }
                }
            });
        });
        baseFile.removePage(0);
        result = (baseFile.getDoc().getPages().getCount() > 0) ? baseFile.save(PREF_BUNDLE.get("OUTPUT_DIR_STRING") + baseFile.generateFileName(PREF_BUNDLE.get("STATEMENT_FILENAME_PREFIX_STRING"), "pdf")) : false;
        processing.set(false);
        saved.set(true);
        return result;
    }

    private Boolean generateByRoom(Predicate<Student> predicate) {
        boolean result;
        baseFile.reset();
        if (metrics == null) {
            metrics = new StatementPage();
        }
        processing.set(true);
        saved.set(false);
        pg = baseFile.getPage(0);
        Map<Room, List<Student>> map = EXAM.getStudents().stream().filter(predicate).collect(Collectors.groupingBy(stu -> ((StudentExamParameters) stu.getUserData()).getRoom()));
        List<String> matters = EXAM.getPlannings().stream().sorted((p1, p2) -> {
            int c = Objects.compare(p1.getDate(), p2.getDate(), Comparator.reverseOrder());
            return (c == 0) ? Objects.compare(p1.getStartTime(), p2.getStartTime(), Comparator.reverseOrder()) : c;

        }).map(p -> p.getLabel()).distinct().collect(Collectors.toList());
        i = 0;
        matters.forEach(mat -> {
            try {
                baseFile.resizeNWrapString(metrics.getMatterHeaderCol(i++, matters.size()), pg, mat);
            } catch (CloneNotSupportedException ex) {
            }
        });
        try {
            metrics.prepareVDividers(matters.size()).forEach(div -> {
                baseFile.drawNFill(div, pg);
            });
        } catch (CloneNotSupportedException ex) {
        }
        map.keySet().stream().sorted((r1, r2) -> Objects.compare(r1.getNumero(), r2.getNumero(), Comparator.naturalOrder())).forEach(room -> {
            pg = baseFile.clonePage(0);
            baseFile.placeMultilineString(metrics.getMinistery(), pg, PREF_BUNDLE.get("KINGDOM_AR_MULTILINE").replaceAll("%new_line%", "\n"));
            baseFile.placeMultilineString(metrics.getSchool(), pg, EXAM.getInfos().getAcademy() + "\n" + EXAM.getInfos().getDirection() + "\n" + EXAM.getInfos().getSchool());
            baseFile.resizeNPlaceString(metrics.getTitle(), pg, EXAM.getLabel());
            baseFile.resizeNPlaceString(metrics.getSubtitle(), pg, PREF_BUNDLE.get("STATEMENT_TITLE_STRING"));
            baseFile.placeNRotateString(metrics.getYear(), pg, EXAM.getInfos().getYear());
            baseFile.placeString(metrics.getLevel(), pg, "المستوى : " + EXAM.getLevel().getName());
            baseFile.resizeNPlaceString(metrics.getGroup(), pg, "القاعة : " + room.getLabel());
            baseFile.resizeNPlaceString(metrics.getCapacity(), pg, "عدد الممتحنين : " + map.get(room).size());
            metrics.prepareRows(map.get(room).size());
            try {
                metrics.prepareDividers().forEach(div -> {
                    baseFile.drawNFill(div, pg);
                });
            } catch (CloneNotSupportedException ex) {
            }
            i = 0;
            map.get(room).forEach(stu -> {
                metrics.prepareRow(i++);
                baseFile.resizeNPlaceString(metrics.getCode(), pg, ((StudentExamParameters) stu.getUserData()).getExamCode() + "");
                baseFile.resizeNPlaceString(metrics.getFullName(), pg, stu.getFullName());
                for (int j = 0, n = matters.size(); j < n; j++) {
                    try {
                        baseFile.resizeNPlaceString(metrics.getMatterCol(j, n), pg, decimalFormat.format(stu.getMarks().getExamMark(EXAM.getType(), matters.get(j))));
                    } catch (CloneNotSupportedException | IllegalArgumentException ex) {
                    }
                }
            });
        });
        baseFile.removePage(0);
        result = (baseFile.getDoc().getPages().getCount() > 0) ? baseFile.save(PREF_BUNDLE.get("OUTPUT_DIR_STRING") + baseFile.generateFileName(PREF_BUNDLE.get("STATEMENT_FILENAME_PREFIX_STRING"), "pdf")) : false;
        processing.set(false);
        saved.set(true);
        return result;
    }

}
