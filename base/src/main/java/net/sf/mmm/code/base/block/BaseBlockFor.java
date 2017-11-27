/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.block;

import java.io.IOException;
import java.util.List;

import net.sf.mmm.code.api.block.CodeBlock;
import net.sf.mmm.code.api.block.CodeBlockFor;
import net.sf.mmm.code.api.copy.CodeCopyMapper;
import net.sf.mmm.code.api.copy.CodeCopyMapperNone;
import net.sf.mmm.code.api.expression.CodeForExpression;
import net.sf.mmm.code.api.statement.CodeStatement;

/**
 * Base implementation of {@link CodeBlockFor}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class BaseBlockFor extends BaseBlockStatement implements CodeBlockFor {

  private CodeForExpression expression;

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param expression the {@link #getExpression() expression}.
   * @param statements the {@link #getStatements() statements}.
   */
  public BaseBlockFor(BaseBlock parent, CodeForExpression expression, CodeStatement... statements) {

    super(parent, statements);
    this.expression = expression;
  }

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param expression the {@link #getExpression() expression}.
   * @param statements the {@link #getStatements() statements}.
   */
  public BaseBlockFor(BaseBlock parent, CodeForExpression expression, List<CodeStatement> statements) {

    super(parent, statements);
    this.expression = expression;
  }

  /**
   * The copy-constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param template the {@link BaseBlockStatement} to copy.
   * @param mapper the {@link CodeCopyMapper}.
   */
  public BaseBlockFor(BaseBlockFor template, BaseBlock parent, CodeCopyMapper mapper) {

    super(template, parent, mapper);
    this.expression = template.expression;
  }

  @Override
  public CodeForExpression getExpression() {

    return this.expression;
  }

  @Override
  public BaseBlockFor copy() {

    return copy(getParent());
  }

  @Override
  public BaseBlockFor copy(CodeBlock newParent) {

    return copy(newParent, CodeCopyMapperNone.INSTANCE);
  }

  @Override
  public BaseBlockFor copy(CodeBlock newParent, CodeCopyMapper mapper) {

    return new BaseBlockFor(this, (BaseBlock) newParent, mapper);
  }

  @Override
  protected void writePrefix(Appendable sink, String newline, String defaultIndent, String currentIndent) throws IOException {

    sink.append("for (");
    this.expression.write(sink, newline, defaultIndent, currentIndent);
    sink.append(") ");
  }

}
