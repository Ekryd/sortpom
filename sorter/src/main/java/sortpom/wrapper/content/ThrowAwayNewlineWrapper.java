package sortpom.wrapper.content;

import org.dom4j.Node;
import org.dom4j.Text;

/**
 * A singleton wrapper that contains a single newline. The content will be thrown away.
 *
 * @author Bjorn
 */
public enum ThrowAwayNewlineWrapper implements Wrapper<Node> {
  THROW_AWAY_NEWLINE_INSTANCE;

  @Override
  public Text getContent() {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isBefore(Wrapper<? extends Node> wrapper) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isContentElement() {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isSortable() {
    throw new UnsupportedOperationException();
  }

  @Override
  public String toString() {
    return "SingleNewlineInTextWrapper";
  }
}
