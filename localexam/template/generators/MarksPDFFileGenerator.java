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
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import static localexam.Settings.PREF_BUNDLE;
import localexam.bones.StudentExamParameters;
import localexam.template.metrics.MarksPage;
import net.mdrassty.object.Student;

/**
 *
 * @author H. KASSIMI (@mdrassty)
 */
public class MarksPDFFileGenerator extends PDFDocumentGenerator {

    private MarksPage metrics;
    private int i, curPage, pagesCount;
    private final BooleanProperty includeMarks = new SimpleBooleanProperty(false);
    private final DecimalFormat decimalFormat = new DecimalFormat("#00.00");

    public MarksPDFFileGenerator() {
        super("/localexam/template/resources/MARKS_TEMPLATE.pdf");
        metrics = null;
    }

    public Boolean generate(String matter) {
        return generate(m -> Objects.equals(matter, m), s -> true);
    }

    public Boolean generate(int firstCode, int lastCode) {
        return generate(r -> true, s -> {
            int code = ((StudentExamParameters) s.getUserData()).getExamCode();
            return code >= firstCode && code <= lastCode;
        });
    }

    public Boolean generate(String matter, int firstCode, int lastCode) {
        return generate(m -> Objects.equals(matter, m), s -> {
            int code = ((StudentExamParameters) s.getUserData()).getExamCode();
            return code >= firstCode && code <= lastCode;
        });
    }

    public Boolean generate() {
        return generate(m -> true, s -> true);
    }

    private Boolean generate(Predicate<String> mattersPredicate, Predicate<Student> codesPredicate) {
        boolean result;
        BiConsumer<Student, String> cons;
        baseFile.reset();
        if (metrics == null) {
            metrics = new MarksPage();
        }
        processing.set(true);
        saved.set(false);
        if (includeMarks.get()) {
            cons = (stu, mat) -> {
                try {
                    baseFile.placeString(metrics.getMark(i), pg, decimalFormat.format(stu.getMarks().getExamMark(EXAM.getType(), mat)) + "");
                } catch (IllegalArgumentException iae) {
                }
            };
        } else {
            cons = (stu, mat) -> {
            };
        }
        EXAM.getPlannings().stream().map(p -> p.getLabel()).filter(mattersPredicate).forEach(mat -> {
            List<Student> stus = EXAM.getStudents().stream().filter(codesPredicate).sorted((s1, s2)
                    -> Objects.compare(((StudentExamParameters) s1.getUserData()).getExamCode(), ((StudentExamParameters) s2.getUserData()).getExamCode(), Comparator.naturalOrder()))
                    .collect(Collectors.toList());
            i = 0;
            curPage = 1;
            pagesCount = stus.size() / metrics.getSize() + (stus.size() % metrics.getSize() == 0 ? 0 : 1);
            stus.forEach(stu -> {
                if (i % metrics.getSize() == 0) {
                    pg = baseFile.clonePage(0);
                    i = 0;
                    baseFile.placeMultilineString(metrics.getMinistery(), pg, PREF_BUNDLE.get("KINGDOM_AR_MULTILINE").replaceAll("%new_line%", "\n"));
                    baseFile.placeMultilineString(metrics.getSchool(), pg, EXAM.getInfos().getAcademy() + "\n" + EXAM.getInfos().getDirection() + "\n" + EXAM.getInfos().getSchool());
                    baseFile.resizeNPlaceString(metrics.getTitle(), pg, EXAM.getLabel());
                    baseFile.resizeNPlaceString(metrics.getSubtitle(), pg, PREF_BUNDLE.get("MARKS_TITLE_STRING"));
                    baseFile.placeNRotateString(metrics.getYear(), pg, EXAM.getInfos().getYear());
                    baseFile.resizeNPlaceString(metrics.getMatter(), pg, "مادة : " + mat);
                    baseFile.resizeNPlaceString(metrics.getCapacity(), pg, "عدد الممتحنين : " + stus.size());
                    baseFile.resizeNPlaceString(metrics.getPager(), pg, curPage + " / " + pagesCount);
                    curPage++;
                }
                //placeString(metrics.getGender(), pg, stu.isFemale() ? "\ue9dd" : "\ue9dc");
                baseFile.placeString(metrics.getCode(i), pg, (((StudentExamParameters) stu.getUserData()).getExamCode() + EXAM.getTranslationGap()) + "");
                cons.accept(stu, mat);
                i++;
            });
        });
        baseFile.removePage(0);
        result = (baseFile.getDoc().getPages().getCount() > 0) ? baseFile.save(PREF_BUNDLE.get("OUTPUT_DIR_STRING") + baseFile.generateFileName(PREF_BUNDLE.get("MARKS_PAPER_FILENAME_PREFIX_STRING"), "pdf")) : false;
        processing.set(false);
        saved.set(true);
        return result;
    }

    public boolean isIncludeMarks() {
        return includeMarks.get();
    }

    public void setIncludeMarks(boolean value) {
        includeMarks.set(value);
    }

    public BooleanProperty includeMarksProperty() {
        return includeMarks;
    }

}
