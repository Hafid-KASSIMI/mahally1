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
import java.util.HashMap;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import localexam.bones.Room;
import localexam.bones.StudentExamParameters;
import localexam.bones.enums.EXAM_STATUS;
import localexam.bones.enums.SORT_CRITERIA;
import net.mdrassty.object.Student;
import net.mdrassty.util.Misc;
import net.mdrassty.util.SpinnerInitializer;

/**
 *
 * @author H. KASSIMI (@mdrassty)
 */
public class ExamParametersController extends BaseController {

    @FXML
    private TextField examNameTF, roomNameTF;
    @FXML
    private Spinner<Integer> maxCandsSP, firstCodeSP, firstRoomSP,
            roomCapacitySP, examCodeSP, transCodeSP;
    @FXML
    private Button randCodeBtn, randTransCodeBtn, switchBtn,
            editRoomBtn, editCandidateBtn, cancelEditRoomBtn, cancelEditCandidateBtn, applyBtn,
            editCandidateCloseBtn, editRoomCloseBtn;
    @FXML
    private Label levStusLbl, roomsLbl, rooms2DetLbl, rooms1DetLbl,
            goodDistribLbl, notDistributedLbl, excessiveLbl, notDistributedCountLbl, excessiveCountLbl;
    @FXML
    private ToggleButton noOrderTB, randOrderTB, nameAscOrderTB, nameDescOrderTB, lNameAscOrderTB, lNameDescOrderTB;
    @FXML
    private ToggleButton codeAscOrderTB, codeDescOrderTB, ageAscOrderTB, ageDescOrderTB, genAscOrderTB, genDescOrderTB;
    @FXML
    private ToggleGroup sortMethodTG;
    @FXML
    private TableView<Student> studentsTV;
    @FXML
    private TableColumn<Student, Integer> numTC, examCodeTC;
    @FXML
    private TableColumn<Student, String> fNameTC, lNameTC, massarTC, groupTC, roomTC;
    @FXML
    private TableView<Room> roomsTV;
    @FXML
    private TableColumn<Room, Integer> capacityTC, numeroTC;
    @FXML
    private TableColumn<Room, String> labelTC;
    @FXML
    private VBox editRoomVB, editCandidateVB, candsModalVB, roomsModalVB;
    @FXML
    private ComboBox<Room> roomsCB;

    private ChangeListener candsOrderListener;
    private final static Random RANDOM = new Random();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        SpinnerInitializer.initialize(maxCandsSP, new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE, 20, 1));
        SpinnerInitializer.initialize(firstCodeSP, new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE, 1, 1));
        SpinnerInitializer.initialize(transCodeSP, new SpinnerValueFactory.IntegerSpinnerValueFactory(Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 1));
        SpinnerInitializer.initialize(firstRoomSP, new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE, 1, 1));
        SpinnerInitializer.initialize(roomCapacitySP, new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE, 1, 1));
        SpinnerInitializer.initialize(examCodeSP, new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE, 1, 1));

        roomsModalVB.visibleProperty().bind(candsModalVB.visibleProperty());

        applyBtn.setOnAction(evt -> {
            EXAM.setLabel(examNameTF.getText());
            EXAM.setFirstCode(firstCodeSP.getValue());
            EXAM.setTranslationGap(transCodeSP.getValue());
            EXAM.setFirstRoom(firstRoomSP.getValue());
            EXAM.setMaxCandidatesPerRoom(maxCandsSP.getValue());
            refreshStats();
            checkRoomsCapacities();
            EXAM.redistribute();
            EXAM.setStatus(EXAM_STATUS.READY);
            EXAM.setStatus(EXAM_STATUS.PENDING_CHANGES);
        });

        sortMethodTG.getToggles().forEach(tb -> {
            ((ToggleButton) tb).disableProperty().bind(tb.selectedProperty());
        });

        candsOrderListener = (obs, old, cur) -> {
            if (cur == noOrderTB) {
                EXAM.setCandsOrder(SORT_CRITERIA.NATURAL);
            } else if (cur == randOrderTB) {
                EXAM.generateRandomIds();
                EXAM.setCandsOrder(SORT_CRITERIA.RANDOM);
            } else if (cur == nameAscOrderTB) {
                EXAM.setCandsOrder(SORT_CRITERIA.FIRST_NAME_ASC);
            } else if (cur == nameDescOrderTB) {
                EXAM.setCandsOrder(SORT_CRITERIA.FIRST_NAME_DESC);
            } else if (cur == lNameAscOrderTB) {
                EXAM.setCandsOrder(SORT_CRITERIA.LAST_NAME_ASC);
            } else if (cur == lNameDescOrderTB) {
                EXAM.setCandsOrder(SORT_CRITERIA.LAST_NAME_DESC);
            } else if (cur == codeAscOrderTB) {
                EXAM.setCandsOrder(SORT_CRITERIA.MASSAR_ASC);
            } else if (cur == codeDescOrderTB) {
                EXAM.setCandsOrder(SORT_CRITERIA.MASSAR_DESC);
            } else if (cur == genAscOrderTB) {
                EXAM.setCandsOrder(SORT_CRITERIA.GENDER_ASC);
            } else if (cur == genDescOrderTB) {
                EXAM.setCandsOrder(SORT_CRITERIA.GENDER_DESC);
            } else if (cur == ageAscOrderTB) {
                EXAM.setCandsOrder(SORT_CRITERIA.AGE_ASC);
            } else if (cur == ageDescOrderTB) {
                EXAM.setCandsOrder(SORT_CRITERIA.AGE_DESC);
            }
        };

        sortMethodTG.selectedToggleProperty().addListener(candsOrderListener);

        randCodeBtn.setOnAction(evt -> {
            firstCodeSP.getValueFactory().setValue(RANDOM.ints(1, 9999 - EXAM.getStudentsCount()).findFirst().getAsInt());
        });

        randTransCodeBtn.setOnAction(evt -> {
            int rand = 0, first = firstCodeSP.getValue();
            try {
                rand = RANDOM.ints(-first, 9999 - first).findFirst().getAsInt();
            } catch (IllegalArgumentException iae) {
            }
            transCodeSP.getValueFactory().setValue(rand);
        });

        switchBtn.setOnAction(evt -> {
            EXAM.setRoomsOrder((EXAM.getRoomsOrder() == SORT_CRITERIA.ASC) ? SORT_CRITERIA.DESC : SORT_CRITERIA.ASC);
            refreshStats();
            EXAM.redistribute();
        });

        numTC.setCellValueFactory(cellData -> new ReadOnlyIntegerWrapper(cellData.getValue().getNum()).asObject());
        fNameTC.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getFirName()));
        lNameTC.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getSecName()));
        massarTC.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getCode()));
        groupTC.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(
                ((StudentExamParameters) cellData.getValue().getUserData()).getGroup()));
        examCodeTC.setCellValueFactory(cellData -> new ReadOnlyIntegerWrapper(
                ((StudentExamParameters) cellData.getValue().getUserData()).getExamCode()).asObject());
        roomTC.setCellValueFactory(cellData -> ((StudentExamParameters) cellData.getValue().getUserData()).getRoom().labelProperty());

        roomsTV.setRowFactory(tv -> {
            final TableRow<Room> row = new TableRow();
            final ContextMenu contextMenu = new ContextMenu();
            final MenuItem removeMenuItem = new MenuItem("حذف القاعة");
            final MenuItem editMenuItem = new MenuItem("تعديل القاعة");
            removeMenuItem.setOnAction(evt -> {
                roomsTV.getItems().remove(row.getItem());
                EXAM.removeRoom(row.getItem());
                checkRoomsCapacities();
                EXAM.setStatus(EXAM_STATUS.PENDING_CHANGES);
            });
            editMenuItem.setOnAction(evt -> {
                startEditingRoom(row);
                editRoomBtn.setOnAction(editRoomNExit(row));
                roomsTV.requestFocus();
            });
            contextMenu.getItems().add(removeMenuItem);
            contextMenu.getItems().add(editMenuItem);
            row.contextMenuProperty().bind(
                    Bindings.when(row.emptyProperty())
                            .then((ContextMenu) null)
                            .otherwise(contextMenu)
            );
            
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    startEditingRoom(row);
                    editRoomBtn.setOnAction(editRoomNExit(row));
                    roomsTV.requestFocus();
                }
            });
            return row;
        });

        numeroTC.setCellValueFactory(cellData -> cellData.getValue().numeroProperty().asObject());
        labelTC.setCellValueFactory(cellData -> cellData.getValue().labelProperty());
        capacityTC.setCellValueFactory(cellData -> cellData.getValue().capacityProperty().asObject());

        studentsTV.setRowFactory(tv -> {
            final TableRow<Student> row = new TableRow();
            final ContextMenu contextMenu = new ContextMenu();
            final MenuItem editMenuItem = new MenuItem("تعديل المعطيات");
            editMenuItem.setOnAction(evt -> {
                startEditingStudent(row);
                editCandidateBtn.setOnAction(editStudentNExit(row));
                studentsTV.requestFocus();
            });
            contextMenu.getItems().add(editMenuItem);
            row.contextMenuProperty().bind(
                    Bindings.when(row.emptyProperty())
                            .then((ContextMenu) null)
                            .otherwise(contextMenu)
            );
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    startEditingStudent(row);
                    editCandidateBtn.setOnAction(editStudentNExit(row));
                    studentsTV.requestFocus();
                }
            });
            return row;
        });

        cancelEditRoomBtn.setOnAction(evt -> {
            editRoomVB.setVisible(false);
        });

        editRoomCloseBtn.setOnAction(evt -> {
            editRoomVB.setVisible(false);
        });

        cancelEditCandidateBtn.setOnAction(evt -> {
            editCandidateVB.setVisible(false);
        });

        editCandidateCloseBtn.setOnAction(evt -> {
            editCandidateVB.setVisible(false);
        });

        rooms1DetLbl.managedProperty().bind(rooms1DetLbl.visibleProperty());
        switchBtn.managedProperty().bind(switchBtn.visibleProperty());
    }

    @Override
    public void getReady() {
        candsModalVB.visibleProperty().bind(EXAM.statusProperty().isEqualTo(EXAM_STATUS.CREATED)
                .or(EXAM.statusProperty().isEqualTo(EXAM_STATUS.PENDING_UNDER_LEVEL_CHANGES)));
    }

    @Override
    public void refresh() {
        examNameTF.setText(EXAM.getLabel());
        maxCandsSP.getValueFactory().setValue(EXAM.getMaxCandidatesPerRoom());
        firstCodeSP.getValueFactory().setValue(EXAM.getFirstCode());
        transCodeSP.getValueFactory().setValue(EXAM.getTranslationGap());
        firstRoomSP.getValueFactory().setValue(EXAM.getFirstRoom());
        sortMethodTG.selectedToggleProperty().removeListener(candsOrderListener);
        switch (EXAM.getCandsOrder()) {
            case RANDOM:
                randOrderTB.setSelected(true);
                break;
            case FIRST_NAME_ASC:
                nameAscOrderTB.setSelected(true);
                break;
            case FIRST_NAME_DESC:
                nameDescOrderTB.setSelected(true);
                break;
            case LAST_NAME_ASC:
                lNameAscOrderTB.setSelected(true);
                break;
            case LAST_NAME_DESC:
                lNameDescOrderTB.setSelected(true);
                break;
            case MASSAR_ASC:
                codeAscOrderTB.setSelected(true);
                break;
            case MASSAR_DESC:
                codeDescOrderTB.setSelected(true);
                break;
            case GENDER_ASC:
                genAscOrderTB.setSelected(true);
                break;
            case GENDER_DESC:
                genDescOrderTB.setSelected(true);
                break;
            case AGE_ASC:
                ageAscOrderTB.setSelected(true);
                break;
            case AGE_DESC:
                ageDescOrderTB.setSelected(true);
                break;
            default:
                noOrderTB.setSelected(true);
        }
        sortMethodTG.selectedToggleProperty().addListener(candsOrderListener);

        roomsCB.setItems(EXAM.getRooms());
        roomsTV.setItems(EXAM.getRooms());
        studentsTV.setItems(EXAM.getStudents());
        refreshStats();
        checkRoomsCapacities();
    }

    private void refreshStats() {
        boolean switchVisible = true;
        Label lbl1, lbl2;
        HashMap<String, Integer> map = Misc.getSemiEvenDivStats(EXAM.getStudentsCount(), maxCandsSP.getValue());
        levStusLbl.setText(Misc.ARCounter("ممتحن", "ممتحنا", "ممتحنان", "ممتحنين", false, EXAM.getStudentsCount()));
        roomsLbl.setText(Misc.ARCounter("قاعة", "قاعة", "قاعتان", "قاعات", true, map.get("nMin") + map.get("nMax")));
        if (EXAM.getRoomsOrder() == SORT_CRITERIA.DESC) {
            lbl1 = rooms1DetLbl;
            lbl2 = rooms2DetLbl;
        } else {
            lbl1 = rooms2DetLbl;
            lbl2 = rooms1DetLbl;
        }
        lbl1.setVisible(true);
        lbl2.setVisible(true);
        if (map.get("nMax") > 0) {
            lbl1.setText(Misc.ARCounter("قاعة", "قاعة", "قاعتان", "قاعات", true, map.get("nMax")) + " [ "
                    + Misc.ARCounter("ممتحن", "ممتحنا", "ممتحنان", "ممتحنين", false, map.get("max")) + " ]");
        } else {
            lbl1.setVisible(false);
            switchVisible = false;
        }
        if (map.get("nMin") > 0) {
            lbl2.setText(Misc.ARCounter("قاعة", "قاعة", "قاعتان", "قاعات", true, map.get("nMin")) + " [ "
                    + Misc.ARCounter("ممتحن", "ممتحنا", "ممتحنان", "ممتحنين", false, map.get("min")) + " ]");
        } else {
            lbl2.setVisible(false);
            switchVisible = false;
        }
        switchBtn.setVisible(switchVisible);
    }

    private void checkRoomsCapacities() {
        int diff = EXAM.getStudentsCount() - EXAM.getRoomsTotalCapacity();
        if (diff > 0) {
            notDistributedCountLbl.setText(Misc.ARCounter("ممتحن", "ممتحنا", "ممتحنان", "ممتحنين", false, diff));
            notDistributedLbl.setVisible(true);
            excessiveLbl.setVisible(false);
            goodDistribLbl.setVisible(false);
        } else if (diff < 0) {
            excessiveCountLbl.setText("" + Misc.ARCounter("مقعد", "مقعدا", "مقعدان", "مقاعد", false, (diff * -1)));
            excessiveLbl.setVisible(true);
            goodDistribLbl.setVisible(false);
            notDistributedLbl.setVisible(false);
        } else {
            goodDistribLbl.setVisible(true);
            notDistributedLbl.setVisible(false);
            excessiveLbl.setVisible(false);
        }

    }
    
    private EventHandler<ActionEvent> editRoomNExit(TableRow<Room> row) {
        return evt -> {
            row.getItem().setLabel(roomNameTF.getText());
            row.getItem().setCapacity(roomCapacitySP.getValue());
            checkRoomsCapacities();
            EXAM.refresh();
            editRoomVB.setVisible(false);
            EXAM.setStatus(EXAM_STATUS.PENDING_CHANGES);
        };
    }
    
    private void startEditingRoom(TableRow<Room> row) {
        roomCapacitySP.getValueFactory().setValue(row.getItem().getCapacity());
        roomNameTF.setText(row.getItem().getLabel());
        editRoomVB.setVisible(true);
    }
    
    private EventHandler<ActionEvent> editStudentNExit(TableRow<Student> row) {
        return evt -> {
            ((StudentExamParameters) row.getItem().getUserData()).setExamCode(examCodeSP.getValue());
            ((StudentExamParameters) row.getItem().getUserData()).setRoom(roomsCB.getSelectionModel().getSelectedItem());
            EXAM.checkRooms();
            checkRoomsCapacities();
            EXAM.refresh();
            editCandidateVB.setVisible(false);
            EXAM.setStatus(EXAM_STATUS.PENDING_CHANGES);
        };
    }
    
    private void startEditingStudent(TableRow<Student> row) {
        examCodeSP.getValueFactory().setValue(((StudentExamParameters) row.getItem().getUserData()).getExamCode());
        roomsCB.getSelectionModel().select(((StudentExamParameters) row.getItem().getUserData()).getRoom());
        editCandidateVB.setVisible(true);
    }
}
