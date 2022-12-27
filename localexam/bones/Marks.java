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

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import net.mdrassty.object.Student;
import net.mdrassty.object.enums.EXAM_TYPE;

/**
 *
 * @author H. KASSIMI (@mdrassty)
 */
public class Marks implements Serializable {

    private String matter, levelName, examLabel;
    private EXAM_TYPE examType;
    private final LocalDateTime creationDateTime;
    private String translation, uid;
    private ArrayList<Student> data;

    public Marks(String matter, ArrayList<Student> data) {
        this.matter = matter;
        this.data = data;
        creationDateTime = LocalDateTime.now();
    }

    public Marks(String examLabel, String levelName, String matter, ArrayList<Student> data) {
        this.matter = matter;
        this.levelName = levelName;
        this.examLabel = examLabel;
        this.data = data;
        creationDateTime = LocalDateTime.now();
    }

    public Marks() {
        this.creationDateTime = LocalDateTime.now();
        data = new ArrayList();
    }

    public String getMatter() {
        return matter;
    }

    public void setMatter(String matter) {
        this.matter = matter;
    }

    public ArrayList<Student> getData() {
        return data;
    }

    public void setData(ArrayList<Student> data) {
        this.data = data;
    }

    public void addStudent(Student student) {
        data.add(student);
    }

    public LocalDateTime getCreationDateTime() {
        return creationDateTime;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public String getExamLabel() {
        return examLabel;
    }

    public void setExamLabel(String examLabel) {
        this.examLabel = examLabel;
    }

    public EXAM_TYPE getExamType() {
        return examType;
    }

    public void setExamType(EXAM_TYPE examType) {
        this.examType = examType;
    }

    public void setExamType(String examType) {
        try {
            this.examType = EXAM_TYPE.valueOf(examType);
        } catch (Exception ex) {
            this.examType = EXAM_TYPE.LOCAL_EXAM;
        }
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

}
