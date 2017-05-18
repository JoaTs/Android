package se.rejjd.taskmanager.repository.sql.wrapper;

import android.database.Cursor;
import android.database.CursorWrapper;

import se.rejjd.taskmanager.model.Team;
import se.rejjd.taskmanager.sql.DatabaseContract;

public class TeamCursorWrapper extends CursorWrapper {

    public TeamCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Team getTeam() {

        long id = getLong(getColumnIndex(DatabaseContract.ModelEntry._ID));
        String teamName = getString(getColumnIndexOrThrow(DatabaseContract.ModelEntry.TEAM_COLUMN_NAME_TEAM_NAME));
        boolean activeTeam = getInt(getColumnIndexOrThrow(DatabaseContract.ModelEntry.TEAM_COLUMN_NAME_ACTIVE_TEAM)) > 0;

        return new Team(id, teamName, activeTeam);
    }

    public Team getFirstTeam() {
        moveToFirst();
        return getTeam();
    }
}
