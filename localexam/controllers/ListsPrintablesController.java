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

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import static localexam.Settings.PREF_BUNDLE;
import localexam.bones.Room;
import localexam.template.generators.List2PDFFileGenerator;
import localexam.template.generators.List3PDFFileGenerator;
import localexam.template.generators.List1PDFFileGenerator;
import localexam.template.generators.ListPDFFileGenerator;

/**
 *
 * @author H. KASSIMI (@mdrassty)
 */
public class ListsPrintablesController extends BasePrintableController {
    @FXML
    private RadioButton listsAllRB, listsByRoomRB, tpl1RB, tpl2RB, tpl3RB;
    @FXML
    private ComboBox<Room> listsRoomsCB;
    @FXML
    private ToggleGroup listsTG, tplTG;
    @FXML
    private ImageView tplIV;
    @FXML
    private Label tplLbl;

    private final ListPDFFileGenerator LISTS1_GENERATOR = new List1PDFFileGenerator();
    private final ListPDFFileGenerator LISTS2_GENERATOR = new List2PDFFileGenerator();
    private final ListPDFFileGenerator LISTS3_GENERATOR = new List3PDFFileGenerator();
    private final Image[] previews = new Image[3];

    public ListsPrintablesController() {
        super();
        generator = LISTS1_GENERATOR;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        super.initialize(url, rb);

        listsRoomsCB.getSelectionModel().selectedIndexProperty().addListener((obs, old, cur) -> {
            listsByRoomRB.setSelected(true);
            PREF_BUNDLE.update("LISTS_PRINTABLE_SELECTED_ROOM_INTEGER", cur + "");
        });
        listsAllRB.selectedProperty().addListener((obs, old, cur) -> PREF_BUNDLE.update("LISTS_PRINTABLE_ALL_SELECTED_BOOLEAN", cur ? "1" : "0"));
        listsByRoomRB.selectedProperty().addListener((obs, old, cur) -> PREF_BUNDLE.update("LISTS_PRINTABLE_SINGLE_ROOM_SELECTED_BOOLEAN", cur ? "1" : "0"));
        
        for (int i = 0; i < 3; i++) {
            try {
                previews[i] = new Image(getClass().getResource("/localexam/template/resources/previews/lists-" + (i + 1) + ".jpeg").openStream());
            } catch (IOException ex) {
            }
        }
        tplIV.setImage(previews[0]);
        tplLbl.setText("القالب 1");

        tpl1RB.selectedProperty().addListener((obs, old, cur) -> {
            if ( cur ) {
                tplIV.setImage(previews[0]);
                tplLbl.setText("القالب 1");
                generator = LISTS1_GENERATOR;
                PREF_BUNDLE.update("LISTS_PRINTABLE_TEMPLATE_1_SELECTED_BOOLEAN", cur ? "1" : "0");
                refreshProcessingEars();
            }
        });

        tpl2RB.selectedProperty().addListener((obs, old, cur) -> {
            if ( cur ) {
                tplIV.setImage(previews[1]);
                tplLbl.setText("القالب 2");
                generator = LISTS2_GENERATOR;
                PREF_BUNDLE.update("LISTS_PRINTABLE_TEMPLATE_2_SELECTED_BOOLEAN", cur ? "1" : "0");
                refreshProcessingEars();
            }
        });

        tpl3RB.selectedProperty().addListener((obs, old, cur) -> {
            if ( cur ) {
                tplIV.setImage(previews[2]);
                tplLbl.setText("القالب 3");
                generator = LISTS3_GENERATOR;
                PREF_BUNDLE.update("LISTS_PRINTABLE_TEMPLATE_3_SELECTED_BOOLEAN", cur ? "1" : "0");
                refreshProcessingEars();
            }
        });

        generateBtn.setOnAction(evt -> {
            if (destination == null) {
                chooseDirectory();
            }
            if (destination != null) {
                new Thread(() -> {
                    if (tpl1RB.isSelected()) {
                        if (listsByRoomRB.isSelected()) {
                            ((ListPDFFileGenerator) generator).generate(listsRoomsCB.getValue());
                        } else {
                            ((ListPDFFileGenerator) generator).generate();
                        }
                    } else if (tpl2RB.isSelected()) {
                        if (listsByRoomRB.isSelected()) {
                            ((ListPDFFileGenerator) generator).generate(listsRoomsCB.getValue());
                        } else {
                            ((ListPDFFileGenerator) generator).generate();
                        }
                    } else {
                        if (listsByRoomRB.isSelected()) {
                            ((ListPDFFileGenerator) generator).generate(listsRoomsCB.getValue());
                        } else {
                            ((ListPDFFileGenerator) generator).generate();
                        }
                    }
                }).start();
            }
        });
    }

    @Override
    public void getReady() {
        super.getReady();
        LISTS1_GENERATOR.setEXAM(EXAM);
        LISTS2_GENERATOR.setEXAM(EXAM);
        LISTS3_GENERATOR.setEXAM(EXAM);
    }

    @Override
    public void refresh() {
        listsRoomsCB.getItems().clear(); // This must be done. Don't know why ?
        listsRoomsCB.getItems().addAll(EXAM.getRooms());
        try {
            listsRoomsCB.getSelectionModel().select(Integer.parseInt(PREF_BUNDLE.get("LISTS_PRINTABLE_SELECTED_ROOM_INTEGER")));
        } catch ( NullPointerException | NumberFormatException ex ) {}
        
        listsAllRB.setSelected("1".equals(PREF_BUNDLE.get("LISTS_PRINTABLE_ALL_SELECTED_BOOLEAN")));
        listsByRoomRB.setSelected("1".equals(PREF_BUNDLE.get("LISTS_PRINTABLE_SINGLE_ROOM_SELECTED_BOOLEAN")));
        tpl1RB.setSelected("1".equals(PREF_BUNDLE.get("LISTS_PRINTABLE_TEMPLATE_1_SELECTED_BOOLEAN")));
        tpl2RB.setSelected("1".equals(PREF_BUNDLE.get("LISTS_PRINTABLE_TEMPLATE_2_SELECTED_BOOLEAN")));
        tpl3RB.setSelected("1".equals(PREF_BUNDLE.get("LISTS_PRINTABLE_TEMPLATE_3_SELECTED_BOOLEAN")));
        if ( listsTG.getSelectedToggle() == null )
            listsTG.selectToggle(listsTG.getToggles().get(0));
        if ( tplTG.getSelectedToggle() == null )
            tplTG.selectToggle(tplTG.getToggles().get(0));
    }

}
