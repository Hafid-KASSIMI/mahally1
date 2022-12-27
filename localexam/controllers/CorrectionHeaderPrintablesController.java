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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.ImageView;
import static localexam.Settings.PREF_BUNDLE;
import localexam.template.generators.CorrectionHeaderPDFFileGenerator;
import net.mdrassty.util.Misc;
import net.mdrassty.util.SpinnerInitializer;

/**
 *
 * @author H. KASSIMI (@mdrassty)
 */
public class CorrectionHeaderPrintablesController extends BasePrintableController {

    @FXML
    private ComboBox<String> mattersCB;
    @FXML
    private Spinner<Integer> filesCountSP;
    @FXML
    private ImageView tplIV;
    @FXML
    private Label statsLbl;

    public CorrectionHeaderPrintablesController() {
        super();
        generator = new CorrectionHeaderPDFFileGenerator();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        super.initialize(url, rb);
        SpinnerInitializer.initialize(filesCountSP, new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE));
        
        filesCountSP.valueProperty().addListener((obs, old, cur) -> {
            predictFilesCountStats(cur);
            PREF_BUNDLE.update("CORRECTION_HEADER_PRINTABLE_FILES_COUNT_INTEGER", cur + "");
        });
        mattersCB.getSelectionModel().selectedIndexProperty().addListener((obs, old, cur) -> {
            PREF_BUNDLE.update("CORRECTION_HEADER_PRINTABLE_SELECTED_MATTER_INTEGER", cur + "");
        });
        generateBtn.setOnAction(evt -> {
            if (!mattersCB.getSelectionModel().isEmpty()) {
            if (destination == null) {
                chooseDirectory();
            }
            if (destination != null) {
                new Thread(() -> {
                    ((CorrectionHeaderPDFFileGenerator) generator).generate(mattersCB.getValue(), filesCountSP.getValue());
                }).start();
            }
            }
        });
    }

    @Override
    public void getReady() {
        super.getReady();
        predictFilesCountStats(filesCountSP.getValue());
    }

    @Override
    public void refresh() {
        mattersCB.getItems().clear();
        mattersCB.getItems().addAll(FXCollections.observableArrayList(EXAM.getPlannings().stream().map(pl -> pl.getLabel()).collect(Collectors.toList())));
        try {
            mattersCB.getSelectionModel().select(Integer.parseInt(PREF_BUNDLE.get("CORRECTION_HEADER_PRINTABLE_SELECTED_MATTER_INTEGER")));
        } catch ( NullPointerException | NumberFormatException ex ) {}
        try {
            filesCountSP.getValueFactory().setValue(Integer.parseInt(PREF_BUNDLE.get("CORRECTION_HEADER_PRINTABLE_FILES_COUNT_INTEGER")));
        } catch ( NullPointerException | NumberFormatException ex ) {}
    }

    private void predictFilesCountStats(Integer n) {
        if (n == null) {
            return;
        }
        int max = EXAM.getStudents().size() / n + (EXAM.getStudents().size() % n == 0 ? 0 : 1);
        Map<String, Integer> stats = Misc.getSemiEvenDivStats(EXAM.getStudents().size(), max);
        statsLbl.setText("");
        if (stats.get("nMin") > 0) {
            statsLbl.setText(Misc.ARCounter("ظرف", "ظرفا", "ظرفان", "أظرفة", false, stats.get("nMin")) + " ("
                    + Misc.ARCounter("ممتحن", "ممتحنا", "ممتحنان", "ممتحنين", false, stats.get("min")) + ") و");
        }
        statsLbl.setText(statsLbl.getText() + Misc.ARCounter("ظرف", "ظرفا", "ظرفان", "أظرفة", false, stats.get("nMax")) + " ("
                + Misc.ARCounter("ممتحن", "ممتحنا", "ممتحنان", "ممتحنين", false, stats.get("max")) + ").");
    }

}
