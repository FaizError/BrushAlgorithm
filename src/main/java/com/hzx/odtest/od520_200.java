package com.hzx.odtest;

import java.util.HashMap;
import java.util.Map;

public class od520_200 {

    /**
     * 题目描述：给定二叉树前序遍历和中序遍历的字符串，以及需要删除的节点，要求先解析这两个字符串获取对应二叉树，再做删除指定节点操作，然后输出该二叉树的后序遍历结果。删除节点规则
     * 1、指定节点是根节点，则不做删除操作；
     * 2、指定节点是叶子节点，则直接删除；
     * 3、指定节点是内部节点：
     * (1）如果是左子节点，则把该节点的左子树挂接到它的父节点，然后删除该节点以及它的右子树；
     * (2）如果是右子节点，则把该节点的右子树挂接到它的父节点，然后删除该节点以及它的左子树。
     */
    public String postorderStr(String preorderStr, String inorderStr, String beDeletedLeafNode) {

        // ===== Bug8修复: null 检查 =====
        if (preorderStr == null || inorderStr == null || beDeletedLeafNode == null) {
            return "";
        }

        // ===== Bug5修复: 先校验长度相等 =====
        if (preorderStr.length() != inorderStr.length()) {
            return "";
        }

        int len = preorderStr.length();

        // ===== Bug6修复: 长度范围 2~26 =====
        if (len < 2 || len > 26) {
            return "";
        }

        // ===== Bug7修复: 待删节点必须是单个大写字母, 否则不删 =====
        boolean shouldDelete = false;
        if (beDeletedLeafNode.length() == 1) {
            char dc = beDeletedLeafNode.charAt(0);
            if (dc >= 'A' && dc <= 'Z') {
                shouldDelete = true;
            }
        }

        // ===== Bug5+Bug6修复: 同时校验两串 + 元素相同 + 去重 =====
        Map<Character, Integer> inorderStrMap = new HashMap<>();
        int[] charCount = new int[26];

        for (int i = 0; i < len; i++) {
            char pc = preorderStr.charAt(i);
            char ic = inorderStr.charAt(i);

            // 非大写字母 → 非法
            if (pc < 'A' || pc > 'Z' || ic < 'A' || ic > 'Z') {
                return "";
            }

            // 前序+1, 中序-1, 最终全0表示两串元素相同
            charCount[pc - 'A']++;
            charCount[ic - 'A']--;

            // 中序去重
            if (inorderStrMap.containsKey(ic)) {
                return "";
            }
            inorderStrMap.put(ic, i);
        }

        for (int c : charCount) {
            if (c != 0) {
                return "";   // 两串元素不相同
            }
        }

        // ===== Bug1修复: 用前序+中序正确重建二叉树 =====
        TreeNode inorderTree = toInorderTreeNode(preorderStr, inorderStr, inorderStrMap);
        if (inorderTree == null) {
            return "";
        }

        // ===== Bug2修复: toPreorderStr 改成返回值 =====
        String newPreorderStr = toPreorderStr(inorderTree);
        if (!preorderStr.equals(newPreorderStr)) {
            return "";
        }

        // ===== Bug3+Bug4修复: 递归删除 =====
        if (shouldDelete) {
            deleteLeafNode(inorderTree, beDeletedLeafNode);
        }

        return toPostorderStr(inorderTree);
    }


    // ============ Bug1修复: 前序+中序 → 二叉树 ============
    // 前序首元素为根 → 在中序定位 → 分治左右

    private TreeNode toInorderTreeNode(String preorderStr, String inorderStr,
                                        Map<Character, Integer> inorderMap) {
        return buildTree(preorderStr, 0, preorderStr.length() - 1,
                         inorderStr, 0, inorderStr.length() - 1,
                         inorderMap);
    }

    private TreeNode buildTree(String preorder, int preStart, int preEnd,
                               String inorder, int inStart, int inEnd,
                               Map<Character, Integer> inorderMap) {
        if (preStart > preEnd || inStart > inEnd) {
            return null;
        }

        char rootVal = preorder.charAt(preStart);          // 前序第一个是根
        TreeNode root = new TreeNode(rootVal);

        int rootIdx = inorderMap.get(rootVal);             // 在中序中定位根
        int leftLen = rootIdx - inStart;                   // 左子树节点数

        root.left = buildTree(preorder, preStart + 1, preStart + leftLen,
                              inorder, inStart, rootIdx - 1, inorderMap);
        root.right = buildTree(preorder, preStart + leftLen + 1, preEnd,
                               inorder, rootIdx + 1, inEnd, inorderMap);
        return root;
    }


    // ============ 遍历方法 (Bug2修复: String不可变 → 改返回值) ============

    private String toPostorderStr(TreeNode root) {
        if (root == null) {
            return "";
        }
        return toPostorderStr(root.left) + toPostorderStr(root.right) + root.val;
    }

    private String toPreorderStr(TreeNode root) {
        if (root == null) {
            return "";
        }
        return root.val + toPreorderStr(root.left) + toPreorderStr(root.right);
    }


    // ============ Bug3+Bug4修复: 递归删除, 区分根/叶/内部节点 ============

    private void deleteLeafNode(TreeNode root, String beDeletedLeafNode) {
        char target = beDeletedLeafNode.charAt(0);

        // 根节点 → 不做删除 (Bug4: 单独的根节点判断)
        if (root.val == target) {
            return;
        }

        // 递归搜索左右子树 (Bug3修复: 不再是只查一层)
        deleteSubLeafNode(root.left, root, target, 'L');
        deleteSubLeafNode(root.right, root, target, 'R');
    }

    private void deleteSubLeafNode(TreeNode subNode, TreeNode parent, char target, char side) {
        // Bug8修复: null 检查
        if (subNode == null) {
            return;
        }

        if (subNode.val == target) {

            // Bug4修复: 情况1 — 叶子节点, 直接删除
            if (subNode.left == null && subNode.right == null) {
                if (side == 'L') {
                    parent.left = null;
                } else {
                    parent.right = null;
                }
                return;
            }

            // 情况2 — 内部节点: 按规则删除
            if (side == 'L') {
                // 左子节点: 保留左子树, 删节点+右子树
                parent.left = subNode.left;
            } else {
                // 右子节点: 保留右子树, 删节点+左子树
                parent.right = subNode.right;
            }
            return;
        }

        // Bug3修复: 递归搜索左右子树 (不再只查一层)
        deleteSubLeafNode(subNode.left, subNode, target, 'L');
        deleteSubLeafNode(subNode.right, subNode, target, 'R');
    }


    // ==================== 树节点 ====================

    class TreeNode {
        char val;
        TreeNode left;
        TreeNode right;

        TreeNode(char x) {
            val = x;
        }
    }
}
