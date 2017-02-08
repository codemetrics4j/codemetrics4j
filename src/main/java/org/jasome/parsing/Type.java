package org.jasome.parsing;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

import java.util.Set;

public class Type extends TreeNode {
    private final ClassOrInterfaceDeclaration declaration;

    public Type(ClassOrInterfaceDeclaration declaration) {
        super(getClassNameFromDeclaration(declaration));
        this.declaration = declaration;
    }

    public ClassOrInterfaceDeclaration getSource() {
        return declaration;
    }

    private static String getClassNameFromDeclaration(ClassOrInterfaceDeclaration classDefinition) {
        String className = classDefinition.getNameAsString();

        if (classDefinition.getParentNode().isPresent()) {
            Node parentNode = classDefinition.getParentNode().get();
            if (parentNode instanceof ClassOrInterfaceDeclaration) {
                className = ((ClassOrInterfaceDeclaration) parentNode).getNameAsString() + "." +
                        classDefinition.getNameAsString();
            }
        }
        return className;
    }

    @SuppressWarnings("unchecked")
    public Set<Method> getMethods() {
        return (Set<Method>)(Set<?>)getChildren();
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.TYPE;
    }

    public void addMethod(Method method) {
        addChild(method);
    }

    public Package getParentPackage() {
        return (Package)getParent();
    }
}
