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
import java.net.URL;
import java.util.IntSummaryStatistics;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import static localexam.Settings.SAVER;
import localexam.bones.MarkInput;
import localexam.bones.Marks;
import localexam.bones.StudentExamParameters;

/**
 *
 * @author H. KASSIMI (@mdrassty)
 */
public class MarkingController extends BaseController {

    @FXML
    private Label matterLbl;
    @FXML
    private Label firstCodeLbl, successLbl;
    @FXML
    private Label lastCodeLbl;
    @FXML
    private ListView<MarkInput> formLV;
    @FXML
    private Button applyBtn;

    private Marks marks;
    private File output;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        formLV.getSelectionModel().setSelectionMode(null);

        applyBtn.setOnAction(evt -> {
            successLbl.setVisible(false);
            formLV.getItems().forEach(mi -> {
                mi.apply();
            });
            SAVER.saveMarks(marks, output);
            successLbl.setVisible(true);
        });
    }

    @Override
    public void refresh() {
        formLV.getItems().clear();
        firstCodeLbl.setText("");
        lastCodeLbl.setText("");
        matterLbl.setText("");
    }

    public void setMarks(Marks marks) {
        IntSummaryStatistics stats;
        this.marks = marks;
        stats = marks.getData().stream().collect(Collectors.summarizingInt(stu -> ((StudentExamParameters) stu.getUserData()).getExamCode()));
        firstCodeLbl.setText(stats.getMin() + "");
        lastCodeLbl.setText(stats.getMax() + "");
        matterLbl.setText(marks.getMatter());
        marks.getData().stream().forEach(stu -> {
            Platform.runLater(() -> {
                formLV.getItems().add(new MarkInput(stu, marks.getExamType(), marks.getMatter()));
            });
        });
    }

    @Override
    public void openFile(File inOutput) {
        this.output = inOutput;
    }

}
