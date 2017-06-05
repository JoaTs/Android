package se.rejjd.taskmanager.repository;

import java.util.List;

import se.rejjd.taskmanager.http.GetTask;
import se.rejjd.taskmanager.model.Team;

public interface TeamRepository {

    List<Team> getTeams(GetTask.OnResultListener listener);

    Team getTeam(String id, GetTask.OnResultListener listener);

    Long addTeam(Team team, GetTask.OnResultListener listener);

    Team updateTeam(Team team, GetTask.OnResultListener listener);

}

