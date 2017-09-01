/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.doc;

import java.util.ArrayList;
import java.util.List;

import net.sf.mmm.code.api.doc.CodeDocMethodLink;

/**
 * Implementation of {@link CodeDocMethodLink}.
 *
 * @author hohwille
 * @since 1.0.0
 */
public class CodeDocMethodLinkImpl implements CodeDocMethodLink {

  private final String name;

  private final List<String> parameters;

  /**
   * The constructor.
   *
   * @param name the {@link #getName() method name}.
   * @param parameters the {@link #getParameters() parameter type names}.
   */
  public CodeDocMethodLinkImpl(String name, List<String> parameters) {

    super();
    this.name = name;
    this.parameters = parameters;
  }

  @Override
  public String getName() {

    return this.name;
  }

  @Override
  public List<String> getParameters() {

    return this.parameters;
  }

  /**
   * @param anchor the {@link CodeDocLinkImpl#getAnchor() anchor} that may link a method.
   * @return the {@link CodeDocMethodLinkImpl} or {@code null} if not a method link.
   */
  public static CodeDocMethodLink of(String anchor) {

    if (anchor == null) {
      return null;
    }
    if (!anchor.endsWith(")")) {
      return null;
    }
    int signatureStart = anchor.indexOf('(');
    if (signatureStart <= 0) {
      return null;
    }
    String method = anchor.substring(0, signatureStart);
    List<String> parameters = new ArrayList<>();
    int typeStart = signatureStart + 1;
    int typeMax = anchor.length() - 1;
    if (typeMax - typeStart > 1) {
      int typeEnd;
      do {
        typeEnd = anchor.indexOf(',', typeStart);
        if (typeEnd < 0) {
          typeEnd = typeMax;
        }
        String arg = anchor.substring(typeStart, typeEnd).trim();
        parameters.add(arg);
      } while (typeEnd < typeMax);
    }
    return new CodeDocMethodLinkImpl(method, parameters);
  }

}
