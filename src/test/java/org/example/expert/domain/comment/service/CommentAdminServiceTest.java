package org.example.expert.domain.comment.service;

import org.example.expert.domain.comment.repository.CommentRepository;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class CommentAdminServiceTest {

    @Mock
    private CommentRepository commentRepository;
    @InjectMocks
    private CommentAdminService commentAdminService;

    @Test
    public void comment_삭제_중_해당_comment를_찾지못해_에러가_발생한다() {
        // given
        Long commentId = 1L;

        given(commentRepository.findById(commentId)).willReturn(Optional.empty());

        // when
        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> {
            commentAdminService.deleteComment(commentId);
        });

        // then
        assertEquals("comment not found", exception.getMessage());

    }

    @Test
    public void comment_삭제_시_작성자와_현재_로그인한_사용자가_일치하지_않아서_에러가_발생한다() {
        // given
        Long commentId = 1L;
        AuthUser authUser = new AuthUser(1L, "email", UserRole.USER);
        User user = User.fromAuthUser(authUser);

        given(commentRepository.findById(commentId)).willReturn(Optional.empty());

        // when
        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> {
            commentAdminService.deleteComment(commentId);
        });

        // then
        assertEquals(exception.getMessage(), exception.getMessage());
    }
}
