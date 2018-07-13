package com.vanya.spi;

import org.apache.commons.lang3.StringUtils;
import org.mapstruct.ap.spi.DefaultAccessorNamingStrategy;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

import java.beans.Introspector;

public class BuilderSupportingAccessorNamingStrategy extends DefaultAccessorNamingStrategy {

    private static final String BUILDER_SETTER_PREFIX_WITH = "with";

    @Override
    public boolean isSetterMethod(final ExecutableElement method) {
        return isSetterForBuilder(method) || super.isSetterMethod(method);
    }

    @Override
    public String getPropertyName(final ExecutableElement getterOrSetterMethod) {
        if (isSetterForBuilder(getterOrSetterMethod)) {
            return Introspector.decapitalize(getBuilderPropertyName(getterOrSetterMethod));
        } else {
            return super.getPropertyName(getterOrSetterMethod);
        }
    }

    private String getBuilderPropertyName(final ExecutableElement method) {
        final String methodName = method.getSimpleName().toString();

        if (isCommonSetterForBuilder(methodName)) {
            return StringUtils.substring(methodName, BUILDER_SETTER_PREFIX_WITH.length());
        } else {
            return methodName;
        }
    }

    private boolean isSetterForBuilder(final ExecutableElement method) {
        final String methodName = method.getSimpleName().toString();

        return isCommonSetterForBuilder(methodName) || isSetterForBuilderWithoutPrefix(method);
    }

    private boolean isCommonSetterForBuilder(final String methodName) {
        return StringUtils.startsWith(methodName, BUILDER_SETTER_PREFIX_WITH)
                && StringUtils.length(methodName) > BUILDER_SETTER_PREFIX_WITH.length();
    }

    private boolean isSetterForBuilderWithoutPrefix(final ExecutableElement method) {
        return isChainingMethod(method)
                && isMethodNameTheSameWithProperty(method);
    }

    private boolean isChainingMethod(final ExecutableElement method) {
        final TypeMirror classType = method.getEnclosingElement().asType();
        final TypeMirror returnType = method.getReturnType();

        return classType == returnType;
    }

    private boolean isMethodNameTheSameWithProperty(final ExecutableElement method) {
        final Name methodName = method.getSimpleName();

        return method.getParameters().stream()
                     .map(VariableElement::getSimpleName)
                     .filter(methodName::equals)
                     .findFirst()
                     .map(propertyName -> doesBuilderContainProperty(method, propertyName))
                     .orElse(false);
    }

    private Boolean doesBuilderContainProperty(final ExecutableElement method, final Name propertyName) {
        final Element builderClass = method.getEnclosingElement();

        return builderClass.getEnclosedElements().stream()
                           .filter(VariableElement.class::isInstance)
                           .map(Element::getSimpleName)
                           .anyMatch(propertyName::equals);
    }
}
