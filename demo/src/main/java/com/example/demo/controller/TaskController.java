@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService service;

    @PostMapping
    public ResponseEntity<Task> create(@RequestBody @Valid TaskRequest request) {
        return ResponseEntity.ok(service.createTask(request));
    }

    @GetMapping("/{taskStrId}")
    public ResponseEntity<Task> get(@PathVariable String taskStrId) {
        return ResponseEntity.ok(service.getTask(taskStrId));
    }

    @PutMapping("/{taskStrId}/status")
    public ResponseEntity<Task> updateStatus(@PathVariable String taskStrId, @RequestBody @Valid StatusUpdateRequest req) {
        return ResponseEntity.ok(service.updateStatus(taskStrId, req.getNewStatus()));
    }

    @GetMapping("/next-to-process")
    public ResponseEntity<Task> nextTask() {
        return ResponseEntity.ok(service.getNextToProcess());
    }

    @GetMapping("/pending")
    public ResponseEntity<List<Task>> listPending(
        @RequestParam(defaultValue = "estimatedTimeMinutes") String sortBy,
        @RequestParam(defaultValue = "asc") String order,
        @RequestParam(defaultValue = "10") int limit) {

        return ResponseEntity.ok(service.getPendingTasks(sortBy, order, limit));
    }
}
