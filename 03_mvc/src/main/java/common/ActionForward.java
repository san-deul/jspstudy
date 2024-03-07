package common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
// @Data -> getter,setter들어있음

public class ActionForward {
  private String view; //어디로
  private boolean isRedirect; //어떻게갈지
}
