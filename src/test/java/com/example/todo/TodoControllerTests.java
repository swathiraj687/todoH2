package com.example.todo;

import com.example.todo.model.Todo;
import com.example.todo.model.TodoRowMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Sql(scripts = { "/schema.sql", "/data.sql" })
public class TodoControllerTests {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private JdbcTemplate jdbcTemplate;

        Todo todo1 = new Todo(1, "Watch Movie", "LOW", "TO DO");
        Todo todo2 = new Todo(2, "Finish Project", "HIGH", "IN PROGRESS");
        Todo todo3 = new Todo(3, "Buy Groceries", "MEDIUM", "TO DO");
        Todo todo4 = new Todo(4, "Learning from NxtWave", "HIGH", "IN PROGRESS");
        Todo todo5 = new Todo(5, "Go for a Run", "MEDIUM", "DONE");

        Todo todoPost = new Todo(6, "Read Book", "MEDIUM", "TO DO");
        Todo todoPut = new Todo(3, "Buy Groceries", "MEDIUM", "DONE");

        ObjectMapper objectMapper = new ObjectMapper();

        ObjectWriter objectWriter = objectMapper.writer();

        @Test
        @Order(1)
        public void testGetTodos() throws Exception {
                mockMvc.perform(get("/todos"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$", Matchers.hasSize(5)))
                                .andExpect(jsonPath("$[0].id", Matchers.equalTo(1)))
                                .andExpect(jsonPath("$[0].todo",
                                                Matchers.equalToIgnoringCase(todo1.getTodo())))
                                .andExpect(jsonPath("$[0].priority",
                                                Matchers.equalToIgnoringCase(todo1.getPriority())))
                                .andExpect(jsonPath("$[0].status",
                                                Matchers.equalToIgnoringCase(todo1.getStatus())))

                                .andExpect(jsonPath("$[1].id", Matchers.equalTo(2)))
                                .andExpect(jsonPath("$[1].todo",
                                                Matchers.equalToIgnoringCase(todo2.getTodo())))
                                .andExpect(jsonPath("$[1].priority",
                                                Matchers.equalToIgnoringCase(todo2.getPriority())))
                                .andExpect(jsonPath("$[1].status",
                                                Matchers.equalToIgnoringCase(todo2.getStatus())))

                                .andExpect(jsonPath("$[2].id", Matchers.equalTo(3)))
                                .andExpect(jsonPath("$[2].todo",
                                                Matchers.equalToIgnoringCase(todo3.getTodo())))
                                .andExpect(jsonPath("$[2].priority",
                                                Matchers.equalToIgnoringCase(todo3.getPriority())))
                                .andExpect(jsonPath("$[2].status",
                                                Matchers.equalToIgnoringCase(todo3.getStatus())))

                                .andExpect(jsonPath("$[3].id", Matchers.equalTo(4)))
                                .andExpect(jsonPath("$[3].todo",
                                                Matchers.equalToIgnoringCase(todo4.getTodo())))
                                .andExpect(jsonPath("$[3].priority",
                                                Matchers.equalToIgnoringCase(todo4.getPriority())))
                                .andExpect(jsonPath("$[3].status",
                                                Matchers.equalToIgnoringCase(todo4.getStatus())))

                                .andExpect(jsonPath("$[4].id", Matchers.equalTo(5)))
                                .andExpect(jsonPath("$[4].todo",
                                                Matchers.equalToIgnoringCase(todo5.getTodo())))
                                .andExpect(jsonPath("$[4].priority",
                                                Matchers.equalToIgnoringCase(todo5.getPriority())))
                                .andExpect(jsonPath("$[4].status",
                                                Matchers.equalToIgnoringCase(todo5.getStatus())));

        }

        @Test
        @Order(2)
        public void testGetNotFound() throws Exception {
                mockMvc.perform(get("/todos/10"))
                                .andExpect(status().isNotFound());
        }

        @Test
        @Order(3)
        public void testGetTodoById() throws Exception {
                mockMvc.perform(get("/todos/1"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id", Matchers.equalTo(1)))
                                .andExpect(jsonPath("$.todo", Matchers.equalToIgnoringCase(todo1.getTodo())))
                                .andExpect(jsonPath("$.priority",
                                                Matchers.equalToIgnoringCase(todo1.getPriority())))
                                .andExpect(jsonPath("$.status",
                                                Matchers.equalToIgnoringCase(todo1.getStatus())));

                mockMvc.perform(get("/todos/2"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id", Matchers.equalTo(2)))
                                .andExpect(jsonPath("$.todo", Matchers.equalToIgnoringCase(todo2.getTodo())))
                                .andExpect(jsonPath("$.priority",
                                                Matchers.equalToIgnoringCase(todo2.getPriority())))
                                .andExpect(jsonPath("$.status",
                                                Matchers.equalToIgnoringCase(todo2.getStatus())));

                mockMvc.perform(get("/todos/3"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id", Matchers.equalTo(3)))
                                .andExpect(jsonPath("$.todo", Matchers.equalToIgnoringCase(todo3.getTodo())))
                                .andExpect(jsonPath("$.priority",
                                                Matchers.equalToIgnoringCase(todo3.getPriority())))
                                .andExpect(jsonPath("$.status",
                                                Matchers.equalToIgnoringCase(todo3.getStatus())));

                mockMvc.perform(get("/todos/4"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id", Matchers.equalTo(4)))
                                .andExpect(jsonPath("$.todo", Matchers.equalToIgnoringCase(todo4.getTodo())))
                                .andExpect(jsonPath("$.priority",
                                                Matchers.equalToIgnoringCase(todo4.getPriority())))
                                .andExpect(jsonPath("$.status",
                                                Matchers.equalToIgnoringCase(todo4.getStatus())));

                mockMvc.perform(get("/todos/5"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id", Matchers.equalTo(5)))
                                .andExpect(jsonPath("$.todo", Matchers.equalToIgnoringCase(todo5.getTodo())))
                                .andExpect(jsonPath("$.priority",
                                                Matchers.equalToIgnoringCase(todo5.getPriority())))
                                .andExpect(jsonPath("$.status",
                                                Matchers.equalToIgnoringCase(todo5.getStatus())));
        }

        @Test
        @Order(4)
        public void testPost() throws Exception {

                String content = objectWriter.writeValueAsString(todoPost);

                MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/todos")
                                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                                .content(content);
                mockMvc.perform(mockRequest)
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id", Matchers.equalTo(6)))
                                .andExpect(jsonPath("$.todo", Matchers.equalToIgnoringCase("Read Book")))
                                .andExpect(jsonPath("$.priority", Matchers.equalToIgnoringCase("MEDIUM")))
                                .andExpect(jsonPath("$.status", Matchers.equalToIgnoringCase("TO DO")));
        }

        @Test
        @Order(5)
        public void testAfterPost() throws Exception {
                mockMvc.perform(get("/todos/6"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id", Matchers.equalTo(6)))
                                .andExpect(jsonPath("$.todo",
                                                Matchers.equalToIgnoringCase(todoPost.getTodo())))
                                .andExpect(jsonPath("$.priority",
                                                Matchers.equalToIgnoringCase(todoPost.getPriority())))
                                .andExpect(jsonPath("$.status",
                                                Matchers.equalToIgnoringCase(todoPost.getStatus())));
        }

        @Test
        @Order(6)
        public void testDbAfterPost() throws Exception {

                Todo employee = jdbcTemplate.queryForObject("select * from todolist where id = ?",
                                new TodoRowMapper(), 6);
                assertEquals(employee.getTodo(), todoPost.getTodo());
                assertEquals(employee.getPriority(), todoPost.getPriority());
                assertEquals(employee.getStatus(), todoPost.getStatus());

        }

        @Test
        @Order(7)
        public void testPutNotFound() throws Exception {
                Todo todo = new Todo(1, "Watch Movie", "LOW", "TO DO");
                String content = objectWriter.writeValueAsString(todo);

                MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/todos/33")
                                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                                .content(content);
                mockMvc.perform(mockRequest)
                                .andExpect(status().isNotFound());

        }

        @Test
        @Order(8)
        public void testPut() throws Exception {

                String content = objectWriter.writeValueAsString(todoPut);
                MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/todos/3")
                                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                                .content(content);
                mockMvc.perform(mockRequest)
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id", Matchers.equalTo(3)))
                                .andExpect(jsonPath("$.todo",
                                                Matchers.equalToIgnoringCase("Buy Groceries")))
                                .andExpect(jsonPath("$.priority",
                                                Matchers.equalToIgnoringCase("MEDIUM")))
                                .andExpect(jsonPath("$.status",
                                                Matchers.equalToIgnoringCase("DONE")));

        }

        @Test
        @Order(9)
        public void testAfterPut() throws Exception {

                mockMvc.perform(get("/todos/3"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id", Matchers.equalTo(3)))
                                .andExpect(jsonPath("$.todo",
                                                Matchers.equalToIgnoringCase(todoPut.getTodo())))
                                .andExpect(jsonPath("$.priority",
                                                Matchers.equalToIgnoringCase(todoPut.getPriority())))
                                .andExpect(jsonPath("$.status",
                                                Matchers.equalToIgnoringCase(todoPut.getStatus())));

        }

        @Test
        @Order(10)
        public void testDbAfterPut() throws Exception {

                Todo todo = jdbcTemplate.queryForObject("select * from todolist where id = ?",
                                new TodoRowMapper(), 3);

                assertEquals(todoPut.getTodo(), todo.getTodo());
                assertEquals(todoPut.getPriority(), todo.getPriority());
                assertEquals(todoPut.getStatus(), todo.getStatus());
        }

        @Test
        @Order(11)
        public void testDeleteNotFound() throws Exception {

                MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.delete("/todos/90");
                mockMvc.perform(mockRequest).andExpect(status().isOk());

        }

        @Test
        @Order(12)
        public void testDelete() throws Exception {

                MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.delete("/todos/6");

                mockMvc.perform(mockRequest).andExpect(status().isOk());

        }

        @Test
        @Order(13)
        public void testAfterDelete() throws Exception {

                mockMvc.perform(get("/todos"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$", Matchers.hasSize(5)))
                                .andExpect(jsonPath("$[0].id", Matchers.equalTo(1)))
                                .andExpect(jsonPath("$[0].todo",
                                                Matchers.equalToIgnoringCase(todo1.getTodo())))
                                .andExpect(jsonPath("$[0].priority",
                                                Matchers.equalToIgnoringCase(todo1.getPriority())))
                                .andExpect(jsonPath("$[0].status",
                                                Matchers.equalToIgnoringCase(todo1.getStatus())))

                                .andExpect(jsonPath("$[1].id", Matchers.equalTo(2)))
                                .andExpect(jsonPath("$[1].todo",
                                                Matchers.equalToIgnoringCase(todo2.getTodo())))
                                .andExpect(jsonPath("$[1].priority",
                                                Matchers.equalToIgnoringCase(todo2.getPriority())))
                                .andExpect(jsonPath("$[1].status",
                                                Matchers.equalToIgnoringCase(todo2.getStatus())))

                                .andExpect(jsonPath("$[2].id", Matchers.equalTo(3)))
                                .andExpect(jsonPath("$[2].todo",
                                                Matchers.equalToIgnoringCase(todoPut.getTodo())))
                                .andExpect(jsonPath("$[2].priority",
                                                Matchers.equalToIgnoringCase(todoPut.getPriority())))
                                .andExpect(jsonPath("$[2].status",
                                                Matchers.equalToIgnoringCase(todoPut.getStatus())))

                                .andExpect(jsonPath("$[3].id", Matchers.equalTo(4)))
                                .andExpect(jsonPath("$[3].todo",
                                                Matchers.equalToIgnoringCase(todo4.getTodo())))
                                .andExpect(jsonPath("$[3].priority",
                                                Matchers.equalToIgnoringCase(todo4.getPriority())))
                                .andExpect(jsonPath("$[3].status",
                                                Matchers.equalToIgnoringCase(todo4.getStatus())))

                                .andExpect(jsonPath("$[4].id", Matchers.equalTo(5)))
                                .andExpect(jsonPath("$[4].todo",
                                                Matchers.equalToIgnoringCase(todo5.getTodo())))
                                .andExpect(jsonPath("$[4].priority",
                                                Matchers.equalToIgnoringCase(todo5.getPriority())))
                                .andExpect(jsonPath("$[4].status",
                                                Matchers.equalToIgnoringCase(todo5.getStatus())));

                mockMvc.perform(get("/todos/6")).andExpect(status().isNotFound());

        }

        @AfterAll
        public void cleanup() {
                jdbcTemplate.execute("drop table todoList");
        }

}