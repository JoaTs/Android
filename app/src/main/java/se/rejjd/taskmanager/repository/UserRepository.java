package se.rejjd.taskmanager.repository;

import java.util.List;

import se.rejjd.taskmanager.http.GetTask;
import se.rejjd.taskmanager.model.User;

public interface UserRepository {

    List<User> getUsers();

    User getUser(String id);

    Long addUser(User user);

    User updateUser(User user);

    boolean addUserToWorkItem(final String userId,final long workItemId);

    List<User> getUsersFromTeam(final long teamId);

}
