public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findByTaskStrId(String taskStrId);

    List<Task> findByStatus(TaskStatus status);

    Optional<Task> findFirstByStatusOrderByEstimatedTimeMinutesAscSubmittedAtAsc(TaskStatus status);
}
