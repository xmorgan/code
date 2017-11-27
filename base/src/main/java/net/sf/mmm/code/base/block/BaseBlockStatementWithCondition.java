/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.block;

import java.util.List;

import net.sf.mmm.code.api.block.CodeBlockStatement;
import net.sf.mmm.code.api.block.CodeBlockWithCondition;
import net.sf.mmm.code.api.copy.CodeCopyMapper;
import net.sf.mmm.code.api.expression.CodeCondition;
import net.sf.mmm.code.api.statement.CodeStatement;

/**
 * Base implementation of {@link CodeBlockStatement}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract class BaseBlockStatementWithCondition extends BaseBlockStatement implements CodeBlockWithCondition {

  private CodeCondition condition;

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param condition the {@link #getCondition() condition}.
   * @param statements the {@link #getStatements() statements}.
   */
  public BaseBlockStatementWithCondition(BaseBlock parent, CodeCondition condition, CodeStatement... statements) {

    super(parent, statements);
  }

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param condition the {@link #getCondition() condition}.
   * @param statements the {@link #getStatements() statements}.
   */
  public BaseBlockStatementWithCondition(BaseBlock parent, CodeCondition condition, List<CodeStatement> statements) {

    super(parent, statements);
  }

  /**
   * The copy-constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param template the {@link BaseBlockStatementWithCondition} to copy.
   * @param mapper the {@link CodeCopyMapper}.
   */
  public BaseBlockStatementWithCondition(BaseBlockStatementWithCondition template, BaseBlock parent, CodeCopyMapper mapper) {

    super(template, parent, mapper);
  }

  @Override
  public CodeCondition getCondition() {

    return this.condition;
  }

}
