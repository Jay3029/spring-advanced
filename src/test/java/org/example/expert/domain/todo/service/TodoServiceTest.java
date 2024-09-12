package org.example.expert.domain.todo.service;

import org.example.expert.client.WeatherClient;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.todo.dto.request.TodoSaveRequest;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.hamcrest.MatcherAssert.assertThat;

@ExtendWith(SpringExtension.class)
public class TodoServiceTest {

    @MockBean
    private TodoRepository todoRepository;
    @MockBean
    private WeatherClient weatherClient;

    @Test
    public void todo가_정상적으로_저장된다() {
        AuthUser authUser = new AuthUser(1L,"email@example.com", UserRole.USER);
        User user = User.fromAuthUser(authUser);
        String weather = weatherClient.getTodayWeather();
        TodoSaveRequest todoSaveRequest = new TodoSaveRequest("title","contents");

        Todo newTodo = new Todo(
                todoSaveRequest.getTitle(),
                todoSaveRequest.getContents(),
                weather,
                user
        );

        Todo savedTodo = todoRepository.save(newTodo);
    }
}
