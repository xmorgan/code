/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.member;

import net.sf.mmm.code.api.item.CodeItem;
import net.sf.mmm.code.api.item.CodeItemWithDeclaringType;
import net.sf.mmm.code.api.node.CodeNodeItemContainer;
import net.sf.mmm.code.api.type.CodeType;

/**
 * {@link CodeItem} that groups all {@link CodeMember}s of a type.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @param <M> type of the contained {@link CodeMember}s.
 * @since 1.0.0
 */
public abstract interface CodeMembers<M extends CodeMember> extends CodeNodeItemContainer<M>, CodeItemWithDeclaringType {

  @Override
  CodeType getParent();

  @Override
  CodeMembers<M> copy();

  /**
   * @param member the member to add. If its {@link CodeMember#getParent() parent} does not point to this
   *        {@link CodeMembers}, it will be {@link CodeMember#copy() copied}.
   */
  void add(M member);

}
