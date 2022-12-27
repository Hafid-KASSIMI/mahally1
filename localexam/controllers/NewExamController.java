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
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import localexam.Exam;
import static localexam.Settings.PREF_BUNDLE;
import localexam.bones.enums.EXAM_STATUS;
import net.mdrassty.massar.DBWorkbook;
import net.mdrassty.object.Level;
import net.mdrassty.object.SchoolInfos;

/**
 *
 * @author H. KASSIMI (@mdrassty)
 */
public class NewExamController extends BaseController {

    @FXML
    private Button selectDbBtn, createBtn;
    @FXML
    private ComboBox<Level> levelCB;
    @FXML
    private TextField examNameTF;
    @FXML
    private Label levStusLbl, malesLbl, femalesLbl;
    @FXML
    private ProgressIndicator loadingPI;

    private final FileChooser fc = new FileChooser();
    private final SimpleBooleanProperty LOADING_STATUS = new SimpleBooleanProperty();
    private final DBWorkbook wb = new DBWorkbook();
    private final BooleanProperty examCreated = new SimpleBooleanProperty(false);

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        createBtn.disableProperty().bind(LOADING_STATUS.not());

        levelCB.getSelectionModel().selectedItemProperty().addListener((obs, old, cur) -> {
            if (cur != null) {
                levStusLbl.setText(cur.getStudentsCount() + "");
                malesLbl.setText(cur.getMalesCount() + "");
                femalesLbl.setText(cur.getFemalesCount() + "");
            }
        });

        LOADING_STATUS.bind(wb.getStatus());

        LOADING_STATUS.addListener((obs, old, cur) -> {
            if (cur) {
                Platform.runLater(() -> {
                    levelCB.getItems().setAll(wb.getSchool().getLevels());
                    levelCB.getSelectionModel().selectFirst();
                    loadingPI.setVisible(false);
                });
            }
        });

        selectDbBtn.setOnAction(evt -> {
            File ini_db_dir = new File(PREF_BUNDLE.get("INPUT_DIR_STRING"));
            fc.setTitle("تحديد قاعدة بيانات المؤسسة");
            if (ini_db_dir.exists() && ini_db_dir.isDirectory()) {
                fc.setInitialDirectory(ini_db_dir);
            }
            fc.getExtensionFilters().clear();
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("مرتبات إكسيل", "*.xls"));
            File f = fc.showOpenDialog(selectDbBtn.getScene().getWindow());
            if (f == null) {
                return;
            }
            if (f.getParentFile() != ini_db_dir) {
                PREF_BUNDLE.update("INPUT_DIR_STRING", f.getParent());
            }
            selectDbBtn.setDisable(false);
            if (wb.setWorkbook(f)) {
                loadingPI.setVisible(true);
                wb.loadDB();
            }
        });

        createBtn.setOnAction(evt -> {
            createNewExam();
        });
    }

    private void createNewExam() {
        EXAM = new Exam();
        EXAM.setLabel(examNameTF.getText());
        try {
            EXAM.setInfos((SchoolInfos) levelCB.getValue().getSchool().getInfos().clone());
            EXAM.setLevel((Level) levelCB.getValue().clone());
            EXAM.setStatus(EXAM_STATUS.CREATED);
            examCreated.set(true);
        } catch (CloneNotSupportedException ex) {
        }
        levelCB.getItems().clear();
        examNameTF.setText("");
    }

    @Override
    public void refresh() {

    }

    public boolean isExamCreated() {
        return examCreated.get();
    }

    public void setExamCreated(boolean value) {
        examCreated.set(value);
    }

    public BooleanProperty examCreatedProperty() {
        return examCreated;
    }

}
