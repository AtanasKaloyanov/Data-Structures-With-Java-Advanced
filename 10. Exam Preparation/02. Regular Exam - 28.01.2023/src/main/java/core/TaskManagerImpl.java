package core;

import models.Task;

import java.util.*;
import java.util.stream.Collectors;

public class TaskManagerImpl implements TaskManager {
    private LinkedHashMap<String, Task> tasksById = new LinkedHashMap<>();
    private LinkedHashSet<Task> pendingTasks = new LinkedHashSet<>();
    private Map<String, Task> executedTasks = new LinkedHashMap<>();

    @Override
    public void addTask(Task task) {
        this.tasksById.put(task.getId(), task);
        this.pendingTasks.add(task);
    }

    @Override
    public boolean contains(Task task) {
        return this.tasksById.containsKey(task.getId());
    }

    @Override
    public int size() {
        return this.pendingTasks.size();
    }

    @Override
    public Task getTask(String taskId) {
        Task task = this.tasksById.get(taskId);
        if (task == null) {
            throw new IllegalArgumentException();
        }
        return task;
    }

    @Override
    public void deleteTask(String taskId) {
        Task removedTask = this.tasksById.remove(taskId);
        if (removedTask == null) {
            throw new IllegalArgumentException();
        }
        this.pendingTasks.remove(removedTask);
        this.executedTasks.remove(taskId);
    }

    @Override
    public Task executeTask() {
        if (this.pendingTasks.isEmpty()) {
            throw new IllegalArgumentException();
        }
        Iterator<Task> iterator = this.pendingTasks.iterator();
        Task first = iterator.next();
        iterator.remove();

        this.executedTasks.put(first.getId(), first);
        return first;
    }

    @Override
    public void rescheduleTask(String taskId) {
        Task changedTask = this.executedTasks.remove(taskId);
        if (changedTask == null) {
            throw new IllegalArgumentException();
        }
        this.pendingTasks.add(changedTask);
    }

    @Override
    public Iterable<Task> getDomainTasks(String domain) {
        List<Task> result = this.pendingTasks.stream()
                .filter((task) -> task.getDomain().equals(domain))
                .collect(Collectors.toList());

        if (result.isEmpty()) {
            throw new IllegalArgumentException();
        }
        return result;
    }

    @Override
    public Iterable<Task> getTasksInEETRange(int lowerBound, int upperBound) {
        return this.pendingTasks.stream()
                .filter((task) -> task.getEstimatedExecutionTime() >= lowerBound
                        && task.getEstimatedExecutionTime() <= upperBound)
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Task> getAllTasksOrderedByEETThenByName() {
        return this.tasksById.values()
                .stream()
                .sorted(Comparator.comparing(Task::getEstimatedExecutionTime).reversed()
                        //  .sorted(Comparator.comparing(Task::getEstimatedExecutionTime, Comparator.reverseOrder())
                        .thenComparing(task -> task.getName().length()))
                .collect(Collectors.toList());
    }
}
