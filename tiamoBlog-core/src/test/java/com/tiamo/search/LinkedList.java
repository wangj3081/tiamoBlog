package com.tiamo.search;

import com.alibaba.fastjson.JSONObject;
import com.tiamo.search.dto.response.BlogDto;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * @Auther: wangjian
 * @Date: 2019-04-19 14:38:34
 */
public class LinkedList<T> implements Serializable{

    private Node<T> headNode; // 头节点

    private Node<T> lastNode; // 尾节点

    private int size; // 集合大小

    public LinkedList() {

    }

    public int size() {
        return size;
    }

    /**
     * 添加节点
     * @param val
     * @return
     */
    public boolean add(T val) {
        insertVal(val);
        return true;
    }
    /**
     *  往节点中设置内容
     * @param val
     */
    private void insertVal(T val) {
        Node<T> l = this.lastNode;
        Node<T> newNode = new Node<>(this.lastNode, null, val); // 新的末尾节点
        this.lastNode = newNode; // 将新插入的节点设置为末尾节点
        if (l == null) { // 如果插入数据前末尾节点不存在，
            this.headNode = newNode;
        } else { // 存在则将新节点移动到其末尾之中
            l.next = newNode;
        }
        size++;
    }

    /**
     * 移除节点
     * @param val
     * @return
     */
    public boolean remove(T val) {
        if (val == null) {
            for (Node<T> node = this.headNode; node != null; node= node.next) {
                if (node.val == null) { // 获取到对应值
                    unlink(node);
                    return true;
                }
            }
        } else {
            for (Node<T> node = this.headNode; node != null; node = node.next) {
                if (node.val.equals(val)) {
                    unlink(node);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 移除节点与链表中的关系
     * @param node
     */
    private void unlink(Node<T> node) {
        Node<T> next = node.next;
        Node<T> pre = node.pre;
        node.val = null; // 将当前节点的值置为空，用于 GC 回收
        if (pre == null) { // 被移除的节点为头部节点
            headNode = next;
        } else {
            pre.next = next; // 将被移除的下一节点连接到被移除节点的上一节点
            node.pre = null; // 将被移除的节点的前节点移除
        }

        if (next == null) { // 被移除的节点为尾部节点
            lastNode = pre;
        } else {
            next.pre = pre;
            node.next = null; // 被移除节点脱离链表
        }
        size--;
    }


    /**
     * 遍历当前链表
     * @return
     */
    public void forEach(Consumer<? super T> action) {
        Objects.requireNonNull(action);
        for (Node<T> node = this.headNode; node != null; node = node.next) {
            action.accept(node.val);
        }
    }

    // 节点
    private static class Node<T>{
        private Node<T> pre;
        private Node<T> next;
        private T val;

        Node(Node<T> pre, Node<T> next, T val) {
            this.pre = pre;
            this.next = next;
            this.val = val;
        }
    }

}
class TestList{
    public static void main(String[] args) {
        LinkedList<String> list = new LinkedList<>();
        list.add("aaa");
        list.add("bbb");
        list.forEach(entity-> {
            System.out.println(entity);
        });
        list.remove("aaa");
        list.remove("bbb");
        list.forEach(entity-> {
            System.out.println(entity);
        });

        LinkedList<BlogDto> list2 = new LinkedList<>();
        BlogDto blogDto = new BlogDto();
        blogDto.setAuthor("caoz");
        list2.add(blogDto);
        BlogDto blogDto1 = new BlogDto();
        blogDto1.setAuthor("Fenng");
        list2.add(blogDto1);
        System.out.println(list2.size());
        list2.forEach(entity-> {
            System.out.println(JSONObject.toJSONString(entity));
        });
        list2.remove(blogDto);
        System.out.println(list2.size());
        list2.forEach(entity-> {
            System.out.println(JSONObject.toJSONString(entity));
        });

    }
}