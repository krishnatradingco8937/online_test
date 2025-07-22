@Service
public class TaskService {

    @Autowired
    private TaskRepository repository;

    public Task createTask(TaskRequest request) {
        if (repository.findByTaskStrId(request.getTaskStrId()).isPresent())
            throw new CustomException("Task ID already exists");

        Task task = new Task();
        task.setTaskStrId(request.getTaskStrId());
        task.setDescription(request.getDescription());
        task.setEstimatedTimeMinutes(request.getEstimatedTimeMinutes());
        return repository.save(task);
    }

    public Task getTask(String taskStrId) {
        return repository.findByTaskStrId(taskStrId)
                .orElseThrow(() -> new CustomException("Task not found"));
    }

    public Task updateStatus(String taskStrId, String newStatusStr) {
        Task task = getTask(taskStrId);
        TaskStatus newStatus = TaskStatus.valueOf(newStatusStr.toUpperCase());

        // Prevent status regression
        if (task.getStatus() == TaskStatus.COMPLETED && newStatus != TaskStatus.COMPLETED)
            throw new CustomException("Cannot revert from COMPLETED");
        if (task.getStatus() == TaskStatus.PROCESSING && newStatus == TaskStatus.PENDING)
            throw new CustomException("Cannot revert from PROCESSING to PENDING");

        task.setStatus(newStatus);
        return repository.save(task);
    }

    public Task getNextToProcess() {
        return repository.findFirstByStatusOrderByEstimatedTimeMinutesAscSubmittedAtAsc(TaskStatus.PENDING)
                .orElseThrow(() -> new CustomException("No pending tasks"));
    }

    public List<Task> getPendingTasks(String sortBy, String order, int limit) {
        List<Task> tasks = repository.findByStatus(TaskStatus.PENDING);

        Comparator<Task> comparator;
        if ("submittedAt".equalsIgnoreCase(sortBy)) {
            comparator = Comparator.comparing(Task::getSubmittedAt);
        } else {
            comparator = Comparator.comparing(Task::getEstimatedTimeMinutes);
        }

        if ("desc".equalsIgnoreCase(order)) {
            comparator = comparator.reversed();
        }

        return tasks.stream()
                .sorted(comparator)
                .limit(limit)
                .collect(Collectors.toList());
    }
}
