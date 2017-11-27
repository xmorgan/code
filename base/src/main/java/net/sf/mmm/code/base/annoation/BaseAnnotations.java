/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.annoation;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sf.mmm.code.api.annotation.CodeAnnotation;
import net.sf.mmm.code.api.annotation.CodeAnnotations;
import net.sf.mmm.code.api.copy.CodeCopyMapper;
import net.sf.mmm.code.api.copy.CodeCopyMapperNone;
import net.sf.mmm.code.api.element.CodeElement;
import net.sf.mmm.code.api.language.CodeLanguage;
import net.sf.mmm.code.api.member.CodeMethod;
import net.sf.mmm.code.api.merge.CodeMergeStrategy;
import net.sf.mmm.code.api.type.CodeType;
import net.sf.mmm.code.base.element.BaseElementImpl;
import net.sf.mmm.code.base.node.BaseNodeItemContainerHierarchical;
import net.sf.mmm.code.base.source.BaseSource;
import net.sf.mmm.code.base.type.BaseType;
import net.sf.mmm.code.base.type.InternalSuperTypeIterator;
import net.sf.mmm.util.collection.base.AbstractIterator;

/**
 * Base implementation of {@link CodeAnnotations}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class BaseAnnotations extends BaseNodeItemContainerHierarchical<CodeAnnotation> implements CodeAnnotations {

  private final BaseElementImpl parent;

  private CodeAnnotations sourceCodeObject;

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   */
  public BaseAnnotations(BaseElementImpl parent) {

    super();
    this.parent = parent;
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link BaseAnnotations} to copy.
   * @param parent the {@link #getParent() parent}.
   * @param mapper the {@link CodeCopyMapper}.
   */
  public BaseAnnotations(BaseAnnotations template, BaseElementImpl parent, CodeCopyMapper mapper) {

    super(template, mapper);
    this.parent = parent;
  }

  @Override
  public BaseElementImpl getParent() {

    return this.parent;
  }

  @Override
  protected boolean isSystemImmutable() {

    boolean systemImmutable = super.isSystemImmutable();
    if (!systemImmutable && (this.parent != null)) {
      systemImmutable = isSystemImmutable(this.parent);
    }
    return systemImmutable;
  }

  @Override
  protected void doInitialize() {

    super.doInitialize();
    if (this.parent == null) {
      return;
    }
    doInitByteCode();
    doInitSourceCode();
  }

  private void doInitByteCode() {

    Object reflectiveObject = this.parent.getReflectiveObject();
    if (reflectiveObject instanceof AnnotatedElement) {
      Annotation[] annotations = ((AnnotatedElement) reflectiveObject).getAnnotations();
      if (annotations.length == 0) {
        return;
      }
      BaseSource source = getSource();
      for (Annotation annotation : annotations) {
        addInternal(new BaseAnnotation(source, annotation));
      }
    }
  }

  private void doInitSourceCode() {

    CodeAnnotations sourceAnnotations = getSourceCodeObject();
    if (sourceAnnotations == null) {
      return;
    }
    List<? extends CodeAnnotation> sourceList = sourceAnnotations.getDeclared();
    if (sourceList.isEmpty()) {
      return;
    }
    Set<String> annotationTypes = createAnnotationTypeNameSet();
    for (CodeAnnotation sourceAnnotation : sourceList) {
      String key = sourceAnnotation.getType().getQualifiedName();
      if (!annotationTypes.contains(key)) {
        addInternal(sourceAnnotation);
      }
    }
  }

  private Set<String> createAnnotationTypeNameSet() {

    Set<String> annotationTypes = null;
    List<? extends CodeAnnotation> declared = getDeclared();
    if (declared.isEmpty()) {
      annotationTypes = Collections.emptySet();
    } else {
      annotationTypes = new HashSet<>();
      for (CodeAnnotation annotation : declared) {
        annotationTypes.add(annotation.getType().getQualifiedName());
      }
    }
    return annotationTypes;
  }

  @Override
  public CodeAnnotation getDeclared(CodeType type) {

    for (CodeAnnotation annotation : getDeclared()) {
      if (annotation.getType().asType().equals(type)) {
        return annotation;
      }
    }
    return null;
  }

  @Override
  public CodeAnnotation add(CodeType type) {

    verifyMutalbe();
    CodeAnnotation annotation = createAnnoation(type);
    add(annotation);
    return annotation;
  }

  /**
   * @param type the {@link BaseAnnotation#getType() type} of the {@link BaseAnnotation} to create.
   * @return the new {@link BaseAnnotation} instance.
   */
  protected BaseAnnotation createAnnoation(CodeType type) {

    return new BaseAnnotation(getSource(), type);
  }

  @Override
  public void add(CodeAnnotation item) {

    super.add(item);
  }

  @Override
  public CodeAnnotation getDeclaredOrAdd(CodeType type) {

    CodeAnnotation annotation = getDeclared(type);
    if (annotation == null) {
      annotation = add(type);
    }
    return annotation;
  }

  @Override
  public Iterable<? extends CodeAnnotation> getAll() {

    if (this.parent instanceof BaseType) {
      return () -> new TypeAnnotationIterator((BaseType) this.parent);
    } else if (this.parent instanceof CodeMethod) {
      return () -> new MethodAnnotationIterator((CodeMethod) this.parent);
    } else {
      return getDeclared();
    }
  }

  @Override
  public CodeAnnotations getSourceCodeObject() {

    if ((this.sourceCodeObject == null) && !isInitialized() && (this.parent != null)) {
      CodeElement sourceElement = this.parent.getSourceCodeObject();
      if (sourceElement != null) {
        this.sourceCodeObject = sourceElement.getAnnotations();
      }
    }
    return this.sourceCodeObject;
  }

  @Override
  public CodeAnnotations merge(CodeAnnotations other, CodeMergeStrategy strategy) {

    if (strategy == CodeMergeStrategy.OVERRIDE) {
      clear();
      getList().addAll(other.getDeclared());
    } else if (strategy != CodeMergeStrategy.KEEP) {
      Set<String> annotationTypes = createAnnotationTypeNameSet();
      for (CodeAnnotation annotation : other.getDeclared()) {
        String key = annotation.getType().getQualifiedName();
        if (!annotationTypes.contains(key)) {
          add(annotation);
        }
      }
    }
    return this;
  }

  @Override
  public BaseAnnotations copy() {

    return copy(this.parent);
  }

  @Override
  public BaseAnnotations copy(CodeElement newParent) {

    return copy(newParent, CodeCopyMapperNone.INSTANCE);
  }

  @Override
  public BaseAnnotations copy(CodeElement newParent, CodeCopyMapper mapper) {

    return new BaseAnnotations(this, (BaseElementImpl) newParent, mapper);
  }

  @Override
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent, CodeLanguage language) throws IOException {

    String prefix = "";
    for (CodeAnnotation annotation : getDeclared()) {
      if (defaultIndent == null) {
        sink.append(prefix);
        prefix = " ";
      } else {
        sink.append(newline);
        sink.append(currentIndent);
      }
      annotation.write(sink, newline, defaultIndent, currentIndent, language);
    }
    sink.append(prefix);
  }

  private abstract class AnnotationIterator extends AbstractIterator<CodeAnnotation> {

    private final Set<CodeType> iteratedAnnotations;

    private Iterator<CodeAnnotation> currentIterator;

    protected AnnotationIterator() {

      super();
      this.iteratedAnnotations = new HashSet<>();
      this.currentIterator = getList().iterator();
    }

    @Override
    protected CodeAnnotation findNext() {

      while (this.currentIterator.hasNext()) {
        CodeAnnotation annotation = this.currentIterator.next();
        CodeType annotationType = annotation.getType().asType();
        boolean added = this.iteratedAnnotations.add(annotationType);
        if (added) {
          return annotation;
        }
      }
      this.currentIterator = nextParent();
      if (this.currentIterator == null) {
        return null;
      }
      return findNext();
    }

    protected abstract Iterator<CodeAnnotation> nextParent();
  }

  private class MethodAnnotationIterator extends AnnotationIterator {

    private CodeMethod method;

    private MethodAnnotationIterator(CodeMethod method) {

      super();
      this.method = method;
      findFirst();
    }

    @Override
    protected Iterator<CodeAnnotation> nextParent() {

      this.method = this.method.getParentMethod();
      if (this.method == null) {
        return null;
      }
      return this.method.getAnnotations().iterator();
    }
  }

  private class TypeAnnotationIterator extends AnnotationIterator {

    private InternalSuperTypeIterator iterator;

    private TypeAnnotationIterator(BaseType type) {

      super();
      this.iterator = new InternalSuperTypeIterator(type);
      findFirst();
    }

    @Override
    protected Iterator<CodeAnnotation> nextParent() {

      if (this.iterator.hasNext()) {
        this.iterator = this.iterator.next();
      } else {
        return null;
      }
      if (this.iterator == null) {
        return null;
      }
      return this.iterator.getType().asType().getAnnotations().iterator();
    }
  }

}
