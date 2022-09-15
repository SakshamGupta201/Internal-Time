package com.saksham.demo.dataloader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.saksham.demo.model.Role;
import com.saksham.demo.model.Task;
import com.saksham.demo.model.User;
import com.saksham.demo.service.RoleService;
import com.saksham.demo.service.TaskService;
import com.saksham.demo.service.UserService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class InitialDataLoader implements ApplicationListener<ContextRefreshedEvent> {

        private UserService userService;
        private TaskService taskService;
        private RoleService roleService;
        private final Logger logger = LoggerFactory.getLogger(InitialDataLoader.class);
        private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        @Value("${default.admin.mail}")
        private String defaultAdminMail;
        @Value("${default.admin.name}")
        private String defaultAdminName;
        @Value("${default.admin.password}")
        private String defaultAdminPassword;
        @Value("${default.admin.image}")
        private String defaultAdminImage;

        @Autowired
        public InitialDataLoader(UserService userService, TaskService taskService, RoleService roleService) {
                this.userService = userService;
                this.taskService = taskService;
                this.roleService = roleService;
        }

        @Override
        public void onApplicationEvent(ContextRefreshedEvent event) {

                // ROLES
                // --------------------------------------------------------------------------------------------------------
                roleService.createRole(new Role("ADMIN"));
                roleService.createRole(new Role("USER"));
                roleService.findAll().stream().map(role -> "saved role: " + role.getRole()).forEach(logger::info);

                // USERS
                // --------------------------------------------------------------------------------------------------------
                // 1
                User admin = new User(
                                defaultAdminMail,
                                defaultAdminName,
                                defaultAdminPassword,
                                defaultAdminImage);
                userService.createUser(admin);
                userService.changeRoleToAdmin(admin);

                // 2
                User manager = new User(
                                "manager@mail.com",
                                "Manager",
                                "112233",
                                "images/admin.png");
                userService.createUser(manager);
                userService.changeRoleToAdmin(manager);

                // 3
                userService.createUser(new User(
                                "user1@mail.com",
                                "User1",
                                "112233",
                                "images/user.jpg"));

                // 4
                userService.createUser(new User(
                                "ann@mail.com",
                                "Ann",
                                "112233",
                                "images/user.jpg"));

                // 5
                userService.createUser(new User(
                                "user2@mail.com",
                                "User2",
                                "112233",
                                "images/user.jpg"));

                // 6
                userService.createUser(new User(
                                "user3@mail.com",
                                "User3",
                                "112233",
                                "images/user.jpg"));

                // 7
                userService.createUser(new User(
                                "user4@mail.com",
                                "User4",
                                "112233",
                                "images/user.jpg"));

                userService.findAll().stream()
                                .map(u -> "saved user: " + u.getName())
                                .forEach(logger::info);

                // TASKS
                // --------------------------------------------------------------------------------------------------------
                // tasks from Web Design Checklist
                // https://www.beewits.com/the-ultimate-web-design-checklist-things-to-do-when-launching-a-website/

                LocalDate today = LocalDate.now();

                // 1
                taskService.createTask(new Task(
                                "Project1 ",
                                "Desc",
                                today.minusDays(0),
                                true,
                                userService.getUserByEmail("user1@mail.com").getName(),
                                userService.getUserByEmail("user1@mail.com"),
                                1));

                // 2
                taskService.createTask(new Task(
                                "Project 2 ",
                                "Desc",
                                today.minusDays(1),
                                true,
                                userService.getUserByEmail("ann@mail.com").getName(),
                                userService.getUserByEmail("ann@mail.com"),
                                1));

                // 3
                taskService.createTask(new Task(
                                "Project 3",
                                "Desc",
                                today.minusDays(2),
                                true,
                                userService.getUserByEmail("user2@mail.com").getName(),
                                userService.getUserByEmail("user2@mail.com"),
                                1));

                // 4
                taskService.createTask(new Task(
                                "Project 4",
                                "Desc",
                                today.minusDays(3),
                                true,
                                userService.getUserByEmail("user3@mail.com").getName(),
                                userService.getUserByEmail("user3@mail.com"), 1));

                // 5
                taskService.createTask(new Task(
                                "Project 5",
                                "Desc",
                                today.minusDays(4),
                                false,
                                userService.getUserByEmail("manager@mail.com").getName(),
                                userService.getUserByEmail("manager@mail.com"), 1));

                // taskService.findAll().stream().map(t -> "saved task: '" + t.getName()
                // + "' for owner: " + getOwnerNameOrNoOwner(t)).forEach(logger::info);
        }

        // private String getOwnerNameOrNoOwner(Task task) {
        // return task.getOwner() == null ? "no owner" : task.getOwner().getName();
        // }
}
