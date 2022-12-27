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

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import localexam.bones.MarkInput;
import localexam.bones.Planning;
import localexam.bones.StudentExamParameters;
import localexam.bones.enums.EXAM_STATUS;
import net.mdrassty.util.SpinnerInitializer;

/**
 *
 * @author H. KASSIMI (@mdrassty)
 */
public class InputTransMarksController extends BaseController {

    @FXML
    private Button applyBtn, showBtn;
    @FXML
    private Label notReadyLbl;
    @FXML
    private ComboBox<Planning> matterCB;
    @FXML
    private Spinner<Integer> firstCodeSP;
    @FXML
    private Spinner<Integer> lastCodeSP;
    @FXML
    private ListView<MarkInput> formLV;

    private final SimpleBooleanProperty READY = new SimpleBooleanProperty(false);

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        SpinnerInitializer.initialize(firstCodeSP, new SpinnerValueFactory.IntegerSpinnerValueFactory(Integer.MIN_VALUE, Integer.MAX_VALUE));
        SpinnerInitializer.initialize(lastCodeSP, new SpinnerValueFactory.IntegerSpinnerValueFactory(Integer.MIN_VALUE, Integer.MAX_VALUE));
        firstCodeSP.valueProperty().addListener((obs, old, cur) -> {
            if (cur > lastCodeSP.getValue()) {
                lastCodeSP.getValueFactory().setValue(cur);
            }
        });
        lastCodeSP.valueProperty().addListener((obs, old, cur) -> {
            if (cur < firstCodeSP.getValue()) {
                firstCodeSP.getValueFactory().setValue(cur);
            }
        });
        formLV.getSelectionModel().setSelectionMode(null);

        applyBtn.setOnAction(evt -> {
            formLV.getItems().forEach(mi -> {
                mi.apply();
            });
            EXAM.setStatus(EXAM_STATUS.PENDING_CHANGES);
        });

        showBtn.setOnAction(evt -> {
            createForm();
        });
        notReadyLbl.managedProperty().bind(notReadyLbl.visibleProperty());
        notReadyLbl.visibleProperty().bind(READY.not());
    }

    @Override
    public void refresh() {
        int first = EXAM.getTranslatedFirstCode();
        int last = EXAM.getTranslatedLastCode();
        SpinnerInitializer.initialize(firstCodeSP, new SpinnerValueFactory.IntegerSpinnerValueFactory(first, last, first));
        SpinnerInitializer.initialize(lastCodeSP, new SpinnerValueFactory.IntegerSpinnerValueFactory(first, last, last));
        firstCodeSP.setDisable(false);
        lastCodeSP.setDisable(false);
        matterCB.getItems().setAll(EXAM.getPlannings());
        matterCB.getSelectionModel().select(0);
        matterCB.setDisable(false);
        READY.set(!EXAM.getPlannings().isEmpty());
        createForm();
    }

    public void refresh(Planning planning, int firstCode, int lastCode) {
        if (!matterCB.getItems().contains(planning)) {
            matterCB.getItems().add(planning);
        }
        matterCB.getSelectionModel().select(planning);
        matterCB.setDisable(true);
        SpinnerInitializer.initialize(firstCodeSP, new SpinnerValueFactory.IntegerSpinnerValueFactory(firstCode, lastCode));
        SpinnerInitializer.initialize(lastCodeSP, new SpinnerValueFactory.IntegerSpinnerValueFactory(firstCode, lastCode));
        firstCodeSP.getValueFactory().setValue(firstCode);
        firstCodeSP.setDisable(true);
        lastCodeSP.getValueFactory().setValue(lastCode);
        lastCodeSP.setDisable(true);
        READY.set(true);
        createForm();
    }

    private void createForm() {
        formLV.getItems().clear();
        if (READY.get()) {
            EXAM.getStudents().stream()
                    .filter(stu -> ((StudentExamParameters) stu.getUserData()).getExamCode() >= (firstCodeSP.getValue() - EXAM.getTranslationGap())
                    && ((StudentExamParameters) stu.getUserData()).getExamCode() <= (lastCodeSP.getValue() - EXAM.getTranslationGap()))
                    .sorted((stu1, stu2) -> Integer.compare(((StudentExamParameters) stu1.getUserData()).getExamCode(),
                    ((StudentExamParameters) stu2.getUserData()).getExamCode()))
                    .forEach(stu -> {
                        Platform.runLater(() -> {
                            formLV.getItems().add(new MarkInput(stu, EXAM.getType(), matterCB.getValue().getLabel(), EXAM.getTranslationGap()));
                        });
                    });
        }
    }

}
