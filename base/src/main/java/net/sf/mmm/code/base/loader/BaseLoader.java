/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.loader;

import net.sf.mmm.code.api.CodeLoader;
import net.sf.mmm.code.api.CodeName;
import net.sf.mmm.code.base.BaseContext;
import net.sf.mmm.code.base.BasePackage;
import net.sf.mmm.code.base.type.BaseType;

/**
 * {@link CodeLoader} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface BaseLoader extends CodeLoader {

  @Override
  BaseContext getContext();

  @Override
  default BasePackage getPackage(String qualifiedName) {

    return getPackage(getContext().parseName(qualifiedName));
  }

  @Override
  BasePackage getPackage(CodeName qualifiedName);

  @Override
  BaseType getType(String qualifiedName);

  @Override
  BaseType getType(CodeName qualifiedName);

}
