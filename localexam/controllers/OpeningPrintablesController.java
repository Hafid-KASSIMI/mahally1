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
import localexam.template.generators.OpeningPVPDFFileGenerator;

/**
 *
 * @author H. KASSIMI (@mdrassty)
 */
public class OpeningPrintablesController extends BasePrintableController {

    @FXML
    private RadioButton pvsAllRB, pvsByDayRB, pvsByMatRB;
    @FXML
    private ToggleGroup pvTG;
    @FXML
    private ComboBox<String> pvsMatsCB;
    @FXML
    private ComboBox<LocalDate> pvsDaysCB;
    @FXML
    private HBox warningHB;
    @FXML
    private TextField directorName, directorCode, observerName, observerCode, shifterName, shifterCode;
    @FXML
    private ImageView tplIV;

    public OpeningPrintablesController() {
        super();
        generator = new OpeningPVPDFFileGenerator();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        super.initialize(url, rb);

        pvsMatsCB.getSelectionModel().selectedIndexProperty().addListener((obs, old, cur) -> {
            pvsByMatRB.setSelected(true);
            PREF_BUNDLE.update("OPENING_TRIAL_PRINTABLE_SELECTED_MATTER_INTEGER", cur + "");
        });
        pvsDaysCB.getSelectionModel().selectedIndexProperty().addListener((obs, old, cur) -> {
            pvsByDayRB.setSelected(true);
            PREF_BUNDLE.update("OPENING_TRIAL_PRINTABLE_SELECTED_DAY_INTEGER", cur + "");
        });
        
        pvsAllRB.selectedProperty().addListener((obs, old, cur) -> PREF_BUNDLE.update("OPENING_TRIAL_PRINTABLE_ALL_SELECTED_BOOLEAN", cur ? "1" : "0"));
        pvsByDayRB.selectedProperty().addListener((obs, old, cur) -> PREF_BUNDLE.update("OPENING_TRIAL_PRINTABLE_SINGLE_DAY_SELECTED_BOOLEAN", cur ? "1" : "0"));
        pvsByMatRB.selectedProperty().addListener((obs, old, cur) -> PREF_BUNDLE.update("OPENING_TRIAL_PRINTABLE_SINGLE_MATTER_SELECTED_BOOLEAN", cur ? "1" : "0"));
        
        directorName.setText(PREF_BUNDLE.get("OPENING_PROTAGONIST_0_FULL_NAME_STRING"));
        directorCode.setText(PREF_BUNDLE.get("OPENING_PROTAGONIST_0_CODE_STRING"));
        observerName.setText(PREF_BUNDLE.get("OPENING_PROTAGONIST_1_FULL_NAME_STRING"));
        observerCode.setText(PREF_BUNDLE.get("OPENING_PROTAGONIST_1_CODE_STRING"));
        shifterName.setText(PREF_BUNDLE.get("OPENING_PROTAGONIST_2_FULL_NAME_STRING"));
        shifterCode.setText(PREF_BUNDLE.get("OPENING_PROTAGONIST_2_CODE_STRING"));
        
        generateBtn.setOnAction(evt -> {
            if (destination == null) {
                chooseDirectory();
            }
            if (destination != null) {
                new Thread(() -> {
                    if (pvsByDayRB.isSelected()) {
                        ((OpeningPVPDFFileGenerator) generator).generate(pvsDaysCB.getValue());
                    } else if (pvsByMatRB.isSelected()) {
                        ((OpeningPVPDFFileGenerator) generator).generate(pvsMatsCB.getValue());
                    } else {
                        ((OpeningPVPDFFileGenerator) generator).generate();
                    }
                }).start();
                PREF_BUNDLE.update("OPENING_PROTAGONIST_0_INCLUDED_BOOLEAN", !directorName.getText().isEmpty() || !directorCode.getText().isEmpty() ? "1" : "0");
                PREF_BUNDLE.update("OPENING_PROTAGONIST_0_FULL_NAME_STRING", directorName.getText());
                PREF_BUNDLE.update("OPENING_PROTAGONIST_0_CODE_STRING", directorCode.getText());
                PREF_BUNDLE.update("OPENING_PROTAGONIST_1_INCLUDED_BOOLEAN", !observerName.getText().isEmpty() || !observerCode.getText().isEmpty() ? "1" : "0");
                PREF_BUNDLE.update("OPENING_PROTAGONIST_1_FULL_NAME_STRING", observerName.getText());
                PREF_BUNDLE.update("OPENING_PROTAGONIST_1_CODE_STRING", observerCode.getText());
                PREF_BUNDLE.update("OPENING_PROTAGONIST_2_INCLUDED_BOOLEAN", !shifterName.getText().isEmpty() || !shifterCode.getText().isEmpty() ? "1" : "0");
                PREF_BUNDLE.update("OPENING_PROTAGONIST_2_FULL_NAME_STRING", shifterName.getText());
                PREF_BUNDLE.update("OPENING_PROTAGONIST_2_CODE_STRING", shifterCode.getText());
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
        pvsMatsCB.getItems().clear();
        pvsMatsCB.getItems().addAll(FXCollections.observableArrayList(EXAM.getPlannings().stream().map(p -> p.getLabel()).distinct().collect(Collectors.toList())));
        pvsDaysCB.getItems().clear();
        pvsDaysCB.getItems().addAll(FXCollections.observableArrayList(EXAM.getPlannings().stream().map(p -> p.getDate()).distinct().collect(Collectors.toList())));
        try {
            pvsMatsCB.getSelectionModel().select(Integer.parseInt(PREF_BUNDLE.get("OPENING_TRIAL_PRINTABLE_SELECTED_MATTER_INTEGER")));
        } catch ( NullPointerException | NumberFormatException ex ) {}
        try {
            pvsDaysCB.getSelectionModel().select(Integer.parseInt(PREF_BUNDLE.get("OPENING_TRIAL_PRINTABLE_SELECTED_DAY_INTEGER")));
        } catch ( NullPointerException | NumberFormatException ex ) {}
        pvsAllRB.setSelected("1".equals(PREF_BUNDLE.get("OPENING_TRIAL_PRINTABLE_ALL_SELECTED_BOOLEAN")));
        pvsByMatRB.setSelected("1".equals(PREF_BUNDLE.get("OPENING_TRIAL_PRINTABLE_SINGLE_MATTER_SELECTED_BOOLEAN")));
        pvsByDayRB.setSelected("1".equals(PREF_BUNDLE.get("OPENING_TRIAL_PRINTABLE_SINGLE_DAY_SELECTED_BOOLEAN")));
        if ( pvTG.getSelectedToggle() == null )
            pvTG.selectToggle(pvTG.getToggles().get(0));
    }

}
