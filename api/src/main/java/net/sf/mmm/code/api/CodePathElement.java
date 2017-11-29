/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api;

import net.sf.mmm.code.api.element.CodeElement;
import net.sf.mmm.code.api.item.CodeItemWithQualifiedNameAndParentPackage;
import net.sf.mmm.code.api.node.CodeContainer;

/**
 * Abstract top-level interface for any item of code as defined by this API. It reflects code structure.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract interface CodePathElement extends CodeElement, CodeItemWithQualifiedNameAndParentPackage {

  @Override
  CodeContainer getParent();

  /**
   * @return {@code true} if this is a {@link CodeFile}, {@code false} otherwise (if a {@link CodePackage}).
   */
  boolean isFile();

}
