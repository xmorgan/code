/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.statement;

/**
 * {@link CodeBlock} for a {@code do while} statement.<br>
 * <pre>
 * <code>do {
 *   {@link #getStatements() ...}
 * } while ({@link #getCondition() condition});</code> </pre>
 *
 * @author hohwille
 * @since 1.0.0
 */
public interface CodeDoWhile extends CodeBlockWithCondition {

}
