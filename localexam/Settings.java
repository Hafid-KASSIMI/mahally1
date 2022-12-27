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

import localexam.bones.Saver;
import net.mdrassty.database.Preferences;
import net.mdrassty.util.EnvVariable;

/**
 *
 * @author H. KASSIMI (@mdrassty)
 */
public class Settings {

    public static final String APP_NAME = "محلي";
    public static final String APP_TITLE = "إعداد لوائح الممتحنين";
    public static final String APP_YEAR = "2023";
    public static final String APP_VERSION = "1.0.1";
    public static final String APP_DATE = "25/12/2022";
    public static final String APP_EXTENSION = ".mahally";
    public static final String APP_EXTENSION2 = ".marks.mahally";

    public static final String DB_FOLDER_PATH = EnvVariable.APPDATADirectory() + "/Mahally/";
    public static final String PREF_DB_NAME = "preferences." + APP_VERSION + ".mahally.properties";
    public static final String PREF_DB_PATH = DB_FOLDER_PATH + PREF_DB_NAME;

    public static Preferences PREF_BUNDLE;
    public static Saver SAVER;
}
