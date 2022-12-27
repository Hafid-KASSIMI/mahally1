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
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import static localexam.Settings.PREF_BUNDLE;
import localexam.template.generators.MarksPDFFileGenerator;
import net.mdrassty.util.SpinnerInitializer;

/**
 *
 * @author H. KASSIMI (@mdrassty)
 */
public class MarksPrintablesController extends BasePrintableController {

    @FXML
    private CheckBox marksIncludeCB;
    @FXML
    private RadioButton marksAllMatsRB, marksOneMatRB, marksAllRB, marksCustomRB;
    @FXML
    private ToggleGroup marksMatsTG, candsTG;
    @FXML
    private ComboBox<String> marksMatterCB;
    @FXML
    private Spinner<Integer> marksFirstCodeSP, marksLastCodeSP;
    @FXML
    private HBox warningHB;

    public MarksPrintablesController() {
        super();
        generator = new MarksPDFFileGenerator();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        super.initialize(url, rb);
        ((MarksPDFFileGenerator) generator).includeMarksProperty().bind(marksIncludeCB.selectedProperty());

        marksMatterCB.getSelectionModel().selectedIndexProperty().addListener((obs, old, cur) -> {
            marksOneMatRB.setSelected(true);
            PREF_BUNDLE.update("MARKS_PRINTABLE_SELECTED_MATTER_INTEGER", cur + "");
        });
        marksAllMatsRB.selectedProperty().addListener((obs, old, cur) -> PREF_BUNDLE.update("MARKS_PRINTABLE_ALL_MATTERS_SELECTED_BOOLEAN", cur ? "1" : "0"));
        marksOneMatRB.selectedProperty().addListener((obs, old, cur) -> PREF_BUNDLE.update("MARKS_PRINTABLE_SINGLE_MATTER_SELECTED_BOOLEAN", cur ? "1" : "0"));
        marksAllRB.selectedProperty().addListener((obs, old, cur) -> PREF_BUNDLE.update("MARKS_PRINTABLE_ALL_SELECTED_BOOLEAN", cur ? "1" : "0"));
        marksCustomRB.selectedProperty().addListener((obs, old, cur) -> PREF_BUNDLE.update("MARKS_PRINTABLE_SINGLE_CODE_SELECTED_BOOLEAN", cur ? "1" : "0"));

        generateBtn.setOnAction(evt -> {
            if (destination == null) {
                chooseDirectory();
            }
            if (destination != null) {
                new Thread(() -> {
                    if (marksOneMatRB.isSelected() && marksAllRB.isSelected()) {
                        ((MarksPDFFileGenerator) generator).generate(marksMatterCB.getValue());
                    } else if (marksAllMatsRB.isSelected() && marksCustomRB.isSelected()) {
                        ((MarksPDFFileGenerator) generator).generate(marksFirstCodeSP.getValue() - EXAM.getTranslationGap(), marksLastCodeSP.getValue() - EXAM.getTranslationGap());
                    } else if (marksOneMatRB.isSelected() && marksCustomRB.isSelected()) {
                        ((MarksPDFFileGenerator) generator).generate(marksMatterCB.getValue(), marksFirstCodeSP.getValue() - EXAM.getTranslationGap(), marksLastCodeSP.getValue() - EXAM.getTranslationGap());
                    } else {
                        ((MarksPDFFileGenerator) generator).generate();
                    }
                }).start();
            }
        });
        warningHB.managedProperty().bind(warningHB.visibleProperty());
    }

    @Override
    public void getReady() {
        super.getReady();
        EXAM.getPlannings().addListener((ListChangeListener) (c -> {
            warningHB.setVisible(c.getList().isEmpty());
        }));
    }

    @Override
    public void refresh() {
        int first = EXAM.getFirstCode() + EXAM.getTranslationGap(), last = EXAM.getLastCode() + EXAM.getTranslationGap();
        marksMatterCB.getItems().clear();
        marksMatterCB.getItems().addAll(FXCollections.observableArrayList(EXAM.getPlannings().stream().map(p -> p.getLabel()).distinct().collect(Collectors.toList())));
        SpinnerInitializer.initialize(marksFirstCodeSP, new SpinnerValueFactory.IntegerSpinnerValueFactory(first, last, first));
        SpinnerInitializer.initialize(marksLastCodeSP, new SpinnerValueFactory.IntegerSpinnerValueFactory(first, last, last));
        marksFirstCodeSP.getValueFactory().valueProperty().addListener((obs, old, cur) -> {
            marksCustomRB.setSelected(true);
            PREF_BUNDLE.update("MARKS_PRINTABLE_FIRST_CODE_INTEGER", cur + "");
        });
        marksLastCodeSP.getValueFactory().valueProperty().addListener((obs, old, cur) -> {
            marksCustomRB.setSelected(true);
            PREF_BUNDLE.update("MARKS_PRINTABLE_LAST_CODE_INTEGER", cur + "");
        });
        try {
            marksMatterCB.getSelectionModel().select(Integer.parseInt(PREF_BUNDLE.get("MARKS_PRINTABLE_SELECTED_MATTER_INTEGER")));
        } catch ( NullPointerException | NumberFormatException ex ) {}
        try {
            marksFirstCodeSP.getValueFactory().setValue(Integer.parseInt(PREF_BUNDLE.get("MARKS_PRINTABLE_FIRST_CODE_INTEGER")));
        } catch ( NullPointerException | NumberFormatException ex ) {}
        try {
            marksLastCodeSP.getValueFactory().setValue(Integer.parseInt(PREF_BUNDLE.get("MARKS_PRINTABLE_LAST_CODE_INTEGER")));
        } catch ( NullPointerException | NumberFormatException ex ) {}
        
        marksAllMatsRB.setSelected("1".equals(PREF_BUNDLE.get("MARKS_PRINTABLE_ALL_MATTERS_SELECTED_BOOLEAN")));
        marksOneMatRB.setSelected("1".equals(PREF_BUNDLE.get("MARKS_PRINTABLE_SINGLE_MATTER_SELECTED_BOOLEAN")));
        marksAllRB.setSelected("1".equals(PREF_BUNDLE.get("MARKS_PRINTABLE_ALL_SELECTED_BOOLEAN")));
        marksCustomRB.setSelected("1".equals(PREF_BUNDLE.get("MARKS_PRINTABLE_SINGLE_CODE_SELECTED_BOOLEAN")));
        if ( marksMatsTG.getSelectedToggle() == null )
            marksMatsTG.selectToggle(marksMatsTG.getToggles().get(0));
        if ( candsTG.getSelectedToggle() == null )
            candsTG.selectToggle(candsTG.getToggles().get(0));
    }

}
