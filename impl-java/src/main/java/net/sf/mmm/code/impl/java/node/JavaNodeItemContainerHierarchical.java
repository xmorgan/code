/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.node;

import java.util.List;

import net.sf.mmm.code.api.item.CodeItem;
import net.sf.mmm.code.api.node.CodeNodeItemContainerHierarchical;
import net.sf.mmm.code.impl.java.item.JavaItem;

/**
 * Implementation of {@link CodeNodeItemContainerHierarchical} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @param <J> the type of the contained {@link JavaItem}.
 * @param <I> the type of the contained {@link CodeItem}s.
 * @since 1.0.0
 */
public abstract class JavaNodeItemContainerHierarchical<I extends CodeItem, J extends I> extends JavaNodeItemContainer<I, J>
    implements CodeNodeItemContainerHierarchical<I> {

  /**
   * The constructor.
   */
  protected JavaNodeItemContainerHierarchical() {

    super();
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaNodeItemContainerHierarchical} to copy.
   */
  public JavaNodeItemContainerHierarchical(JavaNodeItemContainerHierarchical<I, J> template) {

    super(template);
  }

  @Override
  public final List<? extends J> getDeclared() {

    initialize();
    return getList();
  }

}