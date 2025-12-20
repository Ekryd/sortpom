package sortpom.wrapper.content;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

class PhaseTest {
  @Test
  void compareStandardPhasesAndOtherPhases() {
    Phase[] arr = {
      Phase.getPhase("a"),
      Phase.getPhase("teST"),
      Phase.getPhase("site"),
      Phase.getPhase("c"),
      Phase.getPhase("B"),
      Phase.getPhase("01"),
      Phase.getPhase("clea"),
      Phase.getPhase("")
    };
    List<Phase> list = Arrays.asList(arr);
    Collections.shuffle(list);
    list.sort(Phase::compareTo);
    assertThat(arr.length, is(8));
    var i = 0;
    assertThat(arr[i++].text(), is("test"));
    assertThat(arr[i++].text(), is("site"));
    assertThat(arr[i++].text(), is(""));
    assertThat(arr[i++].text(), is("01"));
    assertThat(arr[i++].text(), is("a"));
    assertThat(arr[i++].text(), is("b"));
    assertThat(arr[i++].text(), is("c"));
    assertThat(arr[i].text(), is("clea"));
  }

  @Test
  void toStringForPhase() {
    var test = Phase.getPhase("teST");
    var clea = Phase.getPhase("clea");
    assertThat(test.toString(), is("TEST"));
    assertThat(clea.toString(), is("NonStandardPhase{text='clea'}"));
  }
}
