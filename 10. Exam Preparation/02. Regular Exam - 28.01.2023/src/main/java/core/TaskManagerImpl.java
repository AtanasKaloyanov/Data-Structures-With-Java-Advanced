package core;

import models.Task;

import java.util.*;
import java.util.stream.Collectors;

public class TaskManagerImpl implements TaskManager {
    private Map<String, Task> tasksById = new LinkedHashMap<>();
    private Queue<Task> executableTask = new ArrayDeque<>();
    private Map<String, Task> executedTasks = new LinkedHashMap<>();
    private Map<String, Set<Task>> tasksByDomain = new LinkedHashMap<>();

    @Override
    public void addTask(Task task) {
        this.tasksById.put(task.getId(), task);
        this.executableTask.offer(task);
        // by Domain
        this.tasksByDomain.putIfAbsent(task.getDomain(), new LinkedHashSet<>());
        this.tasksByDomain.get(task.getDomain()).add(task);
    }

    @Override
    public boolean contains(Task task) {
        return this.tasksById.containsKey(task.getId());
    }

    @Override
    public int size() {
        return this.tasksById.size();
    }

    @Override
    public Task getTask(String taskId) {
        if (!this.tasksById.containsKey(taskId)) {
            throw new IllegalArgumentException();
        }

        return this.tasksById.get(taskId);
    }

    @Override
    public void deleteTask(String taskId) {
        if (!this.tasksById.containsKey(taskId)) {
            throw new IllegalArgumentException();
        }

        Task removedTask = this.tasksById.remove(taskId);
        this.executableTask.removeIf(task -> task.getId().equals(taskId));
        this.executedTasks.remove(taskId);
        this.tasksByDomain.get(removedTask.getDomain()).remove(removedTask);
    }

    @Override
    public Task executeTask() {
        Task firstTask = this.executableTask.poll();
        if (firstTask == null) {
            throw new IllegalArgumentException();
        }

        this.tasksById.remove(firstTask.getId());
        this.executedTasks.put(firstTask.getId(), firstTask);
        this.tasksByDomain.get(firstTask.getDomain()).remove(firstTask);
        return firstTask;
    }

    @Override
    public void rescheduleTask(String taskId) {
        Task changedTask = this.executedTasks.remove(taskId);
        if (changedTask == null) {
            throw new IllegalArgumentException();
        }
        this.tasksById.put(changedTask.getId(), changedTask);
        this.executableTask.offer(changedTask);
        this.tasksByDomain.get(changedTask.getDomain()).add(changedTask);
    }

    @Override
    public Iterable<Task> getDomainTasks(String domain) {
        Set<Task> tasks = this.tasksByDomain.get(domain);
        if (tasks == null) {
            throw new IllegalArgumentException();
        }
        return tasks;
    }

    @Override
    public Iterable<Task> getTasksInEETRange(int lowerBound, int upperBound) {
        return this.executableTask.stream()
                .filter( (task) -> task.getEstimatedExecutionTime() <= lowerBound
                        && task.getEstimatedExecutionTime() >= upperBound)
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Task> getAllTasksOrderedByEETThenByName() {
        return this.tasksById.values()
                .stream()
                .sorted(Comparator.comparing(Task::getEstimatedExecutionTime).reversed()
                        .thenComparing(Task::getName))
                .collect(Collectors.toList());
    }
}
