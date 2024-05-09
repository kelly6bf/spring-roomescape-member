package roomescape.member.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import roomescape.member.dto.SaveMemberRequest;
import roomescape.member.model.Member;
import roomescape.member.model.MemberRole;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql(value = {"/schema.sql", "/data.sql"}, executionPhase = BEFORE_TEST_METHOD)
class MemberServiceTest {
    
    @Autowired
    private MemberService memberService;
    
    @DisplayName("회원 정보를 저장한다.")
    @Test
    void saveUserTest() {
        // Given
        final MemberRole role = MemberRole.USER;
        final String password = "kellyPw1234";
        final String email = "kelly6bf@gmail.com";
        final String name = "kelly";
        final SaveMemberRequest saveMemberRequest = new SaveMemberRequest(role, email, name, password);

        // When
        final Member savedMember = memberService.saveUser(saveMemberRequest);

        // Then
        assertAll(
                () -> assertThat(savedMember.getId()).isEqualTo(3L),
                () -> assertThat(savedMember.getEmail().value()).isEqualTo(email),
                () -> assertThat(savedMember.getEmail().value()).isEqualTo(email),
                () -> assertThat(savedMember.getRole()).isEqualTo(role),
                () -> assertThat(savedMember.getName().value()).isEqualTo(name)
        );
    }

    // TODO : 비밀번호 검증 예외 및 이메일 중복 검증 예외 테스트 추가
}
