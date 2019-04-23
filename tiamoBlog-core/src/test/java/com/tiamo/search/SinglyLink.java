package com.tiamo.search;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * 单向链表
 * @Auther: wangjian
 * @Date: 2019-04-23 15:57:29
 */
public class SinglyLink<T> implements Serializable {

    private Node<T> root; // 根节点

    public SinglyLink() {

    }

    public void add(T val) {
        Node<T> node = new Node<T>(val);
        if (this.root == null) {
            this.root = node;
        } else {
            this.root.addNode(node);
        }
    }

    public boolean contains(T val) {
        if (this.root == null) {
            return false;
        } else {
            return this.root.searchNode(val);
        }
    }

    public void delete(T val) {
        if (this.contains(val)) {
            if (this.root.val.equals(val)) {
                this.root = this.root.next;
            } else {
                this.root.next.deleteNode(root, val);
            }
        }
    }

    public void forEatch(Consumer<? super  T> consumer) {
        Objects.requireNonNull(consumer);
        for (Node<T> node = this.root; node != null; node = node.next) {
            consumer.accept(node.val);
        }
    }


    class Node<T> {
        private T val;
        private Node<T> next;
         public Node(T val) { // Node 对象
            this.val = val;
        }

        public void addNode(Node<T> next){ // 增加一个节点
             if (this.next == null) {
                 this.next = next;
             } else {
                 this.next.addNode(next); // 向下添加一个节点
             }
        }

        /**
         * 删除节点
         * @param preNode
         * @param val
         */
        public void deleteNode(Node<T> preNode, T val) {
             if (this.val.equals(val)) {
                 preNode.next = this.next;
             } else {
                 if (this.next != null) {
                     this.next.deleteNode(preNode, val);
                 }
             }
        }

        /**
         * 根据内容查找节点
         * @param val
         * @return
         */
        public boolean searchNode(T val) {
            if (this.val.equals(val)) {
                return true;
            } else {
                if (this.next != null) {
                  return this.next.searchNode(val);
                }
            }
            return false;
        }
    }
}

class TestMain{
    public static void main(String[] args) {
        SinglyLink<String> link = new SinglyLink<>();
        link.add("A");
        link.add("B");
        link.forEatch(entity-> {
            System.out.println(entity);
        });
        link.delete("B");
        link.forEatch(entity-> {
            System.out.println(entity);
        });
    }
}