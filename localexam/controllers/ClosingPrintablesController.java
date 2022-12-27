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
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import static localexam.Settings.PREF_BUNDLE;
import localexam.template.generators.ClosingPVPDFFileGenerator;

/**
 *
 * @author H. KASSIMI (@mdrassty)
 */
public class ClosingPrintablesController extends BasePrintableController {

    @FXML
    private ToggleGroup optionsTG;
    @FXML
    private RadioButton everythingRB, byMatterRB, byDayRB;
    @FXML
    private ComboBox<String> mattersCB;
    @FXML
    private ComboBox<LocalDate> daysCB;
    @FXML
    private HBox warningHB;
    @FXML
    private TextField directorName, directorCode, observerName, observerCode, shifterName, shifterCode, secretaryName, secretaryCode;
    @FXML
    private ImageView tplIV;

    public ClosingPrintablesController() {
        super();
        generator = new ClosingPVPDFFileGenerator();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        super.initialize(url, rb);

        mattersCB.getSelectionModel().selectedIndexProperty().addListener((obs, old, cur) -> {
            byMatterRB.setSelected(true);
            PREF_BUNDLE.update("CLOSING_TRIAL_PRINTABLE_SELECTED_MATTER_INTEGER", cur + "");
        });
        daysCB.getSelectionModel().selectedIndexProperty().addListener((obs, old, cur) -> {
            byDayRB.setSelected(true);
            PREF_BUNDLE.update("CLOSING_TRIAL_PRINTABLE_SELECTED_DAY_INTEGER", cur + "");
        });
        
        everythingRB.selectedProperty().addListener((obs, old, cur) -> PREF_BUNDLE.update("CLOSING_TRIAL_PRINTABLE_ALL_SELECTED_BOOLEAN", cur ? "1" : "0"));
        byDayRB.selectedProperty().addListener((obs, old, cur) -> PREF_BUNDLE.update("CLOSING_TRIAL_PRINTABLE_SINGLE_DAY_SELECTED_BOOLEAN", cur ? "1" : "0"));
        byMatterRB.selectedProperty().addListener((obs, old, cur) -> PREF_BUNDLE.update("CLOSING_TRIAL_PRINTABLE_SINGLE_MATTER_SELECTED_BOOLEAN", cur ? "1" : "0"));
        
        directorName.setText(PREF_BUNDLE.get("CLOSING_PROTAGONIST_0_FULL_NAME_STRING"));
        directorCode.setText(PREF_BUNDLE.get("CLOSING_PROTAGONIST_0_CODE_STRING"));
        observerName.setText(PREF_BUNDLE.get("CLOSING_PROTAGONIST_1_FULL_NAME_STRING"));
        observerCode.setText(PREF_BUNDLE.get("CLOSING_PROTAGONIST_1_CODE_STRING"));
        shifterName.setText(PREF_BUNDLE.get("CLOSING_PROTAGONIST_2_FULL_NAME_STRING"));
        shifterCode.setText(PREF_BUNDLE.get("CLOSING_PROTAGONIST_2_CODE_STRING"));
        secretaryName.setText(PREF_BUNDLE.get("CLOSING_PROTAGONIST_3_FULL_NAME_STRING"));
        secretaryCode.setText(PREF_BUNDLE.get("CLOSING_PROTAGONIST_3_CODE_STRING"));
        
        generateBtn.setOnAction(evt -> {
            if (destination == null) {
                chooseDirectory();
            }
            if (destination != null) {
                new Thread(() -> {
                    if (byDayRB.isSelected()) {
                        ((ClosingPVPDFFileGenerator) generator).generate(daysCB.getValue());
                    } else if (byMatterRB.isSelected()) {
                        ((ClosingPVPDFFileGenerator) generator).generate(mattersCB.getValue());
                    } else {
                        ((ClosingPVPDFFileGenerator) generator).generate();
                    }
                }).start();
                PREF_BUNDLE.update("CLOSING_PROTAGONIST_0_INCLUDED_BOOLEAN", !directorName.getText().isEmpty() || !directorCode.getText().isEmpty() ? "1" : "0");
                PREF_BUNDLE.update("CLOSING_PROTAGONIST_0_FULL_NAME_STRING", directorName.getText());
                PREF_BUNDLE.update("CLOSING_PROTAGONIST_0_CODE_STRING", directorCode.getText());
                PREF_BUNDLE.update("CLOSING_PROTAGONIST_1_INCLUDED_BOOLEAN", !observerName.getText().isEmpty() || !observerCode.getText().isEmpty() ? "1" : "0");
                PREF_BUNDLE.update("CLOSING_PROTAGONIST_1_FULL_NAME_STRING", observerName.getText());
                PREF_BUNDLE.update("CLOSING_PROTAGONIST_1_CODE_STRING", observerCode.getText());
                PREF_BUNDLE.update("CLOSING_PROTAGONIST_2_INCLUDED_BOOLEAN", !shifterName.getText().isEmpty() || !shifterCode.getText().isEmpty() ? "1" : "0");
                PREF_BUNDLE.update("CLOSING_PROTAGONIST_2_FULL_NAME_STRING", shifterName.getText());
                PREF_BUNDLE.update("CLOSING_PROTAGONIST_2_CODE_STRING", shifterCode.getText());
                PREF_BUNDLE.update("CLOSING_PROTAGONIST_3_INCLUDED_BOOLEAN", !secretaryName.getText().isEmpty() || !secretaryCode.getText().isEmpty() ? "1" : "0");
                PREF_BUNDLE.update("CLOSING_PROTAGONIST_3_FULL_NAME_STRING", secretaryName.getText());
                PREF_BUNDLE.update("CLOSING_PROTAGONIST_3_CODE_STRING", secretaryCode.getText());
            }
        });
        warningHB.managedProperty().bind(warningHB.visibleProperty());
    }

    @Override
    public void getReady() {
        super.getReady();
        warningHB.setVisible(EXAM.getPlannings().isEmpty());
        EXAM.getPlannings().addListener((ListChangeListener) (c -> {
            warningHB.setVisible(c.getList().isEmpty());
        }));
    }

    @Override
    public void refresh() {
        mattersCB.getItems().clear();
        mattersCB.getItems().addAll(FXCollections.observableArrayList(EXAM.getPlannings().stream().map(p -> p.getLabel()).distinct().collect(Collectors.toList())));
        daysCB.getItems().clear();
        daysCB.getItems().addAll(FXCollections.observableArrayList(EXAM.getPlannings().stream().map(p -> p.getDate()).distinct().collect(Collectors.toList())));
        try {
            mattersCB.getSelectionModel().select(Integer.parseInt(PREF_BUNDLE.get("CLOSING_TRIAL_PRINTABLE_SELECTED_MATTER_INTEGER")));
        } catch ( NullPointerException | NumberFormatException ex ) {}
        try {
            daysCB.getSelectionModel().select(Integer.parseInt(PREF_BUNDLE.get("CLOSING_TRIAL_PRINTABLE_SELECTED_DAY_INTEGER")));
        } catch ( NullPointerException | NumberFormatException ex ) {}
        everythingRB.setSelected("1".equals(PREF_BUNDLE.get("CLOSING_TRIAL_PRINTABLE_ALL_SELECTED_BOOLEAN")));
        byMatterRB.setSelected("1".equals(PREF_BUNDLE.get("CLOSING_TRIAL_PRINTABLE_SINGLE_MATTER_SELECTED_BOOLEAN")));
        byDayRB.setSelected("1".equals(PREF_BUNDLE.get("CLOSING_TRIAL_PRINTABLE_SINGLE_DAY_SELECTED_BOOLEAN")));
        if ( optionsTG.getSelectedToggle() == null )
            optionsTG.selectToggle(optionsTG.getToggles().get(0));
    }

}
