package io.filipvde.customspringbootstarter.tomcat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ShutdownHealthIndicatorTest {

  @Mock
  private GracefulShutdownCustomizer customizer;

  @InjectMocks
  private ShutdownHealthIndicator healthIndicator;

  @Test
  public void testUp() {
    when(customizer.isShuttingDown()).thenReturn(false);
    Health result = healthIndicator.health();
    assertThat(result.getStatus()).isEqualTo(Status.UP);
  }

  @Test
  public void testDown() {
    when(customizer.isShuttingDown()).thenReturn(true);
    Health result = healthIndicator.health();
    assertThat(result.getStatus()).isEqualTo(Status.DOWN);
  }

}
