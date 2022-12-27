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
package localexam.bones;

import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import net.mdrassty.object.Student;
import net.mdrassty.object.enums.EXAM_TYPE;
import net.mdrassty.util.SpinnerInitializer;

/**
 *
 * @author H. KASSIMI (@mdrassty)
 */
public final class MarkInput extends HBox {

    private final Label codeLbl = new Label();
    private final Spinner<Double> markSP = new Spinner();

    private final SimpleIntegerProperty code = new SimpleIntegerProperty();
    private final SimpleDoubleProperty mark = new SimpleDoubleProperty();
    private Student student;
    private EXAM_TYPE exam;
    private String matter;

    public MarkInput(Student student, EXAM_TYPE exam, String matter) {
        SpinnerInitializer.initMarkSpinner(markSP);
        code.addListener((obs, old, cur) -> {
            if (cur != null) {
                codeLbl.setText(cur + "");
            }
        });
        markSP.getValueFactory().valueProperty().addListener((obs, old, cur) -> {
            if (cur != null) {
                mark.set(cur);
            }
        });
        codeLbl.setPrefWidth(36);
        setSpacing(12);
        getChildren().addAll(codeLbl, new Label(":"), markSP);
        markSP.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                ((BehaviorSkinBase) markSP.getEditor().getSkin()).getBehavior().traverseNext();
            }
        });

        this.matter = matter;
        this.exam = exam;
        setStudent(student);
    }

    public MarkInput(Student student, EXAM_TYPE exam, String matter, int translate) {
        SpinnerInitializer.initMarkSpinner(markSP);
        code.addListener((obs, old, cur) -> {
            if (cur != null) {
                codeLbl.setText(cur + "");
            }
        });
        markSP.getValueFactory().valueProperty().addListener((obs, old, cur) -> {
            if (cur != null) {
                mark.set(cur);
            }
        });
        codeLbl.setPrefWidth(36);
        setSpacing(12);
        getChildren().addAll(codeLbl, new Label(":"), markSP);
        markSP.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                ((BehaviorSkinBase) markSP.getEditor().getSkin()).getBehavior().traverseNext();
            }
        });

        this.matter = matter;
        this.exam = exam;
        setTransStudent(student, translate);
    }

    public int getCode() {
        return code.get();
    }

    public void setCode(Integer code) {
        this.code.set(code);
    }

    public Double getMark() {
        return mark.get();
    }

    public void setMark(Double mark) {
        if (mark != null) {
            markSP.getValueFactory().setValue(mark);
        }
    }

    public void setStudent(Student stu) {
        this.student = stu;
        setCode(((StudentExamParameters) stu.getUserData()).getExamCode());
        setMark(stu.getMarks().getExamMark(exam, matter));
    }

    public void setTransStudent(Student stu, int translate) {
        this.student = stu;
        setCode(((StudentExamParameters) stu.getUserData()).getExamCode() + translate);
        setMark(stu.getMarks().getExamMark(exam, matter));
    }

    public Student getStudent() {
        return student;
    }

    public EXAM_TYPE getExam() {
        return exam;
    }

    public void setExam(EXAM_TYPE exam) {
        this.exam = exam;
    }

    public String getMatter() {
        return matter;
    }

    public void setMatter(String matter) {
        this.matter = matter;
    }

    public void apply() {
        student.getMarks().setExamMark(exam, matter, mark.get());
    }

}
