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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import static java.util.Comparator.comparing;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import static localexam.Settings.PREF_BUNDLE;
import localexam.bones.Planning;
import localexam.bones.Room;
import localexam.bones.StudentExamParameters;
import localexam.template.metrics.RequisitPage;
import net.mdrassty.object.Student;
import net.mdrassty.util.CustomDate;

/**
 *
 * @author H. KASSIMI (@mdrassty)
 */
public class RequisitPDFFileGenerator extends PDFDocumentGenerator {

    private RequisitPage metrics;
    private int i, j;
    private final DecimalFormat intFormat = new DecimalFormat("#00");

    public RequisitPDFFileGenerator() {
        super("/localexam/template/resources/REQUISIT_TEMPLATE.pdf");
        metrics = null;
    }

    public Boolean generate(Room room) {
        return generate(stu -> Objects.equals(room, ((StudentExamParameters) stu.getUserData()).getRoom()),
                (stu1, stu2) -> Objects.compare(((StudentExamParameters) stu1.getUserData()).getExamCode(),
                        ((StudentExamParameters) stu2.getUserData()).getExamCode(),
                        Comparator.naturalOrder()));
    }

    public Boolean generate(String groupName) {
        return generate(stu -> Objects.equals(groupName, ((StudentExamParameters) stu.getUserData()).getGroup()),
                (stu1, stu2) -> Objects.compare(stu1.getNum(), stu2.getNum(), Comparator.naturalOrder()));
    }

    public Boolean generate(int examCode) {
        return generate(stu -> Objects.equals(examCode, ((StudentExamParameters) stu.getUserData()).getExamCode()), (s1, s2) -> 1);
    }

    public Boolean generateByGroup() {
        return generate(stu -> true, (stu1, stu2) -> {
            int c = Objects.compare(((StudentExamParameters) stu1.getUserData()).getGroup(), ((StudentExamParameters) stu2.getUserData()).getGroup(),
                    Comparator.naturalOrder());
            return (c == 0) ? Objects.compare(stu1.getNum(), stu2.getNum(), Comparator.naturalOrder()) : c;
        });
    }

    public Boolean generateByCode() {
        return generate(stu -> true, (stu1, stu2) -> Objects.compare(((StudentExamParameters) stu1.getUserData()).getExamCode(), ((StudentExamParameters) stu2.getUserData()).getExamCode(),
                Comparator.naturalOrder())
        );
    }

    private Boolean generate(Predicate<Student> predicate, Comparator<Student> comp) {
        boolean result;
        Map<LocalDate, List<Planning>> map;
        baseFile.reset();
        if (metrics == null) {
            metrics = new RequisitPage();
        }
        processing.set(true);
        saved.set(false);
        pg = baseFile.getPage(0);
        map = EXAM.getPlannings().stream().collect(Collectors.groupingBy(Planning::getDate));
        metrics.prepareRows(map.size());
        try {
            metrics.prepareDividers().forEach(div -> {
                baseFile.drawNFill(div, pg);
            });
        } catch (CloneNotSupportedException ex) {
        }
        i = 0;
        map.keySet().stream().sorted().forEach(date -> {
            metrics.prepareRow(i++);
            baseFile.placeMultilineString(metrics.getDate(), pg, CustomDate.getArabicMaDay(date.getDayOfWeek()) + "\n" + intFormat.format(date.getDayOfMonth())
                    + " " + CustomDate.getArabicMaMonth(date.getMonth()) + " " + date.getYear());

            map.get(date).stream().collect(Collectors.groupingBy(p -> p.getStartTime().format(DateTimeFormatter.ofPattern("a")))).forEach((halfDay, plans) -> {
                try {
                    metrics.prepareVDividers(halfDay, plans.size()).forEach(div -> {
                        baseFile.drawNFill(div, pg);
                    });
                } catch (CloneNotSupportedException ex) {
                }
                j = 0;
                plans.stream().sorted(comparing(Planning::getStartTime).reversed()).forEach(p -> {
                    try {
                        baseFile.placeMultilineText(metrics.getHalfDayCol(halfDay, j++, plans.size()), pg, p.getLabel() + "\nمن " + p.getStartTime() + " إلى " + p.getEndTime());
                    } catch (CloneNotSupportedException ex) {
                    }
                });
            });
        });
        //
        EXAM.getStudents().stream().filter(predicate).sorted(comp).forEach(stu -> {
            pg = baseFile.clonePage(0);
            baseFile.placeMultilineString(metrics.getMinistery(), pg, PREF_BUNDLE.get("KINGDOM_AR_MULTILINE").replaceAll("%new_line%", "\n"));
            baseFile.placeMultilineString(metrics.getSchool(), pg, EXAM.getInfos().getAcademy() + "\n" + EXAM.getInfos().getDirection() + "\n" + EXAM.getInfos().getSchool());
            baseFile.placeString(metrics.getSchoolName(), pg, EXAM.getInfos().getSchool());
            baseFile.placeNRotateString(metrics.getYear(), pg, EXAM.getInfos().getYear());
            baseFile.placeString(metrics.getTo(), pg, "إلى التلميذ" + (stu.isFemale() ? "ة " : " ") + stu.getFullName());
            baseFile.placeString(metrics.getRoom(), pg, "القاعة : " + ((StudentExamParameters) stu.getUserData()).getRoom().getLabel());
            baseFile.placeString(metrics.getCode(), pg, "رقم الامتحان : " + ((StudentExamParameters) stu.getUserData()).getExamCode());
            baseFile.placeString(metrics.getGroup(), pg, "القسم : " + ((StudentExamParameters) stu.getUserData()).getGroup());
            baseFile.placeString(metrics.getMassar(), pg, "رقم مسار : " + stu.getCode());
            baseFile.resizeNPlaceString(metrics.getSubject(), pg, "الموضوع : " + EXAM.getLabel() + " .");
            baseFile.placeMultilineString(metrics.getInstructions(), pg, PREF_BUNDLE.get("REQUISIT_INSTRUCTIONS_MULTILINE").replaceAll("%new_line%", "\n"));
            baseFile.placeMultilineString(metrics.getMessage(0), pg, PREF_BUNDLE.get("REQUISIT_MSG_ABOVE_PLANNING_TABLE_MULTILINE").replaceAll("%new_line%", "\n"));
            baseFile.placeMultilineString(metrics.getMessage(1), pg, PREF_BUNDLE.get("REQUISIT_MSG_UNDER_PLANNING_TABLE_MULTILINE").replaceAll("%new_line%", "\n"));
        });
        baseFile.removePage(0);
        result = (baseFile.getDoc().getPages().getCount() > 0) ? baseFile.save(PREF_BUNDLE.get("OUTPUT_DIR_STRING") + baseFile.generateFileName(PREF_BUNDLE.get("REQUISIT_FILENAME_PREFIX_STRING"), "pdf")) : false;
        processing.set(false);
        saved.set(true);
        return result;
    }

}
