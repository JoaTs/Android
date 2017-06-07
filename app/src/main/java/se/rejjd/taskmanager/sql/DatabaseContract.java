package se.rejjd.taskmanager.sql;

import android.provider.BaseColumns;

public class DatabaseContract {

    private DatabaseContract() {
    }
    public static class ModelEntry implements BaseColumns {

        public static final String USERS_TABLE_NAME = "users";
        public static final String USERS_COLUMN_NAME_ID = "id";
        public static final String USERS_COLUMN_NAME_USERNAME = "username";
        public static final String USERS_COLUMN_NAME_FIRSTNAME = "firstname";
        public static final String USERS_COLUMN_NAME_LASTNAME = "lastname";
        public static final String USERS_COLUMN_NAME_USER_ID = "userId";
        public static final String USERS_COLUMN_NAME_TEAM_ID = "teamId";
        public static final String USERS_COLUMN_NAME_ACTIVE_USER = "activeUser";

        public static final String WORK_ITEMS_TABLE_NAME = "workItems";
        public static final String WORK_ITEMS_COLUMN_NAME_ID = "id";
        public static final String WORK_ITEMS_COLUMN_NAME_TITLE = "title";
        public static final String WORK_ITEMS_COLUMN_NAME_DESCRIPTION = "description";
        public static final String WORK_ITEMS_COLUMN_NAME_STATUS = "status";
        public static final String WORK_ITEMS_COLUMN_NAME_USER_ID = "userId";

        public static final String ISSUES_TABLE_NAME = "issues";
        public static final String ISSUES_COLUMN_NAME_ID = "id";
        public static final String ISSUES_COLUMN_NAME_DESCRIPTION = "description";
        public static final String ISSUES_COLUMN_NAME_OPEN_ISSUE = "openIssue";

        public static final String TEAM_TABLE_NAME = "teams";
        public static final String TEAM_COLUMN_NAME_ID = "id";
        public static final String TEAM_COLUMN_NAME_TEAM_NAME = "teamName";
        public static final String TEAM_COLUMN_NAME_ACTIVE_TEAM = "activeTeam";
    }
}