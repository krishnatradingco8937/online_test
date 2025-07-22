@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String taskStrId;

    private String description;

    private int estimatedTimeMinutes;

    @Enumerated(EnumType.STRING)
    private TaskStatus status = TaskStatus.PENDING;

    private LocalDateTime submittedAt = LocalDateTime.now();

    // Getters and setters...
}
