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
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import static localexam.Settings.PREF_BUNDLE;
import localexam.template.generators.GuardsPDFFileGenerator;

/**
 *
 * @author H. KASSIMI (@mdrassty)
 */
public class GuardsPrintablesController extends BasePrintableController {

    @FXML
    private RadioButton everythingRB, singleDayRB, singleMatterRB;
    @FXML
    private ToggleGroup optionsTG;
    @FXML
    private ComboBox<String> mattersCB;
    @FXML
    private ComboBox<LocalDate> daysCB;
    @FXML
    private HBox warningHB;
    @FXML
    private ImageView tplIV;

    public GuardsPrintablesController() {
        super();
        generator = new GuardsPDFFileGenerator();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        super.initialize(url, rb);

        mattersCB.getSelectionModel().selectedIndexProperty().addListener((obs, old, cur) -> {
            singleMatterRB.setSelected(true);
            PREF_BUNDLE.update("GUARDS_PRINTABLE_SELECTED_MATTER_INTEGER", cur + "");
        });
        daysCB.getSelectionModel().selectedIndexProperty().addListener((obs, old, cur) -> {
            singleDayRB.setSelected(true);
            PREF_BUNDLE.update("GUARDS_PRINTABLE_SELECTED_DAY_INTEGER", cur + "");
        });
        everythingRB.selectedProperty().addListener((obs, old, cur) -> PREF_BUNDLE.update("GUARDS_PRINTABLE_ALL_SELECTED_BOOLEAN", cur ? "1" : "0"));
        singleDayRB.selectedProperty().addListener((obs, old, cur) -> PREF_BUNDLE.update("GUARDS_PRINTABLE_SINGLE_DAY_SELECTED_BOOLEAN", cur ? "1" : "0"));
        singleMatterRB.selectedProperty().addListener((obs, old, cur) -> PREF_BUNDLE.update("GUARDS_PRINTABLE_SINGLE_MATTER_SELECTED_BOOLEAN", cur ? "1" : "0"));
        
        generateBtn.setOnAction(evt -> {
            if (destination == null) {
                chooseDirectory();
            }
            if (destination != null) {
                new Thread(() -> {
                    if (singleDayRB.isSelected()) {
                        ((GuardsPDFFileGenerator) generator).generate(daysCB.getValue());
                    } else if (singleMatterRB.isSelected()) {
                        ((GuardsPDFFileGenerator) generator).generate(mattersCB.getValue());
                    } else {
                        ((GuardsPDFFileGenerator) generator).generate();
                    }
                }).start();
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
            mattersCB.getSelectionModel().select(Integer.parseInt(PREF_BUNDLE.get("GUARDS_PRINTABLE_SELECTED_MATTER_INTEGER")));
        } catch ( NullPointerException | NumberFormatException ex ) {}
        try {
            daysCB.getSelectionModel().select(Integer.parseInt(PREF_BUNDLE.get("GUARDS_PRINTABLE_SELECTED_DAY_INTEGER")));
        } catch ( NullPointerException | NumberFormatException ex ) {}
        everythingRB.setSelected("1".equals(PREF_BUNDLE.get("GUARDS_PRINTABLE_ALL_SELECTED_BOOLEAN")));
        singleDayRB.setSelected("1".equals(PREF_BUNDLE.get("GUARDS_PRINTABLE_SINGLE_DAY_SELECTED_BOOLEAN")));
        singleMatterRB.setSelected("1".equals(PREF_BUNDLE.get("GUARDS_PRINTABLE_SINGLE_MATTER_SELECTED_BOOLEAN")));
        if ( optionsTG.getSelectedToggle() == null )
            optionsTG.selectToggle(optionsTG.getToggles().get(0));
    }

}
