/*
 Navicat Premium Dump SQL

 Source Server         : ooad-mysql
 Source Server Type    : MySQL
 Source Server Version : 90001 (9.0.1)
 Source Host           : 120.46.2.240:3306
 Source Schema         : comment

 Target Server Type    : MySQL
 Target Server Version : 90001 (9.0.1)
 File Encoding         : 65001

 Date: 23/12/2024 17:09:59
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for comment_comment
-- ----------------------------
DROP TABLE IF EXISTS `comment_comment`;
CREATE TABLE `comment_comment`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `pid` bigint NULL DEFAULT NULL,
  `uid` bigint NULL DEFAULT NULL,
  `status` tinyint NULL DEFAULT NULL,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `update_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `shop_id` bigint NULL DEFAULT NULL,
  `product_id` bigint NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 34 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of comment_comment
-- ----------------------------
INSERT INTO `comment_comment` VALUES (1, 0, 1, 1, '非常好，值得购买！', '2024-12-22 10:15:23', 101, 1001);
INSERT INTO `comment_comment` VALUES (2, 0, 1, 1, '质量一般，有点失望。', '2024-12-21 09:45:11', 102, 1002);
INSERT INTO `comment_comment` VALUES (3, 0, 3, 0, '发货很快，但是包装不太好。', '2024-12-20 08:30:45', 103, 1003);
INSERT INTO `comment_comment` VALUES (4, 0, 4, 1, '产品非常好，超出预期。', '2024-12-19 11:00:10', 104, 1004);
INSERT INTO `comment_comment` VALUES (5, 0, 5, 1, '客服态度很好，处理问题很及时。', '2024-12-18 13:55:02', 105, 1005);
INSERT INTO `comment_comment` VALUES (6, 0, 6, 0, '收到商品有破损，希望改进。', '2024-12-17 17:22:33', 106, 1006);
INSERT INTO `comment_comment` VALUES (7, 0, 7, 1, '价格实惠，性价比高。', '2024-12-16 14:10:21', 107, 1007);
INSERT INTO `comment_comment` VALUES (8, 0, 8, 1, '很好的一次购物体验，推荐购买！', '2024-12-15 16:25:13', 108, 1008);
INSERT INTO `comment_comment` VALUES (9, 0, 9, 0, '商品质量差，完全不符合描述。', '2024-12-14 19:33:00', 109, 1009);
INSERT INTO `comment_comment` VALUES (10, 0, 10, 1, '非常棒的商品，值得推荐。', '2024-12-13 12:00:40', 110, 1010);
INSERT INTO `comment_comment` VALUES (11, 0, 11, 1, '用了几天还行，暂时没有问题。', '2024-12-12 09:22:09', 111, 1011);
INSERT INTO `comment_comment` VALUES (12, 0, 12, 0, '价格贵了点，但是质量确实不错。', '2024-12-11 10:55:55', 112, 1012);
INSERT INTO `comment_comment` VALUES (13, 0, 13, 1, '服务态度很好，快递也很快。', '2024-12-10 14:33:15', 113, 1013);
INSERT INTO `comment_comment` VALUES (14, 0, 14, 0, '商品不值这个价格，质量差。', '2024-12-09 16:20:22', 114, 1014);
INSERT INTO `comment_comment` VALUES (15, 0, 15, 1, '喜欢这款产品，设计简洁大方。', '2024-12-08 17:40:11', 115, 1015);
INSERT INTO `comment_comment` VALUES (16, 0, 16, 1, '还行，就是有点小瑕疵。', '2024-12-07 13:05:25', 116, 1016);
INSERT INTO `comment_comment` VALUES (17, 0, 17, 0, '使用不方便，操作很复杂。', '2024-12-06 11:25:50', 117, 1017);
INSERT INTO `comment_comment` VALUES (18, 0, 18, 1, '非常好，值得一试。', '2024-12-05 08:45:12', 118, 1018);
INSERT INTO `comment_comment` VALUES (19, 0, 19, 1, '总体还不错，就是希望改进售后。', '2024-12-04 19:30:22', 119, 1019);
INSERT INTO `comment_comment` VALUES (20, 0, 20, 1, '不错，给个好评。', '2024-12-03 17:05:36', 120, 1020);
INSERT INTO `comment_comment` VALUES (21, 1, 1, 1, '评追：发货确实快，包装有待改进。', '2024-12-22 11:00:00', 101, 1001);
INSERT INTO `comment_comment` VALUES (22, 2, 2, 1, '评追：物流很快，物品完好无损。', '2024-12-21 10:00:00', 102, 1002);
INSERT INTO `comment_comment` VALUES (23, 3, 3, 1, '评追：挺不错的，使用很方便。', '2024-12-20 09:00:00', 103, 1003);
INSERT INTO `comment_comment` VALUES (24, 4, 4, 1, '评追：客服回复很及时，态度好。', '2024-12-19 15:30:00', 104, 1004);
INSERT INTO `comment_comment` VALUES (25, 5, 5, 1, '评追：这个产品性价比很高，推荐！', '2024-12-18 13:00:00', 105, 1005);
INSERT INTO `comment_comment` VALUES (26, 1, 1, 1, '评追：物品有点破损，希望能改进。', '2024-12-17 16:00:00', 101, 1001);
INSERT INTO `comment_comment` VALUES (27, 1, 101, 1, '回复：感谢您的反馈！我们会继续改进商品的质量和服务，希望您能继续支持我们！', '2024-12-23 09:00:00', 101, 1001);
INSERT INTO `comment_comment` VALUES (28, 2, 102, 1, '回复：非常抱歉给您带来不好的体验，我们会加强产品的质量控制，并提供更好的售后服务。', '2024-12-23 09:05:00', 102, 1002);
INSERT INTO `comment_comment` VALUES (29, 3, 103, 1, '回复：感谢您的反馈，包装问题我们会加强改进，确保下一次您收到货时更满意！', '2024-12-23 09:10:00', 103, 1003);
INSERT INTO `comment_comment` VALUES (30, 21, 104, 1, '回复：感谢您的好评！我们将继续努力，保证每一次的购物体验都能让您满意！', '2024-12-23 09:15:00', 101, 1001);
INSERT INTO `comment_comment` VALUES (31, 4, 105, 1, '回复：感谢您的高度评价，您的支持是我们不断进步的动力！', '2024-12-23 09:20:00', 104, 1004);
INSERT INTO `comment_comment` VALUES (32, 5, 106, 1, '回复：谢谢您的认可，客服会继续保持高效的服务态度。', '2024-12-23 09:25:00', 105, 1005);
INSERT INTO `comment_comment` VALUES (33, 2, 107, 1, '回复：感谢您的反馈，我们将尽快处理质量问题并提供解决方案。', '2024-12-23 09:30:00', 102, 1002);

SET FOREIGN_KEY_CHECKS = 1;
