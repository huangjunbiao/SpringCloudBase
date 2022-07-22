package com.huang.cloudbase.learn.code;

/**
 * 给你两个 非空 的链表，表示两个非负的整数。它们每位数字都是按照 逆序 的方式存储的，并且每个节点只能存储 一位 数字。
 *
 * 请你将两个数相加，并以相同形式返回一个表示和的链表。
 *
 * 你可以假设除了数字 0 之外，这两个数都不会以 0 开头。
 *
 * @author huangjunbiao_cdv
 */
public class Code002 {

    public static class ListNode {
        int val;
        ListNode next;

        public ListNode(int val) {
            this.val = val;
        }

        public ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }

    public static void main(String[] args) {
        ListNode dummy = new ListNode(0);
        ListNode cur = dummy;
        cur.next = new ListNode(121 % 10);
        cur = cur.next;
        System.out.println(cur);
        System.out.println(dummy);
    }

    public static ListNode add(ListNode listNode1, ListNode listNode2) {
        ListNode dummy = new ListNode(0);
        int carry = 0;
        ListNode cur = dummy;
        while (listNode1 != null || listNode2 != null || carry != 0) {
            int s = (listNode1 == null ? 0 : listNode1.val) + (listNode2 == null ? 0 : listNode2.val) + carry;
            carry = s / 10;
            cur.next = new ListNode(s % 10);
            cur = cur.next;
            listNode1 = listNode1 == null ? null : listNode1.next;
            listNode2 = listNode2 == null ? null : listNode2.next;
        }
        return dummy.next;
    }
}
