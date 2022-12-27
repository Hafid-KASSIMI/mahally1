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
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import static localexam.Settings.APP_EXTENSION2;
import static localexam.Settings.PREF_BUNDLE;
import static localexam.Settings.SAVER;
import localexam.template.generators.GenericDocumentGenerator;
import net.mdrassty.util.Misc;
import net.mdrassty.util.SpinnerInitializer;

/**
 *
 * @author H. KASSIMI (@mdrassty)
 */
public class ExportMarksFilesController extends BaseGeneratorController {

    @FXML
    private ComboBox<String> matterCB;
    @FXML
    private Spinner<Integer> filesCountSP;
    @FXML
    private TextField prefixTF;
    @FXML
    private Label statsLbl;
    @FXML
    private CheckBox overwriteCB;

    private int first, counter;
    private final GenericDocumentGenerator GENERATOR = new GenericDocumentGenerator("SAVE_DIR_STRING");

    public ExportMarksFilesController() {
        prefDirectory = "SAVE_DIR_STRING";
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        super.initialize(url, rb);
        SpinnerInitializer.initialize(filesCountSP, new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE));
        filesCountSP.valueProperty().addListener((obs, old, cur) -> {
            predictFilesCountStats(cur);
            PREF_BUNDLE.update("MARKS_FILES_EXPORT_FILES_COUNT_INTEGER", cur + "");
        });
        matterCB.getSelectionModel().selectedIndexProperty().addListener((obs, old, cur) -> {
            PREF_BUNDLE.update("MARKS_FILES_EXPORT_SELECTED_MATTER_INTEGER", cur + "");
        });
        overwriteCB.selectedProperty().addListener((obs, old, cur) -> PREF_BUNDLE.update("MARKS_FILES_EXPORT_OVERWRITE_SELECTED_BOOLEAN", cur ? "1" : "0"));

        generateBtn.setOnAction(evt -> {
            if (!matterCB.getSelectionModel().isEmpty()) {
                if (destination == null) {
                    chooseDirectory();
                }
                if (destination != null) {
                    String path = destination.getAbsolutePath();
                    int max = EXAM.getStudents().size() / filesCountSP.getValue()
                            + (EXAM.getStudents().size() % filesCountSP.getValue() == 0 ? 0 : 1);
                    first = EXAM.getFirstCode();
                    Map<String, Integer> stats = Misc.getSemiEvenDivStats(EXAM.getStudents().size(), max);
                    counter = 1;
                    processingPI.setVisible(true);
                    GENERATOR.setSaved(false);
                    Misc.getSemiEvenDivArray(EXAM.getStudents().size(), max).forEach(n -> {
                        String fileName = path + "/" + prefixTF.getText() + "_("
                                + (first + EXAM.getTranslationGap()) + "_" + (first + n - 1 + EXAM.getTranslationGap()) + ")_" + (counter++);
                        if (!overwriteCB.isSelected()) {
                            fileName = Misc.validateFileName(fileName, APP_EXTENSION2);
                        }
                        SAVER.saveMarks(EXAM, matterCB.getValue(), first, first + n - 1, fileName + APP_EXTENSION2);
                        first = first + n;
                    });
                    processingPI.setVisible(false);
                    GENERATOR.setSaved(true);
                }
            }
            PREF_BUNDLE.update("MARKS_FILES_EXPORT_PREFIX_STRING", prefixTF.getText());
        });
    }

    @Override
    public void getReady() {
        super.getReady();
        predictFilesCountStats(filesCountSP.getValue());
    }

    @Override
    public void refresh() {
        matterCB.getItems().clear();
        matterCB.getItems().addAll(FXCollections.observableArrayList(EXAM.getPlannings().stream().map(pl -> pl.getLabel()).collect(Collectors.toList())));
        prefixTF.setText(PREF_BUNDLE.get("MARKS_FILES_EXPORT_PREFIX_STRING"));
        try {
            matterCB.getSelectionModel().select(Integer.parseInt(PREF_BUNDLE.get("MARKS_FILES_EXPORT_SELECTED_MATTER_INTEGER")));
        } catch (NullPointerException | NumberFormatException ex) {
        }
        try {
            filesCountSP.getValueFactory().setValue(Integer.parseInt(PREF_BUNDLE.get("MARKS_FILES_EXPORT_FILES_COUNT_INTEGER")));
        } catch (NullPointerException | NumberFormatException ex) {
        }
        overwriteCB.setSelected("1".equals(PREF_BUNDLE.get("MARKS_FILES_EXPORT_OVERWRITE_SELECTED_BOOLEAN")));
    }

    private void predictFilesCountStats(Integer n) {
        if (n == null) {
            return;
        }
        int max = EXAM.getStudents().size() / n + (EXAM.getStudents().size() % n == 0 ? 0 : 1);
        Map<String, Integer> stats = Misc.getSemiEvenDivStats(EXAM.getStudents().size(), max);
        statsLbl.setText("");
        if (stats.get("nMin") > 0) {
            statsLbl.setText(Misc.ARCounter("مستند", "مستندا", "مستندان", "مستندات", false, stats.get("nMin")) + " ("
                    + Misc.ARCounter("ممتحن", "ممتحنا", "ممتحنان", "ممتحنين", false, stats.get("min")) + ") و");
        }
        statsLbl.setText(statsLbl.getText() + Misc.ARCounter("مستند", "مستندا", "مستندان", "مستندات", false, stats.get("nMax")) + " ("
                + Misc.ARCounter("ممتحن", "ممتحنا", "ممتحنان", "ممتحنين", false, stats.get("max")) + ").");
    }

}
