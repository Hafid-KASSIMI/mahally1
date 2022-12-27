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
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import static localexam.Settings.APP_EXTENSION2;
import static localexam.Settings.PREF_BUNDLE;
import static localexam.Settings.SAVER;
import localexam.template.generators.GenericDocumentGenerator;
import net.mdrassty.util.SpinnerInitializer;

/**
 *
 * @author H. KASSIMI (@mdrassty)
 */
public class ExportMarksFileController extends BaseGeneratorController {

    @FXML
    private ComboBox<String> matterCB;
    @FXML
    private RadioButton allRB, customRB;
    @FXML
    private Spinner<Integer> firstCodeSP, lastCodeSP;
    @FXML
    private HBox warningHB;
    @FXML
    private ToggleGroup candsTG;

    private final FileChooser fc = new FileChooser();
    private final GenericDocumentGenerator GENERATOR = new GenericDocumentGenerator("SAVE_DIR_STRING");

    public ExportMarksFileController() {
        prefDirectory = "SAVE_DIR_STRING";
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        super.initialize(url, rb);
        SpinnerInitializer.initialize(firstCodeSP, new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE));
        SpinnerInitializer.initialize(lastCodeSP, new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE));
        firstCodeSP.valueProperty().addListener((obs, old, cur) -> {
            if (cur > lastCodeSP.getValue()) {
                lastCodeSP.getValueFactory().setValue(cur);
            }
            PREF_BUNDLE.update("MARKS_FILE_EXPORT_FIRST_CODE_INTEGER", cur + "");
        });
        lastCodeSP.valueProperty().addListener((obs, old, cur) -> {
            if (cur < firstCodeSP.getValue()) {
                firstCodeSP.getValueFactory().setValue(cur);
            }
            PREF_BUNDLE.update("MARKS_FILE_EXPORT_LAST_CODE_INTEGER", cur + "");
        });
        matterCB.getSelectionModel().selectedIndexProperty().addListener((obs, old, cur) -> {
            PREF_BUNDLE.update("MARKS_FILE_EXPORT_SELECTED_MATTER_INTEGER", cur + "");
        });
        allRB.selectedProperty().addListener((obs, old, cur) -> PREF_BUNDLE.update("MARKS_FILE_EXPORT_ALL_SELECTED_BOOLEAN", cur ? "1" : "0"));
        customRB.selectedProperty().addListener((obs, old, cur) -> PREF_BUNDLE.update("MARKS_FILE_EXPORT_CUSTOM_SELECTED_BOOLEAN", cur ? "1" : "0"));
        
        generateBtn.setOnAction(evt -> {
            if (!matterCB.getSelectionModel().isEmpty()) {
                int first, last;
                if (allRB.isSelected()) {
                    first = EXAM.getFirstCode();
                    last = EXAM.getLastCode();
                } else {
                    first = firstCodeSP.getValue() - EXAM.getTranslationGap();
                    last = lastCodeSP.getValue() - EXAM.getTranslationGap();
                }

                fc.getExtensionFilters().clear();
                fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("مستندات محلي", "*" + APP_EXTENSION2));
                fc.setTitle("اختيار اسم ومكان التصدير");
                try {
                    if (!"".equals(PREF_BUNDLE.get("SAVE_DIR_STRING")) && Files.exists(Paths.get(PREF_BUNDLE.get("SAVE_DIR_STRING"))) 
                            && Files.isDirectory(Paths.get(PREF_BUNDLE.get("SAVE_DIR_STRING")))) {
                        fc.setInitialDirectory(new File(PREF_BUNDLE.get("SAVE_DIR_STRING")));
                    }
                } catch (InvalidPathException | SecurityException ipe) {
                }
                File f = fc.showSaveDialog(generateBtn.getScene().getWindow());
                if (f != null) {
                    String path = f.getAbsolutePath();
                    if (!f.getName().endsWith(APP_EXTENSION2)) {
                        path += APP_EXTENSION2;
                    }
                    processingPI.setVisible(true);
                    GENERATOR.setSaved(false);
                    SAVER.saveMarks(EXAM, matterCB.getValue(), first, last, path);
                    processingPI.setVisible(false);
                    GENERATOR.setSaved(true);
                }
            }
        });
    }

    @Override
    public void getReady() {
        super.getReady();
    }

    @Override
    public void refresh() {
        int first = EXAM.getFirstCode() + EXAM.getTranslationGap(), last = EXAM.getLastCode() + EXAM.getTranslationGap();
        if (EXAM.getFirstCode() < 0) {
            warningHB.setVisible(true);
            return;
        }
        warningHB.setVisible(false);
        SpinnerInitializer.initialize(firstCodeSP, new SpinnerValueFactory.IntegerSpinnerValueFactory(first, last, first));
        SpinnerInitializer.initialize(lastCodeSP, new SpinnerValueFactory.IntegerSpinnerValueFactory(first, last, last));
        matterCB.getItems().clear();
        matterCB.getItems().addAll(FXCollections.observableArrayList(EXAM.getPlannings().stream().map(pl -> pl.getLabel()).collect(Collectors.toList())));
        try {
            matterCB.getSelectionModel().select(Integer.parseInt(PREF_BUNDLE.get("MARKS_FILE_EXPORT_SELECTED_MATTER_INTEGER")));
        } catch ( NullPointerException | NumberFormatException ex ) {}
        try {
            firstCodeSP.getValueFactory().setValue(Integer.parseInt(PREF_BUNDLE.get("MARKS_FILE_EXPORT_FIRST_CODE_INTEGER")));
        } catch ( NullPointerException | NumberFormatException ex ) {}
        try {
            lastCodeSP.getValueFactory().setValue(Integer.parseInt(PREF_BUNDLE.get("MARKS_FILE_EXPORT_LAST_CODE_INTEGER")));
        } catch ( NullPointerException | NumberFormatException ex ) {}
        allRB.setSelected("1".equals(PREF_BUNDLE.get("MARKS_FILE_EXPORT_ALL_SELECTED_BOOLEAN")));
        customRB.setSelected("1".equals(PREF_BUNDLE.get("MARKS_FILE_EXPORT_CUSTOM_SELECTED_BOOLEAN")));
        if ( candsTG.getSelectedToggle() == null )
            candsTG.selectToggle(candsTG.getToggles().get(0));
    }

}
