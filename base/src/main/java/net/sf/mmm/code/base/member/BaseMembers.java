/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.member;

import java.io.IOException;
import java.util.function.Consumer;

import net.sf.mmm.code.api.copy.CodeCopyMapper;
import net.sf.mmm.code.api.copy.CodeNodeItemCopyable;
import net.sf.mmm.code.api.language.CodeLanguage;
import net.sf.mmm.code.api.member.CodeMember;
import net.sf.mmm.code.api.member.CodeMembers;
import net.sf.mmm.code.base.node.BaseNodeItemContainer;
import net.sf.mmm.code.base.type.BaseType;

/**
 * Base implementation of {@link CodeMembers}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @param <M> type of the contained {@link BaseMember}s.
 * @since 1.0.0
 */
public abstract class BaseMembers<M extends CodeMember> extends BaseNodeItemContainer<M> implements CodeMembers<M> {

  private final BaseType parent;

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   */
  public BaseMembers(BaseType parent) {

    super();
    this.parent = parent;
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link BaseMembers} to copy.
   * @param parent the {@link #getParent() parent}.
   * @param mapper the {@link CodeCopyMapper}.
   */
  public BaseMembers(BaseMembers<M> template, BaseType parent, CodeCopyMapper mapper) {

    super(template, mapper);
    this.parent = parent;
  }

  @Override
  public BaseType getParent() {

    return this.parent;
  }

  @Override
  protected void rename(M member, String oldName, String newName, Consumer<String> renamer) {

    super.rename(member, oldName, newName, renamer);
    this.parent.getProperties().renameMember(member, oldName, newName);
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  @Override
  public void add(M member) {

    M item = member;
    if (item.getParent() != this) {
      item = (M) ((CodeNodeItemCopyable) member).copy(this);
    }
    super.add(item);
  }

  @Override
  public BaseType getDeclaringType() {

    return getParent();
  }

  @Override
  public abstract CodeMembers<M> getSourceCodeObject();

  @Override
  public abstract BaseMembers<M> copy();

  @Override
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent, CodeLanguage language) throws IOException {

    for (M declaredMember : getDeclared()) {
      sink.append(newline);
      declaredMember.write(sink, newline, defaultIndent, currentIndent, language);
      sink.append(newline);
    }
  }
}
