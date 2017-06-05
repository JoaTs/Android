package se.rejjd.taskmanager.repository;

import java.util.List;

import se.rejjd.taskmanager.http.GetTask;
import se.rejjd.taskmanager.model.Team;

public interface TeamRepository {

    List<Team> getTeams();

    Team getTeam(String id);

    Long addTeam(Team team);

    Team updateTeam(Team team);

}

