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
package localexam.controllers;

import java.io.File;
import javafx.fxml.Initializable;
import localexam.Exam;

/**
 *
 * @author H. KASSIMI (@mdrassty)
 */
public abstract class BaseController implements Initializable {

    protected Exam EXAM;

    public abstract void refresh();
    public void openFile(File file) {
        
    }

    public void getReady() {

    }

    public Exam getExam() {
        return EXAM;
    }

    public void setExam(Exam exam) {
        this.EXAM = exam;
    }
    
    public void closeRequest() {
        
    }

}
