/*
Navicat Oracle Data Transfer
Oracle Client Version : 11.2.0.1.0

Source Server         : 127.0.0.1_1521_scott
Source Server Version : 110200
Source Host           : localhost:1521
Source Schema         : SCOTT

Target Server Type    : ORACLE
Target Server Version : 110200
File Encoding         : 65001

Date: 2017-12-14 09:12:16
*/


-- ----------------------------
-- Table structure for T_PERMISSION
-- ----------------------------
DROP TABLE "SCOTT"."T_PERMISSION";
CREATE TABLE "SCOTT"."T_PERMISSION" (
"ID" NUMBER(10) NOT NULL ,
"URL" VARCHAR2(256 BYTE) NULL ,
"NAME" VARCHAR2(64 BYTE) NULL 
)
LOGGING
NOCOMPRESS
NOCACHE

;
COMMENT ON COLUMN "SCOTT"."T_PERMISSION"."URL" IS 'url地址';
COMMENT ON COLUMN "SCOTT"."T_PERMISSION"."NAME" IS 'url描述';

-- ----------------------------
-- Records of T_PERMISSION
-- ----------------------------
INSERT INTO "SCOTT"."T_PERMISSION" VALUES ('1', '/user', 'user:user');
INSERT INTO "SCOTT"."T_PERMISSION" VALUES ('2', '/user/add', 'user:add');
INSERT INTO "SCOTT"."T_PERMISSION" VALUES ('3', '/user/delete', 'user:delete');

-- ----------------------------
-- Table structure for T_ROLE
-- ----------------------------
DROP TABLE "SCOTT"."T_ROLE";
CREATE TABLE "SCOTT"."T_ROLE" (
"ID" NUMBER NOT NULL ,
"NAME" VARCHAR2(32 BYTE) NULL ,
"MEMO" VARCHAR2(32 BYTE) NULL 
)
LOGGING
NOCOMPRESS
NOCACHE

;
COMMENT ON COLUMN "SCOTT"."T_ROLE"."NAME" IS '角色名称';
COMMENT ON COLUMN "SCOTT"."T_ROLE"."MEMO" IS '角色描述';

-- ----------------------------
-- Records of T_ROLE
-- ----------------------------
INSERT INTO "SCOTT"."T_ROLE" VALUES ('1', 'admin', '超级管理员');
INSERT INTO "SCOTT"."T_ROLE" VALUES ('2', 'test', '测试账户');

-- ----------------------------
-- Table structure for T_ROLE_PERMISSION
-- ----------------------------
DROP TABLE "SCOTT"."T_ROLE_PERMISSION";
CREATE TABLE "SCOTT"."T_ROLE_PERMISSION" (
"RID" NUMBER(10) NULL ,
"PID" NUMBER(10) NULL 
)
LOGGING
NOCOMPRESS
NOCACHE

;
COMMENT ON COLUMN "SCOTT"."T_ROLE_PERMISSION"."RID" IS '角色id';
COMMENT ON COLUMN "SCOTT"."T_ROLE_PERMISSION"."PID" IS '权限id';

-- ----------------------------
-- Records of T_ROLE_PERMISSION
-- ----------------------------
INSERT INTO "SCOTT"."T_ROLE_PERMISSION" VALUES ('1', '2');
INSERT INTO "SCOTT"."T_ROLE_PERMISSION" VALUES ('1', '3');
INSERT INTO "SCOTT"."T_ROLE_PERMISSION" VALUES ('2', '1');
INSERT INTO "SCOTT"."T_ROLE_PERMISSION" VALUES ('1', '1');

-- ----------------------------
-- Table structure for T_USER
-- ----------------------------
DROP TABLE "SCOTT"."T_USER";
CREATE TABLE "SCOTT"."T_USER" (
"ID" NUMBER NOT NULL ,
"USERNAME" VARCHAR2(20 BYTE) NOT NULL ,
"PASSWD" VARCHAR2(128 BYTE) NOT NULL ,
"CREATE_TIME" DATE NULL ,
"STATUS" CHAR(1 BYTE) NOT NULL 
)
LOGGING
NOCOMPRESS
NOCACHE

;
COMMENT ON COLUMN "SCOTT"."T_USER"."USERNAME" IS '用户名';
COMMENT ON COLUMN "SCOTT"."T_USER"."PASSWD" IS '密码';
COMMENT ON COLUMN "SCOTT"."T_USER"."CREATE_TIME" IS '创建时间';
COMMENT ON COLUMN "SCOTT"."T_USER"."STATUS" IS '是否有效 1：有效  0：锁定';

-- ----------------------------
-- Records of T_USER
-- ----------------------------
INSERT INTO "SCOTT"."T_USER" VALUES ('2', 'tester', '243e29429b340192700677d48c09d992', TO_DATE('2017-12-11 17:20:21', 'YYYY-MM-DD HH24:MI:SS'), '1');
INSERT INTO "SCOTT"."T_USER" VALUES ('1', 'mrbird', '42ee25d1e43e9f57119a00d0a39e5250', TO_DATE('2017-12-11 10:52:48', 'YYYY-MM-DD HH24:MI:SS'), '1');

-- ----------------------------
-- Table structure for T_USER_ROLE
-- ----------------------------
DROP TABLE "SCOTT"."T_USER_ROLE";
CREATE TABLE "SCOTT"."T_USER_ROLE" (
"USER_ID" NUMBER(10) NULL ,
"RID" NUMBER(10) NULL 
)
LOGGING
NOCOMPRESS
NOCACHE

;
COMMENT ON COLUMN "SCOTT"."T_USER_ROLE"."USER_ID" IS '用户id';
COMMENT ON COLUMN "SCOTT"."T_USER_ROLE"."RID" IS '角色id';

-- ----------------------------
-- Records of T_USER_ROLE
-- ----------------------------
INSERT INTO "SCOTT"."T_USER_ROLE" VALUES ('1', '1');
INSERT INTO "SCOTT"."T_USER_ROLE" VALUES ('2', '2');

-- ----------------------------
-- Indexes structure for table T_PERMISSION
-- ----------------------------

-- ----------------------------
-- Checks structure for table T_PERMISSION
-- ----------------------------
ALTER TABLE "SCOTT"."T_PERMISSION" ADD CHECK ("ID" IS NOT NULL);
ALTER TABLE "SCOTT"."T_PERMISSION" ADD CHECK (ID IS NOT NULL);

-- ----------------------------
-- Primary Key structure for table T_PERMISSION
-- ----------------------------
ALTER TABLE "SCOTT"."T_PERMISSION" ADD PRIMARY KEY ("ID");

-- ----------------------------
-- Indexes structure for table T_ROLE
-- ----------------------------

-- ----------------------------
-- Checks structure for table T_ROLE
-- ----------------------------
ALTER TABLE "SCOTT"."T_ROLE" ADD CHECK ("ID" IS NOT NULL);
ALTER TABLE "SCOTT"."T_ROLE" ADD CHECK (id IS NOT NULL);

-- ----------------------------
-- Primary Key structure for table T_ROLE
-- ----------------------------
ALTER TABLE "SCOTT"."T_ROLE" ADD PRIMARY KEY ("ID");

-- ----------------------------
-- Indexes structure for table T_USER
-- ----------------------------

-- ----------------------------
-- Checks structure for table T_USER
-- ----------------------------
ALTER TABLE "SCOTT"."T_USER" ADD CHECK ("ID" IS NOT NULL);
ALTER TABLE "SCOTT"."T_USER" ADD CHECK ("USERNAME" IS NOT NULL);
ALTER TABLE "SCOTT"."T_USER" ADD CHECK ("PASSWD" IS NOT NULL);
ALTER TABLE "SCOTT"."T_USER" ADD CHECK ("STATUS" IS NOT NULL);
ALTER TABLE "SCOTT"."T_USER" ADD CHECK (id IS NOT NULL);
ALTER TABLE "SCOTT"."T_USER" ADD CHECK (username IS NOT NULL);
ALTER TABLE "SCOTT"."T_USER" ADD CHECK (passwd IS NOT NULL);
ALTER TABLE "SCOTT"."T_USER" ADD CHECK (status IS NOT NULL);

-- ----------------------------
-- Primary Key structure for table T_USER
-- ----------------------------
ALTER TABLE "SCOTT"."T_USER" ADD PRIMARY KEY ("ID");
