package org.example.expert.domain.comment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.expert.domain.comment.dto.request.CommentSaveRequest;
import org.example.expert.domain.comment.dto.response.CommentSaveResponse;
import org.example.expert.domain.comment.service.CommentService;
import org.example.expert.domain.common.annotation.Auth;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
public class CommentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CommentService commentService;

    @Test
    public void 댓글조회가_정상적으로_작동된다() throws Exception {
        // given
        long todoId = 1L;

        given(commentService.getComments(anyLong())).willReturn(List.of());

        // when
        ResultActions resultActions = mockMvc.perform(get("/todo/{todoId}/comments", todoId));

        // then
        resultActions.andExpect(status().isOk());
    }


    @Test
    public void 댓글저장이_성공적으로_작동된다() throws Exception {
        // given
        long todoId = 1L;
        AuthUser authUser = new AuthUser(1L, "test@example.com", UserRole.USER);
        User user = User.fromAuthUser(authUser);
        UserResponse userResponse = new UserResponse(user.getId(), user.getEmail());
        CommentSaveRequest commentSaveRequest = new CommentSaveRequest("This is a comment");
        CommentSaveResponse commentSaveResponse = new CommentSaveResponse(1L, "This is a comment", userResponse);

        given(commentService.saveComment(any(AuthUser.class), anyLong(), any(CommentSaveRequest.class)))
                .willReturn(commentSaveResponse);

        // when
        ResultActions resultActions = mockMvc.perform(post("/todos/{todoId}/comments", todoId)
                .contentType(MediaType.APPLICATION_JSON) // ContentType 설정
                .content(new ObjectMapper().writeValueAsString(commentSaveRequest))); // 본문 데이터 전송;

        // then
        resultActions.andExpect(status().isOk());
    }

}
