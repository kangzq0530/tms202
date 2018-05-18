-- phpMyAdmin SQL Dump
-- version 4.1.14
-- http://www.phpmyadmin.net
--
-- 主機: 127.0.0.1
-- 產生時間： 2018-05-18: 21:40:57
-- 伺服器版本: 5.6.17
-- PHP 版本： 5.5.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- 資料庫： `msemu`
--

-- --------------------------------------------------------

--
-- 資料表結構 `accounts`
--

CREATE TABLE IF NOT EXISTS `accounts` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `pic` varchar(255) DEFAULT NULL,
  `mac` varchar(255) DEFAULT NULL,
  `gmLevel` int(11) DEFAULT '0',
  `accountTypeMask` int(11) DEFAULT '2',
  `age` int(11) DEFAULT '0',
  `vipGrade` int(11) DEFAULT '0',
  `nBlockReason` int(11) DEFAULT '0',
  `gender` tinyint(4) DEFAULT '0',
  `msg2` tinyint(4) DEFAULT '0',
  `purchaseExp` tinyint(4) DEFAULT '0',
  `pBlockReason` tinyint(4) DEFAULT '3',
  `chatUnblockDate` bigint(20) DEFAULT '0',
  `gradeCode` tinyint(4) DEFAULT '0',
  `characterSlots` int(11) DEFAULT '4',
  `creationDate` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 AUTO_INCREMENT=4 ;

-- --------------------------------------------------------

--
-- 資料表結構 `avatardata`
--

CREATE TABLE IF NOT EXISTS `avatardata` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `characterStat` int(11) DEFAULT NULL,
  `avatarLook` int(11) DEFAULT NULL,
  `zeroAvatarLook` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `characterStat` (`characterStat`),
  KEY `avatarLook` (`avatarLook`),
  KEY `zeroAvatarLook` (`zeroAvatarLook`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 AUTO_INCREMENT=18 ;

-- --------------------------------------------------------

--
-- 資料表結構 `avatarlook`
--

CREATE TABLE IF NOT EXISTS `avatarlook` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `gender` int(11) DEFAULT NULL,
  `skin` int(11) DEFAULT NULL,
  `face` int(11) DEFAULT NULL,
  `hair` int(11) DEFAULT NULL,
  `weaponStickerId` int(11) DEFAULT NULL,
  `weaponId` int(11) DEFAULT NULL,
  `subWeaponId` int(11) DEFAULT NULL,
  `job` int(11) DEFAULT NULL,
  `drawElfEar` tinyint(1) DEFAULT NULL,
  `demonSlayerDefFaceAcc` int(11) DEFAULT NULL,
  `xenonDefFaceAcc` int(11) DEFAULT NULL,
  `beastTamerDefFaceAcc` int(11) DEFAULT NULL,
  `isZeroBetaLook` tinyint(1) DEFAULT NULL,
  `mixedHairColor` int(11) DEFAULT NULL,
  `mixHairPercent` int(11) DEFAULT NULL,
  `ears` int(11) DEFAULT NULL,
  `tail` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 AUTO_INCREMENT=20 ;

-- --------------------------------------------------------

--
-- 資料表結構 `charactercards`
--

CREATE TABLE IF NOT EXISTS `charactercards` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `characterId` int(11) DEFAULT NULL,
  `job` int(11) DEFAULT NULL,
  `level` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 AUTO_INCREMENT=20 ;

-- --------------------------------------------------------

--
-- 資料表結構 `characters`
--

CREATE TABLE IF NOT EXISTS `characters` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `accId` int(11) DEFAULT NULL,
  `characterPos` int(11) NOT NULL DEFAULT '0',
  `avatarData` int(11) DEFAULT NULL,
  `equippedInventory` int(11) DEFAULT NULL,
  `equipInventory` int(11) DEFAULT NULL,
  `consumeInventory` int(11) DEFAULT NULL,
  `etcInventory` int(11) DEFAULT NULL,
  `installInventory` int(11) DEFAULT NULL,
  `cashInventory` int(11) DEFAULT NULL,
  `funcKeyMap_id` int(11) DEFAULT NULL,
  `questManager` bigint(20) DEFAULT NULL,
  `guild` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `avatarData` (`avatarData`),
  KEY `equippedInventory` (`equippedInventory`),
  KEY `equipInventory` (`equipInventory`),
  KEY `consumeInventory` (`consumeInventory`),
  KEY `etcInventory` (`etcInventory`),
  KEY `installInventory` (`installInventory`),
  KEY `cashInventory` (`cashInventory`),
  KEY `funcKeyMap_id` (`funcKeyMap_id`),
  KEY `questManager` (`questManager`),
  KEY `guild` (`guild`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 AUTO_INCREMENT=18 ;

-- --------------------------------------------------------

--
-- 資料表結構 `characterstats`
--

CREATE TABLE IF NOT EXISTS `characterstats` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `characterId` int(11) DEFAULT NULL,
  `characterIdForLog` int(11) DEFAULT NULL,
  `worldIdForLog` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `gender` int(11) DEFAULT NULL,
  `skin` int(11) DEFAULT NULL,
  `face` int(11) DEFAULT NULL,
  `hair` int(11) DEFAULT NULL,
  `mixBaseHairColor` int(11) DEFAULT NULL,
  `mixAddHairColor` int(11) DEFAULT NULL,
  `mixHairBaseProb` int(11) DEFAULT NULL,
  `level` int(11) DEFAULT NULL,
  `job` int(11) DEFAULT NULL,
  `str` int(11) DEFAULT NULL,
  `dex` int(11) DEFAULT NULL,
  `inte` int(11) DEFAULT NULL,
  `luk` int(11) DEFAULT NULL,
  `hp` int(11) DEFAULT NULL,
  `maxHp` int(11) DEFAULT NULL,
  `mp` int(11) DEFAULT NULL,
  `maxMp` int(11) DEFAULT NULL,
  `ap` int(11) DEFAULT NULL,
  `sp` int(11) DEFAULT NULL,
  `exp` int(11) NOT NULL DEFAULT '0',
  `pop` int(11) DEFAULT NULL,
  `money` mediumtext,
  `wp` int(11) DEFAULT NULL,
  `extendSP` int(11) DEFAULT NULL,
  `posMap` int(11) NOT NULL DEFAULT '0',
  `portal` int(11) DEFAULT NULL,
  `subJob` int(11) DEFAULT NULL,
  `defFaceAcc` int(11) DEFAULT NULL,
  `fatigue` int(11) DEFAULT NULL,
  `lastFatigueUpdateTime` int(11) DEFAULT NULL,
  `charismaExp` int(11) DEFAULT NULL,
  `insightExp` int(11) DEFAULT NULL,
  `willExp` int(11) DEFAULT NULL,
  `craftExp` int(11) DEFAULT NULL,
  `senseExp` int(11) DEFAULT NULL,
  `charmExp` int(11) DEFAULT NULL,
  `nonCombatStatDayLimit` int(11) DEFAULT NULL,
  `pvpExp` int(11) DEFAULT NULL,
  `pvpGrade` int(11) DEFAULT NULL,
  `pvpPoint` int(11) DEFAULT NULL,
  `pvpModeLevel` int(11) DEFAULT NULL,
  `pvpModeType` int(11) DEFAULT NULL,
  `eventPoint` int(11) DEFAULT NULL,
  `albaActivityID` int(11) DEFAULT NULL,
  `albaStartTime` int(11) DEFAULT NULL,
  `albaDuration` int(11) DEFAULT NULL,
  `albaSpecialReward` int(11) DEFAULT NULL,
  `burning` tinyint(1) DEFAULT NULL,
  `characterCard` int(11) DEFAULT NULL,
  `accountLastLogout` int(11) DEFAULT NULL,
  `lastLogout` int(11) DEFAULT NULL,
  `gachExp` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `extendSP` (`extendSP`),
  KEY `nonCombatStatDayLimit` (`nonCombatStatDayLimit`),
  KEY `albaStartTime` (`albaStartTime`),
  KEY `characterCard` (`characterCard`),
  KEY `accountLastLogout` (`accountLastLogout`),
  KEY `lastLogout` (`lastLogout`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 AUTO_INCREMENT=18 ;

-- --------------------------------------------------------

--
-- 資料表結構 `dropdata`
--

CREATE TABLE IF NOT EXISTS `dropdata` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `mobID` int(11) NOT NULL,
  `itemID` int(11) NOT NULL DEFAULT '0',
  `minQuantity` tinyint(3) NOT NULL DEFAULT '1',
  `maxQuantity` tinyint(3) NOT NULL DEFAULT '1',
  `questReq` int(11) NOT NULL DEFAULT '0',
  `chance` int(11) NOT NULL DEFAULT '0',
  `world` int(11) NOT NULL DEFAULT '0',
  `channel` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `mobID` (`mobID`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=78577 ;

-- --------------------------------------------------------

--
-- 資料表結構 `equips`
--

CREATE TABLE IF NOT EXISTS `equips` (
  `serialNumber` bigint(20) DEFAULT NULL,
  `itemId` bigint(20) NOT NULL DEFAULT '0',
  `title` varchar(255) DEFAULT NULL,
  `equippedDate` int(11) DEFAULT NULL,
  `prevBonusExpRate` int(11) DEFAULT NULL,
  `ruc` smallint(6) DEFAULT NULL,
  `cuc` smallint(6) DEFAULT NULL,
  `iStr` smallint(6) DEFAULT NULL,
  `iDex` smallint(6) DEFAULT NULL,
  `iInt` smallint(6) DEFAULT NULL,
  `iLuk` smallint(6) DEFAULT NULL,
  `iMaxHp` smallint(6) DEFAULT NULL,
  `iMaxMp` smallint(6) DEFAULT NULL,
  `iPad` smallint(6) DEFAULT NULL,
  `iMad` smallint(6) DEFAULT NULL,
  `iPDD` smallint(6) DEFAULT NULL,
  `iMDD` smallint(6) DEFAULT NULL,
  `iAcc` smallint(6) DEFAULT NULL,
  `iEva` smallint(6) DEFAULT NULL,
  `iCraft` smallint(6) DEFAULT NULL,
  `iSpeed` smallint(6) DEFAULT NULL,
  `iJump` smallint(6) DEFAULT NULL,
  `attribute` smallint(6) DEFAULT NULL,
  `levelUpType` smallint(6) DEFAULT NULL,
  `level` smallint(6) DEFAULT NULL,
  `exp` smallint(6) DEFAULT NULL,
  `durability` smallint(6) DEFAULT NULL,
  `iuc` smallint(6) DEFAULT NULL,
  `iPvpDamage` smallint(6) DEFAULT NULL,
  `iReduceReq` smallint(6) DEFAULT NULL,
  `specialAttribute` smallint(6) DEFAULT NULL,
  `durabilityMax` smallint(6) DEFAULT NULL,
  `iIncReq` smallint(6) DEFAULT NULL,
  `growthEnchant` smallint(6) DEFAULT NULL,
  `psEnchant` smallint(6) DEFAULT NULL,
  `bdr` smallint(6) DEFAULT NULL,
  `imdr` smallint(6) DEFAULT NULL,
  `damR` smallint(6) DEFAULT NULL,
  `statR` smallint(6) DEFAULT NULL,
  `cuttable` smallint(6) DEFAULT NULL,
  `exGradeOption` smallint(6) DEFAULT NULL,
  `itemState` smallint(6) DEFAULT NULL,
  `grade` smallint(6) DEFAULT NULL,
  `chuc` smallint(6) DEFAULT NULL,
  `soulOptionId` smallint(6) DEFAULT NULL,
  `soulSocketId` smallint(6) DEFAULT NULL,
  `soulOption` smallint(6) DEFAULT NULL,
  `rStr` smallint(6) DEFAULT NULL,
  `rDex` smallint(6) DEFAULT NULL,
  `rInt` smallint(6) DEFAULT NULL,
  `rLuk` smallint(6) DEFAULT NULL,
  `rLevel` smallint(6) DEFAULT NULL,
  `rJob` smallint(6) DEFAULT NULL,
  `rPop` smallint(6) DEFAULT NULL,
  `specialGrade` int(11) DEFAULT NULL,
  `fixedPotential` tinyint(1) DEFAULT NULL,
  `tradeBlock` tinyint(1) DEFAULT NULL,
  `isOnly` tinyint(1) DEFAULT NULL,
  `notSale` tinyint(1) DEFAULT NULL,
  `attackSpeed` int(11) DEFAULT NULL,
  `price` int(11) DEFAULT NULL,
  `tuc` int(11) DEFAULT NULL,
  `charmEXP` int(11) DEFAULT NULL,
  `expireOnLogout` tinyint(1) DEFAULT NULL,
  `setItemID` int(11) DEFAULT NULL,
  `exItem` tinyint(1) DEFAULT NULL,
  `equipTradeBlock` tinyint(1) DEFAULT NULL,
  `iSlot` varchar(255) DEFAULT NULL,
  `vSlot` varchar(255) DEFAULT NULL,
  `fixedGrade` int(11) DEFAULT NULL,
  PRIMARY KEY (`itemId`),
  KEY `equippedDate` (`equippedDate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- 資料表結構 `extendsp`
--

CREATE TABLE IF NOT EXISTS `extendsp` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 AUTO_INCREMENT=20 ;

-- --------------------------------------------------------

--
-- 資料表結構 `filetimes`
--

CREATE TABLE IF NOT EXISTS `filetimes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `lowDateTime` int(11) DEFAULT NULL,
  `highDateTime` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 AUTO_INCREMENT=248 ;

-- --------------------------------------------------------

--
-- 資料表結構 `funckeymap`
--

CREATE TABLE IF NOT EXISTS `funckeymap` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 AUTO_INCREMENT=18 ;

-- --------------------------------------------------------

--
-- 資料表結構 `gradenames`
--

CREATE TABLE IF NOT EXISTS `gradenames` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `gradeName` varchar(255) DEFAULT NULL,
  `guildID` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `guildID` (`guildID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- 資料表結構 `guildmembers`
--

CREATE TABLE IF NOT EXISTS `guildmembers` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `charID` int(11) DEFAULT NULL,
  `guildID` int(11) DEFAULT NULL,
  `grade` int(11) DEFAULT NULL,
  `allianceGrade` int(11) DEFAULT NULL,
  `commitment` int(11) DEFAULT NULL,
  `dayCommitment` int(11) DEFAULT NULL,
  `igp` int(11) DEFAULT NULL,
  `commitmentIncTime` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `job` int(11) DEFAULT NULL,
  `level` int(11) DEFAULT NULL,
  `loggedIn` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `guildID` (`guildID`),
  KEY `commitmentIncTime` (`commitmentIncTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- 資料表結構 `guildrequestors`
--

CREATE TABLE IF NOT EXISTS `guildrequestors` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `requestors_id` int(11) DEFAULT NULL,
  `charID` int(11) DEFAULT NULL,
  `guildID` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `job` int(11) DEFAULT NULL,
  `level` int(11) DEFAULT NULL,
  `loggedIn` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `guildID` (`guildID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- 資料表結構 `guilds`
--

CREATE TABLE IF NOT EXISTS `guilds` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `leaderID` int(11) DEFAULT NULL,
  `worldID` int(11) DEFAULT NULL,
  `markBg` int(11) DEFAULT NULL,
  `markBgColor` int(11) DEFAULT NULL,
  `mark` int(11) DEFAULT NULL,
  `markColor` int(11) DEFAULT NULL,
  `maxMembers` int(11) DEFAULT NULL,
  `notice` varchar(255) DEFAULT NULL,
  `points` int(11) DEFAULT NULL,
  `seasonPoints` int(11) DEFAULT NULL,
  `allianceID` int(11) DEFAULT NULL,
  `level` int(11) DEFAULT NULL,
  `rank` int(11) DEFAULT NULL,
  `ggp` int(11) DEFAULT NULL,
  `appliable` tinyint(1) DEFAULT NULL,
  `joinSetting` int(11) DEFAULT NULL,
  `reqLevel` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- 資料表結構 `guildskill`
--

CREATE TABLE IF NOT EXISTS `guildskill` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `skillID` int(11) DEFAULT NULL,
  `level` int(11) DEFAULT NULL,
  `expireDate` int(11) DEFAULT NULL,
  `buyCharacterName` varchar(255) DEFAULT NULL,
  `extendCharacterName` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `expireDate` (`expireDate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- 資料表結構 `guildskills`
--

CREATE TABLE IF NOT EXISTS `guildskills` (
  `guildSkill_id` int(11) NOT NULL AUTO_INCREMENT,
  `skills_id` int(11) DEFAULT NULL,
  `Guild_id` int(11) DEFAULT NULL,
  `skillID` int(11) DEFAULT NULL,
  `fk_GuildSkillID` int(11) DEFAULT NULL,
  PRIMARY KEY (`guildSkill_id`),
  KEY `Guild_id` (`Guild_id`),
  KEY `fk_GuildSkillID` (`fk_GuildSkillID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- 資料表結構 `hairequips`
--

CREATE TABLE IF NOT EXISTS `hairequips` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `alId` int(11) DEFAULT NULL,
  `equipId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `alId` (`alId`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 AUTO_INCREMENT=140 ;

-- --------------------------------------------------------

--
-- 資料表結構 `inventories`
--

CREATE TABLE IF NOT EXISTS `inventories` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` int(11) DEFAULT NULL,
  `slots` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 AUTO_INCREMENT=103 ;

-- --------------------------------------------------------

--
-- 資料表結構 `items`
--

CREATE TABLE IF NOT EXISTS `items` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `inventoryId` int(11) DEFAULT NULL,
  `itemId` int(11) DEFAULT NULL,
  `bagIndex` int(11) DEFAULT NULL,
  `cashItemSerialNumber` bigint(20) DEFAULT NULL,
  `dateExpire` int(11) DEFAULT NULL,
  `invType` int(11) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `isCash` tinyint(1) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `owner` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `inventoryId` (`inventoryId`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 AUTO_INCREMENT=38 ;

-- --------------------------------------------------------

--
-- 資料表結構 `keymaps`
--

CREATE TABLE IF NOT EXISTS `keymaps` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `fkMapId` int(11) DEFAULT NULL,
  `idx` int(11) DEFAULT NULL,
  `type` tinyint(4) DEFAULT NULL,
  `val` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fkMapId` (`fkMapId`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 AUTO_INCREMENT=817 ;

-- --------------------------------------------------------

--
-- 資料表結構 `noncombatstatdaylimit`
--

CREATE TABLE IF NOT EXISTS `noncombatstatdaylimit` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `charisma` smallint(6) DEFAULT NULL,
  `charm` smallint(6) DEFAULT NULL,
  `insight` smallint(6) DEFAULT NULL,
  `will` smallint(6) DEFAULT NULL,
  `craft` smallint(6) DEFAULT NULL,
  `sense` smallint(6) DEFAULT NULL,
  `ftLastUpdateCharmByCashPR` int(11) DEFAULT NULL,
  `charmByCashPR` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `ftLastUpdateCharmByCashPR` (`ftLastUpdateCharmByCashPR`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 AUTO_INCREMENT=20 ;

-- --------------------------------------------------------

--
-- 資料表結構 `options`
--

CREATE TABLE IF NOT EXISTS `options` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `equipId` bigint(20) DEFAULT NULL,
  `optionId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `equipId` (`equipId`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 AUTO_INCREMENT=106 ;

-- --------------------------------------------------------

--
-- 資料表結構 `petids`
--

CREATE TABLE IF NOT EXISTS `petids` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `alId` int(11) DEFAULT NULL,
  `petId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `alId` (`alId`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 AUTO_INCREMENT=46 ;

-- --------------------------------------------------------

--
-- 資料表結構 `questlists`
--

CREATE TABLE IF NOT EXISTS `questlists` (
  `questsList_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `QuestManager_id` bigint(20) DEFAULT NULL,
  `questID` int(11) DEFAULT NULL,
  `fk_questID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`questsList_id`),
  KEY `QuestManager_id` (`QuestManager_id`),
  KEY `fk_questID` (`fk_questID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 AUTO_INCREMENT=158 ;

-- --------------------------------------------------------

--
-- 資料表結構 `questmanagers`
--

CREATE TABLE IF NOT EXISTS `questmanagers` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 AUTO_INCREMENT=17 ;

-- --------------------------------------------------------

--
-- 資料表結構 `questprogressrequirements`
--

CREATE TABLE IF NOT EXISTS `questprogressrequirements` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `progressType` varchar(255) DEFAULT NULL,
  `questID` bigint(20) DEFAULT NULL,
  `unitID` int(11) DEFAULT NULL,
  `requiredCount` int(11) DEFAULT NULL,
  `currentCount` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `questID` (`questID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 AUTO_INCREMENT=10 ;

-- --------------------------------------------------------

--
-- 資料表結構 `quests`
--

CREATE TABLE IF NOT EXISTS `quests` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `qrKey` int(11) DEFAULT NULL,
  `qrValue` varchar(255) DEFAULT NULL,
  `qrExValue` varchar(255) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `completedTime` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `completedTime` (`completedTime`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 AUTO_INCREMENT=158 ;

-- --------------------------------------------------------

--
-- 資料表結構 `skills`
--

CREATE TABLE IF NOT EXISTS `skills` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `charId` int(11) DEFAULT NULL,
  `skillId` int(11) DEFAULT NULL,
  `rootId` int(11) DEFAULT NULL,
  `maxLevel` int(11) DEFAULT NULL,
  `currentLevel` int(11) DEFAULT NULL,
  `masterLevel` int(11) DEFAULT NULL,
  `dateExpire` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `charId` (`charId`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 AUTO_INCREMENT=54 ;

-- --------------------------------------------------------

--
-- 資料表結構 `spset`
--

CREATE TABLE IF NOT EXISTS `spset` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `extendSP_id` int(11) DEFAULT NULL,
  `jobLevel` tinyint(4) DEFAULT NULL,
  `sp` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `extendSP_id` (`extendSP_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 AUTO_INCREMENT=96 ;

-- --------------------------------------------------------

--
-- 資料表結構 `systemtimes`
--

CREATE TABLE IF NOT EXISTS `systemtimes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `year` int(11) DEFAULT NULL,
  `month` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 AUTO_INCREMENT=20 ;

-- --------------------------------------------------------

--
-- 資料表結構 `totems`
--

CREATE TABLE IF NOT EXISTS `totems` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `alId` int(11) DEFAULT NULL,
  `totemId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `alId` (`alId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- 資料表結構 `unseenequips`
--

CREATE TABLE IF NOT EXISTS `unseenequips` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `alId` int(11) DEFAULT NULL,
  `equipId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `alId` (`alId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- 資料表結構 `vmatrixrecords`
--

CREATE TABLE IF NOT EXISTS `vmatrixrecords` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `charId` int(11) NOT NULL,
  `connectSkill1` int(11) NOT NULL,
  `connectSkill2` int(11) NOT NULL,
  `connectSkill3` int(11) NOT NULL,
  `dateExpire` int(11) NOT NULL,
  `coreId` int(11) NOT NULL,
  `coreLevel` tinyint(4) NOT NULL,
  `coreExp` int(11) NOT NULL,
  `coreState` tinyint(4) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `vcores_ibfk_1` (`charId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT AUTO_INCREMENT=1 ;

--
-- 已匯出資料表的限制(Constraint)
--

--
-- 資料表的 Constraints `avatardata`
--
ALTER TABLE `avatardata`
  ADD CONSTRAINT `avatardata_ibfk_1` FOREIGN KEY (`characterStat`) REFERENCES `characterstats` (`id`),
  ADD CONSTRAINT `avatardata_ibfk_2` FOREIGN KEY (`avatarLook`) REFERENCES `avatarlook` (`id`),
  ADD CONSTRAINT `avatardata_ibfk_3` FOREIGN KEY (`zeroAvatarLook`) REFERENCES `avatarlook` (`id`);

--
-- 資料表的 Constraints `characters`
--
ALTER TABLE `characters`
  ADD CONSTRAINT `characters_ibfk_1` FOREIGN KEY (`avatarData`) REFERENCES `avatardata` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `characters_ibfk_10` FOREIGN KEY (`guild`) REFERENCES `guilds` (`id`),
  ADD CONSTRAINT `characters_ibfk_2` FOREIGN KEY (`equippedInventory`) REFERENCES `inventories` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `characters_ibfk_3` FOREIGN KEY (`equipInventory`) REFERENCES `inventories` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `characters_ibfk_4` FOREIGN KEY (`consumeInventory`) REFERENCES `inventories` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `characters_ibfk_5` FOREIGN KEY (`etcInventory`) REFERENCES `inventories` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `characters_ibfk_6` FOREIGN KEY (`installInventory`) REFERENCES `inventories` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `characters_ibfk_7` FOREIGN KEY (`cashInventory`) REFERENCES `inventories` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `characters_ibfk_8` FOREIGN KEY (`funcKeyMap_id`) REFERENCES `funckeymap` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `characters_ibfk_9` FOREIGN KEY (`questManager`) REFERENCES `questmanagers` (`id`) ON DELETE CASCADE;

--
-- 資料表的 Constraints `characterstats`
--
ALTER TABLE `characterstats`
  ADD CONSTRAINT `characterstats_ibfk_1` FOREIGN KEY (`extendSP`) REFERENCES `extendsp` (`id`),
  ADD CONSTRAINT `characterstats_ibfk_2` FOREIGN KEY (`nonCombatStatDayLimit`) REFERENCES `noncombatstatdaylimit` (`id`),
  ADD CONSTRAINT `characterstats_ibfk_3` FOREIGN KEY (`albaStartTime`) REFERENCES `filetimes` (`id`),
  ADD CONSTRAINT `characterstats_ibfk_4` FOREIGN KEY (`characterCard`) REFERENCES `charactercards` (`id`),
  ADD CONSTRAINT `characterstats_ibfk_5` FOREIGN KEY (`accountLastLogout`) REFERENCES `systemtimes` (`id`),
  ADD CONSTRAINT `characterstats_ibfk_6` FOREIGN KEY (`lastLogout`) REFERENCES `filetimes` (`id`);

--
-- 資料表的 Constraints `equips`
--
ALTER TABLE `equips`
  ADD CONSTRAINT `equips_ibfk_1` FOREIGN KEY (`itemId`) REFERENCES `items` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `equips_ibfk_2` FOREIGN KEY (`equippedDate`) REFERENCES `filetimes` (`id`);

--
-- 資料表的 Constraints `gradenames`
--
ALTER TABLE `gradenames`
  ADD CONSTRAINT `gradenames_ibfk_1` FOREIGN KEY (`guildID`) REFERENCES `guilds` (`id`) ON DELETE CASCADE;

--
-- 資料表的 Constraints `guildmembers`
--
ALTER TABLE `guildmembers`
  ADD CONSTRAINT `guildmembers_ibfk_1` FOREIGN KEY (`guildID`) REFERENCES `guilds` (`id`),
  ADD CONSTRAINT `guildmembers_ibfk_2` FOREIGN KEY (`commitmentIncTime`) REFERENCES `filetimes` (`id`) ON DELETE CASCADE;

--
-- 資料表的 Constraints `guildrequestors`
--
ALTER TABLE `guildrequestors`
  ADD CONSTRAINT `guildrequestors_ibfk_1` FOREIGN KEY (`guildID`) REFERENCES `guilds` (`id`) ON DELETE CASCADE;

--
-- 資料表的 Constraints `guildskill`
--
ALTER TABLE `guildskill`
  ADD CONSTRAINT `guildskill_ibfk_1` FOREIGN KEY (`expireDate`) REFERENCES `filetimes` (`id`) ON DELETE CASCADE;

--
-- 資料表的 Constraints `guildskills`
--
ALTER TABLE `guildskills`
  ADD CONSTRAINT `guildskills_ibfk_1` FOREIGN KEY (`Guild_id`) REFERENCES `guilds` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `guildskills_ibfk_2` FOREIGN KEY (`fk_GuildSkillID`) REFERENCES `guildskill` (`id`) ON DELETE CASCADE;

--
-- 資料表的 Constraints `hairequips`
--
ALTER TABLE `hairequips`
  ADD CONSTRAINT `hairequips_ibfk_1` FOREIGN KEY (`alId`) REFERENCES `avatarlook` (`id`) ON DELETE CASCADE;

--
-- 資料表的 Constraints `items`
--
ALTER TABLE `items`
  ADD CONSTRAINT `items_ibfk_1` FOREIGN KEY (`inventoryId`) REFERENCES `inventories` (`id`) ON DELETE CASCADE;

--
-- 資料表的 Constraints `keymaps`
--
ALTER TABLE `keymaps`
  ADD CONSTRAINT `keymaps_ibfk_1` FOREIGN KEY (`fkMapId`) REFERENCES `funckeymap` (`id`);

--
-- 資料表的 Constraints `noncombatstatdaylimit`
--
ALTER TABLE `noncombatstatdaylimit`
  ADD CONSTRAINT `noncombatstatdaylimit_ibfk_1` FOREIGN KEY (`ftLastUpdateCharmByCashPR`) REFERENCES `filetimes` (`id`);

--
-- 資料表的 Constraints `options`
--
ALTER TABLE `options`
  ADD CONSTRAINT `options_ibfk_1` FOREIGN KEY (`equipId`) REFERENCES `equips` (`itemId`) ON DELETE CASCADE;

--
-- 資料表的 Constraints `petids`
--
ALTER TABLE `petids`
  ADD CONSTRAINT `petids_ibfk_1` FOREIGN KEY (`alId`) REFERENCES `avatarlook` (`id`) ON DELETE CASCADE;

--
-- 資料表的 Constraints `questlists`
--
ALTER TABLE `questlists`
  ADD CONSTRAINT `questlists_ibfk_1` FOREIGN KEY (`QuestManager_id`) REFERENCES `questmanagers` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `questlists_ibfk_2` FOREIGN KEY (`fk_questID`) REFERENCES `quests` (`id`);

--
-- 資料表的 Constraints `questprogressrequirements`
--
ALTER TABLE `questprogressrequirements`
  ADD CONSTRAINT `questprogressrequirements_ibfk_1` FOREIGN KEY (`questID`) REFERENCES `quests` (`id`) ON DELETE CASCADE;

--
-- 資料表的 Constraints `quests`
--
ALTER TABLE `quests`
  ADD CONSTRAINT `quests_ibfk_1` FOREIGN KEY (`completedTime`) REFERENCES `filetimes` (`id`);

--
-- 資料表的 Constraints `skills`
--
ALTER TABLE `skills`
  ADD CONSTRAINT `skills_ibfk_1` FOREIGN KEY (`charId`) REFERENCES `characters` (`id`) ON DELETE CASCADE;

--
-- 資料表的 Constraints `spset`
--
ALTER TABLE `spset`
  ADD CONSTRAINT `spset_ibfk_1` FOREIGN KEY (`extendSP_id`) REFERENCES `extendsp` (`id`) ON DELETE CASCADE;

--
-- 資料表的 Constraints `totems`
--
ALTER TABLE `totems`
  ADD CONSTRAINT `totems_ibfk_1` FOREIGN KEY (`alId`) REFERENCES `avatarlook` (`id`) ON DELETE CASCADE;

--
-- 資料表的 Constraints `unseenequips`
--
ALTER TABLE `unseenequips`
  ADD CONSTRAINT `unseenequips_ibfk_1` FOREIGN KEY (`alId`) REFERENCES `avatarlook` (`id`) ON DELETE CASCADE;

--
-- 資料表的 Constraints `vmatrixrecords`
--
ALTER TABLE `vmatrixrecords`
  ADD CONSTRAINT `vmatrixrecords_ibfk_1` FOREIGN KEY (`charID`) REFERENCES `characters` (`id`) ON DELETE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
