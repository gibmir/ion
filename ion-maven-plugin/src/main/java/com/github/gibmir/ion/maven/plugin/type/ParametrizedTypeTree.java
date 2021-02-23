package com.github.gibmir.ion.maven.plugin.type;

import com.github.gibmir.ion.api.schema.type.Types;
import com.github.gibmir.ion.maven.plugin.IonPluginMojo;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class ParametrizedTypeTree {
  private final TypeNode root;
  private final String typeString;


  public static ParametrizedTypeTree from(String typeString) {
    char[] typeStringSymbols = typeString.toCharArray();
    Stack<TypeNode> stack = new Stack<>();
    TypeNode root = new TypeNode(null);
    stack.push(root);
    for (char symbol : typeStringSymbols) {
      if (symbol == '<') {
        TypeNode current = stack.peek();
        TypeNode child = new TypeNode(current);
        current.children.add(child);
        stack.push(child);
      } else if (symbol == ',') {
        TypeNode current = stack.pop();
        TypeNode child = new TypeNode(current.parent);
        current.parent.children.add(child);
        stack.push(child);
      } else if (symbol == '>') {
        TypeNode current = stack.peek();
        String parentValue = current.parent.value.toString();
        //cleanup stack till parent
        while (!current.value.toString().equals(parentValue)) {
          current = stack.pop();
        }
        stack.push(current);
      } else {
        TypeNode current = stack.peek();
        current.value.append(symbol);
      }
    }
    return new ParametrizedTypeTree(root, typeString);
  }

  private ParametrizedTypeTree(TypeNode root, String typeString) {
    this.root = root;
    this.typeString = typeString;
  }

  public Stack<String> buildTypeLoadingStack() {
    Stack<String> result = new Stack<>();
    if (root != null) {
      Stack<TypeNode> stack = new Stack<>();
      stack.push(root);
      while (!stack.isEmpty()) {
        TypeNode top = stack.pop();
        result.push(top.value.toString());
        if (!top.children.isEmpty()) {
          for (TypeNode child : top.children) {
            stack.push(child);
          }
        }
      }
    }
    return result;
  }

  public ParameterizedTypeName buildTypeName() {

    Stack<TypeNode> traverseStack = new Stack<>();
    Stack<TypeNode> stack = new Stack<>();
    traverseStack.push(root);
    stack.push(root);
    while (!traverseStack.isEmpty()) {
      TypeNode current = traverseStack.pop();
      if (!current.children.isEmpty()) {
        for (TypeNode child : current.children) {
          traverseStack.push(child);
          stack.push(child);
        }
      }
    }

    HashMap<TypeNode, ParameterizedTypeName> nodeType = new HashMap<>();
    while (!stack.isEmpty()) {
      TypeNode typeNode = stack.pop();
      ClassName typeClassName = resolveClassName(typeNode.value.toString());
      TypeName[] parametrization = new TypeName[typeNode.children.size()];
      for (int i = 0; i < typeNode.children.size(); i++) {
        ParameterizedTypeName parameterizedTypeName = nodeType.get(typeNode.children.get(i));
        if (parameterizedTypeName != null) {
          parametrization[i] = parameterizedTypeName;
        } else {
          parametrization[i] = resolveClassName(typeNode.children.get(i).value.toString());
        }
      }
      if (Types.from(typeNode.value.toString()).isParametrized()) {
        ParameterizedTypeName parameterizedTypeName = ParameterizedTypeName.get(typeClassName, parametrization);
        nodeType.put(typeNode, parameterizedTypeName);
      }
    }
    return nodeType.get(root);
  }

  private ClassName resolveClassName(String currentTypeName) {
    Types currentType = Types.from(currentTypeName);
    switch (currentType) {
      case BOOLEAN:
      case STRING:
      case NUMBER:
      case LIST:
      case MAP:
        return ClassName.get(currentType.resolve());
      case CUSTOM:
      default:
        return ClassName.bestGuess(IonPluginMojo.asClassName(currentTypeName));
    }
  }

  public static class TypeNode {
    private final List<TypeNode> children = new ArrayList<>();
    private final TypeNode parent;
    private final StringBuilder value = new StringBuilder();

    public TypeNode(TypeNode parent) {
      this.parent = parent;
    }
  }

  @Override
  public String toString() {
    return "ParametrizedTypeTree{" +
      "typeString='" + typeString + '\'' +
      '}';
  }
}
