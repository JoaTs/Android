package se.rejjd.taskmanager.repository;

import java.util.List;

import se.rejjd.taskmanager.http.GetTask;
import se.rejjd.taskmanager.model.User;

public interface UserRepository {

    List<User> getUsers(GetTask.OnResultListener listener);

    User getUser(String id, GetTask.OnResultListener listener);

    Long addUser(User user, GetTask.OnResultListener listener);

    User updateUser(User user, GetTask.OnResultListener listener);

    boolean addUserToWorkItem(final String userId,final long workItemId, GetTask.OnResultListener listener);

    List<User> getUsersFromTeam(final long teamId, GetTask.OnResultListener listenere);

}
