/*
Navicat MySQL Data Transfer

Source Server         : xxxx
Source Server Version : 50173
Source Host           : xxxx:3306
Source Database       : table

Target Server Type    : MYSQL
Target Server Version : 50173
File Encoding         : 65001

Date: 2017-07-20 13:18:00
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for sys_join_role_resource
-- 角色和权限的关联表
-- ----------------------------
DROP TABLE IF EXISTS `sys_join_role_resource`;
CREATE TABLE `sys_join_role_resource` (
  `fk_role_id` varchar(32) NOT NULL,
  `fk_resource_id` varchar(32) NOT NULL,
  PRIMARY KEY (`fk_role_id`,`fk_resource_id`),
  KEY `FK350a5r29srhgp85lwwq9xgqr5` (`fk_resource_id`) USING BTREE
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_join_role_resource
-- 角色和权限关联数据
-- ----------------------------
INSERT INTO `sys_join_role_resource` VALUES ('1', '10');
INSERT INTO `sys_join_role_resource` VALUES ('1', '9');
INSERT INTO `sys_join_role_resource` VALUES ('2', '');
INSERT INTO `sys_join_role_resource` VALUES ('2', '9');

-- ----------------------------
-- Table structure for sys_join_user_role
-- 用户和角色关联表
-- ----------------------------
DROP TABLE IF EXISTS `sys_join_user_role`;
CREATE TABLE `sys_join_user_role` (
  `fk_user_id` varchar(32) NOT NULL,
  `fk_role_id` varchar(32) NOT NULL,
  PRIMARY KEY (`fk_user_id`,`fk_role_id`),
  KEY `FKtp0yd0qlo7nalf7n4n8b77psj` (`fk_role_id`) USING BTREE
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_join_user_role
-- 用户和角色关联数据
-- ----------------------------
INSERT INTO `sys_join_user_role` VALUES ('1', '1');
INSERT INTO `sys_join_user_role` VALUES ('1', '2');
INSERT INTO `sys_join_user_role` VALUES ('40286d815d16da96015d16db5c1d0002', '1');
INSERT INTO `sys_join_user_role` VALUES ('40286d815d16da96015d16db9daa0004', '2');

-- ----------------------------
-- Table structure for sys_log
-- 日志表
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log` (
  `id` varchar(32) NOT NULL,
  `create_time` datetime NOT NULL,
  `entity` int(11) DEFAULT NULL,
  `operation` int(11) DEFAULT NULL,
  `original_value` varchar(4000) DEFAULT NULL,
  `target_value` varchar(4000) DEFAULT NULL,
  `user_name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_log
-- 日志表初始数据
-- ----------------------------
INSERT INTO `sys_log` VALUES ('40286c825d5e6c5c015d5e6d6b730000', '2017-07-20 13:16:55', null, '3', null, null, 'admin');

-- ----------------------------
-- Table structure for sys_resource
-- 权限表
-- ----------------------------
DROP TABLE IF EXISTS `sys_resource`;
CREATE TABLE `sys_resource` (
  `id` varchar(32) NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `order_num` int(11) NOT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `status` int(11) NOT NULL,
  `type` int(11) NOT NULL,
  `url` varchar(255) DEFAULT NULL,
  `parent_id` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK3fekum3ead5klp7y4lckn5ohi` (`parent_id`) USING BTREE
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_resource
-- 权限表数据
-- ----------------------------
INSERT INTO `sys_resource` VALUES ('10', '2017-07-01 00:00:00', '日志查看', '10', '', '1', '1', '/sys_log', null);
INSERT INTO `sys_resource` VALUES ('9', '2017-07-01 00:00:00', '用户管理', '9', '', '1', '1', '/sys_user', null);

-- ----------------------------
-- Table structure for sys_role
-- 角色表
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` varchar(32) NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `role_code` varchar(255) DEFAULT NULL,
  `status` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_role
-- 角色表初始化数据
-- ----------------------------
INSERT INTO `sys_role` VALUES ('1', '2017-06-30 15:52:49', '系统管理', null, null, '1');
INSERT INTO `sys_role` VALUES ('2', '2017-06-30 15:52:57', '业务操作', null, null, '1');

-- ----------------------------
-- Table structure for sys_user
-- 用户表
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` varchar(32) NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `status` int(11) NOT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_51bvuyvihefoh4kp5syh2jpi4` (`username`) USING BTREE
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_user
-- 用户表初始化数据
-- ----------------------------
INSERT INTO `sys_user` VALUES ('1', '2017-07-01 00:54:11', '21232f297a57a5a743894a0e4a801fc3', '1', 'admin');
INSERT INTO `sys_user` VALUES ('40286d815d16da96015d16db5c1d0002', '2017-07-06 15:44:20', '202cb962ac59075b964b07152d234b70', '1', 'user');
INSERT INTO `sys_user` VALUES ('40286d815d16da96015d16db9daa0004', '2017-07-06 15:44:37', '202cb962ac59075b964b07152d234b70', '1', 'user1');
INSERT INTO `sys_user` VALUES ('40286d815d16da96015d16dbceca0006', '2017-07-06 15:44:50', '202cb962ac59075b964b07152d234b70', '1', 'user2');
