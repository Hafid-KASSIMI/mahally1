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
package localexam.bones;

/**
 *
 * @author H. KASSIMI (@mdrassty)
 */
public class StudentExamParameters {

    private Integer examCode = -1;
    private Room room = new Room("", -1, 0);
    private String group;

    public StudentExamParameters() {
    }

    public StudentExamParameters(String group) {
        this.group = group;
    }

    public StudentExamParameters(Integer examCode, Integer numero) {
        this.examCode = examCode;
        this.room.setNumero(numero);
    }

    public Integer getExamCode() {
        return examCode;
    }

    public void setExamCode(Integer examCode) {
        this.examCode = examCode;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(String label, Integer numero, Integer capacity) {
        this.room.setLabel(label);
        this.room.setNumero(numero);
        this.room.setCapacity(capacity);
    }

    public void blankRoom() {
        this.room = new Room("", -1, 0);
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

}
