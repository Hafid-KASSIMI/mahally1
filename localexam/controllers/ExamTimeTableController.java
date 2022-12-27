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
import java.time.LocalTime;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import static localexam.Settings.PREF_BUNDLE;
import localexam.bones.Planning;
import localexam.bones.enums.EXAM_STATUS;
import net.mdrassty.util.Deleter;
import net.mdrassty.util.LocalTimeSpinnerValueFactory;
import net.mdrassty.util.SpinnerInitializer;

/**
 *
 * @author H. KASSIMI (@mdrassty)
 */
public class ExamTimeTableController extends BaseController {

    @FXML
    private ComboBox<String> matterCB, editMatterCB;
    @FXML
    private Spinner<LocalTime> startTimeSP, endTimeSP, editStartTimeSP, editEndTimeSP;
    @FXML
    private DatePicker dateDP, editDateDP;
    @FXML
    private Button addBtn, deleteMatBtn, editBtn, cancelEditBtn, editCloseBtn;
    @FXML
    private TableView<Planning> tableTV;
    @FXML
    private TableColumn<Planning, String> matterTC;
    @FXML
    private TableColumn<Planning, LocalDate> dateTC;
    @FXML
    private TableColumn<Planning, LocalTime> fromTC;
    @FXML
    private TableColumn<Planning, LocalTime> toTC;
    @FXML
    private VBox editVB;

    private final ObservableList<String> matters = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Deleter deleter = new Deleter(deleteMatBtn);
        SpinnerInitializer.initialize(startTimeSP, new LocalTimeSpinnerValueFactory(LocalTime.of(4, 0), LocalTime.of(22, 0), LocalTime.of(8, 0), 60));
        SpinnerInitializer.initialize(endTimeSP, new LocalTimeSpinnerValueFactory(LocalTime.of(4, 0), LocalTime.of(22, 0), LocalTime.of(10, 0), 60));
        SpinnerInitializer.initialize(editStartTimeSP, new LocalTimeSpinnerValueFactory(LocalTime.of(4, 0), LocalTime.of(22, 0), LocalTime.of(8, 0), 60));
        SpinnerInitializer.initialize(editEndTimeSP, new LocalTimeSpinnerValueFactory(LocalTime.of(4, 0), LocalTime.of(22, 0), LocalTime.of(10, 0), 60));
        dateDP.setValue(LocalDate.now());

        startTimeSP.getValueFactory().valueProperty().addListener((obs, old, cur) -> {
            if (startTimeSP.getValue().isAfter(endTimeSP.getValue())) {
                endTimeSP.getValueFactory().setValue(startTimeSP.getValue().plusHours(1));
            }
        });

        endTimeSP.getValueFactory().valueProperty().addListener((obs, old, cur) -> {
            if (endTimeSP.getValue().isBefore(startTimeSP.getValue())) {
                startTimeSP.getValueFactory().setValue(endTimeSP.getValue().minusHours(1));
            }
        });

        editStartTimeSP.getValueFactory().valueProperty().addListener((obs, old, cur) -> {
            if (editStartTimeSP.getValue().isAfter(editEndTimeSP.getValue())) {
                editEndTimeSP.getValueFactory().setValue(editStartTimeSP.getValue().plusHours(1));
            }
        });

        editEndTimeSP.getValueFactory().valueProperty().addListener((obs, old, cur) -> {
            if (editEndTimeSP.getValue().isBefore(editStartTimeSP.getValue())) {
                editStartTimeSP.getValueFactory().setValue(editEndTimeSP.getValue().minusHours(1));
            }
        });

        cancelEditBtn.setOnAction(evt -> {
            editVB.setVisible(false);
        });
        editCloseBtn.setOnAction(evt -> {
            editVB.setVisible(false);
        });
        addBtn.setOnAction(evt -> {
            tableTV.getItems().add(new Planning(matterCB.getValue(), dateDP.getValue(),
                    startTimeSP.getValue(), endTimeSP.getValue()));
            startTimeSP.getValueFactory().setValue(endTimeSP.getValue().plusMinutes(30));
        });
        matterCB.setItems(matters);
        matters.addAll(PREF_BUNDLE.get("MATTERS_STRING").split(PREF_BUNDLE.get("MATTERS_SEPARATOR_STRING")));
        matterCB.valueProperty().addListener((obs, old, cur) -> {
            if (!matters.contains(cur)) {
                matters.add(cur);
            }
        });
        editMatterCB.setItems(matterCB.getItems());

        deleteMatBtn.setOnAction(evt -> {
            if (deleter.isDone()) {
                deleter.start();
            } else {
                matterCB.getItems().remove(matterCB.getSelectionModel().getSelectedItem());
                deleter.stop();
            }
        });

        initTableView();

    }

    @Override
    public void refresh() {
        tableTV.setItems(EXAM.getPlannings());
    }

    private void initTableView() {
        matterTC.setCellValueFactory(cellData -> cellData.getValue().labelProperty());
        dateTC.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        fromTC.setCellValueFactory(cellData -> cellData.getValue().startTimeProperty());
        toTC.setCellValueFactory(cellData -> cellData.getValue().endTimeProperty());

        tableTV.setRowFactory(tv -> {
            final TableRow<Planning> row = new TableRow();
            final ContextMenu contextMenu = new ContextMenu();
            final MenuItem removeMenuItem = new MenuItem("إلغاء المادة");
            final MenuItem editMenuItem = new MenuItem("تعديل المادة");
            removeMenuItem.setOnAction(evt -> {
                tableTV.getItems().remove(row.getItem());
                EXAM.setStatus(EXAM_STATUS.PENDING_CHANGES);
            });
            editMenuItem.setOnAction(evt -> {
                startEditingPlanning(row);
                editBtn.setOnAction(evt2 -> {
                    editPlanningNExit(row);
                    tableTV.requestFocus();
                });
            });
            contextMenu.getItems().add(removeMenuItem);
            contextMenu.getItems().add(editMenuItem);
            row.contextMenuProperty().bind(
                    Bindings.when(row.emptyProperty())
                            .then((ContextMenu) null)
                            .otherwise(contextMenu)
            );
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    startEditingPlanning(row);
                    editBtn.setOnAction(evt2 -> {
                        editPlanningNExit(row);
                        tableTV.requestFocus();
                    });
                }
            });
            return row;
        });
    }

    private void editPlanningNExit(TableRow<Planning> row) {
        row.getItem().setDate(editDateDP.getValue());
        row.getItem().setEndTime(editEndTimeSP.getValue());
        row.getItem().setLabel(editMatterCB.getValue());
        row.getItem().setStartTime(editStartTimeSP.getValue());
        editVB.setVisible(false);
        EXAM.setStatus(EXAM_STATUS.PENDING_CHANGES);
    }

    private void startEditingPlanning(TableRow<Planning> row) {
        editStartTimeSP.getValueFactory().setValue(row.getItem().getStartTime());
        editEndTimeSP.getValueFactory().setValue(row.getItem().getEndTime());
        editMatterCB.getSelectionModel().select(row.getItem().getLabel());
        editDateDP.setValue(row.getItem().getDate());
        editVB.setVisible(true);
    }

}
