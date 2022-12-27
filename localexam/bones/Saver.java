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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import localexam.Exam;
import localexam.bones.enums.EXAM_STATUS;
import localexam.bones.enums.FILE_DATA_TYPE;
import localexam.bones.enums.SORT_CRITERIA;
import net.mdrassty.object.Group;
import net.mdrassty.object.Level;
import net.mdrassty.object.SchoolInfos;
import net.mdrassty.object.Student;
import net.mdrassty.object.enums.EXAM_TYPE;
import net.mdrassty.util.AESCipher;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 *
 * @author H. KASSIMI (@mdrassty)
 */
public class Saver {

    private String version, date, encryptionKey;
    private static final List<String> COMPATIBLE_VERSIONS = Arrays.asList("1.0.0", "1.0.1");
    public enum RESULT {
        SUCCESS,
        UNCOMPATIBLE_VERSION,
        UNKNOWN_FORMAT,
        FILE_DATA_TYPE_NOT_ACCEPTED,
        EXAM_INFOS_NOT_RECOGNIZED,
        UNDEFINED_MATTER,
        UNKNOWN_ISSUE
    };
    private RESULT parseResult;

    public Saver(String version, String date, String encryptionKey) {
        this.version = version;
        this.date = date;
        this.encryptionKey = encryptionKey;
    }

    public Saver() {
    }

    public Saver(String version, String date) {
        this.version = version;
        this.date = date;
    }

    public String getEncryptionKey() {
        return encryptionKey;
    }

    public void setEncryptionKey(String encryptionKey) {
        this.encryptionKey = encryptionKey;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void saveMarks(Exam exam, String matter, int firstCode, int lastCode, String path) {
        this.saveMarks(exam, matter, firstCode, lastCode, new File(path));
    }

    public void saveExam(Exam exam, String path) {
        this.saveExam(exam, new File(path));
    }

    public void saveExam(Exam exam, File outputFile) {
        try (FileOutputStream stringWriter = new FileOutputStream(outputFile)) {
            XMLOutputFactory xMLOutputFactory = XMLOutputFactory.newInstance();
            XMLStreamWriter xMLStreamWriter = xMLOutputFactory.createXMLStreamWriter(stringWriter, "UTF-8");
            xMLStreamWriter.writeStartDocument();
            xMLStreamWriter.writeStartElement("exam");
            xMLStreamWriter.writeStartElement("app");
            xMLStreamWriter.writeStartElement("version");
            xMLStreamWriter.writeCharacters(version);
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeStartElement("date");
            xMLStreamWriter.writeCharacters(date);
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeStartElement("dataType");
            xMLStreamWriter.writeCharacters(FILE_DATA_TYPE.EXAM_FILE.name());
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeStartElement("label");
            xMLStreamWriter.writeCharacters(exam.getLabel());
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeStartElement("infos");
            xMLStreamWriter.writeStartElement("school");
            xMLStreamWriter.writeCharacters(exam.getInfos().getSchool());
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeStartElement("direction");
            xMLStreamWriter.writeCharacters(exam.getInfos().getDirection());
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeStartElement("academy");
            xMLStreamWriter.writeCharacters(exam.getInfos().getAcademy());
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeStartElement("year");
            xMLStreamWriter.writeCharacters(exam.getInfos().getYear());
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeStartElement("options");
            xMLStreamWriter.writeStartElement("maxPerRoom");
            xMLStreamWriter.writeCharacters(exam.getMaxCandidatesPerRoom() + "");
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeStartElement("firstCode");
            xMLStreamWriter.writeCharacters(exam.getFirstCode() + "");
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeStartElement("translation");
            xMLStreamWriter.writeCharacters(exam.getTranslationGap() + "");
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeStartElement("firstRoom");
            xMLStreamWriter.writeCharacters(exam.getFirstRoom() + "");
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeStartElement("sortOrder");
            xMLStreamWriter.writeStartElement("candidates");
            xMLStreamWriter.writeCharacters(exam.getCandsOrder() + "");
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeStartElement("rooms");
            xMLStreamWriter.writeCharacters(exam.getRoomsOrder() + "");
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeStartElement("type");
            xMLStreamWriter.writeCharacters(exam.getType() + "");
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeStartElement("ready");
            xMLStreamWriter.writeCharacters(exam.isReady() ? "1" : "0");
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeStartElement("rooms");
            for (Room room : exam.getRooms()) {
                xMLStreamWriter.writeStartElement("room");
                xMLStreamWriter.writeStartElement("numero");
                xMLStreamWriter.writeCharacters(room.getNumero() + "");
                xMLStreamWriter.writeEndElement();
                xMLStreamWriter.writeStartElement("label");
                xMLStreamWriter.writeCharacters(room.getLabel());
                xMLStreamWriter.writeEndElement();
                xMLStreamWriter.writeStartElement("capacity");
                xMLStreamWriter.writeCharacters(room.getCapacity() + "");
                xMLStreamWriter.writeEndElement();
                xMLStreamWriter.writeEndElement();
            }
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeStartElement("plannings");
            for (Planning planning : exam.getPlannings()) {
                xMLStreamWriter.writeStartElement("planning");
                xMLStreamWriter.writeStartElement("label");
                xMLStreamWriter.writeCharacters(planning.getLabel());
                xMLStreamWriter.writeEndElement();
                xMLStreamWriter.writeStartElement("date");
                xMLStreamWriter.writeCharacters(planning.getDate().toString());
                xMLStreamWriter.writeEndElement();
                xMLStreamWriter.writeStartElement("startTime");
                xMLStreamWriter.writeCharacters(planning.getStartTime().toString());
                xMLStreamWriter.writeEndElement();
                xMLStreamWriter.writeStartElement("endTime");
                xMLStreamWriter.writeCharacters(planning.getEndTime().toString());
                xMLStreamWriter.writeEndElement();
                xMLStreamWriter.writeEndElement();
            }
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeStartElement("level");
            xMLStreamWriter.writeStartElement("name");
            xMLStreamWriter.writeCharacters(exam.getLevel().getName());
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeStartElement("groups");
            for (Group grp : exam.getLevel().getGroups()) {
                xMLStreamWriter.writeStartElement("group");
                xMLStreamWriter.writeStartElement("name");
                xMLStreamWriter.writeCharacters(grp.getName());
                xMLStreamWriter.writeEndElement();
//                xMLStreamWriter.writeStartElement("teachers");
//                for (String matter : grp.getAssociatedMatters()) {
//                    xMLStreamWriter.writeStartElement("teacher");
//                    xMLStreamWriter.writeStartElement("name");
//                    xMLStreamWriter.writeCharacters(grp.getTeacher(matter));
//                    xMLStreamWriter.writeEndElement();
//                    xMLStreamWriter.writeStartElement("matter");
//                    xMLStreamWriter.writeCharacters(matter);
//                    xMLStreamWriter.writeEndElement();
//                    xMLStreamWriter.writeEndElement();
//                }
//                xMLStreamWriter.writeEndElement();
                xMLStreamWriter.writeStartElement("students");
                for (Student stu : grp.getStudents()) {
                    xMLStreamWriter.writeStartElement("student");
                    xMLStreamWriter.writeStartElement("numero");
                    xMLStreamWriter.writeCharacters(stu.getNum() + "");
                    xMLStreamWriter.writeEndElement();
                    xMLStreamWriter.writeStartElement("name");
                    xMLStreamWriter.writeStartElement("first");
                    xMLStreamWriter.writeCharacters(stu.getFirName());
                    xMLStreamWriter.writeEndElement();
                    xMLStreamWriter.writeStartElement("last");
                    xMLStreamWriter.writeCharacters(stu.getSecName());
                    xMLStreamWriter.writeEndElement();
                    xMLStreamWriter.writeEndElement();
                    xMLStreamWriter.writeStartElement("gender");
                    xMLStreamWriter.writeCharacters(stu.getGender());
                    xMLStreamWriter.writeEndElement();
                    xMLStreamWriter.writeStartElement("address");
                    xMLStreamWriter.writeCharacters(stu.getAddress());
                    xMLStreamWriter.writeEndElement();
                    xMLStreamWriter.writeStartElement("massar");
                    xMLStreamWriter.writeCharacters(stu.getCode());
                    xMLStreamWriter.writeEndElement();
                    xMLStreamWriter.writeStartElement("birth");
                    xMLStreamWriter.writeCharacters(stu.getBirthDate());
                    xMLStreamWriter.writeEndElement();
                    StudentExamParameters params = (StudentExamParameters) stu.getUserData();
                    if (params != null) {
                        xMLStreamWriter.writeStartElement("examParameters");
                        xMLStreamWriter.writeStartElement("code");
                        xMLStreamWriter.writeCharacters(params.getExamCode() + "");
                        xMLStreamWriter.writeEndElement();
                        xMLStreamWriter.writeStartElement("room");
                        xMLStreamWriter.writeCharacters(params.getRoom().getNumero() + "");
                        xMLStreamWriter.writeEndElement();
                        xMLStreamWriter.writeEndElement();
                    }
                    xMLStreamWriter.writeStartElement("examMarks");
                    HashMap<String, Double> marks = stu.getMarks().getExamMarks(exam.getType());
                    if (marks != null) {
                        for (String matter : marks.keySet()) {
                            xMLStreamWriter.writeStartElement("examMark");
                            xMLStreamWriter.writeStartElement("matter");
                            xMLStreamWriter.writeCharacters(matter);
                            xMLStreamWriter.writeEndElement();
                            xMLStreamWriter.writeStartElement("mark");
                            xMLStreamWriter.writeCharacters(marks.get(matter) + "");
                            xMLStreamWriter.writeEndElement();
                            xMLStreamWriter.writeEndElement();
                        }
                    }
                    xMLStreamWriter.writeEndElement();
                    xMLStreamWriter.writeEndElement();
                }
                xMLStreamWriter.writeEndElement();
                xMLStreamWriter.writeEndElement();
            }
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeEndElement();

            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeEndDocument();
            xMLStreamWriter.flush();
            xMLStreamWriter.close();
        } catch (XMLStreamException | IOException e) {
        }
    }

    public void saveMarks(Exam exam, String matter, int firstCode, int lastCode, File outputFile) {
        try (FileOutputStream stringWriter = new FileOutputStream(outputFile)) {
            XMLOutputFactory xMLOutputFactory = XMLOutputFactory.newInstance();
            XMLStreamWriter xMLStreamWriter = xMLOutputFactory.createXMLStreamWriter(stringWriter, "UTF-8");
            xMLStreamWriter.writeStartDocument();
            xMLStreamWriter.writeStartElement("exam");
            xMLStreamWriter.writeStartElement("app");
            xMLStreamWriter.writeStartElement("version");
            xMLStreamWriter.writeCharacters(version);
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeStartElement("date");
            xMLStreamWriter.writeCharacters(date);
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeStartElement("dataType");
            xMLStreamWriter.writeCharacters(FILE_DATA_TYPE.EXAM_MARKS_FILE.name());
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeStartElement("examType");
            xMLStreamWriter.writeCharacters(exam.getType().name());
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeStartElement("translation");
            xMLStreamWriter.writeCharacters(AESCipher.encrypt(exam.getTranslationGap() + "", encryptionKey));
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeStartElement("label");
            xMLStreamWriter.writeCharacters(exam.getLabel());
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeStartElement("uid");
            xMLStreamWriter.writeCharacters(exam.getUid() + "");
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeStartElement("matter");
            xMLStreamWriter.writeCharacters(matter);
            xMLStreamWriter.writeEndElement();
            List<Student> stus = exam.getStudents().stream()
                    .filter(stu -> ((StudentExamParameters) stu.getUserData()).getExamCode() >= firstCode
                    && ((StudentExamParameters) stu.getUserData()).getExamCode() <= lastCode)
                    .sorted((stu1, stu2) -> Integer.compare(((StudentExamParameters) stu1.getUserData()).getExamCode(),
                    ((StudentExamParameters) stu2.getUserData()).getExamCode())).collect(Collectors.toList());
            xMLStreamWriter.writeStartElement("marks");
            for (Student stu : stus) {
                xMLStreamWriter.writeStartElement("student");
                xMLStreamWriter.writeStartElement("code");
                xMLStreamWriter.writeCharacters((((StudentExamParameters) stu.getUserData()).getExamCode() + exam.getTranslationGap()) + "");
                xMLStreamWriter.writeEndElement();
                xMLStreamWriter.writeStartElement("mark");
                xMLStreamWriter.writeCharacters(stu.getMarks().getExamMark(exam.getType(), matter) + "");
                xMLStreamWriter.writeEndElement();
                xMLStreamWriter.writeEndElement();
            }
            xMLStreamWriter.writeEndElement();

            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeEndDocument();
            xMLStreamWriter.flush();
            xMLStreamWriter.close();
        } catch (XMLStreamException | IOException e) {
        }
    }

    public void saveMarks(Marks marks, File outputFile) {
        try (FileOutputStream stringWriter = new FileOutputStream(outputFile)) {
            XMLOutputFactory xMLOutputFactory = XMLOutputFactory.newInstance();
            XMLStreamWriter xMLStreamWriter = xMLOutputFactory.createXMLStreamWriter(stringWriter, "UTF-8");
            xMLStreamWriter.writeStartDocument();
            xMLStreamWriter.writeStartElement("exam");
            xMLStreamWriter.writeStartElement("app");
            xMLStreamWriter.writeStartElement("version");
            xMLStreamWriter.writeCharacters(version);
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeStartElement("date");
            xMLStreamWriter.writeCharacters(date);
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeStartElement("dataType");
            xMLStreamWriter.writeCharacters(FILE_DATA_TYPE.EXAM_MARKS_FILE.name());
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeStartElement("examType");
            xMLStreamWriter.writeCharacters(marks.getExamType().name());
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeStartElement("translation");
            xMLStreamWriter.writeCharacters(marks.getTranslation());
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeStartElement("label");
            xMLStreamWriter.writeCharacters(marks.getExamLabel());
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeStartElement("uid");
            xMLStreamWriter.writeCharacters(marks.getUid() + "");
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeStartElement("matter");
            xMLStreamWriter.writeCharacters(marks.getMatter());
            xMLStreamWriter.writeEndElement();

            xMLStreamWriter.writeStartElement("marks");
            for (Student stu : marks.getData()) {
                xMLStreamWriter.writeStartElement("student");
                xMLStreamWriter.writeStartElement("code");
                xMLStreamWriter.writeCharacters(((StudentExamParameters) stu.getUserData()).getExamCode() + "");
                xMLStreamWriter.writeEndElement();
                xMLStreamWriter.writeStartElement("mark");
                xMLStreamWriter.writeCharacters(stu.getMarks().getExamMark(marks.getExamType(), marks.getMatter()) + "");
                xMLStreamWriter.writeEndElement();
                xMLStreamWriter.writeEndElement();
            }
            xMLStreamWriter.writeEndElement();

            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeEndDocument();
            xMLStreamWriter.flush();
            xMLStreamWriter.close();
        } catch (XMLStreamException | IOException e) {
        }
    }

    public Exam parseExam(File inputFile) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            Document doc = factory.newDocumentBuilder().parse(inputFile);
            XPath xpath = XPathFactory.newInstance().newXPath();
            String tmp;
            Exam exam;
            Level level;

            try {
                tmp = ((Node) xpath.compile("/exam/app/version/text()").evaluate(doc, XPathConstants.NODE)).getNodeValue();
            } catch (NullPointerException npe) {
                tmp = "";
            }
            if (!COMPATIBLE_VERSIONS.contains(tmp)) {
                parseResult = RESULT.UNCOMPATIBLE_VERSION;
                return null;
            }
            try {
                tmp = ((Node) xpath.compile("/exam/dataType/text()").evaluate(doc, XPathConstants.NODE)).getNodeValue();
            } catch (NullPointerException npe) {
                tmp = "";
            }
            if (!Objects.equals(FILE_DATA_TYPE.EXAM_FILE.name(), tmp)) {
                parseResult = RESULT.FILE_DATA_TYPE_NOT_ACCEPTED;
                return null;
            }

            exam = new Exam();
            exam.setInfos(new SchoolInfos());
            try {
                exam.getInfos().setAcademy(((Node) xpath.compile("/exam/infos/academy/text()").evaluate(doc, XPathConstants.NODE)).getNodeValue());
            } catch (NullPointerException npe) {
            }
            try {
                exam.getInfos().setDirection(((Node) xpath.compile("/exam/infos/direction/text()").evaluate(doc, XPathConstants.NODE)).getNodeValue());
            } catch (NullPointerException npe) {
            }
            try {
                exam.getInfos().setSchool(((Node) xpath.compile("/exam/infos/school/text()").evaluate(doc, XPathConstants.NODE)).getNodeValue());
            } catch (NullPointerException npe) {
            }
            try {
                exam.getInfos().setYear(((Node) xpath.compile("/exam/infos/year/text()").evaluate(doc, XPathConstants.NODE)).getNodeValue());
            } catch (NullPointerException npe) {
            }
            try {
                exam.setLabel(((Node) xpath.compile("/exam/label/text()").evaluate(doc, XPathConstants.NODE)).getNodeValue());
            } catch (NullPointerException npe) {
            }
            try {
                exam.setMaxCandidatesPerRoom(Integer.parseInt(((Node) xpath.compile("/exam/options/maxPerRoom/text()").evaluate(doc, XPathConstants.NODE)).getNodeValue()));
            } catch (NumberFormatException nfe) {
                exam.setMaxCandidatesPerRoom(0);
            }
            try {
                exam.setFirstCode(Integer.parseInt(((Node) xpath.compile("/exam/options/firstCode/text()").evaluate(doc, XPathConstants.NODE)).getNodeValue()));
            } catch (NumberFormatException nfe) {
                exam.setFirstCode(0);
            }
            try {
                exam.setFirstRoom(Integer.parseInt(((Node) xpath.compile("/exam/options/firstRoom/text()").evaluate(doc, XPathConstants.NODE)).getNodeValue()));
            } catch (NumberFormatException nfe) {
                exam.setFirstRoom(0);
            }
            try {
                exam.setTranslationGap(Integer.parseInt(((Node) xpath.compile("/exam/options/translation/text()").evaluate(doc, XPathConstants.NODE)).getNodeValue()));
            } catch (NumberFormatException nfe) {
            }
            try {
                exam.setType(EXAM_TYPE.valueOf(((Node) xpath.compile("/exam/options/type/text()").evaluate(doc, XPathConstants.NODE)).getNodeValue()));
            } catch (NullPointerException npe) {
            }
            for (int i = 1, n = ((Double) xpath.compile("count(/exam/rooms/room)").evaluate(doc, XPathConstants.NUMBER)).intValue() + 1;
                    i < n; i++) {
                Room room = new Room();
                try {
                    room.setNumero(Integer.parseInt(((Node) xpath.compile("/exam/rooms/room[" + i + "]/numero/text()").evaluate(doc, XPathConstants.NODE)).getNodeValue()));
                } catch (NumberFormatException nfe) {
                }
                try {
                    room.setLabel(((Node) xpath.compile("/exam/rooms/room[" + i + "]/label/text()").evaluate(doc, XPathConstants.NODE)).getNodeValue());
                } catch (NullPointerException npf) {
                }
                try {
                    room.setCapacity(Integer.parseInt(((Node) xpath.compile("/exam/rooms/room[" + i + "]/capacity/text()").evaluate(doc, XPathConstants.NODE)).getNodeValue()));
                } catch (NumberFormatException nfe) {
                }
                exam.getRooms().add(room);
            }
            for (int i = 1, n = ((Double) xpath.compile("count(/exam/plannings/planning)").evaluate(doc, XPathConstants.NUMBER)).intValue() + 1;
                    i < n; i++) {
                Planning planning = new Planning();
                try {
                    planning.setLabel(((Node) xpath.compile("/exam/plannings/planning[" + i + "]/label/text()").evaluate(doc, XPathConstants.NODE)).getNodeValue());
                } catch (NullPointerException npf) {
                }
                try {
                    planning.setDate(LocalDate.parse(((Node) xpath.compile("/exam/plannings/planning[" + i + "]/date/text()").evaluate(doc, XPathConstants.NODE)).getNodeValue()));
                } catch (NumberFormatException | DateTimeParseException nfe) {
                }
                try {
                    planning.setStartTime(LocalTime.parse(((Node) xpath.compile("/exam/plannings/planning[" + i + "]/startTime/text()").evaluate(doc, XPathConstants.NODE)).getNodeValue()));
                } catch (NumberFormatException | DateTimeParseException nfe) {
                }
                try {
                    planning.setEndTime(LocalTime.parse(((Node) xpath.compile("/exam/plannings/planning[" + i + "]/endTime/text()").evaluate(doc, XPathConstants.NODE)).getNodeValue()));
                } catch (NumberFormatException | DateTimeParseException nfe) {
                }
                exam.getPlannings().add(planning);
            }
            try {
                level = new Level(((Node) xpath.compile("/exam/level/name/text()").evaluate(doc, XPathConstants.NODE)).getNodeValue());
            } catch (NullPointerException npe) {
                level = new Level("");
            }
            for (int i = 1, n = ((Double) xpath.compile("count(/exam/level/groups/group)").evaluate(doc, XPathConstants.NUMBER)).intValue() + 1;
                    i < n; i++) {
                Group grp;
                try {
                    grp = level.addGroup(((Node) xpath.compile("/exam/level/groups/group[" + i + "]/name/text()").evaluate(doc, XPathConstants.NODE)).getNodeValue());
                } catch (NullPointerException npe) {
                    grp = level.addGroup("");
                }
                for (int j = 1, m = ((Double) xpath.compile("count(/exam/level/groups/group[" + i + "]/students/student)").evaluate(doc, XPathConstants.NUMBER)).intValue() + 1;
                        j < m; j++) {
                    Student stu = new Student();
                    try {
                        stu.setNum(Integer.parseInt(((Node) xpath.compile("/exam/level/groups/group[" + i + "]/students/student[" + j + "]/numero/text()").evaluate(doc, XPathConstants.NODE)).getNodeValue()));
                    } catch (NumberFormatException nfe) {
                        stu.setNum(0);
                    }
                    try {
                        stu.setFirName(((Node) xpath.compile("/exam/level/groups/group[" + i + "]/students/student[" + j + "]/name/first/text()").evaluate(doc, XPathConstants.NODE)).getNodeValue());
                    } catch (NullPointerException npe) {
                    }
                    try {
                        stu.setSecName(((Node) xpath.compile("/exam/level/groups/group[" + i + "]/students/student[" + j + "]/name/last/text()").evaluate(doc, XPathConstants.NODE)).getNodeValue());
                    } catch (NullPointerException npe) {
                    }
                    try {
                        stu.setGender(((Node) xpath.compile("/exam/level/groups/group[" + i + "]/students/student[" + j + "]/gender/text()").evaluate(doc, XPathConstants.NODE)).getNodeValue());
                    } catch (NullPointerException npe) {
                    }
                    try {
                        stu.setAddress(((Node) xpath.compile("/exam/level/groups/group[" + i + "]/students/student[" + j + "]/address/text()").evaluate(doc, XPathConstants.NODE)).getNodeValue());
                    } catch (NullPointerException npe) {
                    }
                    try {
                        stu.setCode(((Node) xpath.compile("/exam/level/groups/group[" + i + "]/students/student[" + j + "]/massar/text()").evaluate(doc, XPathConstants.NODE)).getNodeValue());
                    } catch (NullPointerException npe) {
                    }
                    try {
                        stu.setBirthDate(((Node) xpath.compile("/exam/level/groups/group[" + i + "]/students/student[" + j + "]/birth/text()").evaluate(doc, XPathConstants.NODE)).getNodeValue());
                    } catch (NullPointerException npe) {
                    }
                    StudentExamParameters sep = new StudentExamParameters(grp.getName());
                    try {
                        sep.setExamCode(
                                Integer.parseInt(((Node) xpath.compile("/exam/level/groups/group[" + i + "]/students/student[" + j + "]/examParameters/code/text()").evaluate(doc, XPathConstants.NODE)).getNodeValue())
                        );
                    } catch (NumberFormatException nfe) {
                        sep.setExamCode(0);
                    }
                    try {
                        sep.setRoom(
                                exam.getRoom(Integer.parseInt(((Node) xpath.compile("/exam/level/groups/group[" + i + "]/students/student[" + j + "]/examParameters/room/text()").evaluate(doc, XPathConstants.NODE)).getNodeValue()))
                        );
                    } catch (NumberFormatException nfe) {
                        sep.blankRoom();
                    }
                    stu.setUserData(sep);
                    for (int k = 1, l = ((Double) xpath.compile("count(/exam/level/groups/group[" + i + "]/students/student[" + j + "]/examMarks/examMark)").evaluate(doc, XPathConstants.NUMBER)).intValue() + 1;
                            k < l; k++) {
                        try {
                            stu.getMarks().setExamMark(exam.getType(),
                                    ((Node) xpath.compile("/exam/level/groups/group[" + i + "]/students/student[" + j + "]/examMarks/examMark[" + k + "]/matter/text()").evaluate(doc, XPathConstants.NODE)).getNodeValue(),
                                    Double.parseDouble(((Node) xpath.compile("/exam/level/groups/group[" + i + "]/students/student[" + j + "]/examMarks/examMark[" + k + "]/mark/text()").evaluate(doc, XPathConstants.NODE)).getNodeValue()));
                        } catch (NumberFormatException nfe) {
                        }
                    }
                    grp.addStudent(stu);
                }
            }
            exam.setReadyLevel(level);
            try {
                exam.setCandsOrderSilently(SORT_CRITERIA.valueOf(((Node) xpath.compile("/exam/options/sortOrder/candidates/text()").evaluate(doc, XPathConstants.NODE)).getNodeValue()));
            } catch (NullPointerException npe) {
            }
            try {
                exam.setRoomsOrderSilently(SORT_CRITERIA.valueOf(((Node) xpath.compile("/exam/options/sortOrder/rooms/text()").evaluate(doc, XPathConstants.NODE)).getNodeValue()));
            } catch (NullPointerException npe) {
            }
            exam.setSource(inputFile);
            try {
                exam.setStatus("1".equals(((Node) xpath.compile("/exam/options/ready/text()").evaluate(doc, XPathConstants.NODE)).getNodeValue())
                        ? EXAM_STATUS.READY : EXAM_STATUS.CREATED);
            } catch (NullPointerException npe) {
            }
            parseResult = RESULT.SUCCESS;
            return exam;
        } catch (SAXException | IOException | ParserConfigurationException
                | XPathExpressionException ex) {
        }
        parseResult = RESULT.UNKNOWN_ISSUE;
        return null;
    }

    public RESULT parseMarks(Exam exam, File inputFile) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            Document doc = factory.newDocumentBuilder().parse(inputFile);
            XPath xpath = XPathFactory.newInstance().newXPath();
            String matter;
            int translation = 0, code;
            double mark;
            Student stu;

            if (!Objects.equals(version, ((Node) xpath.compile("/exam/app/version/text()").evaluate(doc, XPathConstants.NODE)).getNodeValue())) {
                return RESULT.UNCOMPATIBLE_VERSION;
            }

            if (!Objects.equals(FILE_DATA_TYPE.EXAM_MARKS_FILE.name(), ((Node) xpath.compile("/exam/dataType/text()").evaluate(doc, XPathConstants.NODE)).getNodeValue())) {
                return RESULT.FILE_DATA_TYPE_NOT_ACCEPTED;
            }
            
            if (!Objects.equals(exam.getUid() + "", ((Node) xpath.compile("/exam/uid/text()").evaluate(doc, XPathConstants.NODE)).getNodeValue())) {
                return RESULT.EXAM_INFOS_NOT_RECOGNIZED;
            }

            try {
                translation = Integer.parseInt(AESCipher.decrypt(((Node) xpath.compile("/exam/translation/text()").evaluate(doc, XPathConstants.NODE)).getNodeValue(), encryptionKey));
            } catch (NumberFormatException nfe) {
            }
            

            matter = ((Node) xpath.compile("/exam/matter/text()").evaluate(doc, XPathConstants.NODE)).getNodeValue();
            if (matter.isEmpty()) {
                return RESULT.UNDEFINED_MATTER;
            }

            for (int i = 1, n = ((Double) xpath.compile("count(/exam/marks/student)").evaluate(doc, XPathConstants.NUMBER)).intValue() + 1;
                    i < n; i++) {
                try {
                    code = Integer.parseInt(((Node) xpath.compile("/exam/marks/student[" + i + "]/code/text()").evaluate(doc, XPathConstants.NODE)).getNodeValue());
                    code -= translation;
                    try {
                        mark = Double.parseDouble(((Node) xpath.compile("/exam/marks/student[" + i + "]/mark/text()").evaluate(doc, XPathConstants.NODE)).getNodeValue());
                    } catch (NumberFormatException nfe) {
                        mark = 0;
                    }
                    stu = exam.getStudent(code);
                    if (stu != null) {
                        stu.getMarks().setExamMark(exam.getType(), matter, mark);
                    }
                } catch (NumberFormatException nfe) {
                }
            }
            return RESULT.SUCCESS;
        } catch (SAXException | IOException | ParserConfigurationException
                | XPathExpressionException ex) {
        }
        return RESULT.UNKNOWN_ISSUE;
    }

    public Marks parseMarks(File inputFile) {
        Marks marks = new Marks();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            Document doc = factory.newDocumentBuilder().parse(inputFile);
            XPath xpath = XPathFactory.newInstance().newXPath();

            if (!Objects.equals(version, ((Node) xpath.compile("/exam/app/version/text()").evaluate(doc, XPathConstants.NODE)).getNodeValue())
                    || !Objects.equals(FILE_DATA_TYPE.EXAM_MARKS_FILE.name(), ((Node) xpath.compile("/exam/dataType/text()").evaluate(doc, XPathConstants.NODE)).getNodeValue())) {
                return marks;
            }

            marks.setUid(((Node) xpath.compile("/exam/uid/text()").evaluate(doc, XPathConstants.NODE)).getNodeValue());
            marks.setTranslation(((Node) xpath.compile("/exam/translation/text()").evaluate(doc, XPathConstants.NODE)).getNodeValue());
            marks.setMatter(((Node) xpath.compile("/exam/matter/text()").evaluate(doc, XPathConstants.NODE)).getNodeValue());
            marks.setExamType(((Node) xpath.compile("/exam/dataType/text()").evaluate(doc, XPathConstants.NODE)).getNodeValue());
            try {
                marks.setExamLabel(((Node) xpath.compile("/exam/label/text()").evaluate(doc, XPathConstants.NODE)).getNodeValue());
            } catch (NullPointerException npe) {
                marks.setExamLabel("");
            }

            for (int i = 1, n = ((Double) xpath.compile("count(/exam/marks/student)").evaluate(doc, XPathConstants.NUMBER)).intValue() + 1;
                    i < n; i++) {
                Student stu = new Student();
                StudentExamParameters sep = new StudentExamParameters();
                stu.setUserData(sep);
                try {
                    sep.setExamCode(Integer.parseInt(((Node) xpath.compile("/exam/marks/student[" + i + "]/code/text()").evaluate(doc, XPathConstants.NODE)).getNodeValue()));
                } catch (NumberFormatException nfe) {
                }
                try {
                    stu.getMarks().setExamMark(marks.getExamType(), marks.getMatter(),
                            Double.parseDouble(((Node) xpath.compile("/exam/marks/student[" + i + "]/mark/text()").evaluate(doc, XPathConstants.NODE)).getNodeValue()));
                } catch (NumberFormatException nfe) {
                }
                marks.addStudent(stu);
            }

        } catch (SAXException | IOException | ParserConfigurationException
                | XPathExpressionException ex) {
        }
        return marks;
    }

    public RESULT getParseResult() {
        return parseResult;
    }
    
}
