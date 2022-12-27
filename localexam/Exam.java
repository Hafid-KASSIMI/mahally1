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
package localexam;

import java.io.File;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import static localexam.Settings.PREF_BUNDLE;
import localexam.bones.Planning;
import localexam.bones.Room;
import localexam.bones.StudentExamParameters;
import localexam.bones.enums.EXAM_STATUS;
import localexam.bones.enums.SORT_CRITERIA;
import static localexam.bones.enums.SORT_CRITERIA.AGE_ASC;
import static localexam.bones.enums.SORT_CRITERIA.AGE_DESC;
import static localexam.bones.enums.SORT_CRITERIA.FIRST_NAME_DESC;
import static localexam.bones.enums.SORT_CRITERIA.GENDER_ASC;
import static localexam.bones.enums.SORT_CRITERIA.LAST_NAME_DESC;
import static localexam.bones.enums.SORT_CRITERIA.MASSAR_ASC;
import static localexam.bones.enums.SORT_CRITERIA.RANDOM;
import net.mdrassty.object.Level;
import net.mdrassty.object.SchoolInfos;
import net.mdrassty.object.Student;
import net.mdrassty.object.enums.EXAM_TYPE;
import net.mdrassty.util.Misc;

/**
 *
 * @author H. KASSIMI (@mdrassty)
 */
public class Exam implements Serializable {

    private String label = "";
    private Level level;
    private File source;
    private int maxCandidatesPerRoom, firstCode = 1, firstRoom = 1;
    private SORT_CRITERIA candsOrder = SORT_CRITERIA.NATURAL;
    private SORT_CRITERIA roomsOrder = SORT_CRITERIA.DESC;
    private SchoolInfos infos;
    private EXAM_TYPE type = EXAM_TYPE.LOCAL_EXAM;
    private final Random rand = new Random();
    private Comparator<Student> comparator;
    private int examCode, size, roomCapacityIterator, roomIndexIterator, girlsSize;
    private Integer uid, translationGap = 0;
    private final ObservableList<Room> rooms = FXCollections.observableArrayList();
    private final ObservableList<Student> students = FXCollections.observableArrayList();
    private final ObservableList<Planning> plannings = FXCollections.observableArrayList();
    private final ObjectProperty<EXAM_STATUS> STATUS = new SimpleObjectProperty(EXAM_STATUS.EMPTY);
    private final DecimalFormat intFormat = new DecimalFormat("#00");

    public Exam() {
        this(null, null, null);
    }

    public Exam(String label, Level level) {
        this(label, level, null);
    }

    public Exam(String label, Level level, File source) {
        this.label = label;
        this.source = source;
        try {
            maxCandidatesPerRoom = Integer.parseInt(PREF_BUNDLE.get("DEFAULT_MAX_CANDIDATES_PER_ROOM_INTEGER"));
        } catch (NullPointerException | NumberFormatException ex) {
            maxCandidatesPerRoom = 20;
        }
        setLevel(level);
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
        STATUS.set(EXAM_STATUS.PENDING_UNDER_LEVEL_CHANGES);
    }

    public Level getLevel() {
        return level;
    }

    public final void setLevel(Level level) {
        if (level == null) {
            return;
        }

        this.level = level;
        level.getGroups().forEach(grp -> {
            grp.getStudents().forEach(stu -> {
                stu.setUserData(new StudentExamParameters(grp.getName()));
            });
        });

        students.setAll(level.getGroups().stream()
                .flatMap(g -> g.getStudents().stream())
                .collect(Collectors.toList())
        );
        size = students.size();
        girlsSize = students.stream().filter(stu -> stu.isFemale()).mapToInt(stu -> 1).sum();
        STATUS.set(EXAM_STATUS.CREATED);
    }

    public final void setReadyLevel(Level level) {
        if (level == null) {
            return;
        }
        this.level = level;
        students.setAll(level.getGroups().stream()
                .flatMap(g -> g.getStudents().stream()).sorted((stu1, stu2) -> {
            return Objects.compare(((StudentExamParameters) stu1.getUserData()).getExamCode(),
                    ((StudentExamParameters) stu2.getUserData()).getExamCode(), Comparator.naturalOrder());
        }).collect(Collectors.toList())
        );
        size = students.size();
        girlsSize = students.stream().filter(stu -> stu.isFemale()).mapToInt(stu -> 1).sum();
    }

    public File getSource() {
        return source;
    }

    public void setSource(File source) {
        this.source = source;
    }

    public int getMaxCandidatesPerRoom() {
        return maxCandidatesPerRoom;
    }

    public void setMaxCandidatesPerRoom(int maxCandidatesPerRoom) {
        this.maxCandidatesPerRoom = maxCandidatesPerRoom;
        STATUS.set(EXAM_STATUS.PENDING_CHANGES);
    }

    public int getTranslatedFirstCode() {
        return firstCode + translationGap;
    }

    public int getFirstCode() {
        return firstCode;
    }

    public void setFirstCode(int firstCode) {
        this.firstCode = firstCode;
        STATUS.set(EXAM_STATUS.PENDING_CHANGES);
    }

    public int getFirstRoom() {
        return firstRoom;
    }

    public void setFirstRoom(int firstRoom) {
        this.firstRoom = firstRoom;
        generateRooms();
    }

    public int getRoomsTotalCapacity() {
        return rooms.stream().collect(Collectors.summingInt(r -> r.getCapacity()));
    }

    public int getStudentsCount() {
        return size;
    }

    public int getGirlsCount() {
        return girlsSize;
    }

    public SORT_CRITERIA getCandsOrder() {
        return candsOrder;
    }

    public void setCandsOrder(SORT_CRITERIA candsOrder) {
        this.candsOrder = candsOrder;
        sortStudents();
        STATUS.set(EXAM_STATUS.PENDING_CHANGES);
    }

    public void setCandsOrderSilently(SORT_CRITERIA candsOrder) {
        this.candsOrder = candsOrder;
    }

    public SchoolInfos getInfos() {
        return infos;
    }

    public void setInfos(SchoolInfos infos) {
        this.infos = infos;
        uid = Math.abs((infos.getAcademy() + infos.getDirection()
                + infos.getSchool() + infos.getYear()).hashCode());
        STATUS.set(EXAM_STATUS.PENDING_UNDER_LEVEL_CHANGES);
    }

    public Integer getUid() {
        return uid;
    }

    public SORT_CRITERIA getRoomsOrder() {
        return roomsOrder;
    }

    public void setRoomsOrder(SORT_CRITERIA roomsOrder) {
        this.roomsOrder = roomsOrder;
        generateRooms();
    }

    public void setRoomsOrderSilently(SORT_CRITERIA roomsOrder) {
        this.roomsOrder = roomsOrder;
    }

    public ObservableList<Planning> getPlannings() {
        return plannings;
    }

    public EXAM_TYPE getType() {
        return type;
    }

    public void setType(EXAM_TYPE type) {
        this.type = type;
        STATUS.set(EXAM_STATUS.PENDING_CHANGES);
    }

    public ObservableList<Room> getRooms() {
        return rooms;
    }

    public void generateRooms() {
        int i = 0;
        rooms.setAll(Misc.getSemiEvenDivArray(size, maxCandidatesPerRoom).stream()
                .map(r -> new Room("", 0, r)).collect(Collectors.toList()));
        if (roomsOrder == SORT_CRITERIA.DESC) {
            Collections.reverse(rooms);
        }
        for (Room r : rooms) {
            r.setNumero(firstRoom + i);
            r.setLabel(PREF_BUNDLE.get("ROOM_NAME_PREFIX_STRING") + " " + intFormat.format(r.getNumero()));
            i++;
        }
        STATUS.set(EXAM_STATUS.PENDING_CHANGES);
    }

    public void checkRooms() {
        rooms.forEach(room -> {
            room.setCapacity((int) students.stream()
                    .filter(stu -> room.equals(((StudentExamParameters) stu.getUserData()).getRoom()))
                    .count());
        });
    }

    public void removeRoom(Room room) {
        students.stream()
                .filter(stu -> room.equals(((StudentExamParameters) stu.getUserData()).getRoom()))
                .forEach(stu -> {
                    ((StudentExamParameters) stu.getUserData()).blankRoom();
                });
        STATUS.set(EXAM_STATUS.PENDING_CHANGES);
    }

    public void refresh() {
        students.setAll(students.stream().collect(Collectors.toList()));
    }

    public void redistribute() {
        examCode = firstCode;
        roomIndexIterator = 0;
        if (rooms.isEmpty()) {
            generateRooms();
        }
        roomCapacityIterator = rooms.get(0).getCapacity();
        students.setAll(students.stream()
                .peek(stu -> {
                    if (roomCapacityIterator == 0) {
                        roomIndexIterator++;
                        if (roomIndexIterator < rooms.size()) {
                            roomCapacityIterator = rooms.get(roomIndexIterator).getCapacity();
                        }
                    }
                    ((StudentExamParameters) stu.getUserData()).setExamCode(examCode++);
                    ((StudentExamParameters) stu.getUserData()).setRoom(rooms.get(roomIndexIterator));
                    roomCapacityIterator--;
                }).collect(Collectors.toList()));
        STATUS.set(EXAM_STATUS.PENDING_CHANGES);
    }

    public void generateRandomIds() {
        level.getGroups().forEach(g -> {
            g.getStudents().forEach(stu -> stu.setRAND_ID(rand.nextInt()));
        });
    }

    private void sortStudents() {
        switch (candsOrder) {
            case RANDOM:
                comparator = (stu1, stu2) -> {
                    return Objects.compare(stu1.getRAND_ID(), stu2.getRAND_ID(), Comparator.naturalOrder());
                };
                break;
            case FIRST_NAME_ASC:
                comparator = (stu1, stu2) -> {
                    return compareByFirstName(stu1, stu2, SORT_CRITERIA.ASC);
                };
                break;
            case FIRST_NAME_DESC:
                comparator = (stu1, stu2) -> {
                    return compareByFirstName(stu1, stu2, SORT_CRITERIA.DESC);
                };
                break;
            case LAST_NAME_ASC:
                comparator = (stu1, stu2) -> {
                    return compareBySecondName(stu1, stu2, SORT_CRITERIA.ASC);
                };
                break;
            case LAST_NAME_DESC:
                comparator = (stu1, stu2) -> {
                    return compareBySecondName(stu1, stu2, SORT_CRITERIA.DESC);
                };
                break;
            case MASSAR_ASC:
                comparator = (stu1, stu2) -> {
                    return Objects.compare(stu1.getCode(), stu2.getCode(), Comparator.naturalOrder());
                };
                break;
            case MASSAR_DESC:
                comparator = (stu1, stu2) -> {
                    return Objects.compare(stu1.getCode(), stu2.getCode(), Comparator.reverseOrder());
                };
                break;
            case GENDER_ASC:
                comparator = (stu1, stu2) -> {
                    return compareByGender(stu1, stu2, SORT_CRITERIA.ASC);
                };
                break;
            case GENDER_DESC:
                comparator = (stu1, stu2) -> {
                    return compareByGender(stu1, stu2, SORT_CRITERIA.DESC);
                };
                break;
            case AGE_ASC:
                comparator = (stu1, stu2) -> {
                    return compareByAge(stu1, stu2, SORT_CRITERIA.ASC);
                };
                break;
            case AGE_DESC:
                comparator = (stu1, stu2) -> {
                    return compareByAge(stu1, stu2, SORT_CRITERIA.DESC);
                };
                break;
            default:
                comparator = (stu1, stu2) -> {
                    return 1;
                };
        }
        examCode = firstCode;
        roomIndexIterator = 0;
        roomCapacityIterator = rooms.get(0).getCapacity();
        students.setAll(level.getGroups().stream().flatMap(g -> g.getStudents().stream())
                .sorted(comparator).peek(stu -> {
            if (roomCapacityIterator == 0) {
                roomIndexIterator++;
                if (roomIndexIterator < rooms.size()) {
                    roomCapacityIterator = rooms.get(roomIndexIterator).getCapacity();
                }
            }
            ((StudentExamParameters) stu.getUserData()).setExamCode(examCode++);
            if (roomIndexIterator < rooms.size()) {
                ((StudentExamParameters) stu.getUserData()).setRoom(rooms.get(roomIndexIterator));
            } else {
                ((StudentExamParameters) stu.getUserData()).blankRoom();
            }
            roomCapacityIterator--;
        }).collect(Collectors.toList()));
        STATUS.set(EXAM_STATUS.PENDING_CHANGES);
    }

    public ObservableList<Student> getStudents() {
        return students;
    }

    public void refreshStudentsList() {
//        EXAM_STATUS tmp = STATUS.get();
//        students.removeListener(DATA_CHANGE_LISTENER);
//        students.setAll(students.stream().sorted((stu1, stu2) -> {
//            return Objects.compare(((StudentExamParameters) stu1.getUserData()).getExamCode(), 
//                    ((StudentExamParameters) stu2.getUserData()).getExamCode(), Comparator.naturalOrder());
//        }).collect(Collectors.toList()));
//        students.addListener(DATA_CHANGE_LISTENER);
//        STATUS.set(tmp);
    }

    private int compareByFirstName(Student stu1, Student stu2, SORT_CRITERIA order) {
        Comparator c = (order == SORT_CRITERIA.ASC ? Comparator.naturalOrder() : Comparator.reverseOrder());
        int d = Objects.compare(stu1.getFirName(), stu2.getFirName(), c);
        if (d == 0) {
            d = Objects.compare(stu1.getSecName(), stu2.getSecName(), c);
            if (d == 0) {
                d = Objects.compare(stu1.getCode(), stu2.getCode(), c);
            }
        }
        return d;
    }

    private int compareBySecondName(Student stu1, Student stu2, SORT_CRITERIA order) {
        Comparator c = (order == SORT_CRITERIA.ASC ? Comparator.naturalOrder() : Comparator.reverseOrder());
        int d = Objects.compare(stu1.getSecName(), stu2.getSecName(), c);
        if (d == 0) {
            d = Objects.compare(stu1.getFirName(), stu2.getFirName(), c);
            if (d == 0) {
                d = Objects.compare(stu1.getCode(), stu2.getCode(), c);
            }
        }
        return d;
    }

    private int compareByGender(Student stu1, Student stu2, SORT_CRITERIA order) {
        Comparator c = (order == SORT_CRITERIA.ASC ? Comparator.naturalOrder() : Comparator.reverseOrder());
        int d = Objects.compare(stu1.getGender(), stu2.getGender(), c);
        if (d == 0) {
            d = Objects.compare(stu1.getCode(), stu2.getCode(), c);
        }
        return d;
    }

    private int compareByAge(Student stu1, Student stu2, SORT_CRITERIA order) {
        Comparator c = (order == SORT_CRITERIA.ASC ? Comparator.naturalOrder() : Comparator.reverseOrder());
        int d = Objects.compare(stu1.getDaysAge(), stu2.getDaysAge(), c);
        if (d == 0) {
            d = Objects.compare(stu1.getCode(), stu2.getCode(), c);
        }
        return d;
    }

    public int getTranslatedLastCode() {
        return getLastCode() + translationGap;
    }

    public int getLastCode() {
        return students.stream().map(stu -> ((StudentExamParameters) stu.getUserData()).getExamCode())
                .collect(Collectors.maxBy(Comparator.naturalOrder())).get();
    }

    public Student getStudent(int examCode) {
        try {
            return students.filtered(stu -> examCode == ((StudentExamParameters) stu.getUserData()).getExamCode()).get(0);
        } catch (Exception ex) {
            return null;
        }
    }

    public Integer getTranslationGap() {
        return translationGap;
    }

    public void setTranslationGap(Integer translationGap) {
        this.translationGap = translationGap;
        STATUS.set(EXAM_STATUS.PENDING_CHANGES);
    }

    public Room getRoom(int numero) {
        try {
            return rooms.filtered(room -> room.getNumero() == numero).get(0);
        } catch (Exception ex) {
            return null;
        }
    }

    public boolean isReady() {
        return STATUS.isNotEqualTo(EXAM_STATUS.EMPTY).get() && STATUS.isNotEqualTo(EXAM_STATUS.CREATED).get();
    }

    public void getReady() {
        STATUS.set(EXAM_STATUS.NONE);
        STATUS.set(EXAM_STATUS.READY);
    }

    public EXAM_STATUS getStatus() {
        return STATUS.get();
    }

    public void setStatus(EXAM_STATUS value) {
        STATUS.set(value);
    }

    public ObjectProperty statusProperty() {
        return STATUS;
    }

}
