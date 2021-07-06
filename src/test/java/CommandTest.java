import chess.domain.command.Command;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class CommandTest {

    @ParameterizedTest
    @DisplayName("필드 변수로 갖고 있는 문자를 입력받으면 enum 클래스를 정상 반환해야 함")
    @ValueSource(strings = {"start", "status", "end"})
    void inputVarStringThenReturnCommandClass(String input) {
        Assertions.assertThat(Command.of(input)).isInstanceOf(Command.class);
    }

    @ParameterizedTest
    @DisplayName("필드 변수에 없는 문자를 입력받으면 예외가 발생해야 함")
    @ValueSource(strings = {"시작", "끝", "start~", "end!", "점수 보여줘", "@score"})
    void inputNotVarStringThenThrowException(String input) {
        Assertions.assertThatThrownBy(() -> Command.of(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("잘못된 명령어입니다.");
    }
}
