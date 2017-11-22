/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.expression;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.mmm.code.api.expression.CodeConstant;
import net.sf.mmm.code.api.expression.CodeExpression;
import net.sf.mmm.code.api.expression.CodeFieldReference;
import net.sf.mmm.code.api.language.CodeLanguage;
import net.sf.mmm.code.api.type.CodeGenericType;
import net.sf.mmm.code.base.BaseContext;
import net.sf.mmm.code.base.member.BaseField;
import net.sf.mmm.code.base.type.BaseType;

/**
 * Implementation of {@link CodeFieldReference} with lazy evaluation.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class BaseFieldReferenceLazy extends BaseExpression implements CodeFieldReference {

  private static final Logger LOG = LoggerFactory.getLogger(BaseFieldReferenceLazy.class);

  private final BaseContext context;

  private final String typeName;

  private final String fieldName;

  private final Boolean qualfied;

  private BaseType type;

  private BaseField field;

  /**
   * The constructor.
   *
   * @param context the {@link BaseContext}.
   * @param typeName the fully qualified type name.
   * @param qualified - {@code Boolean#TRUE} for {@link BaseType#getQualifiedType() qualified},
   *        {@code Boolean#FALSE} for unqualified, and {@code null} to omit {@link #getType() type} in
   *        reference.
   * @param fieldName the field name.
   */
  public BaseFieldReferenceLazy(BaseContext context, String typeName, Boolean qualified, String fieldName) {

    super();
    this.context = context;
    this.typeName = typeName;
    this.qualfied = qualified;
    this.fieldName = fieldName;
  }

  @Override
  public CodeExpression getExpression() {

    return null;
  }

  @Override
  public CodeGenericType getType() {

    if (this.qualfied == null) {
      return null;
    } else {
      BaseType baseType = getTypeInternal();
      if (Boolean.TRUE.equals(this.qualfied) && (baseType != null)) {
        return baseType.getQualifiedType();
      } else {
        return baseType;
      }
    }
  }

  private BaseType getTypeInternal() {

    if (this.type == null) {
      this.type = this.context.getType(this.typeName);
    }
    return this.type;
  }

  @Override
  public CodeConstant evaluate() {

    if (getMember().getModifiers().isStatic()) {
      CodeExpression initializer = this.field.getInitializer();
      if (initializer != null) {
        return initializer.evaluate();
      }
    }
    return null;
  }

  @Override
  public BaseField getMember() {

    if (this.field == null) {
      BaseType baseType = getTypeInternal();
      if (baseType != null) {
        this.field = baseType.getFields().get(this.fieldName);
      }
      if (this.field != null) {
        LOG.debug("Failed to resolve field {}.{}", this.typeName, this.fieldName);
      }
    }
    return this.field;
  }

  @Override
  public CodeLanguage getLanguage() {

    return this.context.getLanguage();
  }

  @Override
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent, CodeLanguage language) throws IOException {

    CodeGenericType genericType = getType();
    if (genericType != null) {
      genericType.writeReference(sink, false);
      sink.append('.');
    }
    sink.append(this.fieldName);
  }
}
