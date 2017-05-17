package se.rejjd.taskmanager.repository;

import java.util.List;

import se.rejjd.taskmanager.model.Issue;
import se.rejjd.taskmanager.model.Team;

public interface IssueRepository {

    List<Issue> getIssues();

    Issue getIssue(String id);

    Long addIssue(Issue issue);

}

