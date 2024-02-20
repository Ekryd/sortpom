package sortpom.wrapper.content;

import org.dom4j.Element;
import org.dom4j.Node;
import sortpom.content.NewlineText;

/** A wrapper that lets is element be unsorted */
public class UnsortedWrapper<T extends Node> implements Wrapper<T> {
  public static final UnsortedWrapper<Node> KEEP_NEWLINE_INSTANCE =
      new UnsortedWrapper<>(new NewlineText());

  /** The wrapped dom content. */
  private final T content;

  /**
   * Instantiates a new unsorted wrapper.
   *
   * @param content the content
   */
  public UnsortedWrapper(T content) {
    this.content = content;
  }

  /**
   * @see Wrapper#getContent()
   */
  @Override
  public T getContent() {
    return content;
  }

  /**
   * @see Wrapper#isBefore(Wrapper)
   */
  @Override
  public boolean isBefore(Wrapper<? extends Node> wrapper) {
    throw new UnsupportedOperationException("Cannot be sorted");
  }

  /**
   * @see Wrapper#isContentElement()
   */
  @Override
  public boolean isContentElement() {
    return content instanceof Element;
  }

  /**
   * @see Wrapper#isSortable()
   */
  @Override
  public boolean isSortable() {
    return false;
  }

  @Override
  public String toString() {
    return "UnsortedWrapper{"
        + "content="
        + (content == null ? "null" : content.toString().replaceAll(".+@[^ ]+ ", ""))
        + '}';
  }
}
