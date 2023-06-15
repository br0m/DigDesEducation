package com.digdes.java2023.repositories;

import com.digdes.java2023.dto.enums.TaskStatus;
import com.digdes.java2023.dto.enums.TaskTimeFindParam;
import com.digdes.java2023.dto.task.FindTaskDto;
import com.digdes.java2023.model.Member;
import com.digdes.java2023.model.Project;
import com.digdes.java2023.model.Task;
import com.digdes.java2023.model.TeamMember;
import com.digdes.java2023.model.config.ModelConfig;
import com.digdes.java2023.repositories.config.RepositoryConfig;
import com.digdes.java2023.repositories.config.RepositoryTestConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.instancio.Instancio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.instancio.Select.all;
import static org.instancio.Select.field;

@ContextConfiguration(classes = {RepositoryConfig.class, RepositoryTestConfig.class, ModelConfig.class, TaskRepositoryJpa.class, TeamRepositoryJpa.class, ProjectRepositoryJpa.class, MemberRepositoryJpa.class})
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TaskRepositoryJpaTest extends Init {

    private final TaskRepositoryJpa taskRepository;
    private final TeamRepositoryJpa teamRepository;
    private final ProjectRepositoryJpa projectRepository;
    private final MemberRepositoryJpa memberRepository;
    private final ObjectMapper objectMapper;
    private final Sort sortByCreationDate;
    private final LocalDateTime pastTime = LocalDateTime.now().minusYears(1);
    private final LocalDateTime futureTime = LocalDateTime.now().plusYears(1);
    private Task task;

    @Autowired
    public TaskRepositoryJpaTest(TaskRepositoryJpa taskRepository, TeamRepositoryJpa teamRepository, ProjectRepositoryJpa projectRepository, MemberRepositoryJpa memberRepository, ObjectMapper objectMapper, Sort sortByCreationDate) {
        this.taskRepository = taskRepository;
        this.teamRepository = teamRepository;
        this.projectRepository = projectRepository;
        this.memberRepository = memberRepository;
        this.objectMapper = objectMapper;
        this.sortByCreationDate = sortByCreationDate;
    }

    @BeforeEach
    public void getTask() {
        Project project = Instancio.create(Project.class);
        Member responsibleMember = Instancio.create(Member.class);
        Member authorMember = Instancio.create(Member.class);
        project = projectRepository.save(project);
        responsibleMember = memberRepository.save(responsibleMember);
        authorMember = memberRepository.save(authorMember);

        TeamMember responsibleTeamMember = Instancio.of(TeamMember.class)
                .set(field(TeamMember::getMember), responsibleMember)
                .set(field(TeamMember::getProject), project)
                .create();
        TeamMember authorTeamMember = Instancio.of(TeamMember.class)
                .set(field(TeamMember::getMember), authorMember)
                .set(field(TeamMember::getProject), project)
                .create();
        responsibleTeamMember = teamRepository.save(responsibleTeamMember);
        authorTeamMember = teamRepository.save(authorTeamMember);

        task = Instancio.of(Task.class)
                .set(field(Task::getProject), project)
                .set(field(Task::getResponsibleMember), responsibleTeamMember)
                .set(field(Task::getAuthor), authorTeamMember)
                .supply(all(LocalDateTime.class), t -> LocalDateTime.now())
                .generate(field(Task::getHoursCost), gen -> gen.ints().range(1, 10))
                .create();
        task.setDeadline(task.getCreationDate().plusHours(task.getHoursCost()));

        task.setDeadline(nanoToMicroseconds(task.getDeadline()));
        task.setCreationDate(nanoToMicroseconds(task.getCreationDate()));
        task.setLastchangeDate(nanoToMicroseconds(task.getLastchangeDate()));
    }

    private List<Task> getFindTasks() throws JsonProcessingException {
        List<Task> findTasks=new LinkedList<>();
        getTask();
        task = taskRepository.save(task);
        findTasks.add(task);

        task = objectMapper.readValue(objectMapper.writeValueAsString(task), Task.class);
        task.setId(null);
        task.setCreationDate(task.getCreationDate().minusSeconds(1));
        task = taskRepository.save(task);
        findTasks.add(task);

        return findTasks;
    }

    @Test
    public void createTest() {
        task = taskRepository.save(task);
        Optional<Task> actualTask = taskRepository.findById(task.getId());
        assert actualTask.isPresent();
        Assertions.assertEquals(task.hashCode(), actualTask.get().hashCode());
    }

    @Test
    public void updateTest() throws IOException {
        task = taskRepository.save(task);

        Task oldTask = objectMapper.readValue(objectMapper.writeValueAsString(task), Task.class);
        getTask();
        task.setId(oldTask.getId());
        task.setStatus(oldTask.getStatus());
        task.setCreationDate(oldTask.getCreationDate());
        task.setCreationDate(nanoToMicroseconds(task.getCreationDate()));

        int count = taskRepository.update(task.getTitle(), task.getDescription(), task.getResponsibleMember(), task.getHoursCost(), task.getDeadline(), task.getAuthor(), task.getLastchangeDate(), task.getProject(), task.getId());

        Optional<Task> actualUpdateTask = taskRepository.findById(task.getId());
        assert actualUpdateTask.isPresent();
        Assertions.assertEquals(1, count);
        Assertions.assertEquals(task.hashCode(), actualUpdateTask.get().hashCode());
    }

    @Test
    public void setStatusTest() {
        task = taskRepository.save(task);
        Integer id = task.getId();
        TaskStatus status = getAnotherStatus(task.getStatus());
        task.setStatus(status);
        int count = taskRepository.setStatus(status, id);

        Optional<Task> actualTask = taskRepository.findById(id);
        assert actualTask.isPresent();
        Assertions.assertEquals(1, count);
        Assertions.assertEquals(task.hashCode(), actualTask.get().hashCode());
    }

    @Test
    public void findTitleTest() throws JsonProcessingException {
        taskRepository.save(task);
        List<Task> findTasks = getFindTasks();

        Task titleTask = Task.builder()
                .title(findTasks.get(0).getTitle())
                .creationDate(pastTime)
                .deadline(pastTime)
                .build();

        getActualAndCheck(findTasks, titleTask, TaskTimeFindParam.AFTER, TaskTimeFindParam.AFTER);
    }

    @Test
    public void findResponsibleMemberTest() throws JsonProcessingException {
        taskRepository.save(task);
        List<Task> findTasks = getFindTasks();

        Task responsibleMemberTask = Task.builder()
                .responsibleMember(findTasks.get(0).getResponsibleMember())
                .creationDate(pastTime)
                .deadline(pastTime)
                .build();

        getActualAndCheck(findTasks, responsibleMemberTask, TaskTimeFindParam.AFTER, TaskTimeFindParam.AFTER);
    }

    @Test
    public void findStatusTest() throws JsonProcessingException {
        List<Task> findTasks = getFindTasks();
        getTask();
        task.setStatus(getAnotherStatus(findTasks.get(0).getStatus()));
        taskRepository.save(task);

        Task statusTask = Task.builder()
                .status(findTasks.get(0).getStatus())
                .creationDate(pastTime)
                .deadline(pastTime)
                .build();

        getActualAndCheck(findTasks, statusTask, TaskTimeFindParam.AFTER, TaskTimeFindParam.AFTER);
    }

    @Test
    public void findAuthorTest() throws JsonProcessingException {
        taskRepository.save(task);
        List<Task> findTasks = getFindTasks();

        Task authorTask = Task.builder()
                .author(findTasks.get(0).getAuthor())
                .creationDate(pastTime)
                .deadline(pastTime)
                .build();

        getActualAndCheck(findTasks, authorTask, TaskTimeFindParam.AFTER, TaskTimeFindParam.AFTER);
    }

    @Test
    public void findDeadlineAfterTest() throws JsonProcessingException {
        task.setCreationDate(pastTime);
        task.setDeadline(pastTime);
        taskRepository.save(task);
        List<Task> findTasks = getFindTasks();

        Task deadlineTask = Task.builder()
                .creationDate(pastTime)
                .deadline(LocalDateTime.now())
                .build();

        getActualAndCheck(findTasks, deadlineTask, TaskTimeFindParam.AFTER, TaskTimeFindParam.AFTER);
    }

    @Test
    public void findDeadlineBeforeTest() throws JsonProcessingException {
        task.setDeadline(LocalDateTime.now().plusYears(1));
        taskRepository.save(task);
        List<Task> findTasks = getFindTasks();

        FindTaskDto findTaskDto = new FindTaskDto();
        findTaskDto.setDeadlinePeriod(TaskTimeFindParam.BEFORE);
        Task deadlineTask = Task.builder()
                .creationDate(pastTime)
                .deadline(futureTime)
                .build();

        getActualAndCheck(findTasks, deadlineTask, TaskTimeFindParam.BEFORE, TaskTimeFindParam.AFTER);
    }

    @Test
    public void findDeadlineEqualTest() throws JsonProcessingException {
        taskRepository.save(task);
        List<Task> findTasks = getFindTasks();

        Task deadlineTask = Task.builder()
                .creationDate(pastTime)
                .deadline(findTasks.get(0).getDeadline())
                .build();

        getActualAndCheck(findTasks, deadlineTask, TaskTimeFindParam.EQUAL, TaskTimeFindParam.AFTER);
    }

    @Test
    public void findCreationDateAfterTest() throws JsonProcessingException {
        task.setCreationDate(LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC));
        taskRepository.save(task);
        List<Task> findTasks = getFindTasks();

        Task creationDateTask = Task.builder()
                .creationDate(pastTime)
                .deadline(pastTime)
                .build();

        getActualAndCheck(findTasks, creationDateTask, TaskTimeFindParam.AFTER, TaskTimeFindParam.AFTER);
    }

    @Test
    public void findCreationDateBeforeTest() throws JsonProcessingException {
        task.setCreationDate(LocalDateTime.now().plusYears(1));
        task.setDeadline(LocalDateTime.now().plusYears(1));
        taskRepository.save(task);
        List<Task> findTasks = getFindTasks();

        Task creationDateTask = Task.builder()
                .creationDate(futureTime)
                .deadline(pastTime)
                .build();

        getActualAndCheck(findTasks, creationDateTask, TaskTimeFindParam.AFTER, TaskTimeFindParam.BEFORE);
    }

    @Test
    public void findCreationDateEqualTest() throws JsonProcessingException {
        taskRepository.save(task);
        List<Task> findTasks = getFindTasks();
        findTasks.remove(1);

        FindTaskDto findTaskDto = new FindTaskDto();
        findTaskDto.setCreationPeriod(TaskTimeFindParam.EQUAL);
        Task creationDateTask = Task.builder()
                .creationDate(findTasks.get(0).getCreationDate())
                .deadline(pastTime)
                .build();

        TaskSpecification spec = new TaskSpecification(creationDateTask, findTaskDto);
        List<Task> actualFindTasks = taskRepository.findAll(spec.buildSpecification(), sortByCreationDate);
        Assertions.assertEquals(findTasks.hashCode(), actualFindTasks.hashCode());
    }

    @Test
    public void findByAllFieldsTest() throws JsonProcessingException {
        taskRepository.save(task);
        List<Task> findTasks = getFindTasks();

        Task specTask = Task.builder()
                .title(findTasks.get(0).getTitle())
                .responsibleMember(findTasks.get(0).getResponsibleMember())
                .status(findTasks.get(0).getStatus())
                .author(findTasks.get(0).getAuthor())
                .creationDate(pastTime)
                .deadline(pastTime)
                .build();

        getActualAndCheck(findTasks, specTask, TaskTimeFindParam.AFTER, TaskTimeFindParam.AFTER);
    }

    private void getActualAndCheck(List<Task> findTasks, Task specTask, TaskTimeFindParam deadlineTimeParam, TaskTimeFindParam creationTimeParam) {
        FindTaskDto findTaskDto = new FindTaskDto();
        findTaskDto.setDeadlinePeriod(deadlineTimeParam);
        findTaskDto.setCreationPeriod(creationTimeParam);

        TaskSpecification spec = new TaskSpecification(specTask, findTaskDto);
        List<Task> actualFindTasks = taskRepository.findAll(spec.buildSpecification(), sortByCreationDate);
        Assertions.assertEquals(findTasks.get(0).hashCode(), actualFindTasks.get(0).hashCode());
        Assertions.assertEquals(findTasks.get(1).hashCode(), actualFindTasks.get(1).hashCode());
    }

    private TaskStatus getAnotherStatus(TaskStatus status) {
        List<TaskStatus> statuses = new LinkedList<>(List.of(TaskStatus.NEW, TaskStatus.WORKING, TaskStatus.FINISHED, TaskStatus.CLOSED));
        return status.ordinal() == 3 ? statuses.get(status.ordinal() - 1) : statuses.get(status.ordinal() + 1);
    }

    private LocalDateTime nanoToMicroseconds(LocalDateTime time) {
        long nano = time.getNano();
        long microsecond = Math.round(nano / 1000F);
        return time.minusNanos(nano).plusNanos(microsecond * 1000);
    }
}
